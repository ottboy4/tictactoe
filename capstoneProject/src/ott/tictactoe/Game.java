package ott.tictactoe;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ott.image.ConvolutionEffects;
import ott.image.EdgeEffects;
import ott.image.ImageEffects;
import ott.image.data.Point;
import ott.image.data.RGB;
import ott.tictactoe.image.ImageWrapper;
import ott.tictactoe.image.TicTacToeBoard;
import ott.tictactoe.image.X;

public class Game
{
	private static final int TIME_TO_MOVE = 2500;
	
	private Board board;
	private ImageWrapper image;
	private TicTacToeBoard ticTacToeBoard;
	private PropertyChangeSupport support;
	private BufferedImage boardImage;
	
	private boolean running = true;

	public Game(ImageWrapper image, TicTacToeBoard ticTacToeBoard)
	{
		this.image = image;
		this.board = new Board();
		this.ticTacToeBoard = ticTacToeBoard;
		support = new PropertyChangeSupport(this);
		Thread xDetector = new Thread(new XLocator(), "XLocator");
		xDetector.start(); // start detecting x's in board
	}
	
	public void makeHumanMove(Point spot)
	{
		assert (board.spotEmpty(spot)) : "Player played in an already played spot :(";
		// TODO remove dis
		System.out.println("Human moved at Column: " + spot.x + " - Row: " + spot.y);
		support.firePropertyChange("message", null, "Human moved at - Column: " + (spot.x + 1) + " - Row: " + (spot.y + 1));
		if (!gameIsOver())
		{
			board.addMark(Player.Human, spot);
			playComputerMove();
		} else
		{
			fireWinnerEvent();
		}
	}
	
	public void stop()
	{
		running = false;
	}

	private boolean gameIsOver()
	{
		if (board.emptySpots().length == 0)
			return true;

		Player winner = board.whoHas3InARow();
		return winner != null;
	}

	private void playComputerMove()
	{
		if (!gameIsOver())
		{
//			support.firePropertyChange("message", null, "Computer's turn");
			try {
				Thread.sleep(TIME_TO_MOVE);
			} catch (InterruptedException e)
			{
				// swallow - if sleep messes up
			}
			Point playSpot = null;
			Point[] compSpots = board.computerSpots();
			for (Point spot : compSpots)
			{
				if (playSpot != null)
					break;
				Point[] aroundSpots = board.spotsNextTo(spot);
				for (Point aroundSpot : aroundSpots)
				{
					if (board.spotEmpty(aroundSpot))
						playSpot = aroundSpot;
				}
			}

			for (Point spot : board.emptySpots())
			{
				if (playSpot == null)
					playSpot = spot;
			}
			board.addMark(Player.Computer, playSpot);
			if (gameIsOver())
				fireWinnerEvent();
			else
				support.firePropertyChange("message", null, "Human's turn");
		} else
		{
			fireWinnerEvent();
		}
	}

	private void fireWinnerEvent()
	{
		support.firePropertyChange("gameOver", null, board.whoHas3InARow());
	}

	/**
	 * board property is fired whenever a mark is added to the board or when the
	 * board is cleared
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		support.addPropertyChangeListener(listener);
		board.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		support.removePropertyChangeListener(listener);
		board.removePropertyChangeListener(listener);
	}

	public void setTicTacToeBoard(TicTacToeBoard board)
	{
		ticTacToeBoard = board;
	}

	public void setBoardImage(BufferedImage boardImage)
	{
		this.boardImage = boardImage;
	}

	private class XLocator implements Runnable
	{
		@Override
		public void run()
		{
			while (running && !gameIsOver())
			{
				try
				{
					Thread.sleep(500);
				} catch (InterruptedException e)
				{
					// awallow - if sleep threw exception
				}
				BufferedImage newImg = image.getCurrentImage(); // grab new image after sleep
				newImg = ImageEffects.copy(newImg);
				int mostLeft = ticTacToeBoard.getMostLeft();
				int mostTop = ticTacToeBoard.getMostTop();
				int width = ticTacToeBoard.getWidth();
				int height = ticTacToeBoard.getHeight();
				BufferedImage image = newImg.getSubimage(mostLeft, mostTop, width, height);
				image = ImageEffects.copy(image);
				boolean imagesAreEqual = imagesAreEqual(boardImage, image); // checking against the static board image
				// TODO remove dis
				System.out.println(imagesAreEqual);
//				support.firePropertyChange("message", null, imagesAreEqual);
				
				
				if (imagesAreEqual) // compare if they are equal enough
				{
					lookForX(newImg); // if they are, try to make a human move
				}
			}
		}

		private boolean imagesAreEqual(BufferedImage oldImg, BufferedImage newImg)
		{
			final int percent_threshold = 10;
			
			int height = oldImg.getHeight();
			int width = oldImg.getWidth();
			
			double sum = 0;
			for (int x = 0; x < width; x++)
			{
				for (int y = 0; y < height; y++)
				{
					RGB oldConvolute = ConvolutionEffects.convolute(x, y, oldImg, ConvolutionEffects.GAUSSIAN_BLUR);
					RGB newConvolute = ConvolutionEffects.convolute(x, y, newImg, ConvolutionEffects.GAUSSIAN_BLUR);
					int diff = Math.abs(oldConvolute.average() - newConvolute.average());
					sum += diff;
				}
			}
			sum /= height * width * 1.0;
			System.out.println(sum);
			return sum < percent_threshold;
		}

		private void lookForX(BufferedImage image)
		{
			// search open spots of board for an X - call play move if x is found
			image = ImageEffects.convertToGrayscale(image);
			image = EdgeEffects.detectEdges(image, Program.EDGE_THRESHOLD, Program.EDGE_THRESHOLD);
			Point[] emptySpots = board.emptySpots();
			for (Point spot : emptySpots)
			{
				Rectangle spotLoc = ticTacToeBoard.getSquareLocation(spot);
				X x = X.xInLocation(image, spotLoc);
				if (x != null)
				{
					makeHumanMove(spot);
					return;
				}
			}
		}
	}

}
