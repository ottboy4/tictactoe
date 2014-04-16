package ott.capstone;



import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import ott.image.EdgeEffects;
import ott.image.LineDetection;
import ott.image.data.Line;
import ott.tictactoe.image.TicTacToeBoard;
import ott.tictactoe.image.X;
import ott.time.Stopwatch;

public class BoardFromPictureDetect
{
	public static void runDisplay() throws Exception
	{
		JFileChooser chooser = new JFileChooser(new File("C:/users/caleb/ott_caleb_capstone/LibraryTests/pictures"));
		chooser.showOpenDialog(null);
		File imgFile = chooser.getSelectedFile();
		if (imgFile == null)
			System.exit(0);
		BufferedImage testImg = ImageIO.read(imgFile);

		Stopwatch watch = new Stopwatch();
//		watch.start();
//		BufferedImage blurredImg = ConvolutionEffects.convoluteImage(testImg, ConvolutionEffects.GAUSSIAN_BLUR);
//		watch.stop();
//		System.out.println("Blur took: " + watch.getSecond());
		watch.start();
		final BufferedImage img = EdgeEffects.detectEdges(testImg, 100, 100);
		watch.stop();
		System.out.println("Detecting edges took: " + watch.getSecond());
//		IplImage ipImage = IplImage.createFrom(testImg);
//		IplImage edges = cvCreateImage(cvGetSize(ipImage), 8, 1);
//		cvCvtColor(ipImage, edges, CV_RGB2GRAY);
//		
//		cvCanny(edges, edges, 100, 100, 3);
		
//		final BufferedImage img = edges.getBufferedImage();

		JFrame frame = MainClass.display("edges", img, new LookupImage()
		{
			@Override
			public void imageGrabbed(BufferedImage image)
			{
				X locatedX = X.xInLocation(image, new Rectangle(0, 0, image.getWidth(), image.getHeight()));
				System.out.println(locatedX);
			}
			
			@Override
			public void imageGrabbed(BufferedImage image, int x, int y)
			{
				System.out.println("BLAHH");
			}
		}, null); // end maginified mouse lisetnere
		
		frame.getContentPane().addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(final MouseEvent e)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Line l = LineDetection.detectLineAtPoint(img, e.getX(), e.getY(), LineDetection.VERTICAL_LINE);
						//Line l = LineDetection.detectLineAtPoint(img, e.getX(), e.getY(), LineDetection.HORIZONTAL_LINE);
						System.out.println(l);
					}
				}).start();
			}
		});

		watch.start();
		Line[] verticals = LineDetection.detectVerticalLines(img);
		watch.stop();
		System.out.println("Getting vertical lines took: " + watch.getSecond());
		if (verticals != null)
			for (int i = 0; i < verticals.length; i++)
				System.out.println(verticals[i]);
		else
			System.out.println("failed");

		watch.start();
		Line[] horizontals = LineDetection.detectHorizontalLines(img);
		watch.stop();
		System.out.println("Getting horizontal lines took: " + watch.getSecond());
		if (horizontals != null)
			for (int i = 0; i < horizontals.length; i++)
				System.out.println(horizontals[i]);
		else
			System.out.println("failed");

		if (horizontals != null && verticals != null)
		{
			TicTacToeBoard board = TicTacToeBoard.locateBoard(horizontals, verticals);
			System.out.println("TicTacToeBoard: " + board);
		}
	}
}
