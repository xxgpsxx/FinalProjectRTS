public class Crossbowman extends RangedUnit
{
	private static final int health = 50;
	private static final int armor = 2;
	private static final double vision = 400.0;
	private static final double speed = 3.5;
	private static final int damage = 15;
	private static final double radius = 45.0;
	private static final double attackSpeed = 120.0;
	private static final double range = 400.0;
	private static final double projectileSpeed = 60.0;
	public Crossbowman(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
	public Crossbowman(Map map, Location location)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
	public Crossbowman(Location location)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
}