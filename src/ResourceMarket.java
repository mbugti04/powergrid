/*
 * First time setup-
 * 24 coal, 18 oil, 6 trash, 2 uranium
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ResourceMarket
{	
	public HashMap<Resource, Integer> currentStock;
	public HashMap<Resource, ArrayList<Integer>> restockAmount;
	
	public ResourceMarket()
	{
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
	
	public boolean purchase(Player p, Resource r)
	{
		int resourcePrice = getPrice(r);
		if (p.addResourceToStorage(r, 1) && p.addMoney(-resourcePrice) && currentStock.get(r) > 0)
		{
			this.removeFromCurrentStock(r, 1);
			return true;
		}
		return false;
	}
	
	/* Gets the price of resource
	 * based on how many are in stock
	 */
	public int getPrice(Resource r)
	{
		int count = currentStock.get(r);
		if (!r.equals(Resource.uranium))
		{
			return (int)(-1.0/3.0 * count + 9.0);
		}
		else
		{
			if (count <= 4)
			{
				return (int)(-2 * count + 18);
			}
			else
			{
				return (int)(-count + 13);
			}
		}
	}
	
	/* This is the initial setup
	 * for how much each resource will restock
	 * at the end of the round for each step
	 */
	private void restockData()
	{
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/restockData.txt")));
			String nextLine = reader.readLine();
			
			while (nextLine != null)
			{
				/* If data matches the current number of players.
				 * It's there to get the correct data if
				 * the number of players changes
				 */
				
				if (nextLine.contentEquals(GameState.playerCount + ""))
				{
					nextLine = reader.readLine();
					break;
				}
				nextLine = reader.readLine();
			}
			
			/* goes through each resource
			 * to fill in the amount
			 * that will be restocked
			 * at the end of each stage
			 */
			while (nextLine != null)
			{
				String name = nextLine.substring(0, nextLine.indexOf(" "));
				String[] data = nextLine.substring(nextLine.indexOf(" ") + 1).split(" ");
				
				/* converts the data from string to int */
				ArrayList<Integer> intData = new ArrayList<Integer>();
				for (int i = 0; i < data.length; i++)
					intData.add(Integer.parseInt(data[i]));
				
				restockAmount.put(Resource.valueOf(name), intData);
				
				nextLine = reader.readLine();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// sorry nartney I had to change the code
	public void restock()
	{
		// compensating for array index
		int step = GameState.step - 1;
		
		for (int i = 0; i < Resource.values().length; i++)
		{
			Resource current = Resource.values()[i];
			
			// hybrid + free aren't in the resource market
			if (!(current == Resource.hybrid) && !(current == Resource.free))
				addToCurrentStock(current, restockAmount.get(current).get(step));
		}
	}
	
	private void addToCurrentStock(Resource r, int amt)
	{
		int newAmount = currentStock.get(r) + amt;
		if (newAmount > 24)
			newAmount = 24;
		currentStock.put(r, newAmount);
	}
	
	private boolean removeFromCurrentStock(Resource r, int amt)
	{
		int currentAmount = currentStock.get(r);
		if (currentAmount - amt < 0)
			return false;
		currentStock.put(r, currentAmount - amt);
		return true;
	}
}
