import java.util.HashMap;

public class City
{
	private String name;
	
	// possibly temp; just testing for graphics
	private HashMap<City, Integer> connection; // city and cost to get there
	
	public City(String name)
	{
		this.name = name;
		connection = new HashMap<City, Integer>();
	}
	
	public void add(City other, int cost) // possibly returning boolean?
	{
		connection.put(other, cost);
	}

	public String getName()
	{
		return name;
	}

	public HashMap<City, Integer> getConnection()
	{
		return connection;
	}
}
