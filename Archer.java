public class Archer extends RangedUnit
{
	private static final int health = 35;
	private static final int armor = 1;
	private static final double vision = 200.0;
	private static final double speed = 6.75;
	private static final int damage = 5;
	private static final double radius = 35.0;
	private static final double attackSpeed = 90.0;
	private static final double range = 200.0;
	private static final double projectileSpeed = 45.0;
	public Archer(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
	public Archer(Map map, Location location)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
	public Archer(Location location)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed, range, projectileSpeed);
	}
}