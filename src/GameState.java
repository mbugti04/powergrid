import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class GameState
{
	public static int step = 1;
	public static int playerCount = 4;
	public Scanner input;
	public int currentPlayer, turnPhase; // 0=buy power plants. 1=buy resources. 2=building. beaurocracy doesnt have a turn order.
	public PowerplantMarket plantMarket;
	public ResourceMarket resourceMarket;
	public UrbanArea urbanArea;
	public ArrayList<Player> players;
	public boolean hasEnded = false;
	public boolean initialSetup = true;
	public int theBid = 0;
	
	public GameState()
	{
		input = new Scanner(System.in);
		currentPlayer = 0;
		turnPhase = 0;
		plantMarket = new PowerplantMarket();
		resourceMarket = new ResourceMarket();
		urbanArea = new UrbanArea();
		players = new ArrayList<Player>();
		
		mainSetup();
	}
	
	private void mainSetup()
	{
		initialiseCities();
	}
	
	public void gameLoop()
	{
		while (!hasEnded)
		{
			if (initialSetup)
			{
				// select regions
				
				// determine initial player order
				
				// set up plant market, resource market
			}
			
			this.turnOrder();
			
			this.bid();
			
			this.buyResource();
			
			// building houses
			
			this.updateStage();
		}
	}
	
	public void turnOrder()
	{
		if (this.turnPhase == 0)
			Collections.sort(players);
		else if (this.turnPhase == 1 || this.turnPhase == 2)
			Collections.sort(players, Collections.reverseOrder());
	}
	public void bid() 
	{
		turnOrder();
		Powerplant chosenPlant = null;
		Player initialBidder;
		Player bidWinner;
		int bid = 0;
		boolean isBidding = false;
		ArrayList<Boolean> bidding = new ArrayList<Boolean>(); //true = bidding, false = passing
		ArrayList<Boolean> hasBid = new ArrayList<Boolean>(); //true = bidded or passed, false = didn't do anything yet
		ArrayList<Player> order = new ArrayList<Player>();
		ArrayList<Powerplant> availablePlants = plantMarket.getPlantsAvailable();
	
		for(int i = 0; i < playerCount; i++) 
		{
			hasBid.set(i, false);
			order.set(i, players.get(i));
		}
		
		while(!allTrue(hasBid)) 
		{
			for(int i = 0; i < order.size(); i++) 
			{
				displayPlants(availablePlants);
				System.out.println("Player #"+i+", would you like to bid or pass? You have $"+order.get(i).getMoney());
				//set the isBidding boolean
				if(isBidding) 
				{
				System.out.println("Which plant?");	
				//set the chosenPlant variable
				System.out.println("How much are you bidding?");
				//set the bid price
					if(bid > order.get(i).getMoney())
					{
						System.out.println("Insufficient funds, passing player");
						continue;
					}
				initialBidder = order.get(i);
				bidWinner = bidSM(order, initialBidder, bid, chosenPlant);
				System.out.println(bidWinner+" has won the bid and gained powerplant " + chosenPlant.getName());
				
				bidding.set(order.indexOf(bidWinner), true);
				hasBid.set(order.indexOf(bidWinner), true);
				availablePlants.remove(chosenPlant);
				order.remove(bidWinner);
				bidWinner.money -= theBid;

				//not done
				}
				else 
				{
					bidding.set(i, false);
					hasBid.set(i, true);
					
				}
			}
			
		}
	
	}
	public Player bidSM(ArrayList<Player> order, Player ib, int bid, Powerplant plant)// bid sum-method 
	{
		ArrayList<Player> theEverShrinkingListOfBidders = new ArrayList<Player>();
		Player winner = null;
		Player temp = order.get(0);
		order.set(0, ib);
		order.set(order.indexOf(ib), temp);
		
		int unconfirmedBid = 0;
		boolean response = false; //true = increasing bid, false = passing
		
		int i = 1;
		while(theEverShrinkingListOfBidders.size() != 1) 
		{
			System.out.println("Player #"+i+", would you like to increase the bid of $"
					+bid+"for powerplant "+plant.getName()+"? You have $"+theEverShrinkingListOfBidders.get(i).getMoney());
			//set response
			if(!response) 
			{
				theEverShrinkingListOfBidders.remove(theEverShrinkingListOfBidders.get(i));
			}
			else
			{
				System.out.println("How much would you like to bid");
				//set unconfirmedBid
				if(theEverShrinkingListOfBidders.get(i).getMoney() < unconfirmedBid) 
				{
					System.out.println("Infsufficient funds, you are unable to bid on this plant");
					theEverShrinkingListOfBidders.remove(theEverShrinkingListOfBidders.get(i));
					i++;
					continue;
				}
				else if(unconfirmedBid <= bid)
				{
					System.out.println("Your bid has to be higher than the previous player's bid. You've been skipped.");
					i++;
					continue;
				}
				System.out.println("You bid $"+unconfirmedBid);
				bid = unconfirmedBid;
			}
			i++;
		}
		theBid = bid;
		winner = theEverShrinkingListOfBidders.get(0);
		return winner;	
	}
	
	public void updateStage()
	{
		
		for(int x = 0; x < players.size(); x++) {
			players.get(x).income();
			//TODO removing resources from each player
		}
		resourceMarket.restock();
		plantMarket.restock();
		//TODO make sure that it removes the first card from the arraylist, or the last, based on stage
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
		int index;
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
		Scanner input = new Scanner(System.in);
		for(int x = 0; x < players.size();x++) 
		{
			System.out.println("Coal:" + resourceMarket.getPrice(Resource.coal));
			System.out.println("Oil:" + resourceMarket.getPrice(Resource.oil));
			System.out.println("Trash:" + resourceMarket.getPrice(Resource.trash));
			System.out.println("Uranium:" + resourceMarket.getPrice(Resource.uranium));
			System.out.println("What resource would you like to purchase? Please input either \"coal\", \"oil\", \"trash\", or \"uranium\"");
			String ans = input.next();
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
		}
	}
	
	public void buyCity()
	{
		//TODO finish the method
	}
	
	/* creates the cities
	 * and adds them to urbanArea
	 */
	public void initialiseCities()
	{
		try
		{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/text/cities.txt")));
			
			String nextLine = reader.readLine();
			
			while (!nextLine.equals("!!!connections"))
//			while (!nextLine.equals("toki wo tomare"))
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
				
				System.out.printf("name:%s, region:%s, xcoord:%s, ycoord:%s\n", name, region, xcoord, ycoord);
				
				urbanArea.addCity(c);
				
				nextLine = reader.readLine();
			}
		}
		catch (IOException e)
		{
			
		}
	}
	
	public static void main(String[] args)
	{
		GameState state = new GameState();
	}
	
	public void displayPlants(ArrayList<Powerplant> plants) {
		for(Powerplant i : plants) 
		{
			if(i.getResourceType() != Resource.free)
			System.out.println("Cost:" +i.getName()+". This plant requires "+i.getAmountToPower()+" "+i.getResourceType()+" to produce "+ i.getPowerProduced()+"power.");
			else  System.out.println("Cost:" +i.getName()+". This plant produces "+ i.getPowerProduced()+" power without resources.");
		}
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
}

