import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

class Button
{
	static int normalw = 200, normalh = 100;
	Color color = null;
	String name;
	int x, y, w, h;
	boolean on;
	boolean showname = true;
	
	Button(String name, int x, int y, int w, int h)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		on = false;
	}
	
	Button(String name, int x, int y, int w, int h, Color col)
	{
		this(name, x, y, w, h);
		color = col;
	}
	
	Button(String name, int x, int y, int w, int h, boolean showname)
	{
		this(name, x, y, w, h);
		this.showname = showname;
	}
	
	Button(String name, int x, int y, int w, int h, boolean showname, Color col)
	{
		this(name, x, y, w, h, col);
		this.showname = showname;
	}
	
	boolean press()
	{
		on = !on;
		return on;
	}
	
	boolean isPressed()
	{
		return on;
	}
	
	void draw(Graphics2D g2)
	{
		g2.setFont(new Font("Calibri", Font.PLAIN, 16));
		if (!on)
			g2.setColor(Color.DARK_GRAY);
		else if (on)
			g2.setColor(new Color(12, 120, 123));
		if (color != null)
			g2.setColor(color);
		g2.fillRect(x, y, w, h);
		g2.setColor(Color.WHITE);
		if (showname)
			g2.drawString(name, x + w / 10, y + h / 2);
	}
	
	boolean inBounds(MouseEvent m)
	{
		return m.getX() >= x && m.getY() >= y && m.getX() <= x + w && m.getY() <= y + h;
	}
	public String toString() { return name;}
}