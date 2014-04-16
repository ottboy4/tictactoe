package ott.program.test;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ott.tictactoe.image.ImageWrapper;

public class ImagePuller implements ImageWrapper
{
	private BufferedImage image;
	private PropertyChangeSupport support;

	public ImagePuller(BufferedImage img)
	{
		image = img;
	}

	@Override
	public BufferedImage getCurrentImage()
	{
		return image;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		if (support == null)
		{
			support = new PropertyChangeSupport(this);
			Thread send = new Thread(new Sender());
			send.start();
		}
		support.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		support.removePropertyChangeListener(listener);
		if (support.getPropertyChangeListeners().length == 0)
			support = null;
	}
	
	private class Sender implements Runnable
	{
		@Override
		public void run()
		{
			while (support != null)
			{
				support.firePropertyChange("image", null, image);
				try
				{
					Thread.sleep(50);
				} catch (InterruptedException e)
				{
				}
			}
		}
	}
}
