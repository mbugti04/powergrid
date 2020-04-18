import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class GameState
{
	public static int step = 1;
	public static int playerCount = 4;
	public Scanner input;
	public int currentPlayer, turnPhase; // 0=buy power plants. 1=buy resources. 2=building. bueaocracy doesnt have a turn order.
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
			
			// determine player order
			
			// buying powerplants
			
			// building houses
			
			// powering houses
			
			// getting money phase
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
		Scanner input = new Scanner(System.in);
		int plant;
		
		ArrayList<Boolean> hasBid = new ArrayList<>();
		for(int b = 0; b < players.size(); b++)
			hasBid.set(b, false);
		ArrayList<Powerplant> availablePlants;
		availablePlants = plantMarket.getPlantsAvailable();
		
		//idk how this will work graphically so I'll just write it as if it was text based
		System.out.println("It is time for bidding.");
		for(int i = 0; i < players.size(); i++) {
		System.out.println("Player #" + players.get(i)+ ", would you like to bid or pass?");
		String ans = input.next();
		if(ans.equals("bid")) {
			System.out.println("Choose the index of the powerplant to bid on");
			int ansPP = input.nextInt();
			for(int j = 1; j < players.size(); j++)
				System.out.println("Player #" + players.get(i)+", would you like to increase the bid or pass?");
		}
		}
	}
	
	public void updateStage()
	{
		//TODO finish the method
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
	
	/* returs the results formatted as such:
	 * player powerableHouses money citiesOwned
	 */
	public String getResults(int[] ph, int[]m, int[] nc)
	{
		String res = "";
		for(int i = 1; i <= playerCount; i++)
			res += i + " " + ph[i - 1] + " " + m[i - 1] + " " + nc[i - 1] + "\n";
		return res;
	}
	
	public void displayResults(int[] ph, int[]m, int[] nc) {
		for(int i = 1; i <= playerCount; i++)
			System.out.println("Player #"+i+" - Powerable Houses:"+ph[i-1]+", Money: "+m[i-1]+", Number of Cities Owned:"+nc[i-1]);
	}
	
	public void buyResource()
	{
		//TODO finish the method
	}
	
	public void buyCity()
	{
		//TODO finish the method
	}
	
	public void initialiseCities()
	{
		//TODO finish the method
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

