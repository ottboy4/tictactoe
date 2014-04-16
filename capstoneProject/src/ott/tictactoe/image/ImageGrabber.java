package ott.tictactoe.image;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ImageGrabber implements ImageWrapper
{
	// singleton approach
	private static ImageGrabber instance = new ImageGrabber();

	public static ImageGrabber getInstance()
	{
		return instance;
	}

	private final Object IMAGE_SYNC = new Object();
	private FrameGrabber grabber = null;
	private BufferedImage current = null;
	private PropertyChangeSupport propSupport = null;

	// for singleton
	private ImageGrabber()
	{
	}

	private class UpdateThread implements Runnable
	{
		@Override
		public void run()
		{
			while (grabber != null)
			{
				BufferedImage bufImage = null;
				try
				{
					if (grabber != null)
					{
						IplImage image = grabber.grab();
						bufImage = image.getBufferedImage();
					}
				} catch (com.googlecode.javacv.FrameGrabber.Exception e)
				{
					// error grabbing image
					stopGrabbing();
					return;
				}
				// update the current after image is grabbed
				synchronized (IMAGE_SYNC)
				{
					current = bufImage;
					if (propSupport != null)
						propSupport.firePropertyChange("image", null, bufImage);
				}
			}
		}
	}

	public void startGrabbing(int deviceId) throws com.googlecode.javacv.FrameGrabber.Exception
	{
		if (grabber != null)
			throw new IllegalStateException("Grabber is already started");
		Thread updater = new Thread(new UpdateThread(), "ImageGrabber: Updater");
		grabber = new VideoInputFrameGrabber(deviceId);
		grabber.start();
		updater.start();
	}

	public void stopGrabbing()
	{
		if (grabber == null)
			throw new IllegalStateException("Grabber has not started");
		try
		{
			grabber.flush();
			grabber.stop();
			grabber.release();
		} catch (com.googlecode.javacv.FrameGrabber.Exception e)
		{
			assert false : "Grabber had a personal problem";
			// swallow - can't do anything about an error there
			// continue and set to null
		}

		grabber = null;
		synchronized (IMAGE_SYNC)
		{
			current = null;
		}
	}

	public BufferedImage getCurrentImage()
	{
		synchronized (IMAGE_SYNC)
		{
			return current;
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		if (propSupport == null)
			propSupport = new PropertyChangeSupport(this);
		propSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		if (propSupport == null)
			throw new IllegalStateException("No listeners to remove");
		propSupport.removePropertyChangeListener(listener);
		int count = propSupport.getPropertyChangeListeners().length;
		if (count == 0)
			propSupport = null; // gc this
	}

	public boolean isGrabbing()
	{
		return grabber != null;
	}

}
