/*
 * First time setup-
 * 24 coal, 18 oil, 6 trash, 2 uranium
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ResourceMarket
{
	int step;
	int players;
	private HashMap<Resource, Integer> currentStock;
	private HashMap<Resource, ArrayList<Integer>> restockAmount;
	
	public ResourceMarket(int players)
	{
		this.step = 1;
		this.players = players;
		
		currentStock = new HashMap<Resource, Integer>();
		restockAmount = new HashMap<Resource, ArrayList<Integer>>();
		
		firstTimeSetup();
		restockData();
	}
	
	private void firstTimeSetup()
	{
		currentStock.put(Resource.coal, 24);
		currentStock.put(Resource.oil, 18);
		currentStock.put(Resource.trash, 6);
		currentStock.put(Resource.uranium, 2);
	}
	
	/* This is the initial setup
	 * for how much each resource will restock
	 * at the end of the round for each step
	 */
	private void restockData()
	{
		try
		{
			Scanner scanner = new Scanner(new File("src/restockData.txt"));
			while (scanner.hasNextLine())
			{
				/* If data matches the current number of players.
				 * It's there to get the correct data if
				 * the number of players changes
				 */
				if (scanner.nextLine().contentEquals(players + ""))
					break;
			}
			
			/* goes through each resource
			 * to fill in the amount
			 * that will be restocked
			 * at the end of each stage
			 */
			while (scanner.hasNext())
			{
				String next = scanner.nextLine();
				String name = next.substring(0, next.indexOf(" "));
				String[] data = next.substring(next.indexOf(" ") + 1).split(" ");
				
				/* converts the data from string to int */
				ArrayList<Integer> intData = new ArrayList<Integer>();
				for (int i = 0; i < data.length; i++)
					intData.add(Integer.parseInt(data[i]));
				
				restockAmount.put(Resource.valueOf(name), intData);
				
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void restock()
	{
		int step = GameState.step;
		if(step == 1) {
			stock.put(Resource.coal, stock.get(Resource.coal)+5);
			if(stock.get(Resource.coal)>24)
				stock.put(Resource.coal,24);
			
			stock.put(Resource.oil, stock.get(Resource.oil)+3);
			if(stock.get(Resource.oil)>24)
				stock.put(Resource.oil,24);
			
			stock.put(Resource.trash, stock.get(Resource.trash)+2);
			if(stock.get(Resource.trash)>24)
				stock.put(Resource.trash,24);
			
			stock.put(Resource.uranium, stock.get(Resource.uranium)+1);
			if(stock.get(Resource.uranium)>12)
				stock.put(Resource.uranium,12);
		}
		if(step == 2) {
			stock.put(Resource.coal, stock.get(Resource.coal)+6);
			if(stock.get(Resource.coal)>24)
				stock.put(Resource.coal,24);
			
			stock.put(Resource.oil, stock.get(Resource.oil)+4);
			if(stock.get(Resource.oil)>24)
				stock.put(Resource.oil,24);
			
			stock.put(Resource.trash, stock.get(Resource.trash)+3);
			if(stock.get(Resource.trash)>24)
				stock.put(Resource.trash,24);
			
			stock.put(Resource.uranium, stock.get(Resource.uranium)+2);
			if(stock.get(Resource.uranium)>12)
				stock.put(Resource.uranium,12);
		}
		if(step == 3) {
			stock.put(Resource.coal, stock.get(Resource.coal)+4);
			if(stock.get(Resource.coal)>24)
				stock.put(Resource.coal,24);
			
			stock.put(Resource.oil, stock.get(Resource.oil)+5);
			if(stock.get(Resource.oil)>24)
				stock.put(Resource.oil,24);
			
			stock.put(Resource.trash, stock.get(Resource.trash)+4);
			if(stock.get(Resource.trash)>24)
				stock.put(Resource.trash,24);
			
			stock.put(Resource.uranium, stock.get(Resource.uranium)+2);
			if(stock.get(Resource.uranium)>12)
				stock.put(Resource.uranium,12);
		
		}
	}
}
