import java.util.*;
public class GameState
{
	public static int step = 1;
	public static int playerCount = 4;
	public Scanner input;
	public int currentPlayer, turnPhase;
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
		ArrayList<Player> reTurn = new ArrayList<Player>();
		
	}
}

