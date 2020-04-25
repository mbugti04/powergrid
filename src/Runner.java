
public class Runner
{
	public static void main(String[] args)
	{
		Interface f = new Interface();
		
		while (true)
		{
			f.repaint();
			try
			{
				Thread.sleep(1000/30);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
