package ott.capstone;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import ott.image.EdgeEffects;
import ott.image.data.Line;
import ott.tictactoe.image.X;
import ott.time.Stopwatch;

public class TestX implements Testable
{
	public void runTest()
	{
		testFindingInLines();
//		testCreatingXByLines();
	}
	
	public static void testFindingInLines()
	{
		JFileChooser chooser = new JFileChooser(new File("C:/users/caleb/ott_caleb_capstone/LibraryTests/pictures"));
		chooser.showOpenDialog(null);
		File imgFile = chooser.getSelectedFile();
		if (imgFile == null)
			System.exit(0);
		BufferedImage testImg;
		try
		{
			testImg = ImageIO.read(imgFile);
		} catch (IOException e)
		{
			return;
		}
		
		Stopwatch watch = new Stopwatch();
		watch.start();
		final BufferedImage img = EdgeEffects.detectEdges(testImg, 200, 200);
		watch.stop();
		System.out.println("Detecting edges took: " + watch.getSecond());
		
		X result = X.xInImage(img);
		System.out.println(result);
		// do something here to test!!
		
		
	}
	
	public static void testCreatingXByLines()
	{
		Line left = new Line(241, 81, 554, 329);
		Line right = new Line(241, 83, 536, 327);
		Line[] lefts = new Line[] { left };
		Line[] rights = new Line[] { right };
		X result = X.linesFormX(lefts, rights);
		
		System.out.println(result);
	}
	
}
