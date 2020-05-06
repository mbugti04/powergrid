import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Interface extends JPanel implements MouseListener
{
	static int width = 1920, height = 1080;
	static int startx = 260, starty = 135;
	static int citySize = 70;
	/* the position where the map starts
	 * the cities will be relative to this position */
	static int sx = 1653, sy = 876;
	
	HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	/* String tells the state that the buttons is used for
	 * like initial, ingame, bidding, etc.
	 * It's there for each of the booleans.
	 * The ArrayList of buttons is tied to that state
	 */
	HashMap<String, ArrayList<Button>> buttons = new HashMap<String, ArrayList<Button>>();
	
	Font defont = new Font("Calibri", Font.PLAIN, 16);
	Font titlefont = new Font("Calibri", Font.BOLD, 32);
	Font cityfont = new Font("Calibri", Font.PLAIN, 11);
	
	// states
	private boolean
	initial = true,
	regionSelect = false,
	ingame = false;
	
	// gamestate things
	GameState state = new GameState();
	ArrayList<City> cities;
	
	public Interface()
	{
		super();
		setSize(width, height);
		addMouseListener(this);
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		f.setSize(this.getSize());
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		mainSetup();
		
		f.setVisible(true);
	}
	
	
	
	
	
	// -------------------- SETUP METHODS --------------------
	private void mainSetup()
	{
		imageSetup();
		mainMenuSetup();
		regionSelectSetup();
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
				BufferedImage img = ImageIO.read(getClass().getResource("images/" + nextLine));
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
	
	private void mainMenuSetup()
	{
		ArrayList<Button> temp = new ArrayList<Button>();
		temp.add(new Button("start", width / 2 - Button.normalw / 2, height / 2, Button.normalw, Button.normalh));
		temp.add(new Button("quit", width / 2 - Button.normalw / 2, height / 2 + Button.normalh * 2, Button.normalw, Button.normalh));
		
		buttons.put("initial", temp);
	}
	
	private void regionSelectSetup()
	{
		ArrayList<Button> temp = new ArrayList<Button>();
		temp.add(new Button("PURPLE", 485, 245, Button.normalw, Button.normalh));
		temp.add(new Button("BLUE", 525, 575, Button.normalw, Button.normalh));
		temp.add(new Button("YELLOW", 1100, 300, Button.normalw, Button.normalh));
		temp.add(new Button("RED", 1055, 670, Button.normalw, Button.normalh));
		temp.add(new Button("ORANGE", 1580, 410, Button.normalw, Button.normalh));
		temp.add(new Button("GREEN", 1460, 720, Button.normalw, Button.normalh));
		temp.add(new Button("CONTINUE", 1645, 955, Button.normalw, Button.normalh));
		
		buttons.put("regionSelect", temp);
	}
	// -----------------------------------------------------
	
	
	
	
	// -------------------- DRAW METHODS --------------------
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2.clearRect(0, 0, width, height);
		g2.setFont(new Font("Calibri", Font.PLAIN, 12));
		
		if (initial)
		{
			drawMainMenu(g2);
		}
		if (regionSelect)
		{
			drawRegionSelect(g2);
		}
		if (ingame)
		{
			drawMap(g2);
			drawCityConnections(g2);
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
	
	public void drawRegionSelect(Graphics2D g2)
	{
		drawMap(g2);
		for (Button b: buttons.get("regionSelect"))
			b.draw(g2);
		g2.setFont(defont);
		drawCentredString(g2, "Select 4 Regions to Continue", new Rectangle(0, 0, width, height / 8), titlefont);
		
	}
	
	public void drawMap(Graphics2D g2)
	{
		g2.drawImage(images.get("map.png"), 0, 0, 1920, 1080, null);
	}
	
	public void drawCity(Graphics2D g2, City c)
	{
		int x = startx + (int)(c.getX() * sx);
		int y = starty + (int)(c.getY() * sy);
		
		// circle
		g2.setColor(new Color(36, 22, 84));
		g2.fillOval(x - citySize / 2, y - citySize / 2, citySize, citySize);
		
		// text box
		g2.setColor(new Color(22, 26, 107));
		Rectangle rect = new Rectangle(x - citySize / 2, y + citySize / 5, citySize, citySize / 3);
		g2.fill(rect);
		g2.setColor(Color.white);
		drawCentredString(g2, c.getName(), rect, cityfont);
		// houses built in the city
	}
	
	public void drawCityConnections(Graphics2D g2)
	{
		for (City initial: cities)
		{
			Point starting = new Point(startx + (int)(initial.getX() * sx), starty + (int)(initial.getY() * sy));
			
			HashMap<City, Integer> cities = state.urbanArea.cities.get(initial); 
			for (City other: cities.keySet()) // TODO make it more efficient
			{
				Point ending = new Point(startx + (int)(other.getX() * sx), starty + (int)(other.getY() * sy));
				
				g2.drawLine(starting.x, starting.y, ending.x, ending.y);
				
				Rectangle r = new Rectangle(starting);
				r.add(ending);
				drawCentredString(g2, cities.get(other).toString(), r, defont);
			}
		}
	}
	
	public void drawCentredString(Graphics2D g2, String text, Rectangle rect, Font font)
	{
		FontMetrics metrics = g2.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		g2.setFont(font);
		g2.drawString(text, x, y);
	}
	// ------------------------------------------------------
	
	
	
	
	
	// -------------------- MOUSE METHODS -------------------
	@Override
	public void mouseClicked(MouseEvent m) {}

	@Override
	public void mouseEntered(MouseEvent m) {}

	@Override
	public void mouseExited(MouseEvent m) {}

	@Override
	public void mousePressed(MouseEvent m)
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
					regionSelect = true;
				}
				if (b.isPressed() && b.name.equals("quit"))
				{
					System.out.println("game has ended");
					System.exit(0);
				}
			}
		}
		if (regionSelect)
		{
			for (Button b: buttons.get("regionSelect"))
			{
				if (b.inBounds(m))
				{
					if (b.name.equals("CONTINUE"))
					{
						if (state.getActiveRegions().size() == 4)
						{
							b.press();
							state.removeRegions();
							
							regionSelect = false;
							ingame = true;
							
							System.out.println("active regions: " + state.getActiveRegions());

							cities = new ArrayList<City>(state.urbanArea.getListOfAllCities());
						}
					}
					else
					{
						b.press();
						state.toggleRegion(b.name);
						System.out.println("toggled " + b.name + " and active regions: " + state.getActiveRegions());
					}
				}
			}
		}
		if (ingame)
		{
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {}
	// -------------------------------------------------------
}
