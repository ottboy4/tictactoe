package ott.program.test;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import ott.capstone.MainClass;
import ott.image.EdgeEffects;
import ott.tictactoe.Board;
import ott.tictactoe.Game;
import ott.tictactoe.Player;
import ott.tictactoe.image.ImageWrapper;
import ott.tictactoe.image.TicTacToeBoard;

public class GameTest
{
	public static void runTest() throws Exception
	{
//		JFileChooser chooser = new JFileChooser(new File("C:/users/caleb/pictures"));
//		chooser.showOpenDialog(null);
//		File imgFile = chooser.getSelectedFile();
//		if (imgFile == null)
//			System.exit(0);
//		BufferedImage img = ImageIO.read(imgFile);
//		BufferedImage imgLines = EdgeEffects.detectEdges(img, 100, 100);
		
		
		ScreenCapture cap = new ScreenCapture();
		TicTacToeBoard board = null;
		BufferedImage imgLines = null;
		do 
		{
			if (cap.getCurrentImage() != null)
			{
				imgLines = EdgeEffects.detectEdges(cap.getCurrentImage(), 100, 100);
				board = TicTacToeBoard.locateBoard(imgLines);
			}
		} while (imgLines == null || board == null);
		assert board != null;
		
		System.out.println("Board found");
		
		Game g = new Game(cap, board);

		g.addPropertyChangeListener(new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				if (evt.getPropertyName().equals("board"))
					printArray((Board) evt.getNewValue());
//				else if (evt.getPropertyName().equals("winner"))
//					System.out.println("Game over. " + evt.getNewValue() + " won.");
			}

			private void printArray(Board value)
			{
				Object[][] boardArray = value.getBoard();
				for (int i = 0; i < boardArray.length; i++)
				{
					for (int j = 0; j < boardArray[0].length; j++)
					{
						String name = boardArray[i][j] == Player.Computer ? "Comp" : boardArray[i][j] == Player.Human ? "Human" : "empty";
						System.out.print(name + "\t");
					}
					System.out.println();
				}
				System.out.println();
			}
			
		});

	}

	private static class ImagePuller implements ImageWrapper
	{
		private BufferedImage image;

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
		
			
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener)
		{

			
		}
	}
}
