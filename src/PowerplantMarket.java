import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class PowerplantMarket
{
	
	public ArrayList<Powerplant> plantsAvailable;
	public Queue<Powerplant> allPlants;
	public ArrayList<Powerplant> allPlantsAL;
	
	public PowerplantMarket()
	{
		plantsAvailable = new ArrayList<Powerplant>();
		allPlants = new LinkedList<Powerplant>();
		allPlantsAL = new ArrayList<Powerplant>();
	}
	
	public ArrayList<Powerplant> getPlantsAvailable()
	{
		return plantsAvailable;
	}
	public Queue<Powerplant> getAllPlants()
	{
		return allPlants;
	}
	public void shuffle() 
	{
		ArrayList<Powerplant> plants = new ArrayList<Powerplant>();
		plants.addAll(allPlants);
		Collections.shuffle(plants);
		allPlants.clear();
		allPlants.addAll(plants);
	}
	public void restock()
	{
		plantsAvailable.add(allPlants.remove());
	}
	public void sort()
	{
		Collections.sort(plantsAvailable, Powerplant.powerNum);
	}
	
	public boolean removePlant(Powerplant p)
	{
		boolean success = plantsAvailable.remove(p);
		restock();
		sort();
		return success;
	}
	
	public void bid(boolean raise , Powerplant auction)
	{
		int price = auction.getName();
		if(raise == true) 
		{
			price++;
		}
	}
}
