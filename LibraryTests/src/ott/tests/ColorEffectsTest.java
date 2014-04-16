package ott.tests;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import org.junit.Test;

import ott.image.ColorEffects;
import ott.image.ColorEffects.ColorEffectType;

public class ColorEffectsTest
{
	final static int[] testImage = new int[]
	{
		0xFF3300
	};

	@Test
	public void testLuminosity()
	{
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, testImage[0]);
		
		BufferedImage result = ColorEffects.applyEffect(img, ColorEffectType.GRAYSCALE_LUMINOSITY);
		
		assertEquals(0xFF595959, result.getRGB(0, 0));
	}

	@Test
	public void testAverage()
	{
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, testImage[0]);
		
		BufferedImage result = ColorEffects.applyEffect(img, ColorEffectType.GRAYSCALE_AVERAGE);
		
		assertEquals(0xFF666666, result.getRGB(0, 0));
	}
	
	@Test
	public void testLightness()
	{
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, testImage[0]);
		
		BufferedImage result = ColorEffects.applyEffect(img, ColorEffectType.GRAYSCALE_LIGHTNESS);
		
		assertEquals(0xFF7F7F7F, result.getRGB(0, 0));
	}
}

