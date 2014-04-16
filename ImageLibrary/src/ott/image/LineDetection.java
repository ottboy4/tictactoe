package ott.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ott.image.data.Direction;
import ott.image.data.Kernel;
import ott.image.data.Line;
import ott.image.data.Point;

public class LineDetection
{
	public static final int PIXEL_ON = 0xFFFFFFFF;
	private static final int STRAIGHT_LINE_STRENGTH = 1020;
	
	public final static Kernel HORIZONTAL_LINE = new Kernel(3, 3, new int[] 
	{
			-1,-1,-1,
			 2, 2, 2,
			-1,-1,-1
	});
	
	public final static Kernel VERTICAL_LINE = new Kernel(3, 3, new int[] 
	{
			-1,2,-1,
			-1,2,-1,
			-1,2,-1
	});
	
	public final static Kernel LEFT_DIAGNOL_LINE = new Kernel(3, 3, new int[] 
	{
			 3, -1, -2,
			-1,  3, -1,
			-2, -1,  3
	});
	
	public final static Kernel RIGHT_DIAGNOL_LINE = new Kernel(3, 3, new int[] 
	{
			-2, -1,  3,
			-1,  3, -1,
			 3, -1, -2
		});
	
	public static Line[] detectLines(BufferedImage image, Kernel direction)
	{
		List<Line> lines = new ArrayList<>();
		final int line_min_thickness = 2; 

		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++)
			{
				// already ran tells the loop to skip this x,y because it is already inside another already found line
				boolean alreadyRan = false;
				for (Line line : lines)
				{
					// check to see if in bounds... x1 leftmost x2 rightmost
					if (x >= line.x1 && x <= line.x2
							&& y >= line.y1 && y <= line.y2)
					{
						alreadyRan = true;
						break;
					}
				}
				if (!alreadyRan)
				{
					Line line = detectLineAtPoint(image, x, y, direction);
					if (line != null && !lines.contains(line))
					{
						// makes sure line has a width and height of at least the thickness... no tiny stupid lines
						if (line.w > line_min_thickness || line.h > line_min_thickness) 
							lines.add(line);
					}
				}
			}
		
