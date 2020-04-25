import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

class Button
{
	static int normalw = 200, normalh = 100;
	String name;
	int x, y, w, h;
	boolean on;
	
	Button(String name, int x, int y, int w, int h)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		on = false;
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
		if (!on)
			g2.setColor(Color.DARK_GRAY);
		else
			g2.setColor(new Color(12, 120, 123));
		g2.fillRect(x, y, w, h);
		g2.setColor(Color.WHITE);
		g2.drawString(name, x + w / 10, y + h / 2);
	}
	
	boolean inBounds(MouseEvent m)
	{
		return m.getX() >= x && m.getY() >= y && m.getX() <= x + w && m.getY() <= y + h;
	}
}