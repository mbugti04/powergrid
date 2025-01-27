import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class GameState {
	public static int step = 1;
	public static int playerCount = 4;

	public int bid = 0;
	public int currentPlayer = 0;
	public int turnPhase = 0; // 0=buy power plants. 1=buy resources. 2=building. 3=powering. 4=bureaucracy.

	public PowerplantMarket plantMarket = new PowerplantMarket();
	public ResourceMarket resourceMarket = new ResourceMarket();
	public UrbanArea urbanArea = new UrbanArea();
	public ArrayList<Player> players;

	public boolean hasEnded = false; // TESTING PURPOSES
										// **************************************************************************************
	public boolean initialSetup = true;
	public boolean firstTimePicking = true;

	// public Powerplant chosenPlant = null;

	public GameState() {
		mainSetup();
	}

	// -------------------- SETUP METHODS --------------------
	private void mainSetup() {
		initialiseCities();
		initialisePlayers();
		initialiseTurnOrder();
		initialisePlantMarket();
	}

	public void initialiseCities() {
		// comment
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/cities.txt")));

			String nextLine = reader.readLine();

			while (!nextLine.equals("----------connections----------")) {
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
			while (nextLine != null) {
				String firstCity = nextLine.substring(0, nextLine.indexOf("/"));
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);

				String connectorCity = nextLine.substring(0, nextLine.indexOf("/"));
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);

				String connectionCost = nextLine;
				nextLine = nextLine.substring(nextLine.indexOf("/") + 1);

				City first = null, second = null;
				for (City key : urbanArea.cities.keySet()) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initialisePlayers() {
		ArrayList<String> colours = new ArrayList<String>();
		colours.addAll(Arrays.asList("red", "blue", "green", "yellow"));
		Collections.shuffle(colours);

		players = new ArrayList<Player>();
		for (int i = 0; i < playerCount; i++) {
			players.add(new Player(colours.remove(0)));
		}
	}

	public void initialiseTurnOrder() {
		Collections.shuffle(players);
	}

	@SuppressWarnings("unchecked")
	public void initialisePlantMarket() {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/PowerPlantData.txt")));
			String line = reader.readLine();

			int name = 0;
			Resource resource = null;
			int amount = 0;
			int power = 0;
			int index = 0;

			while (!(line.equals("end"))) {
				for (int i = 0; i < line.length(); i++) {
					if (Character.toString(line.charAt(1)).equals(" ")) {
						index = 2;
						name = Integer.parseInt(line.substring(0, 1));
					} else if (Character.toString(line.charAt(2)).equals(" ")) {
						index = 3;
						name = Integer.parseInt(line.substring(0, 2));
					}

					if (Character.toString(line.charAt(index)).equals("c")) {
						resource = Resource.coal;
						index += 5;
						amount = Integer.parseInt(Character.toString(line.charAt(index)));
						index += 2;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if (Character.toString(line.charAt(index)).equals("o")) {
						resource = Resource.oil;
						index += 4;
						amount = Integer.parseInt(Character.toString(line.charAt(index)));
						index += 2;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if (Character.toString(line.charAt(index)).equals("h")) {
						resource = Resource.hybrid;
						index += 7;
						amount = Integer.parseInt(Character.toString(line.charAt(index)));
						index += 2;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if (Character.toString(line.charAt(index)).equals("t")) {
						resource = Resource.trash;
						index += 6;
						amount = Integer.parseInt(Character.toString(line.charAt(index)));
						index += 2;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if (Character.toString(line.charAt(index)).equals("u")) {
						resource = Resource.uranium;
						index += 10;
						amount = 1;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}
					if (Character.toString(line.charAt(index)).equals("f")) {
						resource = Resource.free;
						index += 7;
						amount = 0;
						power = Integer.parseInt(Character.toString(line.charAt(index)));
					}

				}
				// System.out.println(name + ", " + resource + ", " + amount + ", " + power);
				plantMarket.allPlantsAL.add(new Powerplant(name, resource, amount, power));

				line = reader.readLine();
			}
			ArrayList<Powerplant> plants = new ArrayList<Powerplant>();
			for (int i = 0; i < plantMarket.allPlantsAL.size(); i++) {
				if (plantMarket.allPlantsAL.get(i).getName() >= 3 && plantMarket.allPlantsAL.get(i).getName() <= 10
						|| plantMarket.allPlantsAL.get(i).getName() == 13) {
					plants.add(plantMarket.allPlantsAL.get(i));
				}
			}

			Collections.sort(plants);
			for (Powerplant i : plants)
				plantMarket.plantsAvailable.add(i);

			plantMarket.allPlants.add(plantMarket.plantsAvailable.remove(plantMarket.plantsAvailable.size() - 1));
			Collections.shuffle(plantMarket.allPlantsAL);
			plantMarket.allPlantsAL.add(new Powerplant("step3"));
			plantMarket.allPlants.addAll(plantMarket.allPlantsAL);

		} catch (IOException e) {
		}
	}
	// -------------------------------------------------------

	public int nextTurnPhase() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).ownedCities.size() >= 17 || players.get(i).ownedHouses >= 17)
				hasEnded = true;
		}

		turnOrder(); // TODO is this needed
		turnPhase += 1;
		if (turnPhase == 4) {

			if (plantMarket.plantsAvailable.contains(plantMarket.allPlantsAL.get(plantMarket.allPlantsAL.size() - 1))) {
				step = 3;

			}
			resourceMarket.restock();

			for (Player p : players) {
				if (p.ownedCities.size() >= 7) {
					step = 2;
				}
			}

			if (step == 1 || step == 2) {
				plantMarket.getPlantsAvailable().remove(0);
			} else
				plantMarket.getPlantsAvailable().remove(plantMarket.getPlantsAvailable().size() - 1);
			if (plantMarket.getPlantsAvailable().size() < 8) {
				plantMarket.restock();
			}

			turnPhase = 0;
		}
		return turnPhase;
	}

	public int nextPlayer() {
		currentPlayer++;
		if (currentPlayer > playerCount - 1)
			currentPlayer = 0;
		return currentPlayer;
	}

	/*
	 * adds or removes a region inside the list of regions
	 * that will be playable in-game
	 */
	public void toggleRegion(String str) {
		Region r = Region.valueOf(str.toLowerCase());
		urbanArea.toggleRegion(r);
	}

	public void removeRegions() {
		ArrayList<Region> allRegions = new ArrayList<Region>(Arrays.asList(Region.values()));
		allRegions.removeAll(urbanArea.getActiveRegions());
		for (Region toRemove : allRegions) {
			urbanArea.removeRegion(toRemove);
		}
	}

	public ArrayList<Region> getActiveRegions() {
		return urbanArea.getActiveRegions();
	}

	public void turnOrder() {
		if (this.turnPhase == 0)
			Collections.sort(players);
		else if (this.turnPhase == 1 || this.turnPhase == 2)
			Collections.sort(players, Collections.reverseOrder());
	}

	// ------------------------------------------------ BID METHODS
	// -------------------------------------------------
	public Powerplant chosenPlant = null;
	public int currentbid = 0;
	public Player currentBidPlayer = null;
	public ArrayList<Player> nonBidders = new ArrayList<Player>();
	public ArrayList<Player> permanentNonBidders = new ArrayList<Player>();
	public boolean biddingend;
	public boolean replacing = false;
	public Powerplant toBeReplaced;

	public int i = 0;

	public void newBidPhase() {
		i++;
		if (i >= 2)
			firstTimePicking = false;
		chosenPlant = null;
		currentbid = 0;
		currentBidPlayer = null;
		nonBidders = new ArrayList<Player>();
		permanentNonBidders = new ArrayList<Player>();
	}

	public void turnEnd() {
		nextTurnPhase();
		newBidPhase();
		nextPlayer();
		// turnOrder();
		return;
	}

	public void refreshBidPhase() {
		if (permanentNonBidders.size() == playerCount) {
			turnEnd();
			return;
		}
		chosenPlant = null;
		currentbid = 0;
		currentBidPlayer = null;
		currentPlayer = 0;
		nonBidders = new ArrayList<Player>();
		if (!replacing) {
			while (nonBidders.contains(players.get(currentPlayer))
					|| permanentNonBidders.contains(players.get(currentPlayer))) {
				currentPlayer++;
			}
		}
	}

	public void choosePlant(Powerplant chosenPlant) {
		this.chosenPlant = chosenPlant;
	}

	@SuppressWarnings("unchecked")
	public void bid() {
		Collections.sort(plantMarket.plantsAvailable);

		if (chosenPlant != null) {
			if (currentBidPlayer == null) {
				if (players.get(currentPlayer).money > chosenPlant.getName()) {
					currentbid = chosenPlant.getName();
					currentBidPlayer = players.get(currentPlayer);
					nextBidder();
				}
			} else {
				if (players.get(currentPlayer).money > currentbid) {
					currentBidPlayer = players.get(currentPlayer);
					currentbid = currentbid + 1;
					nextBidder();
				}
			}
		}
	}

	public void playerPassedBidPhase() {
		if (firstTimePicking == false && currentBidPlayer == null) {
			permanentNonBidders.add(players.get(currentPlayer));
			nextBidder();
		} else if (firstTimePicking == false && currentBidPlayer != null
				|| firstTimePicking == true && currentBidPlayer != null) {
			nonBidders.add(players.get(currentPlayer));
			nextBidder();
		}
	}

	public void nextBidder() {
		if (permanentNonBidders.size() == playerCount) {
			turnEnd();
			return;
		}
		do {
			nextPlayer();
		} while (nonBidders.contains(players.get(currentPlayer))
				|| permanentNonBidders.contains(players.get(currentPlayer)));

		if ((nonBidders.size() + permanentNonBidders.size() == playerCount - 1) && chosenPlant != null
				&& currentBidPlayer != null) {
			bidWinner();
		}

		System.out.println(turnPhase);
		System.out.println(nonBidders);
		System.out.println(permanentNonBidders);

	}

	//
	public boolean replacePowerplant(Powerplant toBeReplaced, Powerplant replacer) {
		boolean completed = false;
		if (getCurrentPlayer().ownedPlants.remove(toBeReplaced))
			completed = true;
		getCurrentPlayer().ownedPlants.add(replacer);
		return completed;
	}

	public void bidWinner() {
		if (getCurrentPlayer().ownedPlants.size() == 3) {
			replacing = true;
			getCurrentPlayer().addMoney(-currentbid);
			// getCurrentPlayer().replacePowerplant(toBeReplaced, chosenPlant);
			plantMarket.removePlant(chosenPlant);
			permanentNonBidders.add(getCurrentPlayer());
			// refreshBidPhase();
		} else {
			replacing = false;
			getCurrentPlayer().addMoney(-currentbid);
			getCurrentPlayer().addPowerPlant(chosenPlant);
			plantMarket.removePlant(chosenPlant);
			permanentNonBidders.add(getCurrentPlayer());
			refreshBidPhase();
		}
	}

	public Player getCurrentPlayer() {
		return players.get(currentPlayer);
	}

	// -------------------------------------------------------------------

	public HashMap<Powerplant, Integer> togglePlants = new HashMap<Powerplant, Integer>();
	public int powerableHouses = 0;
	public Powerplant poweringPlant = null;

	public void togglePlants(Powerplant pp, int citiesToPower) {
		if (citiesToPower > 1)
			return;
		// debugging
		System.out.println("amt to power: " + pp.getAmountToPower());
		System.out.println("citiestopower: " + citiesToPower);
		System.out.println("ownedCities: " + getCurrentPlayer().ownedCities.size());

		if (pp.getResourceType().equals(Resource.free)) {
			if (pp.getPowerProduced() * citiesToPower >= citiesToPower && citiesToPower >= 0
					&& getCurrentPlayer().ownedCities.size() >= citiesToPower) {
				poweringPlant = pp;
				powerableHouses = 0;

				// either increments or decrements depending on what the cities to power is
				togglePlants.put(pp, citiesToPower);

				for (Powerplant p : togglePlants.keySet()) {
					powerableHouses += togglePlants.get(p);
				}
			}
		}

		else if (pp.getResourceType().equals(Resource.hybrid) &&
				pp.getPowerProduced() * citiesToPower >= citiesToPower && citiesToPower >= 0
				&& getCurrentPlayer().ownedCities.size() >= citiesToPower) {
			int totalAmountResources = getCurrentPlayer().getResources().get(Resource.coal)
					+ getCurrentPlayer().getResources().get(Resource.oil);

			if (totalAmountResources - (pp.getAmountToPower() * citiesToPower) >= 0) {
				poweringPlant = pp;
				powerableHouses = 0;

				// either increments or decrements depending on what the cities to power is
				togglePlants.put(pp, citiesToPower);

				for (Powerplant p : togglePlants.keySet()) {
					powerableHouses += togglePlants.get(p);
				}
			}
		}

		else if (pp.getPowerProduced() * citiesToPower >= citiesToPower && citiesToPower >= 0
				&& getCurrentPlayer().ownedCities.size() >= citiesToPower) {
			// debugging
			System.out.println(
					"amt " + pp.getResourceType() + ": " + getCurrentPlayer().getResources().get(pp.getResourceType()));

			// if the player has enough resources to make the powerplant produce energy
			if (getCurrentPlayer().getResources().get(pp.getResourceType())
					- (pp.getAmountToPower() * citiesToPower) >= 0) {
				poweringPlant = pp;
				powerableHouses = 0;

				// either increments or decrements depending on what the cities to power is
				togglePlants.put(pp, citiesToPower);

				for (Powerplant p : togglePlants.keySet()) {
					powerableHouses += togglePlants.get(p);
				}
				//
				// //if togglePlants has already has pp toggled
				// if(togglePlants.containsKey(pp))
				// {
				// togglePlants.replace(pp, citiesToPower);
				// }
				// //if togglePlants doesnt already have pp toggled
				// else
				// {
				// togglePlants.put(pp, citiesToPower);
				// }
			}
		} else
			return;
	}

	public String powerCities() {
		for (Powerplant p : togglePlants.keySet()) {
			Resource r = p.getResourceType();
			if (r.equals(Resource.free)) {
				continue;
			} else if (r.equals(Resource.hybrid)) {
				ArrayList<Resource> resources = new ArrayList<Resource>();
				for (int i = 0; i < getCurrentPlayer().getResources().get(Resource.oil); i++)
					resources.add(Resource.oil);
				for (int i = 0; i < getCurrentPlayer().getResources().get(Resource.coal); i++)
					resources.add(Resource.coal);

				int amountToRemove = p.getAmountToPower() * togglePlants.get(p);

				for (Resource rem : resources) {
					getCurrentPlayer().getResources().put(rem, getCurrentPlayer().getResources().get(rem) - 1);
					amountToRemove--;
					if (amountToRemove <= 0)
						continue;
				}
			} else {
				int currentAmountOfResources = getCurrentPlayer().getResources().get(r);
				int amountToRemove = p.getAmountToPower() * togglePlants.get(p);

				getCurrentPlayer().getResources().put(r, currentAmountOfResources - amountToRemove);
			}
		}

		getCurrentPlayer().poweredHouses += powerableHouses;
		String income = "" + getCurrentPlayer().income();

		resetCityPowering();

		return "Player " + getCurrentPlayer().colour + " income: " + income;

		// for(int i = 0; i < togglePlants.size(); i++)
		// {
		// getCurrentPlayer().getResources().replace
		// (poweringPlant.getResourceType(),
		// getCurrentPlayer().getResources().get(
		// poweringPlant.getResourceType()) - poweringPlant.getAmountToPower() );
		// }
		// getCurrentPlayer().poweredHouses += powerableHouses;
		// getCurrentPlayer().income();
		// togglePlants.clear();
	}

	public void resetCityPowering() {
		togglePlants = new HashMap<Powerplant, Integer>();
		powerableHouses = 0;
		poweringPlant = null;
		/*
		 * basically resets the arraylist of numCities
		 * AT THE END OF BUREAUCRACY aka when the entire turn ends and we're back to
		 * bidding
		 */
	}

	// public void updateStage()
	// {
	// plantMarket.restock();
	//
	// resourceMarket.restock();
	//
	// if(step == 1 || step == 2)
	// {
	// plantMarket.getPlantsAvailable().remove(plantMarket.getPlantsAvailable().size()-1);
	// }
	// else
	// plantMarket.getPlantsAvailable().remove(0);
	// //TODO make sure that it removes the first card from the arraylist, or the
	// last, based on stage
	// if(plantMarket.getPlantsAvailable().size() < 8)
	// {
	// plantMarket.restock();
	// }
	// }

	public Player whoWon() {
		/*
		 * result stores and returns the result
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
		for (int i = 0; i < players.size(); i++) {
			powerableHouses[i] = players.get(i).getPoweredHouses();
			moneys[i] = players.get(i).getMoney();
			numCities[i] = players.get(i).getCities().size();
		}

		winner = getMax(powerableHouses);
		if (winner == -1) {
			winner = getMax(moneys);
			if (winner == -1) {
				winner = getMax(numCities);
				if (winner == -1) {
					// System.out.println("It's a tie!");
					result = "tie";
				} else {
					// System.out.println("The winner is Player #" + players.get(getIndex(numCities,
					// winner))+"!");
					result = "" + getIndex(numCities, winner);
				}
			} else
				// System.out.println("The winner is Player #" + players.get(getIndex(moneys,
				// winner))+"!");
				result = "" + getIndex(moneys, winner);
		} else
			// System.out.println("The winner is Player #" +
			// players.get(getIndex(powerableHouses, winner))+"!");
			result = "" + getIndex(powerableHouses, winner);

		result += "\n" + getResults(powerableHouses, moneys, numCities);
		hasEnded = true;

		return players.get(winner);
	}

	/*
	 * returns the results formatted as such:
	 * player powerableHouses money citiesOwned
	 */
	public String getResults(int[] ph, int[] m, int[] nc) {
		String res = "";
		for (int i = 1; i <= playerCount; i++)
			res += i + " " + ph[i - 1] + " " + m[i - 1] + " " + nc[i - 1] + "\n";
		return res;
	}

	public void displayResults(int[] ph, int[] m, int[] nc) {
		for (int i = 1; i <= playerCount; i++)
			System.out.println("Player #" + i + " - Powerable Houses:" + ph[i - 1] + ", Money: " + m[i - 1]
					+ ", Number of Cities Owned:" + nc[i - 1]);
	}

	public void buyResource() {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(System.in));
			for (int x = 0; x < players.size(); x++) {
				System.out.println("Coal:" + resourceMarket.getPrice(Resource.coal));
				System.out.println("Oil:" + resourceMarket.getPrice(Resource.oil));
				System.out.println("Trash:" + resourceMarket.getPrice(Resource.trash));
				System.out.println("Uranium:" + resourceMarket.getPrice(Resource.uranium));
				System.out.println(
						"What resource would you like to purchase? Please input either \"coal\", \"oil\", \"trash\", or \"uranium\"");
				String ans = reader.readLine();
				if (ans == "oil") {
					resourceMarket.purchase(players.get(x), Resource.oil);
				} else if (ans == "coal") {
					resourceMarket.purchase(players.get(x), Resource.coal);
				} else if (ans == "trash") {
					resourceMarket.purchase(players.get(x), Resource.trash);
				} else if (ans == "uranium") {
					resourceMarket.purchase(players.get(x), Resource.uranium);
				} else
					continue;
			}
		} catch (IOException e) {
		}
	}

	City selectedCity = null;

	public void selectCity(City c) {
		selectedCity = c;
	}

	public boolean buyCity(City cit) {
		if (cit.nextCost() == -1)
			return false;
		boolean success = false, canAdd = false;
		Player cur = getCurrentPlayer();
		int cost = 0;

		if (cur.getCities().size() == 0) // if this is the player's first city
		{
			// has money
			if (cur.money >= 10) {
				cur.money -= 10;
				cur.addCity(selectedCity);
				cur.ownedHouses++;
				selectedCity.incrementHouse();
				success = true;
				// money spent, city bought.
			}
		} else // if player has at least 1 city
		{
			for (City c : cur.getCities()) {
				if (urbanArea.hasConnection(selectedCity, c) != -1) {
					// has money
					if (cur.money >= selectedCity.nextCost() + urbanArea.hasConnection(selectedCity, c)) {
						canAdd = true;
						cost += urbanArea.hasConnection(selectedCity, c);
						cost += selectedCity.nextCost();
						cur.ownedHouses++;

						selectedCity.incrementHouse();

						success = true;
					}
				}
			}
			if (canAdd) {
				cur.addCity(selectedCity);
			}

			// TODO calculate the shortest path from the closest of the player's cities to
			// the city that the player picked
		}
		cur.money -= cost;
		selectedCity = null;
		return success;

		// ArrayList<Player> order = new ArrayList<Player>();
		// for(Player p : players)
		// order.add(p);
		// Collections.reverse(order);
		// City pickedCity = cit;
		// int cost = 0;
		// //TODO the actual proccess of picking the city
		//
		// for(Player i : order)
		// {
		// if(i.getCities().size() == 0) //if this is the player's first city
		// if(i.money < 10)
		// return; //no money
		// else
		// {
		// i.money -= 10;
		// i.addCity(pickedCity);
		// i.ownedHouses++;
		// //money spent, city bought.
		// return;
		// }
		// else //if player has at least 1 city
		// {
		// for(City c : i.getCities())
		// {
		// if(urbanArea.hasConnection(pickedCity, c) != -1)
		// {
		// if(i.money < pickedCity.nextCost())
		// return; //no money
		// cost += urbanArea.hasConnection(pickedCity, c);
		// cost += pickedCity.nextCost();
		// i.addCity(pickedCity);
		// i.ownedHouses++;
		// }
		// }
		// //TODO calculate the shortest path from the closest of the player's cities to
		// the city that the player picked
		// }
		// i.money -= cost;
		// }
		//
		//
		// //TODO finish the method
	}

	/*
	 * creates the cities
	 * and adds them to urbanArea
	 */

	public void displayPlants(ArrayList<Powerplant> plants) {
		for (Powerplant i : plants) {
			if (i.getResourceType() != Resource.free)
				System.out.println("Cost:" + i.getName() + ". This plant requires " + i.getAmountToPower() + " "
						+ i.getResourceType() + " to produce " + i.getPowerProduced() + "power.");
			else
				System.out.println("Cost:" + i.getName() + ". This plant produces " + i.getPowerProduced()
						+ " power without resources.");
		}
	}

	public boolean allTrue(ArrayList<Boolean> bool) {
		for (Boolean i : bool)
			if (!i)
				return false;
		return true;
	}

	public int getMax(int[] x) {
		int max = x[0];
		boolean isTie = false;
		for (int i = 0; i < x.length; i++) {
			if (i != x.length - 1)
				max = Math.max(max, x[i + 1]);
		}
		if (!isTie)
			return max;
		else
			return -1;
	}

	public int getIndex(int[] x, int num) {
		int ind = -1;
		for (int i = 0; i < x.length; i++) {
			ind = i;
			if (x[i] == num)
				break;
		}
		return ind;
	}

	public ArrayList<Player> getPlayerList() {
		return players;
	}
}
