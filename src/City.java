
public class City
{
	private String name;
	private int currentNumOfHouses;
	private Region region;
	
	public City(String name)
	{
		this.name = name;
	}
	
	/* Cost to build based on number of houses built.
	 * Returns -1 if no available space.
	 * Factors in available space. ex: it won't return 15
	 * if game is still in step 1.
	 */
	public int nextCost() 
	{
		if (isAvailable())
		{
			if(currentNumOfHouses == 0) 
				return 10;
			else if(currentNumOfHouses == 1)
				return 15;
			else if (currentNumOfHouses == 2)
				return 20;
		}
		return -1;
	}
	
	public boolean isAvailable()
	{
		int step = GameState.step;
		if (currentNumOfHouses < step)
			return true;
		return false;
	}
	
	public int availableSpace()
	{
		int step = GameState.step;
		return step - currentNumOfHouses;
	}
		
	public String toString()
	{
		return name + ": " + currentNumOfHouses;
	}
}
