import javafx.event.*;
import javafx.scene.input.*;
public class Building extends Thing
{
	private int[] size = new int[2];
	private Location center;
	public Building(Map map, int health, int armor, Location location, int owner, double vision, int x, int y)
	{
		super(map, health, armor, location, owner, vision);
		size[0] = x;
		size[1] = y;
		adjustCenter();
	}
	public Building(Map map, int health, int armor, Location location, double vision, int x, int y)
	{
		super(map, health, armor, location, vision);
		size[0] = x;
		size[1] = y;
		adjustCenter();
	}
	public Building(int health, int armor, Location location, double vision, int x, int y)
	{
		super(health, armor, location, vision);
		size[0] = x;
		size[1] = y;
		adjustCenter();
	}
	public int[] setSize(int x, int y)
	{
		size[0] = x;
		size[1] = y;
		adjustCenter();
		return size;
	}
	public void handle(InputEvent input)
	{
	}
	public Location getCenter() { return center; }
	public Location adjustCenter()
	{
		int x = getLocation().getX() + (size[0]/2);
		int y = getLocation().getY() + (size[1]/2);

		center = new Location(x, y);
		return center;
	}
	public int getMaxHealth() { return super.getMaxHealth(); }
	public double getVision() { return super.getVision(); }
	public int setOwner(int x) { return super.setOwner(x); }
	public Map setMap(Map map) { return super.setMap(map); }
	public Map getMap() { return super.getMap(); }
	public int getWidth() { return size[0]; }
	public int getHeight() { return size[1]; }
}