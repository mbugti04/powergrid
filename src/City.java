public class City
{
	private String name;
	private int costToBuild;
	private int availableSpace;
	private boolean isPowered;
//	private region inRegion; 
	
	// possibly temp; just testing for graphics
//	private HashMap<City, Integer> connection; // city and cost to get there
	
	public City(String name)
	{
		this.name = name;
//		connection = new HashMap<City, Integer>();
	}
	
	public void addCity(City other, int cost) // possibly returning boolean?
	{
//		connection.put(other, cost);
	}
	
	public int nextCost() {
		return 0; 
	}
	
	public boolean isAvailale() {
		return true;
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
	public boolean powered() {
		return isPowered;
	}
	/*public HashMap<City, Integer> getConnection()
	{
		return connection;
	}*/
	
	public String toString()
	{
		return name;
	}
}
