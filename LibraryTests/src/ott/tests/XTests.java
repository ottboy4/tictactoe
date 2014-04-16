package ott.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ott.image.data.Line;
import ott.tictactoe.image.X;

public class XTests
{
	@Test
	public void testFormsX()
	{
		Line goodLeft = new Line(10, 10, 200, 200);
		Line goodRight = new Line(10, 10, 200, 200);
		runFormsXTest(goodLeft, goodRight, true);
		
		Line goodLeft2 = new Line(241, 81, 554, 329);
		Line goodRight2 = new Line(241, 83, 536, 327);
		runFormsXTest(goodLeft2, goodRight2, true);
		
		Line badLeft = new Line(21, 12, 47, 40);
		Line badRight = new Line(15, 30, 35, 45);
		runFormsXTest(badLeft, badRight, false);
		
		Line badLeft1 = new Line(23, 12, 35, 30);
		Line badRight1 = new Line(38, 22, 65, 30);
		runFormsXTest(badLeft1, badRight1, false);
	}
	
	private void runFormsXTest(Line left, Line right, boolean works)
	{
		boolean result = X.formsX(left, right);
		if (works)
			assertTrue(result);
		else
			assertFalse(result);
	}
	

}
