package ott.capstone;

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
import ott.time.Stopwatch;

public class LineStuffTest
{
	public static void runDisplay() throws Exception
	{

		JFileChooser chooser = new JFileChooser(new File("C:/users/caleb/pictures"));
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
		final BufferedImage img = EdgeEffects.detectEdges(testImg, 200, 200);
		watch.stop();
		System.out.println("Detecting edges took: " + watch.getSecond());
		
		JFrame frame = MainClass.display("edges", img);
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
//						Line l = LineDetection.detectLineAtPoint(img, e.getX(), e.getY(), LineDetection.HORIZONTAL_LINE);
						System.out.println(l);
					}
				}).start();
			}
		});
		
		watch.start();
		Line[] verticals = LineDetection.detectVerticalLines(img);
		watch.stop();
		System.out.println("Getting vertical lines took: " + watch.getSecond());
//		if (lines != null)
//		for (int i = 0; i < lines.length; i++)
//			System.out.println(lines[i]);
//		else
//			System.out.println("failed");
		
		watch.start();
		Line[] horizontals = LineDetection.detectHorizontalLines(img);
		watch.stop();
		System.out.println("Getting horizontal lines took: " + watch.getSecond());
//		if (lines2 != null)
//		for (int i = 0; i < lines2.length; i++)
//			System.out.println(lines2[i]);
//		else
//			System.out.println("failed");

//		if (lines2 != null)
//			for (Line l : lines2)
//				drawLine(testImg, l);
		
		
		TicTacToeBoard board = TicTacToeBoard.locateBoard(horizontals, verticals);
		System.out.println(board);
		
		
	}
	
	public static void drawLine(BufferedImage img, Line line)
	{
		drawLine(img, line.x1, line.y1, line.x1, line.y2);
		drawLine(img, line.x1, line.y2, line.x2, line.y2);
		drawLine(img, line.x2, line.y1, line.x2, line.y2);
		drawLine(img, line.x1, line.y1, line.x2, line.y1);
	}
	
	 static void drawLine(BufferedImage img, int x, int y, int x2, int y2)
	{
		assert (x <= x2 && y <= y2);

		for (; x <= x2; x++)
			for (; y <= y2; y++)
			{
				img.setRGB(x, y, 0xFF00FF00);
			}
	}
}











