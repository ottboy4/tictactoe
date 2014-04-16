package ott.image;

import java.awt.image.BufferedImage;

import ott.image.ColorEffects.ColorEffectType;

public class ImageEffects
{
	public static BufferedImage copy(BufferedImage img)
	{
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		newImg.setRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
		return newImg;
	}
	
	public static BufferedImage convertToGrayscale(BufferedImage img)
	{
		return ColorEffects.applyEffect(img, ColorEffectType.GRAYSCALE_LIGHTNESS);
	}
}








