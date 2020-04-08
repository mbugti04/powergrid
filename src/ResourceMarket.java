/*
 * First time setup-
 * 24 coal, 18 oil, 6 trash, 2 uranium
 */
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class ResourceMarket
{
	int step;
	int players;
	private HashMap<Resource, Integer> stock;
	private HashMap<Integer, HashMap<Resource, Integer>> price; // step, <resource, price>
	
	public ResourceMarket(int players)
	{
		this.step = 1;
		this.players = players;
		
		stock = new HashMap<Resource, Integer>();
		price = new HashMap<Integer, HashMap<Resource, Integer>>();
		
		firstTimeSetup();
		restockData();
	}
	
	private void firstTimeSetup()
	{
		stock.put(Resource.coal, 24);
		stock.put(Resource.oil, 18);
		stock.put(Resource.trash, 6);
		stock.put(Resource.uranium, 2);
	}
	
	private void restockData()
	{
		try
		{
			// step 1
			price.put(1, new HashMap<Resource, Integer>());
			
			Scanner s = new Scanner(new File("src/restockData.txt"));
			String data = "";
			while (true)
			{
				data = s.nextLine();
				if (data.equals(players + " players"))
					break;
			}
			while (true)
			{
				data = s.nextLine();
				if (data.equals("step " + step))
					break;
			}
			while (true)
			{
				data = s.nextLine();
				if (data.equals("coal"))
				{
					data = s.nextLine();
					break;
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void restock()
	{
		int step = GameState.step;
		if(step == 1) {
			stock.put(Resource.coal, stock.get(Resource.coal)+5);
			stock.put(Resource.oil, stock.get(Resource.oil)+3);
			stock.put(Resource.trash, stock.get(Resource.trash)+2);
			stock.put(Resource.uranium, stock.get(Resource.uranium)+1);
		}
		if(step == 2) {
			stock.put(Resource.coal, stock.get(Resource.coal)+6);
			stock.put(Resource.oil, stock.get(Resource.oil)+4);
			stock.put(Resource.trash, stock.get(Resource.trash)+3);
			stock.put(Resource.uranium, stock.get(Resource.uranium)+2);
		}
		if(step == 3) {
			stock.put(Resource.coal, stock.get(Resource.coal)+4);
			stock.put(Resource.oil, stock.get(Resource.oil)+5);
			stock.put(Resource.trash, stock.get(Resource.trash)+4);
			stock.put(Resource.uranium, stock.get(Resource.uranium)+2);
		}
	}
}
