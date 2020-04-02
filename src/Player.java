import java.util.*;
public class Player {

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
		int amount; 
		for(int i = 0; i < ownedPlants.size(); i++)
		{
			if(ownedPlants.get(i).getResourceType() == Resource.coal || ownedPlants.get(i).getResourceType() == Resource.hybrid)
				coalSpace += ownedPlants.get(i).getStorageCapacity();
			if(ownedPlants.get(i).getResourceType() == Resource.oil || ownedPlants.get(i).getResourceType() == Resource.hybrid)
				oilSpace += ownedPlants.get(i).getStorageCapacity();
			if(ownedPlants.get(i).getResourceType() == Resource.trash)
				trashSpace += ownedPlants.get(i).getStorageCapacity();	
			if(ownedPlants.get(i).getResourceType() == Resource.uranium)
				uraniumSpace += ownedPlants.get(i).getStorageCapacity();	
		}
		
		if(r.equals(Resource.coal) && coalSpace - resourcesStored.get(Resource.coal) >= count) {
			resourcesStored.replace(Resource.coal, resourcesStored.get(Resource.coal)+1);
			return true;
		}
		if(r.equals(Resource.oil) && oilSpace - resourcesStored.get(Resource.oil) >= count) {
			resourcesStored.replace(Resource.oil, resourcesStored.get(Resource.oil)+1);
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

	public void setMoney(int money) {
		this.money = money;
	}

	public int getPoweredHouses() {
		return poweredHouses;
	}

	public void setPoweredHouses(int poweredHouses) {
		this.poweredHouses = poweredHouses;
	}

	public boolean isHasBid() {
		return hasBid;
	}

	public void setHasBid(boolean hasBid) {
		this.hasBid = hasBid;
	}

	public boolean isHasBought() {
		return hasBought;
	}

	public void setHasBought(boolean hasBought) {
		this.hasBought = hasBought;
	}

	public ArrayList<Powerplant> getOwnedPlants() {
		return ownedPlants;
	}

	public void setOwnedPlants(ArrayList<Powerplant> ownedPlants) {
		this.ownedPlants = ownedPlants;
	}

	public ArrayList<City> getOwnedCities() {
		return ownedCities;
	}

	public void setOwnedCities(ArrayList<City> ownedCities) {
		this.ownedCities = ownedCities;
	}

	public HashMap<Resource, Integer> getResourcesStored() {
		return resourcesStored;
	}

	public void setResourcesStored(HashMap<Resource, Integer> resourcesStored) {
		this.resourcesStored = resourcesStored;
	}
	
	
}
