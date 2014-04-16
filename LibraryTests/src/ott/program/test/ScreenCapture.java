package ott.program.test;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ott.tictactoe.image.ImageWrapper;

public class ScreenCapture implements ImageWrapper
{
    BufferedImage img =null;
	Robot bot;
	PropertyChangeSupport support;
	
	public ScreenCapture()
	{
		try
		{
			bot = new Robot();
		} catch (AWTException e)
		{
		}
		support = new PropertyChangeSupport(this);
		Thread t = new Thread(new Updator());
		t.start();
	}
	
	private class Updator implements Runnable
	{
		@Override
		public void run()
		{
			while (true)
			{
//				Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
//				size.width /= 2;
//				size.width -= 300;
//				size.height -= 500;
//				img = bot.createScreenCapture(new Rectangle(new Point(size.width + 500, 150), size));
				
				img = bot.createScreenCapture(new Rectangle(1200, 400, 400, 400));
				support.firePropertyChange("image", null, img);
			}
		}
	}

	@Override
	public BufferedImage getCurrentImage()
	{
		return img;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		support.addPropertyChangeListener(listener);
	}


	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		support.removePropertyChangeListener(listener);
	}

	
	
}
