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
	static int width = 1920, height = 1080;
	static int startx = 260, starty = 135;
	static int citySize = 70;
	static int sx = 1653, sy = 876;
	HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	
	/* String tells the state that the buttons is used for
	 * like initial, ingame, bidding, etc.
	 * It's there for each of the booleans.
	 * The ArrayList of buttons is tied to that state
	 */
	HashMap<String, ArrayList<Button>> buttons = new HashMap<String, ArrayList<Button>>();
	
	// states
	private boolean initial = true;
	private boolean ingame = false;
	
	// gamestate things
	GameState state = new GameState();
	ArrayList<City> cities = new ArrayList<City>(state.urbanArea.getAllCities());
	
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
			for (City c: cities)
			{
				drawCity(g2, c);
			}
		}
	}
	
	// draws start button, quit button, and background
	public void drawMainMenu(Graphics2D g2)
	{
		g2.drawImage(images.get("mainMenu.png"), 0, 0, null);
		for (Button b: buttons.get("initial"))
			b.draw(g2);
	}
	
	public void drawMap(Graphics2D g2)
	{
		g2.drawImage(images.get("map.png"), 0, 0, null);
	}
	
	public void drawCity(Graphics2D g2, City c)
	{
		int x = startx + (int)(c.getX() * sx);
		int y = starty + (int)(c.getY() * sy);
		
		g2.setColor(new Color(0, 0, 0));
		g2.fillOval(x - citySize / 2, y - citySize / 2, citySize, citySize);
		
		g2.setColor(new Color(120, 120, 120));
		g2.fillRect(x - citySize / 2, y + citySize / 4, citySize, citySize / 3);
	}
	
	private void imageSetup()
	{
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/imageNames.txt")));
			String nextLine = reader.readLine();
			
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
		ArrayList<Button> temp = new ArrayList<Button>();
		temp.add(new Button("start", width / 2 - Button.normalw / 2, height / 2, Button.normalw, Button.normalh));
		temp.add(new Button("quit", width / 2 - Button.normalw / 2, height / 2 + Button.normalh * 2, Button.normalw, Button.normalh));
		
		buttons.put("initial", temp);
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
		System.out.println(m.getX() + ", " + m.getY());
		
		if (initial)
		{
			for (Button b: buttons.get("initial"))
			{
				if (b.inBounds(m))
				{
					b.press();
					System.out.println("pressed " + b.name + " and it is now " + b.isPressed());
				}
				if (b.isPressed() && b.name.equals("start"))
				{
					initial = false;
					ingame = true;
					System.out.println("game has started");
				}
				if (b.isPressed() && b.name.equals("quit"))
				{
					System.out.println("game has ended");
					System.exit(0);
				}
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