		Line[] result = generateLineArray(lines);
		return result;
	}

	public static Line detectLineAtPoint(BufferedImage image, int x, int y, Kernel direction)
	{
		Line line = null;
		if (pixelIsOnStraitLine(image, x, y, direction))
		{
			Direction edgeDir = whichDirection(direction);
			Point otherLine = findParallelLine(image, edgeDir, x, y, direction);
			
			if (otherLine != null)
			{
				Point[] forwardPoint = traceForward(image, new Point(x,y), otherLine, edgeDir, direction);
				Point[] backwardPoint = traceBackward(image, new Point(x,y), otherLine, edgeDir, direction);
				line = generateLine(forwardPoint, backwardPoint);
			}
		}
		return line;
	}
	
	private static Direction whichDirection(Kernel kern)
	{
		Direction result = null;
		if (kern == VERTICAL_LINE)
			result = Direction.ANGLE_0;
		else if (kern == HORIZONTAL_LINE)
			result = Direction.ANGLE_90;
		else if (kern == LEFT_DIAGNOL_LINE)
			result = Direction.ANGLE_45;
		else if (kern == RIGHT_DIAGNOL_LINE)
			result = Direction.ANGLE_135;
		return result;
	}
	
	public static Line generateLine(Point[] f, Point[] b)
	{
		if (f == null || b == null)
			return null;
		int topX = Math.min(Point.lowestX(f), Point.lowestX(b));
		int botX = Math.max(Point.highestX(f), Point.highestX(b));
		int topY = Math.min(Point.lowestY(f), Point.lowestY(b));
		int botY = Math.max(Point.highestY(f), Point.highestY(b));
		Line result = new Line(topX, topY, botX, botY);
		return result;
	}
	
	public static Point[] traceForward(BufferedImage image, Point first, Point second, Direction direction, Kernel checker)
	{
		Point forwardGradient = direction.forwardGradient();
		Point forwardLine = direction.forwardLine();
		return trace(image, first, second, forwardGradient, forwardLine, checker);
	}
	
	public static Point[] traceBackward(BufferedImage image, Point first, Point second, Direction direction, Kernel checker)
	{
		Point backwardGradient = direction.backwardGradient();
		Point backwardLine = direction.backwardLine();
		return trace(image, first, second, backwardGradient, backwardLine, checker);
	}

	public static Point[] trace(BufferedImage image, Point first, Point second, Point gradient, Point lineMove, Kernel checker)
	{
		first = new Point(first);
		second = new Point(second);
		final Point endFirst = traceToEnd(image, first, lineMove, null, true, checker);
		final Point endSecond = traceToEnd(image, second, lineMove, null, true, checker);
		if (endFirst == null || endSecond == null)
			return null;
		boolean match = seeIfTheyMatch(image, endFirst, endSecond, gradient);
		
		Point[] result = null;
		if (match)
		{
			result = new Point[2];
			result[0] = endFirst;
			result[1] = endSecond;
		}
		return result;
	}

	public static boolean seeIfTheyMatch(BufferedImage img, Point first, Point second, Point move)
	{
		Point f = new Point(first);
		Point s = new Point(second);
		
		Point firstEnd = traceToEnd(img, f, move, second, false, null);
		if (second.equals(firstEnd))
			return true;
		Point secondEnd = traceToEnd(img, s, move, first, false, null);
		if (first.equals(secondEnd))
			return true;
		return false;
	}

	public static Point traceToEnd(BufferedImage image, Point current, Point move, Point stopAt, boolean traceGaps, Kernel checker)
	{
		boolean optionFound = true;
		
		while (optionFound)
		{
			if (stopAt != null && stopAt.equals(current))
				break;
			
			optionFound = false;
			int x = current.x + move.x;
			int y = current.y + move.y;
			if (isOutOfBounds(image, new Point(x,y)))
				return null;
			if (image.getRGB(x, y) == PIXEL_ON)
			{
				optionFound = true;
				current.translate(move);
			} 
			
			// if option has not been found.. search forward to see if something can be found
			if (!optionFound && traceGaps)
			{
				Point result = checkFoward(image, new Point(current), move, checker);
				if (result != null)
				{
					optionFound = true;
					current = result;
				}
			}
			
			// check for 1 px to either side
			if (!optionFound)
			{
				Point[] alts = findOtherPossibleMoves(current, move);
				for (Point p : alts)
				{
					if (!optionFound)
					{
						x = current.x + p.x;
						y = current.y + p.y;
						if (isOutOfBounds(image, new Point(x,y)))
							return null;
						if (image.getRGB(x, y) == PIXEL_ON)
						{
							optionFound = true;
							current.translate(p);
						}
					}
				}
			}
			
		}
		
		return new Point(current);
	}
	
	private static Point checkFoward(BufferedImage image, Point current, Point move, Kernel checker)
	{
		current.translate(move);
		while (!isOutOfBounds(image, current))
		{
			if (image.getRGB(current.x, current.y) == PIXEL_ON)
			{

				boolean strongEnough = pixelIsOnStraitLine(image, current.x, current.y, checker);
				strongEnough = strongEnough || pixelIsOnStraitLine(image, current.x + move.x, current.y + move.y, checker);
				if (strongEnough)
					return current;
				else
				{
					Point p = traceToEnd(image, new Point(current), move, null, false, checker);
					if (p != null)
						if (!p.equals(current) && pixelIsOnStraitLine(image, p.x - move.x, p.y - move.y, checker))
							return p;
				}
			}
			current.translate(move);
		}
		return null;
	}
	
	public static boolean isOutOfBounds(BufferedImage img, Point p)
	{
		return p.x >= img.getWidth() || p.x < 0 || p.y >= img.getHeight() || p.y < 0;
	}
	
	private static final Point[] POSSIBLE_MOVE_DIRECTIONS = new Point[]
	{
		new Point(1,0),
		new Point(-1, 0),
		new Point(0, 1),
		new Point(0, -1)
	};
	
	public static Point[] findOtherPossibleMoves(final Point start, final Point move)
	{
		ArrayList<Point> possibilities = new ArrayList<>();
		Point moved = new Point(start);
		moved.translate(move.x, move.y);
		for (Point dir : POSSIBLE_MOVE_DIRECTIONS)
		{
			Point cur = new Point(moved);
			cur.translate(dir.x, dir.y);
			boolean xDis = Math.abs(cur.x - start.x) < 2;
			boolean yDis = Math.abs(cur.y - start.y) < 2;
			boolean toStart = cur.x == start.x && cur.y == start.y;
			if (xDis && yDis && !toStart)
			{
				Point toAdd = new Point(dir);
				toAdd.translate(move.x, move.y);
				possibilities.add(toAdd);
			}
		}
		return possibilities.toArray(new Point[0]);
	}
	
	public static Point findParallelLine(BufferedImage img, Direction direction, int x, int y, Kernel checker)
	{
		Point line = findLineInDirection(img, direction, x, y, checker);
		return line;
	}

	public static Point findLineInDirection(BufferedImage img, Direction direction, int x, int y, Kernel checker)
	{
		Point move = direction.forwardGradient();
		do
		{
			x += move.x;
			y += move.y;
			int lineStrength = ConvolutionEffects.convolute(x, y, img, checker).r;
			boolean strongEnough = lineStrength >= STRAIGHT_LINE_STRENGTH;
			if (strongEnough)
			{
				return new Point(x, y);
			} 
		} while (x > -1 && x < img.getWidth() && y < img.getHeight() && y > -1);
		return null;
	}
	
	public static Line[] generateLineArray(List<Line> lines)
	{
		Line[] result = (lines.size() == 0) ? null : lines.toArray(new Line[0]);
		return result;
	}
	
	public static boolean pixelIsOnStraitLine(BufferedImage image, int x, int y, Kernel verticalLine)
	{
		int lineStrength = ConvolutionEffects.convolute(x, y, image, verticalLine).r;
		return lineStrength >= STRAIGHT_LINE_STRENGTH;
	}
	
	public static Line[] detectHorizontalLines(BufferedImage image)
	{
		return detectLines(image, HORIZONTAL_LINE);
	}
	
	public static Line[] detectVerticalLines(BufferedImage image)
	{
		return detectLines(image, VERTICAL_LINE);
	}
	
	public static Line[] detectLeftDiagnolLines(BufferedImage image)
	{
		return detectLines(image, LEFT_DIAGNOL_LINE);
	}
	
	public static Line[] detectRightDiagnolLines(BufferedImage image)
	{
		return detectLines(image, RIGHT_DIAGNOL_LINE);
	}
	
}
