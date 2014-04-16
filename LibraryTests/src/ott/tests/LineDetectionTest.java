package ott.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ott.capstone.MainClass;
import ott.image.LineDetection;
import ott.image.data.Direction;
import ott.image.data.Edge;
import ott.image.data.Line;
import ott.image.data.Point;

public class LineDetectionTest
{

	@Test
	public void testDetectLines()
	{
//		int[] imgArray = new int[]
//		{
//				0,0,0,0,0,
//				0,0,0xFFFFFF,0,0,
//				0,0,0xFFFFFF,0,0,
//				0,0,0xFFFFFF,0,0,
//				0,0,0,0,0
//		};
//		
//		BufferedImage img = MainClass.buildImg(imgArray, 5, 5);
//
//		Line[] foundLines = LineDetection.detectVerticalLines(img);
//		assertNotNull(foundLines);
//		Line expected = new Line(2, 1, 2, 3);
//		assertEquals(expected, foundLines[0]);
	}

	@Test
	public void testDetectLineAtPoint()
	{
//		int[] imgArray = new int[]
//				{
//						0,0,0,0,0,
//						0,0,0xFFFFFF,0,0,
//						0,0,0xFFFFFF,0,0,
//						0,0,0xFFFFFF,0,0,
//						0,0,0,0,0
//				};
//				
//				BufferedImage img = MainClass.buildImg(imgArray, 5, 5);
//
//				Line foundLine = LineDetection.detectLineAtPoint(img, 2, 1, LineDetection.VERTICAL_LINE);
//				assertNotNull(foundLine);
//				Line expected = new Line(2, 1, 2, 3);
//				assertEquals(expected, foundLine);
	}

	@Test
	public void testGenerateLine()
	{
		Point[] first = new Point[]
		{
				new Point(0,10),
				new Point(10, 0)
		};
		Point[] second = new Point[]
		{
				new Point(5, 5),
				new Point(2, 15) 
		};
		
		Line result = LineDetection.generateLine(first, second);
		Line expected = new Line(0, 0, 10, 15);
		assertEquals(expected, result);
	}

	@Test
	public void testTraceForward()
	{
//		fail("Not yet implemented");
	}

	@Test
	public void testTraceBackward()
	{
//		fail("Not yet implemented");
	}

	@Test
	public void testTrace()
	{
//		fail("Not yet implemented");
	}

	@Test
	public void testSeeIfTheyMatch()
	{
//		fail("Not yet implemented");
	}

	@Test
	public void testTraceToEnd()
	{
//		fail("Not yet implemented");
	}

	@Test
	public void testIsOutOfBounds()
	{
		int[] imgArray = new int[36];
		BufferedImage img = MainClass.buildImg(imgArray, 6, 6);
		Point[] inBounds = new Point[] 
		{
				new Point(0,0),
				new Point(5,5),
				new Point(0,5),
				new Point(5,0)
		};
		Point[] outBounds = new Point[]
		{
				new Point(-1, 0),
				new Point(0, -1),
				new Point(-1, -1),
				new Point(6, 6),
				new Point(6,0),
				new Point(0,6)
		};
		for (Point in : inBounds)
			assertFalse(LineDetection.isOutOfBounds(img, in));
		
		for (Point out : outBounds)
			assertTrue(LineDetection.isOutOfBounds(img, out));
	}

	@Test
	public void testFindOtherPossibleMoves()
	{
		Point start = new Point(10, 10);
		Point move = new Point(1, 0); // move positive 1 in x direction
		
		Point[] moves = LineDetection.findOtherPossibleMoves(start, move);
		List<Point> movesList = Arrays.asList(moves);
		assertEquals(2, moves.length);
		
		Point first = new Point(1, 1);
		Point second = new Point(1, -1);
		
		assertTrue(movesList.contains(first));
		assertTrue(movesList.contains(second));
	}
	
	@Test
	public void testFindParallelLine()
	{
//		int[] imgArray = new int[]
//		{
//				0,0,0,0,0,0,0,
//				0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//				0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//				0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//				0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//				0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//				0,0,0,0,0,0,0
//		};
//		BufferedImage img = MainClass.buildImg(imgArray, 7, 7);
//	
//		Edge start = new Edge();
//		start.direction = Direction.ANGLE_0;
//		Point found = LineDetection.findParallelLine(img, start, 2, 2, LineDetection.VERTICAL_LINE);
//		Point expected = new Point(5, 2);
//		assertEquals(expected, found);
	}

	@Test
	public void testFindLineInDirection()
	{
//		int[] imgArray = new int[]
//		{
//						0,0,0,0,0,0,0,
//						0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//						0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//						0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//						0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//						0,0,0xFFFFFF,0,0,0xFFFFFF,0,
//						0,0,0,0,0,0,0
//		};
//		BufferedImage img = MainClass.buildImg(imgArray, 7, 7);
//
//		Edge start = new Edge();
//		start.direction = Direction.ANGLE_0;
//		Point found = LineDetection.findLineInDirection(img, start, 2, 2, LineDetection.VERTICAL_LINE, start.direction.forwardGradient());
//		Point expected = new Point(5, 2);
//		assertEquals(expected, found);
	}

	@Test
	public void testGenerateLineArray()
	{
		List<Line> lines = new ArrayList<>();
		
		Line[] result1 = LineDetection.generateLineArray(lines);
		assertNull(result1);
		
		Line toAdd = new Line(0, 0, 0, 0);
		lines.add(toAdd);
		
		Line[] result2 = LineDetection.generateLineArray(lines);
		assertEquals(toAdd, result2[0]);
	}

	@Test
	public void testPixelIsOnStraitLine()
	{
		int[] imgArray = new int[]
		{
				0,0,0,0,0,
				0,0,0xFFFFFF,0,0,
				0,0,0xFFFFFF,0,0,
				0,0,0xFFFFFF,0,0,
				0,0,0,0,0
		};
		
		BufferedImage img = MainClass.buildImg(imgArray, 5, 5);
		boolean isTrue = LineDetection.pixelIsOnStraitLine(img, 2, 2, LineDetection.VERTICAL_LINE);
		assertTrue(isTrue);
		
		boolean isFalse = LineDetection.pixelIsOnStraitLine(img, 1, 2, LineDetection.VERTICAL_LINE);
		assertFalse(isFalse);
	}

}
