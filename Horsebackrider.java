public class Horsebackrider extends MeleeUnit
{
	private static final int health = 200;
	private static final int armor = 3;
	private static final double vision = 220.0;
	private static final double speed = 10.0;
	private static final int damage = 15;
	private static final double radius = 65.0;
	private static final double attackSpeed = 50.0;
	public Horsebackrider(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed);
	}
	public Horsebackrider(Map map, Location location)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed);
	}
	public Horsebackrider(Location location)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed);
	}
}