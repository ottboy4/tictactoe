package ott.tictactoe.image;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ott.image.ImageEffects;
import ott.image.LineDetection;
import ott.image.data.Line;
import ott.math.Vector;

public class X
{
	private Line left;
	private Line right;
	
	public X()
	{
		super();
	}
	
	public X(Line left, Line right)
	{
		this.left = left;
		this.right = right;
	}
	
	public Line getLeft()
	{
		return left;
	}

	public Line getRight()
	{
		return right;
	}

	public static X xInLocation(BufferedImage img, Rectangle rect)
	{
		BufferedImage subImage = img.getSubimage(rect.x, rect.y, rect.width, rect.height);
		subImage = ImageEffects.copy(subImage);
		return xInImage(subImage);
	}
	
	public static X xInImage(BufferedImage image)
	{
		Line[] leftDiagnols = LineDetection.detectLeftDiagnolLines(image);
		Line[] rightDiagnols = LineDetection.detectRightDiagnolLines(image);
		if (leftDiagnols == null || rightDiagnols == null)
			return null;
		return linesFormX(leftDiagnols, rightDiagnols);
	}
	
	public static X linesFormX(Line[] lefts, Line[] rights)
	{
		for (Line left : lefts)
			for (Line right : rights)
			{
				if (formsX(left, right))
					return new X(left, right);
			}
		return null;
	}

	@Override
	public String toString()
	{
		return "X [left=" + left + ", right=" + right + "]";
	}

	public static boolean formsX(Line left, Line right)
	{
		boolean result = true;
		
		result = result && left.x1 < right.x2;
		result = result && left.y1 < right.y2;
		result = result && left.x2 > right.x1;
		result = result && left.y2 > right.y1;
		result = result && crossesLine(left, right);

		return result;
	}
	
	private static final int DISTANCE_FROM_CROSS = 0; // 150
	
	private static boolean crossesLine(Line left, Line right)
	{
		Vector left1 = new Vector(left.x1, left.y1);
		Vector left2 = new Vector(left.x2, left.y2);
		Vector right1 = new Vector(right.x2, right.y1);
		Vector right2 = new Vector(right.x1, right.y2);
		Vector leftDiff = Vector.subtract(left1, left2);
		Vector norm = leftDiff.normalized();
		Vector perp = norm.perpCCW();
		
		Vector firstCheck = Vector.subtract(left1, right1);
		Vector secondCheck = Vector.subtract(left1, right2);
		
		boolean result = true;
		
		double firstCheckDot = firstCheck.dot(perp);
		result = result && firstCheckDot < -DISTANCE_FROM_CROSS;
		double secondCheckDot = secondCheck.dot(perp);
		result = result && secondCheckDot > DISTANCE_FROM_CROSS;
		
		return result;
	}
}
