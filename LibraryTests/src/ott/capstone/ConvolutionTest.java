package ott.capstone;


import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ott.image.ConvolutionEffects;
import ott.image.data.Kernel;

public class ConvolutionTest
{
//	static Kernel kern = new Kernel(3, 3, new int[] // flat kernel
//			{
//				1,1,1,
//				1,1,1,
//				1,1,1
//			});
	
	static Kernel blur = new Kernel(5, 5, new int[]  // gaussian kernel
			{
				1,4,7,4,1,
				4,16,26,16,4,
				7,26,41,26,7,
				4,16,26,16,4,
				1,4,7,4,1
			});
	
	static Kernel sharpen = new Kernel(3,3, new int[] // kernel for image sharpening
			{
				0,-1,0,
				-1,12,-1,
				0,-1,0
			});
	
	
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
			
			
			
	public static void testApplyKernel2()
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
				
				
			}
		}
		
	}
	
	
	
	public static void runDisplay() throws Exception
	{
		BufferedImage img = ImageIO.read(new File("Tulips.jpg"));
//		BufferedImage img = ImageIO.read(new File("C:/Users/caleb/Pictures/english, project/n10113913_41517494_8475.jpg"));

		
		
		MainClass.display("original", img);
		MainClass.display("after blur", convertPic(img, blur));
		MainClass.display("after sharpen", convertPic(img, sharpen));
	}
	
	public static BufferedImage convertPic(BufferedImage img, Kernel kern)
	{
		long before = System.nanoTime();
		BufferedImage img2 = ConvolutionEffects.convoluteImage(img, kern);
		long after = System.nanoTime();
		double timeTaken = (after - before) / 1000000000.0;
		System.out.println("Convolution time: " + timeTaken);
		return img2;
	}
	
//		Kernel kern = new Kernel(5, 5, new int[] { 0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0 });
//		Kernel kern = new Kernel(3, 3, new int[] { 0,0,0,0,1,0,0,0,0 });
}
