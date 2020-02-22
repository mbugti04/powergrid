import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class UI extends JPanel implements MouseListener
{
	private String[] titles = new String[]
			{"the", "testing", "another test", "UI"};
	private int width = 1920, height = 1080;
	
	private JFrame frame;
	
	public UI()
	{
		super();
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		addMouseListener(this);
		
		frame = new JFrame(titles[(int)(Math.random() * titles.length)]); // selects a random title
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
//		frame.setResizable(false);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		frame.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
}
