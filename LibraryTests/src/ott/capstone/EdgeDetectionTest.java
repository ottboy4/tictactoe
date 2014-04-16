package ott.capstone;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ott.image.ColorEffects;
import ott.image.ColorEffects.ColorEffectType;
import ott.image.EdgeEffects;
import ott.image.data.Direction;
import ott.image.data.Edge;

public class EdgeDetectionTest
{
	public static void runDisplay() throws Exception
	{
		//		BufferedImage testImg = ImageIO.read(new File("Tulips.jpg"));
		//		BufferedImage testImg = ImageIO.read(new File("Aqua-Ball-Red-icon.png"));
		JFileChooser chooser = new JFileChooser(new File("C:/users/caleb/pictures"));
		chooser.showOpenDialog(null);
		File imgFile = chooser.getSelectedFile();
		//		BufferedImage testImg = ImageIO.read(new File("C:/users/caleb/Pictures/Desktop Backrounds/Power_Mac_by_PepsiMan12.jpg"));
		BufferedImage testImg = ImageIO.read(imgFile);

		//		BufferedImage img = ConvolutionTest.convertPic(testImg, ConvolutionTest.blur);
		BufferedImage img = ColorEffects.applyEffect(testImg, ColorEffectType.GRAYSCALE_AVERAGE);

		//		MainClass.display("edges", img);

		JFrame panel = new JFrame("edge detections");
		JLabel label = new JLabel();
		final Edge[][] edges = EdgeEffects.findEdgesInImage(img, null, 200, 100);
		img = runDetectEdges(img);
		label.setIcon(new ImageIcon(img));
		label.setSize(1440, 900);
		label.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				int x = e.getX();
				int y = e.getY();
				System.out.println(Math.toDegrees(edges[x][y].theta) + " " + Direction.determineDirection(edges[x][y].theta) + " strength:" + edges[x][y].strength);
				System.out.println("x" + x + " y" + y);
			}
		});
		panel.add(label);
		panel.pack();
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setVisible(true);

	}

	public static BufferedImage runDetectEdges(BufferedImage img)
	{
		long before = System.nanoTime();
		BufferedImage img2 = EdgeEffects.detectEdges(img, 200, 100);
		long after = System.nanoTime();
		double timeTaken = (after - before) / 1000000000.0;
		System.out.println("Edges time: " + timeTaken);

		return img2;
	}

}
