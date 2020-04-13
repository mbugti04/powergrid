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
	public static void main(String[] args)
	{
		ResourceMarket r = new ResourceMarket();
		System.out.println(r.getPrice(Resource.uranium));
	}
	
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
		if (p.addMoney(-resourcePrice))
		{
			this.removeFromCurrentStock(r, 1);
			p.addResourceToStorage(r, 1);
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
			Scanner scanner = new Scanner(new File("src/restockData.txt"));
			while (scanner.hasNextLine())
			{
				/* If data matches the current number of players.
				 * It's there to get the correct data if
				 * the number of players changes
				 */
				if (scanner.nextLine().contentEquals(GameState.playerCount + ""))
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
