 import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
public class Player implements Comparable<Player>{

	// push
	public String colour;
	public int money;
	public int poweredHouses;
	public int ownedHouses;
	
	
	
	public ArrayList<Powerplant> ownedPlants;
	public ArrayList<City> ownedCities;
	
	private HashMap<Resource, Integer> resourcesStored;
	
	
	public Player(String colour) {
		money = 50;
		poweredHouses = 0;
		ownedHouses = 0;
		this.colour = colour;
		
		ownedPlants = new ArrayList<Powerplant>();
		ownedCities = new ArrayList<City>();
		
		resourcesStored = new HashMap<Resource, Integer>();
		ArrayList<Resource> valid = new ArrayList<Resource>(Arrays.asList(Resource.coal, Resource.oil, Resource.uranium, Resource.trash));
		for (Resource r: valid)
		{
			resourcesStored.put(r, 0);
		}
	}
	
	public boolean addResourceToStorage(Resource r, int count) {
		int coalSpace = 0;
		int oilSpace = 0;
		int trashSpace = 0;
		int uraniumSpace = 0;
		for(int i = 0; i < ownedPlants.size(); i++)
		{
			if(ownedPlants.get(i).getResourceType() == Resource.coal )
				coalSpace += ownedPlants.get(i).storageCapacity();
			if(ownedPlants.get(i).getResourceType() == Resource.oil)
				oilSpace += ownedPlants.get(i).storageCapacity();
			if(ownedPlants.get(i).getResourceType() == Resource.trash)
				trashSpace += ownedPlants.get(i).storageCapacity();	
			if(ownedPlants.get(i).getResourceType() == Resource.uranium)
				uraniumSpace += ownedPlants.get(i).storageCapacity();	
			
			if(ownedPlants.get(i).getResourceType() == Resource.hybrid) {
				if(resourcesStored.get(Resource.oil)<=ownedPlants.get(i).getAmountToPower()*2)
				coalSpace += ownedPlants.get(i).storageCapacity()-resourcesStored.get(Resource.oil);
				else
					coalSpace += ownedPlants.get(i).storageCapacity()-ownedPlants.get(i).getAmountToPower()*2;
				
				if(resourcesStored.get(Resource.coal)<=ownedPlants.get(i).getAmountToPower()*2)
					oilSpace += ownedPlants.get(i).storageCapacity()-resourcesStored.get(Resource.coal);
					else
						oilSpace += ownedPlants.get(i).storageCapacity()-ownedPlants.get(i).getAmountToPower()*2;
			}
		}
		
		System.out.println(String.format("coal:%d, oil:%d, trash:%d, uranium:%d", coalSpace, oilSpace, trashSpace, uraniumSpace));
		if(r.equals(Resource.coal) && coalSpace - resourcesStored.get(Resource.coal) >= count) {
			resourcesStored.replace(Resource.coal, resourcesStored.get(Resource.coal)+1);
			return true;
		}
		if(r.equals(Resource.oil) && oilSpace - resourcesStored.get(Resource.oil) >= count) {
			resourcesStored.replace(Resource.oil, resourcesStored.get(Resource.oil)+1);
			return true;
		}
		if(r.equals(Resource.trash) && trashSpace - resourcesStored.get(Resource.trash) >= count) {
			resourcesStored.replace(Resource.trash, resourcesStored.get(Resource.trash)+1);
			return true;
		}
		if(r.equals(Resource.uranium) && uraniumSpace - resourcesStored.get(Resource.uranium) >= count) {
			resourcesStored.replace(Resource.uranium, resourcesStored.get(Resource.uranium)+1);
			return true;
		}
		
		return false;
	}
	
