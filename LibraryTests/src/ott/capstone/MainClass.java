package ott.capstone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ott.image.ImageEffects;
import ott.image.data.Line;
import ott.tictactoe.image.TicTacToeBoard;

public class MainClass
{
	public static void main(String[] args) throws Exception
	{
		//		EdgeDetectionTest.runDisplay();
		//		ConvolutionTest.testApplyKernel2();
		//		GrayscaleTest.runDisplay();
		//		new TestX().runTest();
//				TestBoardDetector.runTest();
//				LineStuffTest.runDisplay(); // find tictactoe board in pc image - grid2.png
				XDetect.runDisplay(); // recognize x - anothder x.png
//				BoardFromPictureDetect.runDisplay(); // worked on projector images - logitech_dark_screen_image1.png
//		GameTest.runTest();

	}
	
	public static JFrame display(String type, BufferedImage img, TicTacToeBoard boardToDraw)
	{
		return display(type, img, null, boardToDraw);
	}
	
	public static JFrame display(String type, BufferedImage img)
	{
		return display(type, img, null, null);
	}

	public static JFrame display(String type, BufferedImage img, LookupImage magnifiedImageListener, final TicTacToeBoard b)
	{
		JFrame frame = new JFrame(type);
		
		BufferedImage image = img;
		int imgW = image.getWidth();
		int imgH = image.getHeight();

		frame.setContentPane(new ContentPanel(image, magnifiedImageListener, b));

		frame.setMinimumSize(new Dimension(imgW, imgH));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		return frame;
	}

	private static class ContentPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final BufferedImage image;
		Line box = null;
		boolean dragged = false;
		LookupImage parentListener;
		TicTacToeBoard b;
		

		public ContentPanel(BufferedImage img, LookupImage parentListener, TicTacToeBoard b)
		{
			super(new BorderLayout());
			this.b = b;
			this.parentListener = parentListener;
			MouseDraggerDrawer draggerdrawer = new MouseDraggerDrawer();
			this.addMouseListener(draggerdrawer);
			this.addMouseMotionListener(draggerdrawer);
			image = img;
		}

		class MouseDraggerDrawer extends MouseAdapter implements MouseMotionListener
		{
			int startX = -1;
			int startY = -1;

			@Override
			public void mouseDragged(MouseEvent e)
			{
				dragged = true;
				int x = Math.min(startX, e.getX());
				int y = Math.min(startY, e.getY());
				int x2 = Math.max(startX, e.getX());
				int y2 = Math.max(startY, e.getY());
				box = new Line(x, y, x2, y2);
				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				dragged = false;
				if (e.getButton() == MouseEvent.BUTTON2)
					box = null;
				startX = e.getX();
				startY = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				repaint();

				if (dragged)
				{
					if (box.w != 0 && box.w != 0)
					{
						final BufferedImage tempPic = image.getSubimage(box.x1, box.y1, box.w, box.h);
						final BufferedImage subPicture = ImageEffects.copy(tempPic);
						final ImageMag mag = new ImageMag(image, subPicture, box.x1, box.y1);
						
						if (parentListener != null)
						{
							new Thread(new Runnable()
							{
								@Override
								public void run()
								{
									parentListener.imageGrabbed(subPicture);
								}
							}, "Listener grabbed").start();
							mag.getContentPane().addMouseListener(new MouseAdapter()
							{
								@Override
								public void mouseClicked(final MouseEvent e)
								{
									new Thread(new Runnable()
									{
										@Override
										public void run()
										{
											
											int mouseX = e.getX();
											int mouseY = e.getY();
											
											double ratioX = 1.0 * mag.getContentPane().getWidth() / subPicture.getWidth();
											double ratioY = 1.0 * mag.getContentPane().getHeight() / subPicture.getHeight();
											
											int coordLocaleX = ((int) (mouseX / ratioX));
											int coordLocaleY = ((int) (mouseY / ratioY));
											
											parentListener.imageGrabbed(subPicture, coordLocaleX, coordLocaleY);
										}
									}, "Image grabbed with coordinates").start();
								}
							});
						}
					}
				}
				box = null;
			}
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(image, 0, 0, null);
		}

