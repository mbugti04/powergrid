import java.util.HashMap;
import java.util.HashSet;

// this is a 'graph' class for the cities
public class UrbanArea
{
	private HashMap<City, HashMap<City, Integer>> cities = new HashMap<>();

	public void addCity(City c)
	{
		cities.put(c, new HashMap<City, Integer>());
	}

	public void addConnection(City source, City destination, int cost)
	{
		if (!cities.containsKey(source))
			addCity(source);

		if (!cities.containsKey(destination))
			addCity(destination);

		// these connect both of them to each other
		
		cities.get(source).put(destination, cost);

		cities.get(destination).put(source, cost);
	}
	
	public void removeConnection()
	{
		// TODO
	}

	public int getNumCities()
	{
		return cities.keySet().size();
	}

	public int getConnectionCount()
	{
		int count = 0;
		for (City c : cities.keySet())
		{
			count += cities.get(c).size();
		}
		
		count /= 2;
		
		return count;
	}

	public boolean hasCity(City c)
	{
		if (cities.containsKey(c))
			return true;
		return false;
	}

	public int hasConnection(City a, City b)
	{
		if (cities.get(a).containsKey(b))
			return cities.get(a).get(b);
		return -1;
	}
	
	public HashMap<City, HashMap<City, Integer>> getCities()
	{
		return cities;
	}
	
	public HashSet<City> getAllCities()
	{
		HashSet<City> temp = new HashSet<City>();
		temp.addAll(cities.keySet());
		return temp;
	}
}
