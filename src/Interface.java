import java.awt.Color;
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
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Interface extends JPanel implements MouseListener, MouseMotionListener
{
	GameState state = new GameState();
	
	static int width = 1920, height = 1080;
	static double scale = 1;
	
	HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	
	HashMap<String, Button> mainMenuButtons = new HashMap<String, Button>();
	
	// states
	private boolean initial = true;
	private boolean ingame = false;
	
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
		mainMenuSetup();
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2.clearRect(0, 0, width, height);
		
		if (initial)
		{
			drawMainMenu(g2);
		}
		if (ingame)
		{
			drawMap(g2);
			
			// drawing cities TODO very temporary
			ArrayList<City> cities = new ArrayList<City>(state.urbanArea.cities.keySet());
			
			int citySize = 70;
			int scaleX = scale(1480, scale), scaleY = scale(754, scale);
			
			for (City c: cities)
			{
				drawCity(g2, c, scaleX, scaleY, citySize);
			}
		}
	}
	
	// draws start button, quit button, and background
	public void drawMainMenu(Graphics2D g2)
	{
		g2.drawImage(images.get("mainMenu.png"), 0, 0, null);
		for (Button b: mainMenuButtons.values())
			b.draw(g2);
	}
	
	public void drawMap(Graphics2D g2)
	{
		BufferedImage map = images.get("map.png");
		g2.drawImage(map, -200, -300, scale(map.getWidth(), scale), scale(map.getHeight(), scale), null); // original was -200, -300
	}
	
	public int scale(int initial, double scale)
	{
		return (int)(initial * scale);
	}
	
	public void drawCity(Graphics2D g2, City c, int sx, int sy, int size)
	{
		int startx = 395;
		int starty = 190;
		int x = startx + (int)(c.getX() * sx);
		int y = starty + (int)(c.getY() * sy);
		
		g2.setColor(new Color(0, 0, 0));
		g2.fillOval(x - size / 2, y - size / 2, size, size);
		
		g2.setColor(new Color(120, 120, 120));
		g2.fillRect(x - size / 2, y + size / 4, size, size / 3);
	}
	
	// TODO temp
	public void info()
	{
//		System.out.println("City size: " + citySizeSlider.getValue());
//		System.out.println("map size: " + mapSizeSlider.getValue() / 100.);
//		System.out.println("centre: " + centre);
	}
	
	private void imageSetup()
	{
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/imageNames.txt")));
			String nextLine = reader.readLine();
			
			int val = 0;
			while (nextLine != null)
			{
				BufferedImage img = loadImage("images/" + nextLine);
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
	
	private void mainMenuSetup()
	{
		Button start = new Button("start", width / 2 - Button.normalw / 2, height / 2, Button.normalw, Button.normalh);
		Button quit = new Button("quit", width / 2 - Button.normalw / 2, height / 2 + Button.normalh * 2,
				Button.normalw, Button.normalh);
		mainMenuButtons.put(start.name, start);
		mainMenuButtons.put(quit.name, quit);
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
		System.out.println(m.getX() + ", " + m.getY());
		
		if (initial)
		{
			for (Button b: mainMenuButtons.values())
			{
				if (b.inBounds(m))
				{
					b.press();
					System.out.println("pressed " + b.name + " and it is now " + b.isPressed());
				}
			}
			if (mainMenuButtons.get("start").isPressed())
			{
				initial = false;
				ingame = true;
				System.out.println("game has started");
			}
			if (mainMenuButtons.get("quit").isPressed())
			{
				System.out.println("game has ended");
				System.exit(0);
			}
		}
		if (ingame)
		{
			
		}
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
