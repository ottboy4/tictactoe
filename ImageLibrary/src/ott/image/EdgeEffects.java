package ott.image;

import java.awt.image.BufferedImage;

import ott.image.data.Direction;
import ott.image.data.Edge;
import ott.image.data.Kernel;
import ott.image.data.Point;
import ott.image.data.RGB;

public class EdgeEffects
{
	private final static Kernel HORIZONTAL_GRADIENT = new Kernel(3, 3, new int[]
	{
			-1, 0, 1,
			-2, 0, 2,
			-1, 0, 1 });
	private final static Kernel VERTICAL_GRADIENT = new Kernel(3, 3, new int[]
	{
			1, 2, 1,
			0, 0, 0,
			-1,-2,-1 });



	public static BufferedImage detectEdges(BufferedImage img, int highThreshold, int lowThreshold)
	{
		Edge[][] edges = new Edge[img.getWidth()][img.getHeight()];
		findEdgesInImage(img, edges, highThreshold, lowThreshold);
		markHighEdges(edges);
		thresholdEdges(edges);
		BufferedImage newImg = markEdgesInImage(edges);
		return newImg;
	}

	public static BufferedImage markEdgesInImage(Edge[][] edges)
	{
		int width = edges.length;
		int height = edges[0].length;
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				newImg.setRGB(x, y, ((edges[x][y].isEdge)) ? 0xFFFFFF : 0x000000);
		return newImg;
	}

	public static void markHighEdges(Edge[][] edges)
	{
		for (int x = 1; x < edges.length - 1; x++)
		{
			for (int y = 1; y < edges[0].length - 1; y++)
			{
				Edge edge = edges[x][y];
				if (edge.isHighEdge)
				{
					Direction direction = edge.direction;
					edge.isEdge = determineHighestOnGradient(edges, direction, x, y, edge);
				}
			}
		}
	}

	private static void thresholdEdges(Edge[][] edges)
	{
		for (int x = 1; x < edges.length - 1; x++)
			for (int y = 1; y < edges[0].length - 1; y++)
				if (edges[x][y].isEdge)
					thresholdEdge(edges, x, y);
	}

	private static void thresholdEdge(Edge[][] edges, int x, int y)
	{
		if (x <= 0 || y <= 0 || edges.length - 1 <= x || edges[0].length - 1 <= y)
			return;
		Edge edge = edges[x][y];
		Direction direction = edge.direction;

		checkDirectionThreshold(edges, x, y, new Point(direction.forwardLine()), direction);
		checkDirectionThreshold(edges, x, y, new Point(direction.backwardLine()), direction);
	}

	private static void checkDirectionThreshold(Edge[][] edges, int x, int y, Point location, Direction directionToMatch)
	{
		location.translate(x, y);
		Edge edge = edges[location.x][location.y];
		Direction edgeDirection = edge.direction;
		if (edge.isLowEdge && !edge.isHighEdge && !edge.isEdge && directionToMatch == edgeDirection)
		{
			edge.isEdge = true;
			thresholdEdge(edges, location.x, location.y);
		}
	}

	private static boolean determineHighestOnGradient(Edge[][] edges, Direction toCheck, int x, int y, Edge checkAgainst)
	{
		Point forward = toCheck.forwardGradient();
		forward.translate(x, y);
		boolean forwLower = suppressEdge(edges[forward.x][forward.y], checkAgainst);

		Point backward = toCheck.backwardGradient();
		backward.translate(x, y);
		boolean backLower = suppressEdge(edges[backward.x][backward.y], checkAgainst);

		return forwLower && backLower;
	}

	private static boolean suppressEdge(Edge toCheck, Edge checkAgainst)
	{
		boolean isLower = checkAgainst.strength > toCheck.strength;
		if (isLower)
		{
			toCheck.isLowEdge = false;
			toCheck.isHighEdge = false;
			toCheck.strength = 0;
		} else
		{
			checkAgainst.isLowEdge = false;
			checkAgainst.isHighEdge = false;
			checkAgainst.strength = 0;
		}
		return isLower;
	}

	public static Edge[][] findEdgesInImage(BufferedImage img, Edge[][] edges, int highThreshold, int lowThreshold)
	{
		if (edges == null)
			edges = new Edge[img.getWidth()][img.getHeight()];
		for (int x = 0; x < img.getWidth(); x++)
		{
			for (int y = 0; y < img.getHeight(); y++)
			{
				Edge pixelEdge = determineEdge(img, x, y);
				pixelEdge.isHighEdge = pixelEdge.strength > highThreshold;
				pixelEdge.isLowEdge = pixelEdge.strength > lowThreshold;
				edges[x][y] = pixelEdge;
			}
		}
		return edges;
	}

	public static Edge determineEdge(BufferedImage img, int x, int y)
	{
		Edge result = new Edge();
		RGB gX = ConvolutionEffects.convolute(x, y, img, HORIZONTAL_GRADIENT);
		RGB gY = ConvolutionEffects.convolute(x, y, img, VERTICAL_GRADIENT);
		int gradientX = gX.average();
		int gradientY = gY.average();
		result.strength = Math.abs(gradientX) + Math.abs(gradientY);
		if (gradientX == 0)
			result.theta = (gradientY == 0) ? 0 : Math.PI / 2;
		else
			result.theta = Math.atan((gradientY * 1.0) / gradientX);
		result.positiveGradient = (gradientX + gradientY) > 0;
		result.direction = Direction.determineDirection(result.theta);
		return result;
	}

}
