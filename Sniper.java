public class Sniper extends RangedUnit
{
	private static final int health = 60;
	private static final int armor = 3;
	private static final double vision = 750.0;
	private static final double speed = 1.5;
	private static final int damage = 100;
	private static final double radius = 25.0;
	private static final double attackSpeed = 240.0;
	private static final double range = 750.0;
	private static final double projectileSpeed = 400.0;
	public Sniper(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
	public Sniper(Map map, Location location)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
	public Sniper(Location location)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
}