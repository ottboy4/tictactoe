package ott.tictactoe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import ott.capstone.MainClass;
import ott.image.EdgeEffects;
import ott.image.ImageEffects;
import ott.image.data.Line;
import ott.image.data.Point;
import ott.tictactoe.image.ImageGrabber;
import ott.tictactoe.image.ImageWrapper;
import ott.tictactoe.image.TicTacToeBoard;

import com.googlecode.javacv.FrameGrabber.Exception;

public class Program extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final int EDGE_THRESHOLD = 100;
	private final static Dimension MIN_SIZE = new Dimension(932, 685);

	private Game game;
	private DrawnPanel mainPanel;
	private ImageWrapper imageWrapper = null;
	private TicTacToeBoard board = null;
	private BufferedImage boardImage = null;

	public Program(ImageWrapper wrapper)
	{
		super("Tic Tac Toe");
		imageWrapper = wrapper;
		mainPanel = new DrawnPanel(imageWrapper, this);
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(MIN_SIZE);
	}

	/*
	 * Runs on separate thread locate a board using the webcam when a board is
	 * detected it automatically calls "start game" starting the game
	 */
	public void recognizeBoard()
	{
		board = null;
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				String previousText = mainPanel.getText();
//				mainPanel.setText("Detecting board", Color.RED);
				mainPanel.setText(null, Color.RED);
				TicTacToeBoard board = null;
				while (board == null)
				{
					BufferedImage image = imageWrapper.getCurrentImage();
					BufferedImage grayedImage = ImageEffects.convertToGrayscale(image);
					BufferedImage edgedImage = EdgeEffects.detectEdges(grayedImage, EDGE_THRESHOLD, EDGE_THRESHOLD);
					if (edgedImage != null)
					{
						board = TicTacToeBoard.locateBoard(edgedImage);
						if (board != null)
						{
							boardImage = image.getSubimage(board.getMostLeft(), board.getMostTop(), board.getWidth(), board.getHeight());
							boardImage = ImageEffects.copy(boardImage);
							if (game != null)
								game.setBoardImage(boardImage);
							// TODO remove tihs
							BufferedImage newImage = edgedImage.getSubimage(0, 0, edgedImage.getWidth(), edgedImage.getHeight());
							System.out.println(board);
							MainClass.display("recognized baord", newImage, board).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
					}
				}
				mainPanel.setText("Board found", Color.GREEN);

				Program.this.board = board;
				if (game != null)
				{
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e)
					{
					}
					mainPanel.setText(previousText, Color.BLACK);
				}
			}
		}, "Program: Recognize Board");
		t.start();
	}

	/**
	 * returns whether or not game started
	 * 
	 * @return
	 */
	public boolean startGame()
	{
		if (board == null)
			return false;
		if (game != null)
			game.stop();
		mainPanel.setText("Human's turn", Color.BLACK);
		game = new Game(imageWrapper, board);
		game.setBoardImage(boardImage);
		mainPanel.displayBoard.clearCircles();
		game.addPropertyChangeListener(new PropertyChangeListener()
		{
			// callback from board to update GUI
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				String propName = evt.getPropertyName();
				switch (propName)
				{
				case "board":
				{
					Board board = (Board) evt.getNewValue();
					Point[] spots = board.computerSpots();
					mainPanel.displayBoard.clearCircles();
					for (Point p : spots)
						mainPanel.displayBoard.addCircle(p);
					break;
				}
				case "message":
				{
					mainPanel.setText(evt.getNewValue().toString(), Color.BLACK);
					break;
				}
				case "gameOver":
				{
					Player winner = (Player) evt.getNewValue();
					String winnerText = winner == null ? "Tie!" : winner + " won!";
					mainPanel.setText(winnerText, Color.BLACK);
					game.removePropertyChangeListener(this);
					game = null;
					break;
				}
				}
			}
		});
		return true;
	}

	public static void main(String[] args)
	{
		try
		{
			final ImageGrabber wrapper = ImageGrabber.getInstance();
			wrapper.startGrabbing(0);
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					Program p = new Program(wrapper);
					p.setVisible(true);
				}
			});
		} catch (Exception e)
		{
			assert false : "Webcam exception threw";
			JOptionPane.showMessageDialog(null, "Unable to start webcam");
		}
	}
}

class DrawnPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	final TicTacToeDisplay displayBoard;
	final JLabel text = new JLabel("", JLabel.CENTER);
	Font textFont = null;

	BufferedImage image = null;
	String msg = null;
	Program program;

	public DrawnPanel(ImageWrapper image, Program p)
	{
		super(new BorderLayout());
		program = p;
		image.addPropertyChangeListener(new ImageUpdator());
		displayBoard = new TicTacToeDisplay();
		textFont = new Font("Arial", Font.BOLD, 25);
		text.setFont(textFont);
		setupDisplayBoard();
		setupBottom();
	}

	private void setupBottom()
	{
		final int bottom_height = 200;
		final Dimension buttons_size = new Dimension(150, bottom_height);
		final Dimension webcam_size = new Dimension(300, bottom_height);
		JPanel bottom = new JPanel(new BorderLayout());
		bottom.add(text, BorderLayout.CENTER);
		JPanel left = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		left.setPreferredSize(buttons_size);

		JButton setBoard = new JButton("Detect Board");
		JButton startGame = new JButton("Start Game");
		left.add(setBoard);
		left.add(startGame);

		setBoard.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				program.recognizeBoard();
			}
		});

		startGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				program.startGame();
			}
		});

		bottom.add(left, BorderLayout.WEST);
		JComponent right = new JComponent()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g)
			{
				if (image != null)
				{
					double imgRatio = image.getWidth() * 1.0 / image.getHeight();
					double altHeight = this.getWidth() / imgRatio;
					double altWidth = this.getHeight() * imgRatio;
					int width;
					int height;
					if (altHeight > this.getHeight())
					{
						width = (int) altWidth;
						height = this.getHeight();
					} else
					{
						width = this.getWidth();
						height = (int) altHeight;
					}
					int x = (this.getWidth() - width) / 2;
					int y = (this.getHeight() - height) / 2;
					g.drawImage(image, x, y, width, height, null);
				}
			}
		};
		right.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.GRAY, Color.BLACK));
		right.setPreferredSize(webcam_size);
		bottom.add(right, BorderLayout.EAST);

		this.add(bottom, BorderLayout.NORTH); // TODO this is where change whether stuff is on top or bottom
	}

	private void setupDisplayBoard()
	{
		final int gap = 20;
		final JPanel top = new JPanel(null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			// paints the board (and sizes and locates) on this component
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				int smallerSize = Math.min(this.getSize().width, this.getSize().height);
				smallerSize -= gap * 2;
				int x = (this.getWidth() - smallerSize) / 2;
				int y = (this.getHeight() - smallerSize) / 2;
				displayBoard.setLocation(x, y);
				displayBoard.setSize(smallerSize, smallerSize);
				displayBoard.paint(g);
			}
		};
		displayBoard.setBackground(getBackground());
		this.add(top, BorderLayout.CENTER);
	}

	private class ImageUpdator implements PropertyChangeListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt)
		{
			if ("image".equals(evt.getPropertyName()))
			{
				image = (BufferedImage) evt.getNewValue();
				repaint();
			}
		}
	}

	public void setText(String t, Color c)
	{
		text.setText(t);
		text.setForeground(c);
	}
	
	public String getText()
	{
		return text.getText();
	}

	public void setTextFont(Font f)
	{
		textFont = f;
	}
}

class TicTacToeDisplay extends JComponent
{
	private static final long serialVersionUID = 1L;

	final TicTacToeBoard board = new TicTacToeBoard();
	final List<Point> circles = new LinkedList<>();

	final Color CIRCLE_COLOR = Color.BLACK;
	final Color BOARD_COLOR = Color.BLACK;

	public TicTacToeDisplay()
	{
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				adjustSize();
			}

			@Override
			public void componentMoved(ComponentEvent e)
			{
				adjustSize();
			}
		});
		adjustSize();
	}

	private void adjustSize()
	{
		final int width = getWidth();
		final int height = getHeight();
		final int line_width = Math.max(width, height) / 75;
		final int heightThird = (height - line_width * 2) / 3;
		final int widthThird = (width - line_width * 2) / 3;

		final int x = this.getX();
		final int y = this.getY();

		board.left = Line.buildLine(x + widthThird, y, line_width, height);
		board.right = Line.buildLine(x + widthThird * 2 + line_width, y, line_width, height);
		board.top = Line.buildLine(x, y + heightThird, width, line_width);
		board.bottom = Line.buildLine(x, y + heightThird * 2 + line_width, width, line_width);
	}

	public void addCircle(Point loc)
	{
		circles.add(loc);
		repaint();
	}

	public void clearCircles()
	{
		circles.clear();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(BOARD_COLOR);
		for (Line l : board.getLines())
			drawLine(l, g);
		g.setColor(CIRCLE_COLOR);
		for (Point loc : circles)
			drawCircle(loc, g);
	}

	private void drawCircle(Point square, Graphics g)
	{
		Rectangle loc = board.getSquareLocation(square);

		int outerWidth = loc.width / 5;
		int outerX = loc.x + outerWidth;
		outerWidth = loc.width - outerWidth * 2;

		int outerHeight = loc.height / 5;
		int outerY = loc.y + outerHeight;
		outerHeight = loc.height - outerHeight * 2;

		int innerWidth = loc.width / 4;
		int innerX = loc.x + innerWidth;
		innerWidth = loc.width - innerWidth * 2;

		int innerHeight = loc.height / 4;
		int innerY = loc.y + innerHeight;
		innerHeight = loc.height - innerHeight * 2;

		g.setColor(Color.BLACK);
		g.fillOval(outerX, outerY, outerWidth, outerHeight); // outer
		g.setColor(getBackground());
		g.fillOval(innerX, innerY, innerWidth, innerHeight); // inner
	}

	private void drawLine(Line l, Graphics g)
	{
		g.fillRect(l.x1, l.y1, l.w, l.h);
	}
}
