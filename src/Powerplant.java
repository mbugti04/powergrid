import java.util.Comparator;
public class Powerplant 
{
	private int name;
	private Resource resourceType;
	private int amountToPower;
	private int powerProduced;
	
	public Powerplant(int name, Resource resource, int amount, int power) 
	{
		this.name = name;
		this.resourceType = resource;
		this.amountToPower = amount;
		this.powerProduced = power;
	}
	
	public int storageCapacity()
	{
		return amountToPower * 2;
	}

	public int getName() {
		return name;
	}
	
	public Resource getResourceType() {
		return resourceType;
	}
	
	public int getAmountToPower() {
		return amountToPower;
	}
	
	public int getPowerProduced() {
		return powerProduced;
	}
	
	public static Comparator<Powerplant> powerNum = new Comparator<Powerplant>() {
		public int compare(Powerplant p1, Powerplant p2) {
			int pNum1 = p1.getName();
			int pNum2 = p2.getName();
			return pNum1 - pNum2;
		}
	};
}
