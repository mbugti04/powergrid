import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UI extends JPanel implements MouseListener, MouseMotionListener
{
	private String[] titles = new String[]
			{"the", "testing", "another test", "UI"};
	private int width = 1920, height = 1080;
	private HashMap<String, BufferedImage> images;
	
	private JFrame frame;
	
	// temp code
	Rectangle piece = new Rectangle(50, 20);
	int lastx, lasty;
	boolean firstTime = true;
	boolean pressOut = false;
	////
	
	public UI()
	{
		super();
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		frame = new JFrame(titles[(int)(Math.random() * titles.length)]); // selects a random title
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(size);
		frame.setLocationRelativeTo(null);
//		frame.setResizable(false);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		images = new HashMap<String, BufferedImage>();
		try
		{
			images.put("water", ImageIO.read(new File("assets/water.png")));
			images.put("map", ImageIO.read(new File("assets/us_map.png")));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		frame.setVisible(true);
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		
		// temp code (dragging test)
		if (firstTime)
		{
			piece.setLocation(width/2, height/2);
			firstTime = false;
		}
		
		paintBackground(g2);
		
		g2.setColor(new Color(132, 232, 182));
		g2.fill(piece);
		//
	}
	
	public void paintBackground(Graphics2D g2)
	{
		g2.drawImage(images.get("water"), 0, 0, null);
		g2.drawImage(images.get("map"), 300, 170, (int)(width * 0.8), (int)(height * 0.8), null);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		System.out.println(e.getX() + ", " + e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		lastx = piece.x - e.getX();
		lasty = piece.y - e.getY();
		
		if (piece.contains(e.getX(), e.getY()))
		{
			System.out.println("yes");
			updateLocation(e);
		}
		else
		{
			pressOut = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (piece.contains(e.getX(), e.getY()) )
		{
			updateLocation(e);
		}
		else
		{
			pressOut = false;
		}
	}
	
	public void updateLocation(MouseEvent e)
	{
		piece.setLocation(lastx + e.getX(), lasty + e.getY());
		/*if (piece.x + piece.width >= width)
			piece.setLocation(width - piece.width, lasty + e.getY());
		if (piece.y + piece.height > height)
			piece.setLocation(lastx + e.getX(), height - piece.height);*/
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (!pressOut)
		{
			updateLocation(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
}
