package ott.tictactoe.image;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ott.image.LineDetection;
import ott.image.data.Line;
import ott.image.data.Point;

public class TicTacToeBoard
{
	public Line left;
	public Line right;
	public Line top;
	public Line bottom;

	public TicTacToeBoard(Line left, Line right, Line top, Line bottom)
	{
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	
	public TicTacToeBoard()
	{
		super();
	}

	public Line getLeft()
	{
		return left;
	}

	public Line getRight()
	{
		return right;
	}

	public Line getTop()
	{
		return top;
	}

	public Line getBottom()
	{
		return bottom;
	}

	public Rectangle getSquareLocation(Point square)
	{
		return getSquareLocation(top, left, right, bottom, square);
	}
	
	public int getMostLeft()
	{
		return getSquareLocation(new Point(0,0)).x;
	}
	
	public int getMostTop()
	{
		return getSquareLocation(new Point(0,0)).y;
	}
	
	public int getMostBottom()
	{
		Rectangle r = getSquareLocation(new Point(2,2));
		return r.y + r.height;
	}
	
	public int getMostRight()
	{
		Rectangle r = getSquareLocation(new Point(2,2));
		return r.x + r.width;
	}
	
	public int getWidth()
	{
		int mostRight = getMostRight();
		int mostLeft = getMostLeft();
		return mostRight - mostLeft;
	}
	
	public int getHeight()
	{
		int mostBottom = getMostBottom();
		int mostTop = getMostTop();
		return mostBottom - mostTop;
	}
	
	private static Rectangle getSquareLocation(Line top, Line left, Line right, Line bottom, Point square)
	{
		assert square.x > -1;
		assert square.y > -1;
		assert square.x < 3;
		assert square.y < 3;

		Rectangle result = new Rectangle();
		switch (square.x)
		{
		case 0:
			result.x = Math.min(top.x1, bottom.x1);
			result.width = left.x1 - result.x;
			break;
		case 1:
			result.x = left.x2;
			result.width = right.x1 - result.x;
			break;
		case 2:
			result.x = right.x2;
			result.width = Math.max(top.x2, bottom.x2) - result.x;
			break;
		}

		switch (square.y)
		{
		case 0:
			result.y = Math.min(left.y1, right.y1);
			result.height = top.y1 - result.y;
			break;
		case 1:
			result.y = top.y2;
			result.height = bottom.y1 - result.y;
			break;
		case 2:
			result.y = bottom.y2;
			result.height = Math.max(left.y2, right.y2) - result.y;
			break;
		}
		return result;
	}
	
	public Line[] getLines()
	{
		return new Line[]
		{
				left,
				top,
				right,
				bottom
		};
	}

	@Override
	public String toString()
	{
		return "TicTacToeBoard [left=" + left + ", right=" + right + ", top=" + top + ", bottom=" + bottom + "]";
	}
	
	/**
	 * Takes in image and returns a board if found
	 * @param image
	 * 	Takes in a black and white thin edge image
	 * @return
	 */
	public static TicTacToeBoard locateBoard(BufferedImage image)
	{
		Line[] horizontals = LineDetection.detectHorizontalLines(image);
		Line[] verticals = LineDetection.detectVerticalLines(image);
		if (horizontals == null || verticals == null)
			return null;
		return locateBoard(horizontals, verticals);
	}

	public static TicTacToeBoard locateBoard(Line[] horizontalLines, Line[] verticalLines)
	{
		for (Line horzontalT : horizontalLines)
			for (Line horizontalB : horizontalLines)
				for (Line verticalL : verticalLines)
					for (Line verticalR : verticalLines)
					{
						if (!(horzontalT.equals(horizontalB) || verticalL.equals(verticalR)))
							if (formsBoard(verticalL, verticalR, horzontalT, horizontalB))
								return new TicTacToeBoard(verticalL, verticalR, horzontalT, horizontalB);
					}
		return null;
	}

	public static boolean formsBoard(Line left, Line right, Line top, Line bottom)
	{
		boolean result = true;

		// checking on x axis
		result = result && top.x1 < left.x1 && bottom.x1 < left.x1;
		result = result && left.x2 < right.x1;
		result = result && right.x2 < top.x2 && right.x2 < bottom.x2;

		// checking on y axis
		result = result && left.y1 < top.y1 && right.y1 < top.y1;
		result = result && top.y2 < bottom.y1;
		result = result && bottom.y2 < left.y2 && bottom.y2 < right.y2;

		return result;
	}
}
