import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// this is a 'graph' class for the cities
public class UrbanArea
{
	ArrayList<Region> activeRegions = new ArrayList<Region>();
	HashMap<City, HashMap<City, Integer>> cities = new HashMap<>();

	public void addCity(City c)
	{
		cities.put(c, new HashMap<City, Integer>());
	}

	public void addConnection(City source, City destination, int cost)
	{
//		if (!cities.containsKey(source))
//			addCity(source);
//
//		if (!cities.containsKey(destination))
//			addCity(destination);

		// these connect both of them to each other
		
		if (source != null && destination != null)
		{
			cities.get(source).put(destination, cost);
			cities.get(destination).put(source, cost);
		}

//		cities.get(destination).put(source, cost);
	}
	
	// needed for limiting regions
	public void removeRegion(Region r)
	{
		for (City c: new ArrayList<City>(cities.keySet()))
		{
			if (c.getRegion().equals(r))
				removeCity(c);
		}
	}
	public void removeCity(City toRemove)
	{
		// removes key
		if (cities.containsKey(toRemove))
			cities.remove(toRemove);
		
		// removes connections from all cities
		for (City c: new ArrayList<City>(cities.keySet()))
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
	public void toggleRegion(Region r)
	{
		if (!activeRegions.contains(r))
			activeRegions.add(r);
		else
			activeRegions.remove(r);
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
		if (a == null || b == null)
			return -1;
		if (cities.get(a).containsKey(b))
			return cities.get(a).get(b);
		return -1;
	}
	
	public HashSet<City> getListOfAllCities()
	{
		HashSet<City> temp = new HashSet<City>();
		temp.addAll(cities.keySet());
		return temp;
	}
	
	public ArrayList<Region> getActiveRegions()
	{
		return activeRegions;
	}
}
