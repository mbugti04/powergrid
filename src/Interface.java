import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

public class Interface extends JPanel implements MouseListener, MouseMotionListener
{
	GameState state;
	static int width = 1920, height = 1080;
	
	HashMap<String, BufferedImage> images;
	
	// states
	boolean initial = false;
	boolean ingame = true;
	
	public Interface()
	{
		super();
		setSize(width, height);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		f.setSize(this.getSize());
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		state = new GameState();
		images = new HashMap<String, BufferedImage>();
		
		importImages();
		
		f.setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (initial)
		{
			drawMainMenu(g2);
		}
		if (ingame)
		{
			tempDraw(g2);
		}
	}
	
	public void drawMainMenu(Graphics g2)
	{
		
	}
	
	public void tempDraw(Graphics2D g2)
	{
		g2.drawImage(images.get("map.png"), -200, -300, null);
//		g2.drawImage(images.get("water.png"), 0, 0, null);
	}
	
	public void importImages()
	{
		File imageFiles[] = new File("assets/").listFiles();
		
		for (int i = 0; i < imageFiles.length; i++)
		{
				String name = imageFiles[i].getName();
				try
				{
					images.put(name, ImageIO.read(imageFiles[i]));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}

	@Override
	public void mouseDragged(MouseEvent m)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent m)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent m)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent m)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent m)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent m)
	{
		// TODO Auto-generated method stub
		
	}

}
