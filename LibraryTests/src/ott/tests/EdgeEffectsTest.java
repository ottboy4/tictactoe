package ott.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.Test;

import ott.capstone.MainClass;
import ott.image.EdgeEffects;
import ott.image.data.Edge;

public class EdgeEffectsTest
{
	int[][] testImg = new int [][]
	{
			{ 0, 0, 		0, 0,  0},
			{ 0, 0xFFFFFF, 	0, 10, 0},
			{ 0, 0xFFFFFF, 	0, 10, 0},
			{ 0, 0xFFFFFF, 	0, 10, 0},
			{ 0, 0, 		0, 0,  0}
	};
	
	int[][] resultImg = new int[][]
	{
			{ 0, 0, 		0, 			0, 0},
			{ 0, 0xFFFFFF, 	0xFFFFFF, 	0, 0},
			{ 0, 0, 		0xFFFFFF, 	0, 0},
			{ 0, 0xFFFFFF, 	0xFFFFFF, 	0, 0},
			{ 0, 0, 		0, 			0, 0}
	};
	
	@Test
	public void testDetectEdges()
	{
		BufferedImage img = MainClass.buildImg(testImg);
		BufferedImage afterImg = EdgeEffects.detectEdges(img, 50, 50);
		for (int r = 0; r < 5; r++)
			for (int c = 0; c < 5; c++)
			{
				int expected = resultImg[c][r] + 0xFF000000;
				assertEquals(expected, afterImg.getRGB(r, c));
			}
	}
	
	@Test
	public void testFindEdgesInImage()
	{
		BufferedImage img = MainClass.buildImg(testImg);
		Edge[][] afterImg = EdgeEffects.findEdgesInImage(img, null, 50, 50);
		for (int r = 0; r < 5; r++)
			for (int c = 0; c < 5; c++)
			{
				if (resultImg[c][r] == 0xFFFFFF)
					assertTrue(afterImg[r][c].isHighEdge);
			}
	}
	
	@Test
	public void testMarkHighEdges()
	{
		BufferedImage img = MainClass.buildImg(testImg);
		Edge[][] edges = EdgeEffects.findEdgesInImage(img, null, 50, 50);
		EdgeEffects.markHighEdges(edges);
		for (int r = 0; r < 5; r++)
			for (int c = 0; c < 5; c++)
			{
				if (resultImg[c][r] == 0xFFFFFF)
					assertTrue(edges[r][c].isEdge);
			}
	}
	
	
	@Test
	public void testMarkEdgesInImage()
	{
		boolean[][] whereEdges = new boolean[][] 
			{
				{ true, false, true },
				{ false, false, false },
				{ true, false, true }
			};
		
		Edge[][] edges = new Edge[whereEdges.length][whereEdges[0].length];
		for (int r = 0; r < edges.length; r++)
			for (int c = 0; c < edges[0].length; c++)
			{
				edges[r][c] = new Edge();
				edges[r][c].isEdge = whereEdges[r][c];
			}
		
		BufferedImage img = EdgeEffects.markEdgesInImage(edges);
		
		for (int r = 0; r < edges.length; r++)
			for (int c = 0; c < edges[0].length; c++)
				if (whereEdges[r][c])
					assertEquals(0xFFFFFFFF, img.getRGB(r, c));
	}
	

}
