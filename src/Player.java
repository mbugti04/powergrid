 import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class Player implements Comparable<Player>{

	private int money;
	private int poweredHouses;
	
	private boolean hasBid;
	private boolean hasBought;
	
	private ArrayList<Powerplant> ownedPlants;
	private ArrayList<City> ownedCities;
	
	private HashMap<Resource, Integer> resourcesStored;
	
	public Player() {
		money = 50;
		poweredHouses = 0;
		
		hasBid = false;
		hasBought = false;
		
		ownedPlants = new ArrayList<Powerplant>();
		ownedCities = new ArrayList<City>();
		
		resourcesStored = new HashMap<Resource, Integer>();
	}
	
	public boolean addResourceToStorage(Resource r, int count) {
		int coalSpace = 0;
		int oilSpace = 0;
		int trashSpace = 0;
		int uraniumSpace = 0;
		for(int i = 0; i < ownedPlants.size(); i++)
		{
			if(ownedPlants.get(i).getResourceType() == Resource.coal || ownedPlants.get(i).getResourceType() == Resource.hybrid)
				coalSpace += ownedPlants.get(i).storageCapacity();
			if(ownedPlants.get(i).getResourceType() == Resource.oil || ownedPlants.get(i).getResourceType() == Resource.hybrid)
				oilSpace += ownedPlants.get(i).storageCapacity();
			if(ownedPlants.get(i).getResourceType() == Resource.trash)
				trashSpace += ownedPlants.get(i).storageCapacity();	
			if(ownedPlants.get(i).getResourceType() == Resource.uranium)
				uraniumSpace += ownedPlants.get(i).storageCapacity();	
		}
		
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
	public boolean addPowerPlant(Powerplant pp) {
		
		Scanner input = new Scanner(System.in);
		String ans;
		int ppAns;
		if(ownedPlants.size() < 3) {
			ownedPlants.add(pp);
			input.close();
			return true;
		}
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
	public void income() {
	int income = 0;
	int increment = 11;
	if(poweredHouses == 0) {
		money += 10;
		return;
	}
	if(poweredHouses == 1) {
		money+= 22;
		return;
	}
	if(poweredHouses == 2) {
		money+=33;
		return;
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
	}

	public int getMoney() {
		return money;
	}

	public int getPoweredHouses() {
		return poweredHouses;
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
}
