package ott.tictactoe.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ott.image.data.Point;
import ott.tictactoe.Board;
import ott.tictactoe.Player;

public class BoardTests
{
	Board b = new Board();
	
	public BoardTests()
	{
		b.addMark(Player.Human, new Point(0,0));
		b.addMark(Player.Human, new Point(1,1));
		b.addMark(Player.Human, new Point(2,1));
		b.addMark(Player.Human, new Point(0, 2));
		b.addMark(Player.Computer, new Point(1, 0));
		b.addMark(Player.Computer, new Point(2, 0));
		b.addMark(Player.Computer, new Point(0, 1));
		b.addMark(Player.Computer, new Point(1, 2));
		b.addMark(Player.Computer, new Point(2, 2));
	}

	@Test
	public void testAddMark()
	{
//		fail("Not yet implemented");
	}

	@Test
	public void testClearBoard()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testWhoHas3InARow()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetBoard()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSpotsNextTo()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSpotEmpty()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testEmptySpots()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testComputerSpots()
	{
		fail("Not yet implemented");
	}
	
	private Board copy(Board b)
	{
		Board b2 = new Board();
		Player[][] board = b.getBoard();
		for (int x = 0; x < board.length; x++)
		{
			for (int y = 0; y < board[0].length; y++)
			{
				if (board[x][y] != null)
				{
					b2.addMark(board[x][y], new Point(x,y));
				}
			}
		}
		return b2;
	}

}
