import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UI extends JPanel implements MouseListener, MouseMotionListener
{
	public static final int width = 1920, height = 1080;
	
	private JFrame frame;
	
	private String[] titles = new String[]
			{"Stone is Unbreakable", "Now with updated graphics!", "test 14829"};
	
	private HashMap<String, BufferedImage> images;
	private HashMap<City, Rectangle> pieces = new HashMap<City, Rectangle>();
	private HashMap<City, Point> lastPoint = new HashMap<City, Point>();
	private HashMap<City, Boolean> pressedOut = new HashMap<City, Boolean>();
	
	private ArrayList<City> allCities;
	
	private UrbanArea area;
	
	private int lastx, lasty;
	private boolean firstTime = true;
	private boolean pressOut = false;
	
	// more temp code yay
	Button start = new Button("button test", 860, 0, 200, 50);
	//
	
	
	public UI()
	{
		super();
		initFrame();
		initImages();
		
		// temp code? //
		area = new UrbanArea();
		
//		area.addCity(new City("Houston"));
//		area.addCity(new City("Dallas"));
//		area.addCity(new City("Austin"));
//		area.addCity(new City("New Orleans"));
//		area.addCity(new City("Austin"));
		
		allCities = new ArrayList<City>();
		allCities.addAll(area.getCitySet());
		
		System.out.println(allCities);
		
		area.addConnection(allCities.get(3), allCities.get(0), 5);
		area.addConnection(allCities.get(2), allCities.get(3), 3);
		area.addConnection(allCities.get(2), allCities.get(0), 3);
		area.addConnection(allCities.get(1), allCities.get(3), 7);
		
		area.removeCity(allCities.get(3));
		
		
		// have to reinitialise allCities when removing a city
		allCities = new ArrayList<City>();
		allCities.addAll(area.getCitySet());
		
		initCities();
		////////
		
		frame.setVisible(true);
	}
	
	public void initFrame()
	{
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
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public void initImages()
	{
		images = new HashMap<String, BufferedImage>();
		try
		{
			images.put("water", ImageIO.read(new File("assets/water.png")));
			images.put("map", ImageIO.read(new File("assets/us_map.png")));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// TODO temp code
	public void initCities()
	{
		for (int i = 0; i < allCities.size(); i++)
			pieces.put(allCities.get(i), new Rectangle(65, 65));
		
		for (City city : allCities)
			pressedOut.put(city, false);
		
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		// temp code (dragging test)
		if (firstTime)
		{
			for (int i = 0; i < allCities.size(); i++)
				pieces.get(allCities.get(i)).setLocation(width / 10 * i, height / 10 * i);
			
			firstTime = false;
		}
		
		paintBackground(g2);
		
//		g2.setColor(new Color(132, 232, 182));
		g2.setColor(Color.DARK_GRAY);
		for (int i = 0; i < allCities.size(); i++)
			drawCity(g2, allCities.get(i));
		
		// more temp code
		start.drawButton(g2);
	}
	
	// another test
	public void drawCity(Graphics2D g2, City c)
	{
		Rectangle cityRectangle = pieces.get(c);
		g2.fill(cityRectangle);
//		g2.drawString(c.getName(), cityRectangle.x, cityRectangle.y);
		
		ArrayList<City> connections = new ArrayList<City>(area.getGraph().get(c).keySet());
		for (City connec: connections)
			g2.drawLine((int)cityRectangle.getCenterX(), (int)cityRectangle.getCenterY(), (int)pieces.get(connec).getCenterX(), (int)pieces.get(connec).getCenterY());
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
		
		// if (e.get)
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
		for (City city : allCities)
		{
//			lastx = pieces.get(allCities.get(0)).x - e.getX();
//			lasty = pieces.get(allCities.get(0)).y - e.getY();
			
			lastPoint.put(city, new Point(pieces.get(city).x - e.getX(), pieces.get(city).y - e.getY()));
			
			if (pieces.get(city).contains(e.getX(), e.getY()))
			{
				updateLocation(e, city);
			}
			else
			{
				pressedOut.put(city, true);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		for (City city : allCities)
		{
			if (pieces.get(city).contains(e.getX(), e.getY()) )
			{
				updateLocation(e, city);
			}
			else
			{
				pressedOut.put(city, false);
			}
		}
	}
	
	public void updateLocation(MouseEvent e, City city)
	{
		pieces.get(city).setLocation(lastPoint.get(city).x + e.getX(), lastPoint.get(city).y + e.getY());
		/*if (piece.x + piece.width >= width)
			piece.setLocation(width - piece.width, lasty + e.getY());
		if (piece.y + piece.height > height)
			piece.setLocation(lastx + e.getX(), height - piece.height);*/
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		for (City city : allCities)
		{
			if (!pressedOut.get(city))
			{
				updateLocation(e, city);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	class Button extends Rectangle
	{
		String text;
		Color buttonCol;
		Color textCol;
		
		Integer fontW, fontH; // width/height of text
		
		public Button(String text, int x, int y, int width, int height)
		{
			super(x, y, width, height);
			this.text = text;
			buttonCol = Color.white;
			textCol = Color.black;
		}
		
		public Button(String text, int x, int y, int width, int height, Color buttonCol, Color textCol)
		{
			this(text, x, y, width, height);
			this.buttonCol = buttonCol;
			this.textCol = textCol;
		}
		
		public boolean isPressed(double x, double y)
		{
			if (this.contains(x, y))
				return true;
			return false;
		}
		
		public boolean isPressed(Point p)
		{
			return isPressed(p.getX(), p.getY());
		}
		
		public void drawButton(Graphics2D g2)
		{
			g2.setColor(buttonCol);
			g2.fill(this);
			
			g2.setColor(textCol);
			if (fontW == null)
				fontW = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			if (fontH == null)
				fontH = (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
			g2.drawString(text, this.x + this.width/2 + fontW/2 - fontW, this.y + this.height/2 + fontH/2);
//			g2.drawString(text, x + fontW, y + fontH);
			
		}
	}
	
}
