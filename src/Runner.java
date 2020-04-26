
public class Runner
{
	public static void main(String[] args)
	{
		runGame();
//		bidTest();
	}
	
	public static void bidTest()
	{
		GameState e = new GameState();
		e.bid();
	}
	public static void runGame()
	{
		Interface f = new Interface();
		
		while (true)
		{
			f.repaint();
			f.info();
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
