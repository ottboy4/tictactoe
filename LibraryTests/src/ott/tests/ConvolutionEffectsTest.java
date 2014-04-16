package ott.tests;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;

import org.junit.Test;

import ott.image.ConvolutionEffects;
import ott.image.data.Kernel;

public class ConvolutionEffectsTest
{

	static final Kernel testKern = new Kernel(3, 3, new int[] // flat kernel
	{
		1,1,1,
		1,1,1,
		1,1,1
	});
	
	static final int[] testImage1 = new int[] 
	{
		200
	};
	
	static final int[] testImage2 = new int[] 
	{
		1,2,3,
		4,5,6,
		7,8,9
	};
	
	static final int[] testImage2Expected = new int[]
	{
		3, 3, 4,
		4, 5, 5,
		6, 6, 7
	};
	
	
	
	@Test
	public void testApplyKernel1()
	{
		BufferedImage testImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		testImg.setRGB(0, 0, 1, 1, testImage1, 0, 1);
		
		BufferedImage result = ConvolutionEffects.convoluteImage(testImg, testKern);
		
		assertEquals(testImage1[0] + 0xFF000000, result.getRGB(0, 0));	
	}
	
	@Test
	public void testApplyKernel2()
	{

		BufferedImage testImg = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
		testImg.setRGB(0, 0, 3, 3, testImage2, 0, 3);
		
		BufferedImage result = ConvolutionEffects.convoluteImage(testImg, testKern);
		
		
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				int actual = result.getRGB(j, i);
				int expected = testImage2Expected[i * 3 + j];
				assertEquals(expected + 0xFF000000, actual);
				
				
			}
		}
		
	}

}
