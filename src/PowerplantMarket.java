import java.util.*;

public class PowerplantMarket
{
	private ArrayList<Powerplant> plantsAvailable;
	private Queue<Powerplant> allPlants;
	
	public PowerplantMarket()
	{
		plantsAvailable = new ArrayList<Powerplant>();
	}
	
	public ArrayList<Powerplant> getPlantsAvailable()
	{
		return plantsAvailable;
	}
	public Queue<Powerplant> getAllPlants()
	{
		return allPlants;
	}
	
	public void restock()
	{
		plantsAvailable.add(allPlants.remove());
	}
	
	public void sort()
	{
		Collections.sort(plantsAvailable, Powerplant.powerNum);
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
