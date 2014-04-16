package ott.capstone;

import java.awt.Rectangle;

import ott.image.data.Line;
import ott.image.data.Point;
import ott.tictactoe.image.TicTacToeBoard;

public class TestBoardDetector
{
	public static void runTest()
	{
		testFindBoard();

		
	}
	
	public static void testGettingSquareLocation()
	{
		
		Line top = new Line(10, 100, 500, 120);
		Line bottom = new Line(50, 300, 550, 320);
		Line left = new Line(200, 10, 220, 400);
		Line right = new Line(400, 30, 420, 450);
		
		TicTacToeBoard board = new TicTacToeBoard(left, right, top, bottom);

		for (int r = 0; r < 3; r++)
			for (int c = 0; c < 3; c++)
			{
				System.out.print("R: " + r + " C: " + c + " ");
				Rectangle rec = board.getSquareLocation(new Point(r, c));
				System.out.println(rec);
			}
	}
	
	
	public static void testFindBoard()
	{
		Line top = new Line(10, 100, 500, 120);
		Line bottom = new Line(50, 300, 550, 320);
		Line left = new Line(200, 10, 220, 400);
		Line right = new Line(400, 30, 420, 450);
		
		TicTacToeBoard board = TicTacToeBoard.locateBoard(new Line[] { top, bottom }, new Line[] { left, right });
		assert board != null;
		System.out.println(board.getTop());
		System.out.println(board.getBottom());
		System.out.println(board.getLeft());
		System.out.println(board.getRight());
	}
}
