import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
public class GameState
{
	public static int step = 1;
	public static int playerCount = 4;
	
	public int bid = 0;
	public int currentPlayer = 0;
	public int turnPhase = 0; // 0=buy power plants. 1=buy resources. 2=building. 3=powering. 4=bureaucracy.
	
	public PowerplantMarket plantMarket = new PowerplantMarket();
	public ResourceMarket resourceMarket = new ResourceMarket();
	public UrbanArea urbanArea = new UrbanArea();
	public ArrayList<Player> players;
	public ArrayList<Player> alreadyBid = new ArrayList<Player>();
	public ArrayList<Player> bidOrder;
	public ArrayList<String> bidOrderC;
	
	public boolean hasEnded = false;
	public boolean initialSetup = true;
	
	public Player bidWinner;
	
	public Powerplant chosenPlant = null;
	
	public GameState()
	{
		mainSetup();
	}
	
	// -------------------- SETUP METHODS --------------------
	private void mainSetup()
	{
		initialiseCities();
		initialisePlayers();
		initialiseTurnOrder();
		initialisePlantMarket();
		initialiseResourceMarket();
	}
	
	public void initialiseCities()
	{
		// comment
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/cities.txt")));
			
			String nextLine = reader.readLine();
			
			while (!nextLine.equals("----------connections----------"))
			{
				String name = nextLine.substring(0, nextLine.indexOf("/"));
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);
				
				String region = nextLine.substring(0, nextLine.indexOf("/"));
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);
				
