import java.util.*;
public class Player {

	private int money;
	private int poweredCities;
	
	private boolean hasBid;
	private boolean hasBought;
	
	private ArrayList<Powerplant> ownedPlants;
	private ArrayList<City> ownedCities;
	
	private HashMap<Resource, Integer> resourcesStored;
}
