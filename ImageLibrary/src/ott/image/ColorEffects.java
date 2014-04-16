package ott.image;

import java.awt.image.BufferedImage;

import ott.image.data.RGB;

public class ColorEffects
{
	public static BufferedImage applyEffect(BufferedImage img, ColorEffectType type)
	{
		int[] rgb = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		ColorEffect effect = type.effect;
		for (int i = 0; i < rgb.length; i++)
			rgb[i] = effect.applyEffect(new RGB(rgb[i])).toInt();
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		newImg.setRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
		return newImg;
	}
	
	public enum ColorEffectType
	{
		GRAYSCALE_LIGHTNESS(new Grayscale_Lightness()),
		GRAYSCALE_AVERAGE(new Grayscale_Average()),
		GRAYSCALE_LUMINOSITY(new Grayscale_Luminosity());
		
		ColorEffect effect;
		private ColorEffectType(ColorEffect effect)
		{
			this.effect = effect;
		}
	}
	
	private static interface ColorEffect 
	{
		RGB applyEffect(RGB rgb);
	}
	
	private static class Grayscale_Lightness implements ColorEffect{
		@Override
		public RGB applyEffect(RGB pixel){
			int value = (pixel.min() + pixel.max()) / 2;
			return new RGB(value, value, value);
		}
	}
	
	private static class Grayscale_Average implements ColorEffect {
		@Override
		public RGB applyEffect(RGB pixel){
			int value =  pixel.sum() / 3;
			return new RGB(value, value, value);
		}
	}
	
	private static class Grayscale_Luminosity implements ColorEffect {
		@Override
		public RGB applyEffect(RGB pixel){
			int value =  (int) (.21 * pixel.r + .71 * pixel.g + .07 * pixel.b);
			return new RGB(value, value, value);
		}
	}
}
