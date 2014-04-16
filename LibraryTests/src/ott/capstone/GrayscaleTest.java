package ott.capstone;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ott.image.ColorEffects;
import ott.image.ColorEffects.ColorEffectType;

public class GrayscaleTest
{
	public static void runDisplay() throws Exception
	{
		BufferedImage img = ImageIO.read(new File("Tulips.jpg"));
		MainClass.display("Original", img);
		img = ColorEffects.applyEffect(img, ColorEffectType.GRAYSCALE_LIGHTNESS);
		MainClass.display("Lightness", img);
		img = ColorEffects.applyEffect(img, ColorEffectType.GRAYSCALE_AVERAGE);
		MainClass.display("Average", img);
		img = ColorEffects.applyEffect(img, ColorEffectType.GRAYSCALE_LUMINOSITY);
		MainClass.display("Luminosity", img);
	}
	

}
