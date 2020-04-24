import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Interface extends JPanel implements MouseListener, MouseMotionListener
{
	GameState state = new GameState();
	static int width = 1920, height = 1080;
	
	HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	
	// states
	private boolean initial = false;
	private boolean ingame = true;
	
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
		
		mainSetup();
		
		f.setVisible(true);
	}
	
	private void mainSetup()
	{
		imageSetup();
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
	
	private void imageSetup()
	{
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/imageNames.txt")));
			// TODO: get an actual path
			String nextLine = reader.readLine();
			
			int val = 0;
			while (nextLine != null)
			{
				BufferedImage img = loadImage("images/" + nextLine);
				// TODO: store images somewhere
				images.put(nextLine, img);
				nextLine = reader.readLine();
			}
			System.out.println(images.keySet());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private BufferedImage loadImage(String path)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(getClass().getResource(path));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return img;
	}
	
	/*
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
	}*/

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
