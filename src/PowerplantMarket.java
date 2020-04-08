import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

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
		//i feel as though this method is redundant, there is no point to having
		//a sort method when the arraylist is going to be sorted in the first place.
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