				String xcoord = nextLine.substring(0, nextLine.indexOf("/"));
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);
				
				String ycoord = nextLine;
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);
				
				City c = new City(name, Region.valueOf(region), Double.parseDouble(xcoord), Double.parseDouble(ycoord));
				
				urbanArea.addCity(c);
				
				nextLine = reader.readLine();
			}
			
			nextLine = reader.readLine();
			while (nextLine != null)
			{
				String firstCity = nextLine.substring(0, nextLine.indexOf("/"));
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);
				
				String connectorCity = nextLine.substring(0, nextLine.indexOf("/"));
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);
				
				String connectionCost = nextLine;
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);
				
				City first = null, second = null;
				for (City key: urbanArea.cities.keySet())
				{
					if (key.getName().equals(firstCity))
						first = key;
					if (key.getName().equals(connectorCity))
						second = key;
					if (first != null && second != null)
						break;
				}
				
				urbanArea.addConnection(first, second, Integer.parseInt(connectionCost));
				
				nextLine = reader.readLine();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void initialisePlayers()
	{
		ArrayList<String> colours = new ArrayList<String>();
		colours.addAll(Arrays.asList("red", "blue", "green", "yellow"));
		Collections.shuffle(colours);
		
		players = new ArrayList<Player>();
		for (int i = 0; i < playerCount; i++)
		{
			players.add(new Player(colours.remove(0)));
		}
		bidOrder = new ArrayList<Player>();
		bidOrderC = new ArrayList<String>();
		for(Player p : players)
		bidOrderC.add(p.colour);
	}
	
	public void initialiseTurnOrder()
	{
		Collections.shuffle(players);
	}
	
	@SuppressWarnings("unchecked")
	public void initialisePlantMarket()
	{
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/PowerPlantData.txt")));
			String line = reader.readLine();
			
			String x = "";
			int name = 0;
			Resource resource = null;
			int amount = 0;
			int power = 0;
			int index = 0;
			
			while(!(line.equals("end"))) 
			{
				for(int i = 0; i < line.length(); i++) 
				{
					String nl;
					if(Character.toString(line.charAt(1)).equals(" ")) 
					{
						index = 2;
						name = Integer.parseInt(line.substring(0, 1));
					}
					else if (Character.toString(line.charAt(2)).equals(" ")) 
					{
						index = 3;
						name = Integer.parseInt(line.substring(0, 2));
					}
					
					if(Character.toString(line.charAt(index)).equals("c")) 
					{
						resource = Resource.coal;
						index += 5;
						amount = Integer.parseInt(Character.toString(line.charAt(index)));
						index += 2;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if(Character.toString(line.charAt(index)).equals("o")) 
					{
						resource = Resource.oil;
						index += 4;
						amount = Integer.parseInt(Character.toString(line.charAt(index)));
						index += 2;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if(Character.toString(line.charAt(index)).equals("h")) 
					{
						resource = Resource.hybrid;
						index += 7;
						amount = Integer.parseInt(Character.toString(line.charAt(index)));
						index += 2;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if(Character.toString(line.charAt(index)).equals("t")) 
					{
						resource = Resource.trash;
						index += 6;
						amount = Integer.parseInt(Character.toString(line.charAt(index)));
						index += 2;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if(Character.toString(line.charAt(index)).equals("u")) 
					{
						resource = Resource.uranium;
						index += 10;
						amount = 1;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if(Character.toString(line.charAt(index)).equals("f")) 
					{
						resource = Resource.free;
						index += 7;
						amount = 0;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					
				}
//				System.out.println(name + ", " + resource + ", " + amount + ", " + power);
				plantMarket.allPlantsAL.add(new Powerplant(name, resource, amount, power));
				
				line = reader.readLine();
			}
			ArrayList<Powerplant> plants = new ArrayList<Powerplant>();
			for(int i = 0; i < plantMarket.allPlantsAL.size(); i++) 
			{
				if(plantMarket.allPlantsAL.get(i).getName() >= 3 && plantMarket.allPlantsAL.get(i).getName() <= 10 || plantMarket.allPlantsAL.get(i).getName() == 13) 
				{
					plants.add(plantMarket.allPlantsAL.get(i));
				}
			}
			
			Collections.sort(plants);
			for(Powerplant i : plants)
				plantMarket.plantsAvailable.add(i);
			
			plantMarket.allPlants.add(plantMarket.plantsAvailable.remove(plantMarket.plantsAvailable.size()-1));
			Collections.shuffle(plantMarket.allPlantsAL);
			plantMarket.allPlantsAL.add(new Powerplant("step3"));
			plantMarket.allPlants.addAll(plantMarket.allPlantsAL);
		}
		catch(IOException e) {}
	}
	
	public void initialiseResourceMarket()
	{
		
	}
	
	// -------------------------------------------------------
	
	public int nextTurnPhase()
	{
		turnPhase+= 1;
		if (turnPhase == 4)
			turnPhase = 0;
		return turnPhase;
	}
	
//	public int nextPlayer()
//	{
//		currentPlayer++;
//		if (currentPlayer > playerCount - 1)
//			currentPlayer = 0;
//		return currentPlayer;
//	}
	/* adds or removes a region inside the list of regions
	 * that will be playable in-game
	 */
	public void toggleRegion(String str)
	{
		Region r = Region.valueOf(str.toLowerCase());
		urbanArea.toggleRegion(r);
	}
	public void removeRegions()
	{
		ArrayList<Region> allRegions = new ArrayList<Region>(Arrays.asList(Region.values()));
		allRegions.removeAll(urbanArea.getActiveRegions());
		for (Region toRemove: allRegions)
		{
			urbanArea.removeRegion(toRemove);
		}
	}
	public ArrayList<Region> getActiveRegions()
	{
		return urbanArea.getActiveRegions();
	}
	
	public void turnOrder()
	{
		if (this.turnPhase == 0)
			Collections.sort(players);
		else if (this.turnPhase == 1 || this.turnPhase == 2)
			Collections.sort(players, Collections.reverseOrder());
	}
	
	@SuppressWarnings("unchecked")
	public void bid(Powerplant plantChosen)
	{	
		turnOrder();
		syncBidOrders();		
		Collections.sort(plantMarket.plantsAvailable);
		
//		try 
//		{
//			BufferedReader reader = new BufferedReader(
//					new InputStreamReader(System.in));
//			
//			//TODO? prompt which plant the user would like to bid on
//			String choice = reader.readLine();
//			
//			//the user picks the pp, we use the index of the pp
			chosenPlant = plantChosen;	
			
			if(bidOrder.get(currentPlayer).money < chosenPlant.getName()) //player doesnt have enough money to bid
			{
				chosenPlant = null;
				//return; not sure what to do here
			}
			else 
			{
				bid = chosenPlant.getName();
				bidWinner = bidSM(bidOrder, bidOrder.get(currentPlayer), chosenPlant);
				bidWinner.money -= bid;
				bidWinner.ownedPlants.add(chosenPlant);
			}
			syncBidOrders();
			nextPlayer();
//		}
//		catch(IOException e) {}
		
	}
	public void syncBidOrders() 
	{
		if(bidOrderC.size() != bidOrder.size()) 
		{
		for(int i = 0; i < bidOrder.size(); i++) 
		{
			if(!bidOrder.get(i).colour.equals(bidOrderC.get(i))) 
			{
				bidOrderC.remove(i);
				return;
			}
		}
		bidOrderC.remove(bidOrderC.size()-1);
		}
	}
	public void playerPassedBidPhase() 
	{
		//remove the player from bidOrder ArrayList
		bidOrder.remove(currentPlayer);
		syncBidOrders();
		
		nextPlayer();
	}
	
	public void bidPhaseEnded() 
	{
		bidOrder.clear();
		bidOrderC.clear();
		bidOrder.addAll(players);
		for(Player p : players)
			bidOrderC.add(p.colour);
	}
	public Player bidSM(ArrayList<Player> order, Player ib, Powerplant plant)// bid sub-method 
	{
		ArrayList<Player> theEverShrinkingListOfBidders = new ArrayList<Player>();
		theEverShrinkingListOfBidders.addAll(order);
		Player winner = null;
		Player temp = order.get(0);
		order.set(0, ib);
		order.set(order.indexOf(ib), temp);
		
		int unconfirmedBid = 0;
		boolean response = false; //true = increasing bid, false = passing
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(System.in));
		
		int i = 1;
		String ans;
		while(theEverShrinkingListOfBidders.size() != 1) 
		{
			System.out.println("Player #"+i+", would you like to increase the bid of $"
					+bid+"for powerplant "+plant.getName()+"?(type 'increase bid' or 'pass') You have $"+theEverShrinkingListOfBidders.get(i).getMoney());
			//set response
			ans = reader.readLine();
			if(ans.equals("increase bid"))
				response = true;
			
			if(!response) 
			{
				theEverShrinkingListOfBidders.remove(theEverShrinkingListOfBidders.get(i));
			}
			else
			{
				
				//set unconfirmedBid to be $1 higher than current bid
				unconfirmedBid = bid + 1;
				
				if(theEverShrinkingListOfBidders.get(i).getMoney() < unconfirmedBid) 
				{
//					System.out.println("Infsufficient funds, you are unable to bid on this plant");
					theEverShrinkingListOfBidders.remove(theEverShrinkingListOfBidders.get(i));
					i++;
					continue;
				}
//				else if(unconfirmedBid <= bid)
//				{
//					System.out.println("Your bid has to be higher than the previous player's bid. You've been skipped.");
//					i++;
//					continue;
//				}
				System.out.println("You bid $"+unconfirmedBid);
				bid = unconfirmedBid;
			}
			i++;
		}
		winner = theEverShrinkingListOfBidders.get(0);
		bidOrder.remove(winner);
		return winner;	
	}
	catch (IOException e) {}
		return winner;
	}
	
	public void updateStage()
	{

		HashMap<Player, HashMap<Resource, Integer>> allPlayersResources = new HashMap<Player, HashMap<Resource, Integer>>();
		
		HashMap<Player, Integer> allPlayersHouses = new HashMap<Player, Integer>();;
		
		for(int x = 0; x < players.size(); x++) {
			allPlayersResources.put(players.get(x), players.get(x).getResources());
			players.get(x).calcPowerableHouses();
			allPlayersHouses.put(players.get(x), players.get(x).ownedHouses);
			players.get(x).income();
			
		}
		resourceMarket.restock();
		plantMarket.restock();
		if(step == 1 || step == 2)
		{
			plantMarket.getPlantsAvailable().remove(plantMarket.getPlantsAvailable().size()-1);
		}
		else
			plantMarket.getPlantsAvailable().remove(0);
		//TODO make sure that it removes the first card from the arraylist, or the last, based on stage
		if(plantMarket.getPlantsAvailable().size() < 8)
		{
			plantMarket.restock();
		}
	}
	
	public String whoWon()
	{
		/* result stores and returns the result
		 * at the end. It either returns 'tie'
		 * or the index of the player that won.
		 * It also returns the data of each player
		 * including their powered houses,
		 * money, and cities owned at the end.
		 */
		String result = "";
		
		int winner;
		int[] powerableHouses = new int[playerCount];
		int[] moneys = new int[playerCount];
		int[] numCities = new int[playerCount];
		for(int i = 0; i < players.size(); i++) {
			powerableHouses[i] = players.get(i).getPoweredHouses();
			moneys[i] = players.get(i).getMoney();
			numCities[i] = players.get(i).getCities().size();
		}
		winner = getMax(powerableHouses);
		if(winner == -1) {
			winner = getMax(moneys);
			if(winner == -1) {
				winner = getMax(numCities);
				if(winner == -1) {
//					System.out.println("It's a tie!");
					result = "tie";
				}
				else
//					System.out.println("The winner is Player #" + players.get(getIndex(numCities, winner))+"!");
					result = "" + getIndex(numCities, winner);
			}
			else 
//				System.out.println("The winner is Player #" + players.get(getIndex(moneys, winner))+"!");
				result = "" + getIndex(moneys, winner);
		}
		else
//			System.out.println("The winner is Player #" + players.get(getIndex(powerableHouses, winner))+"!");
			result = "" + getIndex(powerableHouses, winner);
		
		result += "\n" + getResults(powerableHouses, moneys, numCities);
		hasEnded = true;
		
		return result;
	}
	
	/* returns the results formatted as such:
	 * player powerableHouses money citiesOwned
	 */
	public String getResults(int[] ph, int[]m, int[] nc)
	{
		String res = "";
		for(int i = 1; i <= playerCount; i++)
			res += i + " " + ph[i - 1] + " " + m[i - 1] + " " + nc[i - 1] + "\n";
		return res;
	}
	
	public void displayResults(int[] ph, int[]m, int[] nc) 
	{
		for(int i = 1; i <= playerCount; i++)
			System.out.println("Player #"+i+" - Powerable Houses:"+ph[i-1]+", Money: "+m[i-1]+", Number of Cities Owned:"+nc[i-1]);
	}
	
	public void buyResource()
	{
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(System.in));
		for(int x = 0; x < players.size();x++) 
		{
			System.out.println("Coal:" + resourceMarket.getPrice(Resource.coal));
			System.out.println("Oil:" + resourceMarket.getPrice(Resource.oil));
			System.out.println("Trash:" + resourceMarket.getPrice(Resource.trash));
			System.out.println("Uranium:" + resourceMarket.getPrice(Resource.uranium));
			System.out.println("What resource would you like to purchase? Please input either \"coal\", \"oil\", \"trash\", or \"uranium\"");
			String ans = reader.readLine();
			if(ans == "oil") {
				resourceMarket.purchase(players.get(x),Resource.oil); 
			}
			else if(ans == "coal") {
				resourceMarket.purchase(players.get(x),Resource.coal); 
			}
			else if(ans == "trash") {
				resourceMarket.purchase(players.get(x),Resource.trash); 
			}
			else if(ans == "uranium") {
				resourceMarket.purchase(players.get(x),Resource.uranium); 
			}
			else
				continue;
		}}
		catch(IOException e) {}
	}
	
	public void buyCity(City cit)
	{
		ArrayList<Player> order = new ArrayList<Player>();
		for(Player p : players)
			order.add(p);
		Collections.reverse(order);
		City pickedCity = cit;
		int cost = 0;
		//TODO the actual proccess of picking the city
		
		for(Player i : order) 
		{
			if(i.getCities().size() == 0) //if this is the player's first city
				if(i.money < 10)
					return; //no money
				else 
				{
					i.money -= 10;
					i.addCity(pickedCity);
					i.ownedHouses++;
					//money spent, city bought.
					return;
				}
			else //if player has at least 1 city
			{
				for(City c : i.getCities()) 
				{
					if(urbanArea.hasConnection(pickedCity, c) != -1) 
					{
						if(i.money < pickedCity.nextCost())
							return; //no money
						cost += urbanArea.hasConnection(pickedCity, c);
						cost += pickedCity.nextCost();
						i.addCity(pickedCity);
						i.ownedHouses++;
					}
				}
				//TODO calculate the shortest path from the closest of the player's cities to the city that the player picked
			}
			i.money -= cost;
		}
		
		
		//TODO finish the method
	}
	
	/* creates the cities
	 * and adds them to urbanArea
	 */
	
	
	public void displayPlants(ArrayList<Powerplant> plants) {
		for(Powerplant i : plants) 
		{
			if(i.getResourceType() != Resource.free)
			System.out.println("Cost:" +i.getName()+". This plant requires "+i.getAmountToPower()+" "+i.getResourceType()+" to produce "+ i.getPowerProduced()+"power.");
			else  System.out.println("Cost:" +i.getName()+". This plant produces "+ i.getPowerProduced()+" power without resources.");
		}
	}
	
	public void nextPlayer() 
	{
		if(currentPlayer != 3)
			currentPlayer++;
		else currentPlayer = 0;
	}
	
	public boolean allTrue(ArrayList<Boolean> bool) {
		for(Boolean i: bool)
			if(!i)
				return false;
		return true;
	}
	
	public int getMax(int[] x) {
		int max = x[0];
		boolean isTie = false;
		for(int i = 0; i < x.length; i++) {
			if(i != x.length-1)
				max = Math.max(max, x[i+1]);
		}
		if(!isTie)
		return max;
		else
			return -1;
	}
	public int getIndex(int[] x, int num) {
		int ind = -1;
		for(int i = 0; i < x.length; i++)
		{
			ind = i;
			if(x[i] == num)
				break;
		}
		return ind;
	}
	
	public ArrayList<Player> getPlayerList()
	{
		return players;
	}
}

