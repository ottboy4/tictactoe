package ott.webcam;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/*
 * Paths that need to be used:
 * 
 * -Djava.library.path="C:\opencv\build\common\tbb\intel64\vc10"
 * -Djava.library.path="C:\opencv\build\x64\vc10\bin"
 */

public class WebcamDisplayTest
{
	public static void main(String[] args) throws Exception
	{
				GrabberShow gs = new GrabberShow();
				Thread th = new Thread(gs);
				th.start();
//		CanvasFrame canvas = new CanvasFrame("blah");
//		Pattern p = new Pattern(ImageIO.read(new File("testImg.png")));
//		canvas.showImage(p.pattSource);

	}
}

class GrabberShow implements Runnable
{
	final int INTERVAL = 50;///you may use interval
	CanvasFrame canvas = new CanvasFrame("Web Cam");
	IplImage img;

	public GrabberShow()
	{
		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void run()
	{
		FrameGrabber grabber = new VideoInputFrameGrabber(0); // 1 for next camera
//		int i = 0;
		final Point p = new Point(0, 0);
		try
		{
			grabber.start();
			Thread t = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					Scanner scan = new Scanner(System.in);
					scan.nextLine();
					p.x = -1;
					String filename = JOptionPane.showInputDialog(null, "Enter fileName");
					cvSaveImage(filename + ".png", img);
				}
			});
			t.start();
			while (p.x == 0)
			{
				img = grabber.grab();
				if (img != null)
				{
					//					canvas.showImage(img);
//					cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
					//					cvSaveImage((i++) + "-aa.jpg", img);
					// show image on window
					
					
					
					IplImage edges = cvCreateImage(cvGetSize(img), 8, 1);
					cvCvtColor(img, edges, CV_RGB2GRAY);
					
					
					cvCanny(edges, edges, 100, 100, 3);
//					canvas.showImage(edges);
					canvas.showImage(img);
				}
//				Thread.sleep(INTERVAL);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}