		@Override
		protected void paintChildren(Graphics g)
		{
			super.paintChildren(g);
			int mouseX = MouseInfo.getPointerInfo().getLocation().x;
			int mouseY = MouseInfo.getPointerInfo().getLocation().y;
			mouseX -= this.getLocationOnScreen().x;
			mouseY -= this.getLocationOnScreen().y;

			String coordX = "X: " + mouseX;
			String coordY = "Y: " + mouseY;
			String color = null;
			try
			{
				color = Integer.toHexString(image.getRGB(mouseX, mouseY)).toUpperCase();
			} catch (Exception e)
			{
			}
			g.setColor(Color.WHITE);
			g.drawString(coordX, 30, 30);
			g.drawString(coordY, 30, 50);
			if (color != null)
				g.drawString(color, 30, 70);

			g.setColor(Color.GREEN);
			if (box != null)
				g.drawRect(box.x1, box.y1, box.w, box.h);
			
			
			if (b != null)
			{
				g.setColor(Color.green);
				g.drawRect(b.top.x1, b.top.y1, b.top.w, b.top.h);
				g.drawRect(b.bottom.x1, b.bottom.y1, b.bottom.w, b.bottom.h);
				g.drawRect(b.left.x1, b.left.y1, b.left.w, b.left.h);
				g.drawRect(b.right.x1, b.right.y1, b.right.w, b.right.h);
			}
			
		}
	};

	private static class ImageMag extends JFrame
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final Image img;

		public ImageMag(final BufferedImage fullImage, final Image image, final int offsetX, final int offsetY)
		{
			super("Magnifier");
			img = image;
			final int width2 = img.getWidth(null);
			final int height2 = img.getHeight(null);

			double ratio = 1.0 * width2 / height2;
			this.setMinimumSize(new Dimension((int) (500 * ratio), 500));
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setLocationRelativeTo(null);

			this.setContentPane(new JPanel(new BorderLayout())
			{
				private static final long serialVersionUID = 1L;
				@Override
				protected void paintChildren(Graphics g)
				{
					g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);

					int mouseX = MouseInfo.getPointerInfo().getLocation().x;
					int mouseY = MouseInfo.getPointerInfo().getLocation().y;
					mouseX -= this.getLocationOnScreen().x;
					mouseY -= this.getLocationOnScreen().y;
					
					double ratioX = 1.0 * this.getWidth() / width2;
					double ratioY = 1.0 * this.getHeight() / height2;
					
					
					String coordLocaleX = "Locale X: " + ((int) (mouseX / ratioX));
					String coordLocaleY = "Locale Y: " + ((int) (mouseY / ratioY));

					g.setColor(Color.WHITE);
					g.drawString(coordLocaleX, 30, 70);
					g.drawString(coordLocaleY, 30, 90);

					
					if (ratioX != 0 && ratioY != 0)
					{
						mouseX = (int) (mouseX / ratioX);
						mouseY = (int) (mouseY / ratioY);
						mouseX += offsetX;
						mouseY += offsetY;

						String coordX = "X: " + mouseX;
						String coordY = "Y: " + mouseY;
						g.setColor(Color.WHITE);
						g.drawString(coordX, 30, 30);
						g.drawString(coordY, 30, 50);
					}

				}
			});

			this.getContentPane().addMouseMotionListener(new MouseMotionAdapter()
			{
				@Override
				public void mouseMoved(MouseEvent e)
				{
					repaint();
				}
			});

//			this.getContentPane().addMouseListener(new MouseAdapter()
//			{
//				@Override
//				public void mouseClicked(final MouseEvent e)
//				{
//					new Thread(new Runnable()
//					{
//						@Override
//						public void run()
//						{
//							
//						}
//					}).start();
//				}
//			});

			this.setVisible(true);

		}

	}

	public static BufferedImage buildImg(int[][] pixels)
	{
		BufferedImage testImg = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);
		for (int r = 0; r < pixels[0].length; r++)
			for (int c = 0; c < pixels.length; c++)
				testImg.setRGB(r, c, pixels[c][r]);
		return testImg;
	}

	public static BufferedImage buildImg(int[] pixels, int width, int height)
	{
		BufferedImage testImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				int pixel = pixels[x + y * height];
				testImg.setRGB(x, y, pixel);
			}
		return testImg;
	}
}
