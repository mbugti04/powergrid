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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	int timesNextTurnWasPressed = 0;
	
	String incomeText = "";
	HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	HashMap<String, BufferedImage> plantimg = new HashMap<String, BufferedImage>(); // plant image
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
	bidding = false,
	buyresource = false,
	buycity = false,
	nextTurn = false,
	powering = false,
	winScreen = false;
	
	// gamestate things
	GameState state = new GameState();
	
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
		f.setUndecorated(true);
		try {
			f.setIconImage(ImageIO.read(new File("assets/images/icon.png")));
		} catch (IOException e) {
			System.out.println("Could not load icon!");
		}
		
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
		buyresourceSetup();
		nextTurnSetup();
		buyCitySetup();
		poweringSetup();
		winScreenSetup();
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
		try
		{
			for (int i = 3; i <= 40; i++)
			{
				BufferedImage img = ImageIO.read(getClass().getResource("powerplants/" + i + ".png"));
				plantimg.put(i + ".png", img);
			}
			for (int i = 42; i <= 50; i += 2)
			{
				if (i == 48)
					i += 2;
				BufferedImage img = ImageIO.read(getClass().getResource("powerplants/" + i + ".png"));
				plantimg.put(i + ".png", img);
			}
			BufferedImage img = ImageIO.read(getClass().getResource("powerplants/step3.png"));
			plantimg.put("step.png", img);
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
		temp.add(new Button("REPLACE", 838, 825, Button.normalw, Button.normalh));
		
		buttons.put("bidding", temp);
	}
	
	private void buyresourceSetup()
	{
		int rw = 120, rh = 120;
		ArrayList<Button> temp = new ArrayList<Button>();
		temp.add(new Button("oil", 10, 175, rw, rh, false));
		temp.add(new Button("coal", 140, 175, rw, rh, false));
		temp.add(new Button("uranium", 10, 305, rw, rh, false));
		temp.add(new Button("trash", 140, 305, rw, rh, false));
		
		buttons.put("buyresource", temp);
	}
	private void nextTurnSetup()
	{
		ArrayList<Button> temp = new ArrayList<Button>();
		temp.add(new Button("Next Turn", 1680, 950, Button.normalw, Button.normalh));		
		buttons.put("nextTurn", temp);
	}
	
	private void buyCitySetup()
	{
		ArrayList<Button> temp = new ArrayList<Button>();
		for (City c: state.urbanArea.cities.keySet())
        {
			int x = startx + (int)(c.getX() * mapx);
			int y = starty + (int)(c.getY() * mapy);
			
            temp.add(new Button(c.getName(), x - cityScale / 2, y - cityScale / 2, cityScale, cityScale, false, new Color(0, 0, 0, 0)));
        }
		temp.add(new Button("Buy City", 1680, 820, Button.normalw, Button.normalh));
		buttons.put("buycity", temp);
	}
	
	private void poweringSetup()
	{
		ArrayList<Button> temp = new ArrayList<Button>();
		temp.add(new Button("Power City", 1680, 950, Button.normalw, Button.normalh));
		
//		Player current = state.players.get(state.currentPlayer);
		for (Player current: state.players)
		{
			int posx = 185, posy = 580;
			int numpow = current.ownedPlants.size();
			for (int i = 0; i < 3; i++)
			{
				if (numpow-- > 0)
				{
	//				temp.add(new Button( "" + current.ownedPlants.get(i).getName(), posx, posy + 10 * i + 150 * i, 150, 150, new Color(0,0,0,0)));
					temp.add(new Button("+p" + current.ownedPlants.get(i).getName(), posx, posy + 10 * i + 150 * i, 30, 50));
					temp.add(new Button("-p" + current.ownedPlants.get(i).getName(), posx, 100 + posy + 10 * i + 150 * i, 30, 50));
				}
			}
			buttons.put("powering", temp);
		}
		
	}
	
	private void winScreenSetup()
	{
		ArrayList<Button> temp = new ArrayList<Button>();
		temp.add(new Button("EXIT", 1680, 950, Button.normalw, Button.normalh));
		
		buttons.put("winScreen", temp);
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
			for (City c: state.urbanArea.cities.keySet())
			{
				drawCity(g2, c);
			}
			drawCurrentStep(g2);
			drawTurnOrder(g2);
			drawPhase(g2);
			
			drawBidding(g2);
		}
		if (buyresource)
		{
			drawMap(g2);
			drawCityConnections(g2);
			for (City c: state.urbanArea.cities.keySet())
			{
				drawCity(g2, c);
			}
			drawCurrentStep(g2);
			drawTurnOrder(g2);
			drawOwnPlants(g2);
			drawPhase(g2);
			
			drawMarket(g2);
			drawNextTurn(g2);
		}
		if (buycity)
		{
			drawMap(g2);
			drawCityConnections(g2);
			for (City c: state.urbanArea.cities.keySet())
			{
				drawCity(g2, c);
			}
			drawCurrentStep(g2);
			drawTurnOrder(g2);
//			drawMarket(g2);
			drawOwnPlants(g2);
			drawPhase(g2);
			
			drawBuyCity(g2);
			drawNextTurn(g2);
		}
		if (powering)
		{
			drawMap(g2);
			drawCityConnections(g2);
			for (City c: state.urbanArea.cities.keySet())
			{
				drawCity(g2, c);
			}
			drawCurrentStep(g2);
			drawTurnOrder(g2);
			drawOwnPlants(g2);
			drawPhase(g2);
			
			drawPowering(g2);
		}
		if (state.hasEnded == true)
		{
			drawWinScreen(g2);
		}
