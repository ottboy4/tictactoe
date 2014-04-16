package ott.image;

import java.awt.image.BufferedImage;

import ott.image.data.Kernel;
import ott.image.data.RGB;

public class ConvolutionEffects
{
	public static final Kernel GAUSSIAN_BLUR = new Kernel(5, 5, new int[]  // gaussian kernel
			{
				1,4,7,4,1,
				4,16,26,16,4,
				7,26,41,26,7,
				4,16,26,16,4,
				1,4,7,4,1
			});
	
	/**
	 * Applies the kernel to the image and returns a new image after the kernel has been applied.
	 * @param img
	 * 			Image being analyzed
	 * @param kern
	 * 			Kernel being applied to the image
	 * @return
	 * 			A new image with the transformed pixels
	 */
	public static BufferedImage convoluteImage(BufferedImage img, Kernel kern)
	{
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < img.getWidth(); x++)
		{
			for (int y = 0; y < img.getHeight(); y++)
			{
				RGB newPixel = convolute(x, y, img, kern);
				newPixel.removeNegatives();
				newImg.setRGB(x, y, newPixel.toInt());
			}
		}
		return newImg;
	}

	/**
	 * Applies the kernel to the pixel located at (x,y) and returns the new pixel value.
	 * @param x
	 * 			The x coordinate of the pixel
	 * @param y
	 * 			The y coordinate of the pixel
	 * @param img
	 * 			The image being analyzed
	 * @param kern
	 * 			The kernel being applied to the pixel
	 * @return
	 * 			Pixel value at that coordinate after the kernel is applied
	 */
	public static RGB convolute(int x, int y, BufferedImage img, Kernel kern)
	{
		int imageStartX = x - kern.getHalfWidth();
		int chunkX = determineChunkStart(imageStartX);
		imageStartX += chunkX;
		
		int imageStartY = y - kern.getHalfHeight();
		int chunkY = determineChunkStart(imageStartY);
		imageStartY += chunkY;

		int chunkW = determineSize(chunkX, kern.getWidth(), imageStartX, img.getWidth());
		int chunkH = determineSize(chunkY, kern.getHeight(), imageStartY, img.getHeight()); 
		
		int[] chunk = new int[kern.getWidth() * kern.getHeight()];
		int arrayOffset = chunkX + chunkY * kern.getWidth();
		img.getRGB(imageStartX, imageStartY, chunkW, chunkH, chunk, arrayOffset, kern.getWidth());
		
		RGB newPixel = evaluateChunk(chunkX, chunkY, chunkW, chunkH, kern, chunk);
		return newPixel;
	}
	
	private static int determineSize(int kernelStart, int kernelSize, int imageStart, int imageSize)
	{
		int resultKernelSize = kernelSize - kernelStart;
		if (imageStart + resultKernelSize >= imageSize)
			resultKernelSize = imageSize - imageStart;
		return resultKernelSize;
	}
	
	private static int determineChunkStart(int imageStart)
	{
		return (imageStart > 0) ? 0 : Math.abs(imageStart);
	}

	private static RGB evaluateChunk(int chunkX, int chunkY, int chunkW, int chunkH, Kernel kern, int[] chunk)
	{
		int[] kernel = kern.getData();
		assert (kernel.length == chunk.length);
		int divisorSum = 0;
		RGB sum = new RGB();
		for (int ky = 0, cy = chunkY; ky < chunkH; ky++, cy++)
		{
			for (int kx = 0, cx = chunkX; kx < chunkW; kx++, cx++)
			{
				int i = cy * kern.getWidth() + cx;
				RGB currentPixel = new RGB(chunk[i]);
				currentPixel.multiply(kernel[i]);
				sum.add(currentPixel);
				divisorSum += kernel[i];
			}
		}
		if (divisorSum == 0)
			divisorSum = 1;
		sum.divide(divisorSum);
		return sum;
	}
}

