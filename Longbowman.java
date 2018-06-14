public class Longbowman extends RangedUnit
{
	private static final int health = 40;
	private static final int armor = 2;
	private static final double vision = 500.0;
	private static final double speed = 2.5;
	private static final int damage = 1;
	private static final double radius = 15.0;
	private static final double attackSpeed = 30.0;
	private static final double range = 500.0;
	private static final double projectileSpeed = 200.0;
	public Longbowman(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
	public Longbowman(Map map, Location location)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
	public Longbowman(Location location)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
}
