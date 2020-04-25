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
	
	public GameState()
	{
		input = new Scanner(System.in);
		currentPlayer = 0;
		turnPhase = 0;
		plantMarket = new PowerplantMarket();
		resourceMarket = new ResourceMarket();
		urbanArea = new UrbanArea();
		players = new ArrayList<Player>();
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
		Player actualBuyer;
		Scanner input = new Scanner(System.in);
		int plant;
		boolean bidOver = false;
		
		int[] stillBidding = new int[playerCount];
		for(int sb = 0; sb < stillBidding.length; sb++)
			stillBidding[sb] = sb;
		
		ArrayList<Player> order2 = new ArrayList<Player>();
		order2 = players;
		ArrayList<Boolean> hasBid = new ArrayList<Boolean>();
		for(int b = 0; b < players.size(); b++)
			hasBid.set(b, false);
		
		ArrayList<Powerplant> availablePlants;
		availablePlants = plantMarket.getPlantsAvailable();
		
		//idk how this will work graphically so I'll just write it as if it was text based
		System.out.println("It is time for bidding.");
		for(int i = 0; i < players.size(); i++) {
		System.out.println("Player #" + i + ", would you like to bid or pass? You currently have " + players.get(i).getMoney()+"$");
		String ans = input.next();
		if(ans.equals("pass")) {
			order2 = rotate(order2);
			continue;
		}
		if(ans.equals("bid")) 
		{
			System.out.println("Choose the index of the powerplant to bid on");
			int ansPP = input.nextInt();
			
			System.out.println("How much money would you like to bid?");
			int ansMon = input.nextInt();
			
			if(players.get(i).getMoney() < ansMon)
			{
				System.out.println("Insufficient funds, passing Player #"+i);
				continue;
			}
			
			String ansPP2;
			hasBid.set(0, true);
			for(int j = 1; j < order2.size(); j++) 
			{
				System.out.println("Player #" + players.get(i)+
						", would you like to increase the bid or pass on Powerplant #"+availablePlants.get(ansPP).getName()+"?");
				ansPP2 = input.next();
				if(ansPP2.equals("pass"))
					continue;
				if(ansPP2.equals("bid"))
					hasBid.set(j, true);
			}
		}	
		}
		//TODO carry out the buying procedure
		
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
			
			while (nextLine != null)
			{
				
			}
		}
		catch (IOException e)
		{
			
		}
	}
	
	public ArrayList<Player> rotate(ArrayList<Player> list)
	{
		ArrayList<Player> newList = new ArrayList<Player>();
		newList.set(0, list.get(list.size()-1));
		for(int i = 1; i < list.size()-1; i++)
		{
			newList.set(i, list.get(i));
		}
		return newList;
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

