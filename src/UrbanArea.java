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
	
	// needed for limiting regions
	public void removeCity(City toRemove)
	{
		// removes key
		if (cities.containsKey(toRemove))
			cities.remove(toRemove);
		
		// removes connections from all cities
		for (City c: cities.keySet())
		{
			HashMap<City, Integer> tempMap = cities.get(c);
			if (tempMap.containsKey(toRemove))
			{
				tempMap.remove(toRemove);
			}
		}

		// code for checking connections
		/*
		for (City c: cities.keySet())
		{
			System.out.println(c.getName());
			for (City s: cities.get(c).keySet())
			{
				System.out.print(c.getName() + "->");
				System.out.println(s.getName());
			}
		}
		*/
		//
		
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
	
	public HashMap<City, HashMap<City, Integer>> getGraph()
	{
		return cities;
	}
	
	public HashSet<City> getCitySet()
	{
		HashSet<City> temp = new HashSet<City>();
		temp.addAll(cities.keySet());
		return temp;
	}
}
