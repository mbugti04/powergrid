
public class City
{
	private String name;
	private int costToBuild;
	private int availableSpace;
	private int numOfHouses;
	private boolean isPowered;
//	private region inRegion; 
	
	// possibly temp; just testing for graphics
//	private HashMap<City, Integer> connection; // city and cost to get there
	
	public City(String name)
	{
		this.name = name;
//		connection = new HashMap<City, Integer>();
	}
	
	/*public HashMap<City, Integer> getConnection()
	{
		return connection;
	}*/
	
	public void addCity(City other, int cost) // possibly returning boolean?
	{
//		connection.put(other, cost);
	}
	
	public int nextCost() 
	{
		if(numOfHouses == 0) 
		{
			return 10;
		}
		else if(numOfHouses == 1)
		{
			return 15;
		}
		else
			return 20;
	}
	
	public boolean isAvailale() {
		int step = GameState.step;
		if(numOfHouses < step)
		return true;
		return false;
	}
	
	public String getName()
	{
		return name;
	}
	public int getAvailableSpace() {
		return availableSpace;
	}
	public int getCostToBuild() {
		return costToBuild;
	}
	public int getNumOfHouses() {
		return numOfHouses;
	}
	public boolean isPowered() {
		return isPowered;
	}

	public void setPowered(boolean isPowered) {
		this.isPowered = isPowered;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCostToBuild(int costToBuild) {
		this.costToBuild = costToBuild;
	}

	public void setAvailableSpace(int availableSpace) {
		this.availableSpace = availableSpace;
	}

	public void setNumOfHouses(int numOfHouses) {
		this.numOfHouses = numOfHouses;
	}
	
	public String toString()
	{
		return name;
	}
}
