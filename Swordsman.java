public class Swordsman extends MeleeUnit
{
	private static final int health = 50;
	private static final int armor = 1;
	private static final double vision = 250.0;
	private static final double speed = 5.5;
	private static final int damage = 7;
	private static final double radius = 40.0;
	private static final double attackSpeed = 60.0;
	public Swordsman(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed);
	}
	public Swordsman(Map map, Location location)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed);
	}
	public Swordsman(Location location)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed);
	}
}