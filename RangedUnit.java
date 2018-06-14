import java.util.ArrayList;
public class RangedUnit extends Unit
{
	private double range = 0;
	private double projectileSpeed = 10.0;
	private int counter = 0;
	public RangedUnit(Map map, int health, int armor, Location location, int owner, double vision, double speed, int damage, double radius, double attackSpeed, double range, double projectileSpeed)
	{
		super(map, health, armor, location, owner, vision, speed, damage, radius, attackSpeed);
		this.range = range;
		this.projectileSpeed = projectileSpeed;
	}
	public RangedUnit(Map map, int health, int armor, Location location, double vision, double speed, int damage, double radius, double attackSpeed, double range, double projectileSpeed)
	{
		super(map, health, armor, location, vision, speed, damage, radius, attackSpeed);
		this.range = range;
		this.projectileSpeed = projectileSpeed;
	}
	public RangedUnit(int health, int armor, Location location, double vision, double speed, int damage, double radius, double attackSpeed, double range, double projectileSpeed)
	{
		super(health, armor, location, vision, speed, damage, radius, attackSpeed);
		this.range = range;
		this.projectileSpeed = projectileSpeed;
	}
	public double getProjectileSpeed() { return projectileSpeed; }
	public double getVision() { return super.getVision(); }
	public int setOwner(int x) { return super.setOwner(x); }
	public Map setMap(Map map) { return super.setMap(map); }
	public Map getMap() { return super.getMap(); }
	public double getRange() { return range; }
	public int getMaxHealth() { return super.getMaxHealth(); }
	public Boolean inRange(Thing other)
	{
		return range >= getLocation().distanceFrom(other.getCenter());
	}
	public void attackMove()
	{
		ArrayList <Thing> thingsInVision = super.getThingsInVision();

		Thing target = null;
		double minDistance = 100000.0;

		for(Thing thing : thingsInVision)
		{
			if(thing.getOwner() != getOwner())
			{
				if(getLocation().distanceFrom(thing.getCenter()) < minDistance)
				{
					target = thing;
					minDistance = getLocation().distanceFrom(thing.getCenter());
				}
			}
		}
		if(target != null)
		{
			if(inRange(target))
			{
				stop();
				attack();
				act();
				if(counter <= 0)
				{
					dealDamage(target);
					counter = (int)getAttackSpeed();
				}
				counter--;
			}

			else
			{
				moveTo(target.getLocation());
				super.move();
			}
		}
		else if(getTarget() != null)
		{
			moveTo(getTarget());
			super.move();
		}
	}

}

