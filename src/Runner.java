
public class Runner
{
	public static void main(String[] args)
	{
		// UI game = new UI();
		ResourceMarket x = new ResourceMarket();
		
		System.out.println(x.currentStock);
		x.restock();
		System.out.println(x.currentStock);
	}
}
