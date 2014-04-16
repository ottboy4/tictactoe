package ott.capstone;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import ott.image.EdgeEffects;
import ott.image.ImageEffects;
import ott.image.LineDetection;
import ott.image.data.Line;
import ott.tictactoe.image.X;
import ott.time.Stopwatch;

public class XDetect
{
	public static void runDisplay() throws Exception
	{
		JFileChooser chooser = new JFileChooser(new File("C:/users/caleb/ott_caleb_capstone/LibraryTests/pictures"));
		chooser.showOpenDialog(null);
		File imgFile = chooser.getSelectedFile();
		if (imgFile == null)
			System.exit(0);
		BufferedImage testImg = ImageIO.read(imgFile);

		testImg = ImageEffects.convertToGrayscale(testImg);
		Stopwatch watch = new Stopwatch();
		watch.start();
		final BufferedImage img = EdgeEffects.detectEdges(testImg, 200, 200);
		watch.stop();
		System.out.println("Detecting edges took: " + watch.getSecond());
		
		JFrame frame = MainClass.display("edges", img, new LookupImage()
		{
			
			@Override
			public void imageGrabbed(BufferedImage image, int x, int y)
			{
				Line l1 = LineDetection.detectLineAtPoint(image, x, y, LineDetection.LEFT_DIAGNOL_LINE);
				System.out.println("Left diag: " + l1);
				Line l2 = LineDetection.detectLineAtPoint(image, x, y, LineDetection.RIGHT_DIAGNOL_LINE);
				System.out.println("right diag: " + l2);
			}
			
			@Override
			public void imageGrabbed(BufferedImage image)
			{
				X x = X.xInImage(image);
				System.out.println("x found: " + x);
			}
		}, null);
		frame.getContentPane().addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(final MouseEvent e)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Line l = LineDetection.detectLineAtPoint(img, e.getX(), e.getY(), LineDetection.RIGHT_DIAGNOL_LINE);
						System.out.println(l);
					}
				}).start();
			}
		});
		
		watch.start();
		Line[] lines = LineDetection.detectLeftDiagnolLines(img);
		watch.stop();
		System.out.println("Getting left diagnol lines took: " + watch.getSecond());
		if (lines != null)
		for (int i = 0; i < lines.length; i++)
			System.out.println(lines[i]);
		else
			System.out.println("failed");
		
		watch.start();
		Line[] lines2 = LineDetection.detectRightDiagnolLines(img);
		watch.stop();
		System.out.println("Getting right diagnol lines took: " + watch.getSecond());
		if (lines2 != null)
		for (int i = 0; i < lines2.length; i++)
			System.out.println(lines2[i]);
		else
			System.out.println("failed");
		X detected = null;
		if (lines != null && lines2 != null)
		{
			detected = X.linesFormX(lines, lines2);
		}
		System.out.println("X: " + detected);
		
		
	}
}
