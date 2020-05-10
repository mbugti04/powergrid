import java.awt.BasicStroke;
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
	static int cityScale = 70;
	/* the position where the map starts
	 * the cities will be relative to this position */
	static int mapx = 1653, mapy = 876;
	
	HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	/* String tells the state that the buttons is used for
	 * like initial, ingame, bidding, etc.
	 * It's there for each of the booleans.
	 * The ArrayList of buttons is tied to that state
	 */
	HashMap<String, ArrayList<Button>> buttons = new HashMap<String, ArrayList<Button>>();
	
	Font defaultfont = new Font("Calibri", Font.PLAIN, 16);
	Font bigfont = new Font("Calibri", Font.BOLD, 48);
	Font titlefont = new Font("Calibri", Font.BOLD, 32);
	Font subtitlefont = new Font("Calibri", Font.BOLD, 20);
	Font cityfont = new Font("Calibri", Font.PLAIN, 11);
	
	// states
	private boolean
	initial = true,
	regionSelect = false,
	ingame = false,
	bidding = false;
	
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
	
	
	
	
	
	// ---------------------------------------- SETUP METHODS ----------------------------------------
	private void mainSetup()
	{
		imageSetup();
		mainMenuSetup();
		regionSelectSetup();
		biddingSetup();
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
	
	private void biddingSetup()
	{
		ArrayList<Button> temp = new ArrayList<Button>();
		temp.add(new Button("BID", 625, 825, Button.normalw, Button.normalh, new Color(0, 200, 0)));
		temp.add(new Button("PASS", 1050, 825, Button.normalw, Button.normalh, new Color(200, 0, 0)));
		
		buttons.put("bidding", temp);
	}
	// ----------------------------------------------------------------------------------------------------
	
	
	
	
	// ---------------------------------------- DRAW METHODS ----------------------------------------
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2.clearRect(0, 0, width, height);
		g2.setFont(defaultfont);
		g2.setStroke(new BasicStroke(1));
		
		if (initial)
		{
			drawMainMenu(g2);
		}
		if (regionSelect)
		{
			drawRegionSelect(g2);
		}
		if (bidding)
		{
			drawMap(g2);
			drawCityConnections(g2);
			for (City c: cities)
			{
				drawCity(g2, c);
			}
			drawCurrentStep(g2);
			drawTurnOrder(g2);
			drawBidding(g2);
		}
		if (ingame)
		{
			drawMap(g2);
			drawCityConnections(g2);
			for (City c: cities)
			{
				drawCity(g2, c);
			}
			drawBidding(g2);
			drawCurrentStep(g2);
			drawTurnOrder(g2);
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
		g2.setFont(defaultfont);
		drawCentredString(g2, "Select 4 Regions to Continue", new Rectangle(0, 0, width, height / 8), titlefont);
	}
	
	public void drawMap(Graphics2D g2)
	{
		g2.drawImage(images.get("map.png"), 0, 0, 1920, 1080, null);
	}
	
	public void drawCity(Graphics2D g2, City c)
	{
		int x = startx + (int)(c.getX() * mapx);
		int y = starty + (int)(c.getY() * mapy);
		
		// circle
		g2.setColor(new Color(36, 22, 84));
		g2.fillOval(x - cityScale / 2, y - cityScale / 2, cityScale, cityScale);
		
		// text box
		g2.setColor(new Color(22, 26, 107));
		Rectangle rect = new Rectangle(x - cityScale / 2, y + cityScale / 5, cityScale, cityScale / 3);
		g2.fill(rect);
		g2.setColor(Color.white);
		drawCentredString(g2, c.getName(), rect, cityfont);
		// houses built in the city
	}
	
	public void drawCityConnections(Graphics2D g2)
	{
		for (City initial: cities)
		{
			Point starting = new Point(startx + (int)(initial.getX() * mapx), starty + (int)(initial.getY() * mapy));
			
			HashMap<City, Integer> cities = state.urbanArea.cities.get(initial); 
			for (City other: cities.keySet()) // TODO make it more efficient
			{
				Point ending = new Point(startx + (int)(other.getX() * mapx), starty + (int)(other.getY() * mapy));
				
				// draws connection line
				g2.setColor(new Color(128, 128, 128));
				g2.setStroke(new BasicStroke(20));
				g2.drawLine(starting.x, starting.y, ending.x, ending.y);
				
				// uses this rectangle for centring the cost
				g2.setColor(new Color(255, 255, 255));
				Rectangle r = new Rectangle(starting);
				r.add(ending);
				
				// doesn't draw the cost if the cost is 0
				if (cities.get(other) != 0)
					drawCentredString(g2, cities.get(other).toString(), r, defaultfont);
			}
		}
	}
	
	public void drawCurrentStep(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(1));
		
		Point starting = new Point(1800, 20);
		int width = 100, height = 40;
		
		for (int i = 1; i <= 3; i++)
		{
			if (i == state.step)
				g2.setColor(new Color(60, 220, 170));
			else
				g2.setColor(new Color(45, 150, 120));
			
			Rectangle rect = new Rectangle(starting.x, starting.y +  height * (i - 1), width, height);
			g2.fill(rect);
			
			g2.setColor(Color.white);
			drawCentredString(g2, "Step " + i, rect, defaultfont);
		}
	}
	
	public void drawTurnOrder(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(1));
		
		drawCentredString(g2, "Turn Order", new Rectangle(10, 10, 290, 40), subtitlefont);
		drawCentredString(g2, "Click on player for more info", new Rectangle(10, 40, 290, 20), defaultfont);
		Point starting = new Point(10, 65);
		int width = 60, height = 60;
		
		g2.setColor(new Color(255, 255, 255, 100));
		g2.fillRect(starting.x, starting.y, 290, 80);
		
		for (int i = 1; i <= 4; i++)
		{
			Rectangle rect = new Rectangle(10 * i + starting.x + width * (i - 1), 10 + starting.y, width, height);
			g2.fill(rect);
			
			g2.drawImage(images.get(state.getPlayerList().get(i - 1).colour + ".png"), 10 * i + starting.x + width * (i - 1), 10 + starting.y, null);
			
			if (i - 1 == state.currentPlayer)
			{
				g2.drawImage(images.get("arrow.png"), 10 * i + starting.x + width * (i - 1), 60 + starting.y, null);
			}
		}
	}
	
	public void drawBidding(Graphics2D g2)
	{
		g2.setColor(new Color(255, 255, 255, 230));
		int edges = 150;
		g2.fillRect(edges, edges, width - edges * 2, 800);
		
		g2.setColor(new Color(0, 0, 0));
		Rectangle title = new Rectangle(edges, edges, width - edges * 2, edges / 2);
		drawCentredString(g2, "Bidding", title, bigfont);
		
		for (Button b: buttons.get("bidding"))
			b.draw(g2);
		
		
	}
	
	public void drawCentredString(Graphics2D g2, String text, Rectangle rect, Font font)
	{
		FontMetrics metrics = g2.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		g2.setFont(font);
		g2.drawString(text, x, y);
	}
	// ----------------------------------------------------------------------------------------------------
	
	
	
	
	
	// ---------------------------------------- MOUSE METHODS ---------------------------------------
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
//							ingame = true;
							bidding = true;
							
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
		if (bidding)
		{
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {}
	// ----------------------------------------------------------------------------------------------------
}
