public class Giant extends MeleeUnit
{
	private static final int health = 450;
	private static final int armor = 5;
	private static final double vision = 150.0;
	private static final double speed = 0.75;
	private static final int damage = 33;
	private static final double radius = 75.0;
	private static final double attackSpeed = 150.0;
	public Giant(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed);
	}
	public Giant(Map map, Location location)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed);
	}
	public Giant(Location location)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed);
	}
}