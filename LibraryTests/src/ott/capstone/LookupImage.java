package ott.capstone;

import java.awt.image.BufferedImage;

public interface LookupImage
{
	public void imageGrabbed(BufferedImage image);
	public void imageGrabbed(BufferedImage image, int x, int y);
}
