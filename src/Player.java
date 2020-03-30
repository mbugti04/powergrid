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
	
//	public boolean addResourceToStorage(Resource r) {
//		
//	}
	public boolean addPowerPlant(Powerplant pp) {
		// are we going to check if the player has 3 powerplants before calling this method?
		// bc we need to ask them which powerplant they want to discard
		
		if(ownedPlants.size() < 3) {
			ownedPlants.add(pp);
			return true;
		}
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
	if(poweredHouses == 0) {
		money += 10;
		return;
	}
	if(poweredHouses == 1) {
		money+= 22;
		return;
	}

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