	public int getSpace(Resource r)
	{
		int coalSpace = 0;
		int oilSpace = 0;
		int trashSpace = 0;
		int uraniumSpace = 0;
		for(int i = 0; i < ownedPlants.size(); i++)
		{
			if(ownedPlants.get(i).getResourceType() == Resource.coal )
				coalSpace += ownedPlants.get(i).storageCapacity();
			if(ownedPlants.get(i).getResourceType() == Resource.oil)
				oilSpace += ownedPlants.get(i).storageCapacity();
			if(ownedPlants.get(i).getResourceType() == Resource.trash)
				trashSpace += ownedPlants.get(i).storageCapacity();	
			if(ownedPlants.get(i).getResourceType() == Resource.uranium)
				uraniumSpace += ownedPlants.get(i).storageCapacity();	
			
			if(ownedPlants.get(i).getResourceType() == Resource.hybrid) {
				if(resourcesStored.get(Resource.oil)<=ownedPlants.get(i).getAmountToPower()*2)
				coalSpace += ownedPlants.get(i).storageCapacity()-resourcesStored.get(Resource.oil);
				else
					coalSpace += ownedPlants.get(i).storageCapacity()-ownedPlants.get(i).getAmountToPower()*2;
				
				if(resourcesStored.get(Resource.coal)<=ownedPlants.get(i).getAmountToPower()*2)
					oilSpace += ownedPlants.get(i).storageCapacity()-resourcesStored.get(Resource.coal);
					else
						oilSpace += ownedPlants.get(i).storageCapacity()-ownedPlants.get(i).getAmountToPower()*2;
		}
	}
		
		if (r.equals(Resource.coal))
			return coalSpace;
		if (r.equals(Resource.oil))
			return oilSpace;
		if (r.equals(Resource.trash))
			return trashSpace;
		return uraniumSpace;
	}
	public void calcPowerableHouses() {
		int powerable = 0;
		int amount = 0;
		int amountStored = 0;
		int used = 0;
		Resource type;
		
		for(int pp = 0; pp < ownedPlants.size(); pp++) 
		{
			type = ownedPlants.get(pp).getResourceType();
			amount = ownedPlants.get(pp).getAmountToPower();
			amountStored = resourcesStored.get(type);
			
			powerable = amountStored / amount;
			used = powerable * (amount - (amountStored % amount));
			
			resourcesStored.replace(type, amountStored - used);
			
			if(powerable >= ownedHouses)
				poweredHouses = ownedHouses;
			else poweredHouses = powerable;
			
		}
	}
	
	
	public boolean addPowerPlant(Powerplant pp) {
		
//		Scanner input = new Scanner(System.in);
		if(ownedPlants.size() < 3)
		{
			ownedPlants.add(pp);
//			input.close();
			return true;
		}
		return false;
		/*
		else {
			System.out.println("You have 3 Powerplants already. "
					+ "Do you want to discard one? Y or N. If you choose N, the current powerplant (powerplant #"+pp.getName()+")"
					+ " won't be yours and will still be available for purchase at the Powerplant market.");
			ans = input.next();
			if(ans.equals("Y")) {
				System.out.println("Choose a Powerplant to discard");
				for(int i = 0; i < 3; i++)
					System.out.println("Powerplant #" + ownedPlants.get(i).getName());
				ppAns = input.nextInt();
				Powerplant pplt = ownedPlants.get(ppAns);
				ownedPlants.remove(ownedPlants.get(ppAns));
				ownedPlants.add(pp);
				System.out.println("Removed Powerplant #" + pplt.getName()+" and added Powerplant #"+pp.getName()+" to owned Powerplants");
			}
		}
		input.close();
		return false;
		*/
	}
	
	/* If a player were to add a powerplant
	 * and their inventory were full,
	 * addPowerPlant would return false
	 * and then GameState would ask them
	 * if they want to replace a powerplant.
	 * If they do, then the player will see
	 * the options to replace and
	 * GameState will call this method
	 * with a powerplant to replace
	 * as input
	 */
	public boolean replacePowerplant(Powerplant toBeReplaced, Powerplant replacer)
	{
		boolean completed = false;
		if (ownedPlants.remove(toBeReplaced))
			completed = true;
		ownedPlants.add(replacer);
		return completed;
	}
	
	public boolean removePowerplant(Powerplant pp) {
		Scanner input = new Scanner(System.in);
		System.out.println("Are you sure you want to remove this powerplant? Y or N");
		String answer = input.next();
		input.close();
		if(answer.equals("Y"))
		{
			ownedPlants.remove(pp);
			return true;
		}
		return false;
	}
	public int income() {
	    int income = 0;
	    int increment = 11;
	    if(poweredHouses == 0) {
	        money += 10;
	        return 10;
	    }
	    if(poweredHouses == 1) {
	        money += 22;
	        return 22;
	    }
	    if(poweredHouses == 2) {
	        money+=33;
	        return 33;
	    }
	    income = 22;
	    if(poweredHouses > 2)
	    for(int numHouse = 3; numHouse <= poweredHouses; numHouse+=2) {
	        income += increment*2;
	        increment--;
	    }
	    if(poweredHouses % 2 == 0 && poweredHouses > 2) {
	        income += increment;
	    }
	    money += income;
	    return income;
	}

	public int getMoney() {
		return money;
	}

	public ArrayList<City> getCities() {
		return ownedCities;
	}
	
	public int getPoweredHouses() {
		return poweredHouses;
	}
	public void addCity(City c) {
		ownedCities.add(c);
	}
	
	public boolean addMoney(int amount)
	{
		int initial = this.money;
		if (initial - amount < 0)
			return false;
		this.money += amount;
		return true;

	}
	public int addPowerplants()
	{
		int number = 0;
		for(int x = 0; x < ownedPlants.size(); x++)
		{
			number = number + ownedPlants.get(x).getName();
		}
		return number;
	}
	@Override
	public int compareTo(Player other) 
	{
		if(this.getPoweredHouses() > other.getPoweredHouses())
		{
			return 1;
		}
		else if(this.getPoweredHouses() < other.getPoweredHouses())
		{
			return -1;
		}
		else if(this.getPoweredHouses() == other.getPoweredHouses())
		{
			if(this.addPowerplants() > other.addPowerplants())
			{
				return 1;
			}
			else if(this.addPowerplants() < other.addPowerplants())
			{
				return -1;
			}
			else if(this.addPowerplants() == other.addPowerplants())
			{
				if(this.getMoney() > other.getMoney())
				{
					return 1;
				}
				else if(this.getMoney() < other.getMoney())
				{
					return -1;
				}
			}
		}
		return 0;
	}
	public HashMap<Resource, Integer> getResources() {
		return resourcesStored;
	}
	public String toString() {return "Player " + colour;}
}
