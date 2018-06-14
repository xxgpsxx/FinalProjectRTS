public class Spearman extends MeleeUnit
{
	private static final int health = 150;
	private static final int armor = 3;
	private static final double vision = 75.0;
	private static final double speed = 6.0;
	private static final int damage = 10;
	private static final double radius = 55.0;
	private static final double attackSpeed = 75.0;
	public Spearman(Map map, Location location, int owner)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed);
	}
	public Spearman(Map map, Location location)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed);
	}
	public Spearman(Location location)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed);
	}
}	