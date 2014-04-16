package ott.tictactoe.image;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;

public interface ImageWrapper
{
	public BufferedImage getCurrentImage();
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
}
