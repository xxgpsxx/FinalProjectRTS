import java.util.ArrayList;
public class MineralNode extends Building
{
	private int amount = 5000;
	private ArrayList <Worker> workers = new ArrayList <Worker>();
	public MineralNode(Map map, Location location)
	{
		super(map,100000, 100000, location, 0.0, 20, 20);
	}
	public MineralNode(Map map, Location location, int amount)
	{
		super(map, 100000, 100000, location, 0.0, amount / 250, amount / 250);
		this.amount = amount;
	}
	public MineralNode(Location location, int amount)
	{
		super(100000, 100000, location, 0.0, amount / 250, amount / 250);
		this.amount = amount;
	}
	public MineralNode(Location location)
	{
		super(100000, 100000, location, 0.0, 20, 20);
	}
	public Location getLocation() { return super.getLocation(); }
	public void add(Worker worker) { workers.add(worker); }
	public ArrayList <Worker> getWorkers() { return workers; }
	public int mine(int amount)
	{
		this.amount -= amount;
		super.setSize(amount / 250, amount / 250);
		return this.amount;
	}
	public int getAmount() { return amount; }
	public int setAmount(int amount)
	{
		this.amount = amount;
		return this.amount;
	}
	public String toString()
	{
		return "Amount: " + amount;
	}
}