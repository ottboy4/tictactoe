package ott.tictactoe;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import ott.image.data.Point;

public class Board
{
	private Player[][] board = new Player[3][3];
	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	public void addMark(Player who, Point spot)
	{
		if (board[spot.y][spot.x] != null)
			throw new IllegalStateException("A mark is already on that spot");
		board[spot.y][spot.x] = who;
		support.firePropertyChange("board", null, this);
	}

	public void clearBoard()
	{
		Player[][] oldBoard = board;
		board = new Player[3][3];
		support.firePropertyChange("board", oldBoard, this);
	}

	public Player whoHas3InARow()
	{
		for (int y = 0; y < board.length; y++)
			for (int x = 0; x < board[0].length; x++)
			{
				Player current = board[x][y];
				if (current == null)
					continue;

				for (double rad = 0; rad < Math.PI * 2; rad += Math.PI / 4)
				{
					int dx = (int) Math.round(Math.sin(rad));
					int dy = (int) Math.round(Math.cos(rad));
					Point temp = new Point(y,x);
					int numFound = 1;
					do {
						temp.translate(dx, dy);
						if (temp.x > 2 || temp.y > 2 || temp.x < 0 || temp.y < 0)
							break;
						if (board[temp.y][temp.x] == current)
							numFound++;
						else
							break;
						if (numFound > 2) 
							break;
					} while(true);
					if (numFound > 2)
					{
						return current;
					}
				}
			}
		return null;
	}

	public Player[][] getBoard()
	{
		return board;
	}

	/**
	 * returns all the spots that are empty next to the passed in spot
	 * 
	 * @param spot
	 * @return
	 */
	public Point[] spotsNextTo(Point spot)
	{
		return moves(spot).toArray(new Point[0]);
	}

	public boolean spotEmpty(Point spot)
	{
		return board[spot.y][spot.x] == null;
	}

	public Point[] emptySpots()
	{
		List<Point> spots = new ArrayList<>();
		for (int y = 0; y < board.length; y++)
			for (int x = 0; x < board[0].length; x++)
				if (board[x][y] == null)
					spots.add(new Point(y, x));
		return spots.toArray(new Point[spots.size()]);
	}

	public Point[] computerSpots()
	{
		List<Point> spots = new ArrayList<>();
		for (int y = 0; y < board.length; y++)
			for (int x = 0; x < board[0].length; x++)
				if (board[x][y] == Player.Computer)
					spots.add(new Point(y, x));
		return spots.toArray(new Point[spots.size()]);
	}

	private List<Point> moves(Point startPos)
	{
		List<Point> possibleMoves = new ArrayList<Point>();
		for (double rad = 0; rad < Math.PI * 2; rad += Math.PI / 4)
		{
			int x = (int) Math.round(Math.sin(rad));
			int y = (int) Math.round(Math.cos(rad));
			Point temp = new Point(startPos);
			temp.translate(x, y);
			if (temp.x > 2 || temp.y > 2 || temp.x < 0 || temp.y < 0)
				continue;
			if (board[temp.y][temp.x] == null)
				possibleMoves.add(new Point(temp.x, temp.y));
		}
		return possibleMoves;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		support.removePropertyChangeListener(listener);
	}

}