//		if (ingame)
//		{
//			drawMap(g2);
//			drawCityConnections(g2);
//			for (City c: cities)
//			{
//				drawCity(g2, c);
//			}
//			drawBidding(g2);
//			drawCurrentStep(g2);
//			drawTurnOrder(g2);
//		}
	}
	
	// draws start button, quit button, and background
	public void drawMainMenu(Graphics2D g2)
	{
		g2.drawImage(images.get("mainMenu.png"), 0, 0, null);
		for (Button b: buttons.get("initial"))
			b.draw(g2);
	}
	
	public void drawWinScreen(Graphics2D g2)
	{
		g2.drawImage(images.get("mainMenu.png"), 0, 0, null);
		for (Button b: buttons.get("winScreen"))
		{
			b.draw(g2);
		}
		drawCentredString(g2, "RESULTS", new Rectangle(0, 0, width, height / 8), titlefont);
		
	}
	public void drawRegionSelect(Graphics2D g2)
	{
		drawMap(g2);
		for (Button b: buttons.get("regionSelect"))
			b.draw(g2);
		g2.setFont(defaultfont);
		drawCentredString(g2, "Select 4 Regions that are ADJACENT to Continue", new Rectangle(0, 0, width, height / 8), titlefont);
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
		if (state.selectedCity != null && state.selectedCity.equals(c))
			g2.setColor(new Color(103, 108, 220));
		Rectangle rect = new Rectangle(x - cityScale / 2, y + cityScale / 5, cityScale, cityScale / 3);
		g2.fill(rect);
		g2.setColor(Color.white);
		drawCentredString(g2, c.getName(), rect, cityfont);		
		
		// -- drawing the indicators --
		
		ArrayList<String> cols = new ArrayList<String>();
		// houses built in the city
		for (Player p: state.players)
		{
			if (p.ownedCities.contains(c))
			{
				cols.add(p.colour);
			}
		}
		ArrayList<Color> colours = new ArrayList<Color>();
		for (String colour: cols)
		{
			if (colour.equals("red"))
				colours.add(new Color(223, 91, 91));
			else if (colour.equals("yellow"))
				colours.add(new Color(225, 178, 24));
			else if (colour.equals("green"))
				colours.add(new Color(115, 214, 117));
			else if (colour.equals("blue"))
				colours.add(new Color(117, 232, 226));
		}
		
		x = x - (cityScale / 3);
		int pos = 0;
		for (Color colour: colours)
		{
			g2.setColor(colour);
			Point indicator = new Point(x + (cityScale / 3) * pos++, y);
			g2.fillRect(indicator.x, indicator.y, 5, 5);
		}
	}
	
	public void drawCityConnections(Graphics2D g2)
	{
		for (City initial: state.urbanArea.cities.keySet())
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
			g2.setColor(new Color(255, 255, 255, 100));
			if (state.permanentNonBidders.contains(state.players.get(i - 1)))
				g2.setColor(new Color(200, 0, 0, 100));
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
		Rectangle title = new Rectangle(edges, edges, width - edges * 2, 50);
		drawCentredString(g2, "Select any power plant from the top four", title, subtitlefont);
		
		title = new Rectangle(edges, edges + 25, width - edges * 2, 50);
		drawCentredString(g2, "Make sure that you have enough money to continue", title, subtitlefont);
		
//		for (Button b: buttons.get("bidding"))  //For drawing the buttons in biddingSetup()
//			b.draw(g2);
		for (Button b: buttons.get("bidding"))
		{
			if (b.name.equals("REPLACE"))
			{
				if (state.replacing)
					b.draw(g2);
			}
			else
			{
				b.draw(g2);
			}
		}
		
		// money
		g2.setColor(Color.black);
		Player current = state.players.get(state.currentPlayer);
		drawAString(g2, "Player " + current.colour, new Point(170, 185), titlefont);
		drawAString(g2, "Money:", new Point(170, 220), titlefont);
		g2.setColor(Color.green);
		drawAString(g2, "$" + current.getMoney(), new Point(170, 280), bigfont);
		
		// power plants
		g2.setColor(Color.black);
		drawAString(g2, "Player " + current.colour, new Point(170, 340), titlefont);
		drawAString(g2, "Power Plants:", new Point(170, 380), titlefont);
		
		// own plants
		int numpow = current.ownedPlants.size();
		g2.setColor(new Color(100, 100, 100));
		for (int i = 0; i < 3; i++)
		{
			if (numpow-- > 0)
			{
				g2.drawImage(plantimg.get(current.ownedPlants.get(i).getName() + ".png"), 170, 400 + 10 * i + 150 * i, 150, 150, null);
			}
			else
			{
				g2.fillRect(170, 400 + 10 * i + 150 * i, 150, 150);
			}
		}
		
		// other plants
		ArrayList<Powerplant> allPlants = state.plantMarket.plantsAvailable;
//		System.out.println(allPlants);
		int maxIterations = 8;
		if (state.step == 3)
			maxIterations = 6;
		for (int i = 0; i < maxIterations; i++)
		{
			int xcoord = 625 + 10 * (i % 4) + 150 * (i % 4);
			int ycoord = 250 + 10 * (i / 4) + 150 * (i / 4);
			g2.drawImage(plantimg.get(allPlants.get(i).getName() + ".png"), xcoord, ycoord, 150, 150, null);
			if (state.chosenPlant != null && allPlants.get(i).getName() == state.chosenPlant.getName())
			{
				g2.setColor(new Color(0, 200, 10, 100));
				g2.fillRect(xcoord, ycoord, 150, 150);
			}
		}
		
		// bid info
		g2.setColor(Color.black);
		drawCentredString(g2, "Current Player: Player " + current.colour, new Rectangle(625, 575, 635, 50), titlefont);
		
		if (state.currentbid == 0 || state.chosenPlant == null || state.currentBidPlayer == null)
			drawCentredString(g2, "No Bid Currently", new Rectangle(625, 615, 635, 50), titlefont);
		else
		{
			drawCentredString(g2, "Player " + state.currentBidPlayer.colour + " has bid $" + state.currentbid + " on plant " + state.chosenPlant.getName(),
					new Rectangle(625, 615, 635, 50), titlefont);
			drawCentredString(g2, "Bid $" + (state.currentbid + 1) + " on plant " + state.chosenPlant.getName() + "?",
					new Rectangle(625, 655, 635, 50), titlefont);
		}
		
		if (state.replacing)
		{
			if (state.toBeReplaced != null)
			{
				numpow = current.ownedPlants.size();
				g2.setColor(new Color(0, 200, 10, 100));
				for (int i = 0; i < 3; i++)
				{
					
					if (numpow-- > 0 && current.ownedPlants.get(i).equals(state.toBeReplaced))
					{
						g2.fillRect(170, 400 + 10 * i + 150 * i, 150, 150);
//						System.out.println("yup it does work at plant " + state.getCurrentPlayer().ownedPlants.get(i));
					}
				}
			}
		}
	}
	
	public void drawMarket(Graphics2D g2)
	{
		for (Button b: buttons.get("buyresource"))
			b.draw(g2);
		
		int sx = 10, sy = 175, w = 120;
		
		ArrayList<Resource> res = new ArrayList<Resource>(Arrays.asList(Resource.oil, Resource.coal, Resource.uranium, Resource.trash));
		ResourceMarket r = state.resourceMarket;
		ArrayList<Integer> costs = new ArrayList<Integer>(Arrays.asList(r.getPrice(res.get(0)), r.getPrice(res.get(1)),
				r.getPrice(res.get(2)), r.getPrice(res.get(3))));
		ArrayList<Integer> counts = new ArrayList<Integer>(Arrays.asList(r.currentStock.get(res.get(0)), r.currentStock.get(res.get(1)),
				r.currentStock.get(res.get(2)), r.currentStock.get(res.get(3))));
		for (int i = 0; i < 4; i++)
		{
			drawCentredString(g2, "Buy " + res.get(i), new Rectangle(10 + (i % 2) * w + 10 * (i % 2), 175 + (i / 2) * w + 10 * (i / 2), w, w / 3), defaultfont);
			drawCentredString(g2, "Cost: " + costs.get(i), new Rectangle(10 + (i % 2) * w + 10 * (i % 2), 190 + (i / 2) * w + 10 * (i / 2), w, w / 3), defaultfont);
			drawCentredString(g2, "Remaining: " + counts.get(i), new Rectangle(10 + (i % 2) * w + 10 * (i % 2), 250 + (i / 2) * w + 10 * (i / 2), w, w / 3), defaultfont);
		}
	}
	
	public void drawOwnPlants(Graphics2D g2)
	{
		Player current = state.players.get(state.currentPlayer);
		int posx = 25, posy = 580;
		int numpow = current.ownedPlants.size();
		g2.setColor(new Color(100, 100, 100));
		for (int i = 0; i < 3; i++)
		{
			if (numpow-- > 0)
			{
				g2.drawImage(plantimg.get(current.ownedPlants.get(i).getName() + ".png"), posx, posy + 10 * i + 150 * i, 150, 150, null);
			}
			else
			{
				g2.fillRect(posx, posy + 10 * i + 150 * i, 150, 150);
			}
		}
		
		g2.setColor(Color.white);
		int g = 0, w = 200, h = 30;
		drawAString(g2, "Resources Stored:", new Point(260, 925), titlefont);
		for (Resource r: current.getResources().keySet())
		{
			drawAString(g2, r + ": " + current.getResources().get(r) + "/" + current.getSpace(r), new Point(260, 955 + h * g++), titlefont);
		}
		
		g2.setColor(Color.white);
		Rectangle temp = new Rectangle(10, 450, 245, 120);
//		temp.add(255, 570);
		drawCentredString(g2, "Your Power Plants", temp, titlefont);
		temp = new Rectangle(10, 520, 245, 60);
		drawCentredString(g2, "Current Money: " + current.money, temp, subtitlefont);
	}
	
	public void drawPhase(Graphics2D g2)
	{
		g2.setColor(Color.white);
		String statement = "";
		if (state.turnPhase == 0)
			statement = "Bidding";
		if (state.turnPhase == 1) // buying resources
			statement = "Buying Resources";
		if (state.turnPhase == 2) // buying cities
			statement = "Buying Cities";
		if (state.turnPhase == 3) // powering
			statement = "Powering Cities";
		drawCentredString(g2, "Phase " + (state.turnPhase + 1) + ": " + statement, new Rectangle(0, 0, 1920, 130), bigfont);
		drawCentredString(g2, "Player " + state.players.get(state.currentPlayer).colour + " turn", new Rectangle(0, 40, 1920, 130), titlefont);
	}
	
	public void drawNextTurn(Graphics2D g2)
	{
		for (Button b: buttons.get("nextTurn"))
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
	
	public void drawAString(Graphics2D g2, String text, Point p, Font font)
	{
		g2.setFont(font);
		g2.drawString(text, p.x, p.y);
	}
	
	public void drawBuyCity(Graphics2D g2)
	{
//		Rectangle title = new Rectangle(edges, edges, width - edges * 2, 50);
//		drawCentredString(g2, "Select any power plant from the top four", title, subtitlefont);
//		
//		title = new Rectangle(edges, edges + 25, width - edges * 2, 50);
//		drawCentredString(g2, "Make sure that you have enough money to continue", title, subtitlefont);
		for (Button b: buttons.get("buycity"))
			b.draw(g2);
	}
	
	public void drawPowering(Graphics2D g2)
	{
		ArrayList<Powerplant> temp = state.getCurrentPlayer().ownedPlants;
		ArrayList<String> names = new ArrayList<String>();
		for (Powerplant p: temp)
		{
			names.add("+p" + p.getName());
			names.add("-p" + p.getName());
		}
		names.add("Power City");
		
		for (Button b: buttons.get("powering"))
		{
			if (names.contains(b.name))
				b.draw(g2);
		}
		
		int posx = 185, posy = 580;
		int numpow = state.getCurrentPlayer().ownedPlants.size();
		
		int i = 0;
		for (Powerplant p: state.getCurrentPlayer().ownedPlants)
		{
			int owned = 0; // amount of owned resources
			if (p.getResourceType().equals(Resource.hybrid))
			{
				owned = state.getCurrentPlayer().getResources().get(Resource.coal) + state.getCurrentPlayer().getResources().get(Resource.oil);
			}
			else if (!p.getResourceType().equals(Resource.free))
			{
				owned = state.getCurrentPlayer().getResources().get(p.getResourceType());
			}
			
			int max = state.getCurrentPlayer().ownedPlants.get(i).getAmountToPower();
			// max needed to power
			
			int result = 1;
			if (!p.getResourceType().equals(Resource.free))
				result = Math.min(owned / max, 1);
			String text = state.powerableHouses + "/" + Math.min(result, state.getCurrentPlayer().ownedCities.size());
			drawCentredString(g2, text,
					new Rectangle(posx, 50 + posy + 150 * i + 10 * i, 30, 50), defaultfont);
			i++;
		}
		
		drawAString(g2, incomeText, new Point(1335, 60), bigfont);
		
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
						if (state.getActiveRegions().size() == 4 && areAdjacent(state.getActiveRegions()))
						{
							b.press();
							state.removeRegions();
							
							regionSelect = false;
							bidding = true;
							state.newBidPhase();
							
							buyCitySetup(); // needed for reset
							System.out.println("turnphase: " + state.turnPhase);
							
							System.out.println("active regions: " + state.getActiveRegions());
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
			if (state.currentBidPlayer == null)
			{
				int maxIterations = 4;
				if (state.step == 3)
					maxIterations = 6;
				for (int i = 0; i < maxIterations; i++)
				{
					int xcoord = 625 + 10 * (i % 4) + 150 * (i % 4);
					int ycoord = 250 + 10 * (i / 4) + 150 * (i / 4);
					Rectangle r = new Rectangle(xcoord, ycoord, 150, 150);
					if (contains(r, m))
					{
	//					System.out.println("selected plant " + state.plantMarket.plantsAvailable.get(i).getName());
						state.choosePlant(state.plantMarket.plantsAvailable.get(i));
					}
				}
			}
			if (state.replacing)
			{
				int numpow = state.getCurrentPlayer().ownedPlants.size();
				for (int i = 0; i < 3; i++)
				{
					if (numpow-- > 0)
					{
						int xcoord = 170;
						int ycoord = 400 + 10 * i + 150 * i;
						Rectangle r = new Rectangle(xcoord, ycoord, 150, 150);
						if (contains(r, m))
							state.toBeReplaced = state.getCurrentPlayer().ownedPlants.get(i);
					}
				}
			}
			for (Button b: buttons.get("bidding"))
			{
				if (b.inBounds(m))
				{
					if (b.name.equals("BID"))
					{
						state.bid();
					}
					else if (b.name.equals("PASS"))
					{
						state.playerPassedBidPhase();
					}
					else if (state.replacing && b.name.equals("REPLACE") && state.toBeReplaced != null)
					{
						state.replacePowerplant(state.toBeReplaced, state.chosenPlant);
						state.refreshBidPhase();
					}
				}
			}
		}
		if (buyresource)
		{
			Player current = state.players.get(state.currentPlayer);
			for (Button b: buttons.get("buyresource"))
			{
				if (b.inBounds(m))
				{
					state.resourceMarket.purchase(current, Resource.valueOf(b.name));
				}
			}
			for (Button b: buttons.get("nextTurn"))
			{
				if (b.inBounds(m))
				{
					state.nextPlayer();
					timesNextTurnWasPressed++;
					if (timesNextTurnWasPressed >= state.playerCount)
					{
						timesNextTurnWasPressed = 0;
						state.nextTurnPhase();
					}
				}
			}
			poweringSetup();
		}
		
		if (buycity)
		{
			Player current = state.players.get(state.currentPlayer);
			for(Button b: buttons.get("buycity")) 
			{
				if(b.inBounds(m))
				{
					if (!b.name.equals("Buy City"))
					{
						for (City c: state.urbanArea.cities.keySet())
						{
							if(c.getName() == b.name)
							{
								state.selectCity(c);
								System.out.println("Player " + current.colour + " selected " + c.getName());
							}
						}
					}
					if (b.name.equals("Buy City"))
					{
						System.out.println("Buying success: " + state.buyCity(state.selectedCity));
					}
				}
			}
			for (Button b: buttons.get("nextTurn"))
			{
				if (b.inBounds(m))
				{
					state.nextPlayer();
					timesNextTurnWasPressed++;
					if (timesNextTurnWasPressed >= state.playerCount)
					{
						timesNextTurnWasPressed = 0;
						state.nextTurnPhase();
					}
				}
			}
		}
		
		if(powering) 
		{
			Player current = state.players.get(state.currentPlayer);
			int i = 0;
			for (Button b: buttons.get("powering"))
			{
				if (b.inBounds(m))
				{
					for (Powerplant p: current.ownedPlants)
					{
						if (b.name.equals("+p" + p.getName()))
						{
							state.togglePlants(p, state.powerableHouses + 1);
							System.out.println("toggled + and is now " + state.powerableHouses);
						}
						if (b.name.equals("-p" + p.getName()))
						{
							state.togglePlants(p, state.powerableHouses - 1);
							System.out.println("toggled - and is now " + state.powerableHouses);
						}
						if (b.name.equals("Power City"))
						{
							incomeText = state.powerCities();
							
							state.nextPlayer();
							timesNextTurnWasPressed++;
							if (timesNextTurnWasPressed >= state.playerCount)
							{
								timesNextTurnWasPressed = 0;
								state.nextTurnPhase();
							}
						}
					}
				}
			}
		}
		
		if (state.hasEnded == true)
		{
			winScreen = true;
			for (Button b: buttons.get("winScreen"))
			{
				if (b.inBounds(m) && b.name.equals("EXIT"))
				{
					System.out.println("game has ended");
					System.exit(0);
				}
			}
		}
		
		if (!regionSelect && !initial)
		{
			if (state.turnPhase == 0)
			{
				bidding = true;
				buyresource = false;
				buycity = false;
				powering = false;
			}
			if (state.turnPhase == 1)
			{
				bidding = false;
				buyresource = true;
				buycity = false;
				powering = false;
			}
			if (state.turnPhase == 2)
			{
				bidding = false;
				buyresource = false;
				buycity = true;
				powering = false;
			}
			if (state.turnPhase == 3)
			{
				bidding = false;
				buyresource = false;
				buycity = false;
				powering = true;
			}
		}
	}
	
	public boolean contains(Rectangle r, MouseEvent m)
	{
		return r.contains(m.getPoint());
	}
	
	public boolean areAdjacent(ArrayList<Region> r)
	{
		if (r.contains(Region.purple) && r.contains(Region.blue) && r.contains(Region.orange) && r.contains(Region.green))
			return false;
		if (r.contains(Region.yellow) && r.contains(Region.blue) && r.contains(Region.orange) && r.contains(Region.green))
			return false;
		if (r.contains(Region.purple) && r.contains(Region.blue) && r.contains(Region.orange) && r.contains(Region.red))
			return false;
		return true;
	}

	@Override
	public void mouseReleased(MouseEvent m) {}
	// ----------------------------------------------------------------------------------------------------
}
