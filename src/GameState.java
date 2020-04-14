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
	public boolean endGame;
	
	public GameState()
	{
		input = new Scanner(System.in);
		currentPlayer = 0;
		turnPhase = 0;
		plantMarket = new PowerplantMarket();
		resourceMarket = new ResourceMarket();
		urbanArea = new UrbanArea();
		players = new ArrayList<Player>();
		endGame = false;
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
		//TODO finish the method
	}
	
	public void updateStage()
	{
		//TODO finish the method
	}
	
	public void whoWon()
	{
		//TODO finish the method
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
}

