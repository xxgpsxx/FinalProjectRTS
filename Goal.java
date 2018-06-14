public class Goal extends Building
{
	private final static int[] size = {125, 125};
	private final static int health = 2000;
	private final static int armor = 10;
	private final static double vision = 0.0;
	public Goal(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, size[0], size[1]);
	}
	public Goal(Map map, Location location)
	{
		super(map, health, armor, location, vision, size[0], size[1]);
	}
	public Goal(Location location)
	{
		super(health, armor, location, vision, size[0], size[1]);
	}
	public Goal(int x, int y)
	{
		super(health, armor, new Location(x, y), vision, size[0], size[1]);
	}
}