
public class Powerplant 
{
	private int name;
	private Resource resourceType;
	private int amountToPower;
	private int powerProduced;
	private int storageCapacity;
	
	public Powerplant(int name, Resource resource, int amount, int power, int storage) 
	{
		this.name = name;
		this.resourceType = resource;
		this.amountToPower = amount;
		this.powerProduced = power;
		this.storageCapacity = storage;
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
	public int getpowerProduced() {
		return powerProduced;
	}
	public int getStorageCapacity() {
		return storageCapacity;
	}
	
	
}
