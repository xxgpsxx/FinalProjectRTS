import java.util.ArrayList;
import java.awt.geom.*;
import javafx.event.*;
import javafx.scene.input.*;
public class Unit extends Thing
{
	private double speed = 0;
	private int damage = 0;
	private double radius = 0.0;
	private boolean attacking = false;
	private Location target;
	private double direction = 90.0;
	private double attackSpeed = 0.0;
	private boolean moving = false;
	private double[] vector;
	private int steps;
	private int counter = 0;
	private Thing victim;
	private Thing leader;
	private Location center;
	private boolean patrolling = false;
	public Unit(Map map, int health, int armor, Location location, int owner, double vision, double speed, int damage, double radius, double attackSpeed)
	{
		super(map, health, armor, location, owner, vision);
		this.speed = speed;
		this.damage = damage;
		this.radius = radius;
		this.attackSpeed = attackSpeed;
		adjustCenter();
	}
	public Unit(Map map, int health, int armor, Location location, double vision, double speed, int damage, double radius, double attackSpeed)
	{
		super(map, health, armor, location, vision);
		this.speed = speed;
		this.damage = damage;
		this.radius = radius;
		this.attackSpeed = attackSpeed;
		adjustCenter();
	}
	public Unit(int health, int armor, Location location, double vision, double speed, int damage, double radius, double attackSpeed)
	{
		super(health, armor, location, vision);
		this.speed = speed;
		this.damage = damage;
		this.radius = radius;
		this.attackSpeed = attackSpeed;
		adjustCenter();
	}
	public Location adjustCenter()
	{
		int x = (int)(getLocation().getX() + radius);
		int y = (int)(getLocation().getY() + radius);

		center = new Location(x, y);

		return center;
	}
	public boolean isPatrolling() { return patrolling; }
	public void patrol() { patrolling = true; }
	public int setOwner(int x) { return super.setOwner(x); }
	public Map setMap(Map map) { return super.setMap(map); }
	public double getAttackSpeed() { return attackSpeed; }
	public int moveTo(int x, int y) { return moveTo(new Location(x, y)); }
	public int moveTo(Location target)
	{
		boolean prev = attacking;
		stop();
		attacking = prev;

		//if(!target.isValid(this.getMap()))
			//Util.throwError("Target location ( " + target + " ) does not exist on map ( " + this.getMap() + " )");

		moving = true;

		double deltaX = this.getLocation().getDifferenceX(target);
		double deltaY = this.getLocation().getDifferenceY(target);

		direction = this.getLocation().getAngleTo(target);

		double distance = this.getLocation().distanceFrom(target);
		steps = (int)(distance/speed);

		this.target = target;
		vector = new double[2];

		vector[0] = -deltaX/steps;
		vector[1] = -deltaY/steps;

		return steps;
	}
	public int moveTo(Building building)
	{
		leader = building;
		return moveTo(building.getCenter());
	}
	public int moveTo(Unit unit)
	{
		leader = unit;
		return moveTo(unit.getCenter());
	}
	public ArrayList <Thing> getThingsInVision()
	{
		Map map = getMap();

		ArrayList <Player> players = map.getPlayers();
		ArrayList <Thing> output = new ArrayList <Thing>();

		//players.remove(getOwner());

		for(Player player : players)
		{
			for(Unit unit : player.getArmyUnits())
				if(getLocation().distanceFrom(unit.getCenter()) <= getVision())
					output.add(unit);

			for(Building building : player.getBuildings())
				if(getLocation().distanceFrom(building.getCenter()) <= getVision())
					output.add(building);
		}

		return output;
	}
	public ArrayList <Thing> getHostileInVision()
	{
		ArrayList <Thing> output = new ArrayList <Thing>();
		ArrayList <Thing> thingsInVision = getThingsInVision();

		for(Thing thing : thingsInVision)
			if(thing.getOwner() != getOwner())
				output.add(thing);

		return output;
	}
	public Thing getClosestHostile()
	{
		ArrayList <Thing> thingsInVision = getHostileInVision();

		Thing target = null;
		double minDistance = 100000.0;

		for(Thing thing : thingsInVision)
		{
			if(getLocation().distanceFrom(thing.getCenter()) < minDistance)
			{
				target = thing;
				minDistance = getLocation().distanceFrom(thing.getCenter());
			}
		}

		return target;
	}
	public void station()
	{
		Thing target = getClosestHostile();

		if(target != null)
		{
			attack();
			moveTo(target.getLocation());
		}


	}
	public void move()
	{
		double newX = getLocation().getExactX();
		double newY = getLocation().getExactY();
		/**
		int centerX = center.getX();
		int centerY = center.getY();

		int x = getLocation().getX();
		int y = getLocation().getY();

		for(Unit other : getMap().getUnits())
		{
			Rectangle2D.Double unit = new Rectangle2D.Double(
				x, y,
				radius * 2,
				radius * 2);

			Rectangle2D.Double otherBox = new Rectangle2D.Double(
				other.getLocation().getX(),
				other.getLocation().getY(),
				other.getRadius() * 2,
				other.getRadius() * 2);

			if(otherBox.intersects(unit))
			{
				int otherX = other.getCenter().getX();
				int otherY = other.getCenter().getY();

				int xDifference = centerX - otherX;
				int yDifference = centerY - otherY;

				x += xDifference;
				y += yDifference;
			}
		}

		for(Building other : getMap().getBuildings())
		{
			Rectangle2D.Double unit = new Rectangle2D.Double(
				x, y,
				radius * 2,
				radius * 2);

			Rectangle2D.Double otherBox = new Rectangle2D.Double(
				other.getLocation().getX(),
				other.getLocation().getY(),
				other.getWidth(),
				other.getHeight());

			if(otherBox.intersects(unit))
			{
				int otherX = other.getCenter().getX();
				int otherY = other.getCenter().getY();

				int xDifference = centerX - otherX;
				int yDifference = centerY - otherY;

				x += xDifference;
				y += yDifference;
			}
		}

		newX = x;
		newY = y;
		**/
		if(vector == null)
			Util.throwError("Null vector - use moveTo(target) before using move()");
		else if(!moving)
			Util.throwError("Unit is not supposed to move - use moveTo(target) before using move()");
		else if(steps == counter)
			stop();
		else if(leader != null && inContact(leader))
			stop();
		else
		{
			newX += vector[0];
			newY += vector[1];



			setLocation(new Location(newX, newY));
		}
		adjustCenter();
	}
	public void stop()
	{
		steps = 0;
		counter = 0;
		moving = false;
		vector = null;
		attacking = false;
		victim = null;
	}
	public void reset()
	{
		steps = 0;
		counter = 0;
		moving = false;
		vector = null;
		attacking = false;
		target = null;
		leader = null;
		victim = null;
	}
	public double setDirection(double x)
	{
		direction = x;
		return x;
	}
	public Location setLocation(Location location)
	{
		if(teleportTo(location) == target)
			moving = false;
		else if(this.getLocation() != null || target != null)
			direction = this.getLocation().getAngleTo(target);
		counter++;
		adjustCenter();
		return location;
	}
	public double turnTo(Unit other)
	{
		direction = this.getLocation().getAngleTo(other.getLocation());
		return direction;
	}
	public double turn(double direction)
	{
		this.direction = direction;
		return direction;
	}
	public boolean inContact(Thing thing)
	{
		if(thing instanceof Unit)
			return inContact((Unit)thing);
		else if(thing instanceof Building)
			return inContact((Building)thing);
		return false;
	}
	public boolean inContact(Unit other)
	{
		Rectangle2D.Double otherBox = new Rectangle2D.Double(
			other.getLocation().getX(),
			other.getLocation().getY(),
			other.getRadius() * 2,
			other.getRadius() * 2);

		Ellipse2D.Double unitBox = new Ellipse2D.Double(
			getLocation().getX(),
			getLocation().getY(),
			radius,
			radius);

		return unitBox.intersects(otherBox);
	}
	public boolean inContact(Building building)
	{
		Ellipse2D.Double contactBox = new Ellipse2D.Double(
			getLocation().getX(),
			getLocation().getY(),
			radius,
			radius);

		Rectangle2D.Double other = new Rectangle2D.Double(
			building.getLocation().getX(),
			building.getLocation().getY(),
			building.getWidth(),
			building.getHeight());

		return contactBox.intersects(other);
	}
	public void handle(InputEvent input)
	{
		if(input instanceof KeyEvent)
		{
			KeyEvent event = (KeyEvent)input;
			KeyCode code = event.getCode();
			if(code == KeyCode.W)
				stop();
			else if(code == KeyCode.R)
				patrol();
		}
		else if(input instanceof MouseEvent)
		{
			MouseEvent event = (MouseEvent)input;

			int x = (int)event.getX();
			int y = (int)event.getY();

			if(event.isSecondaryButtonDown())
			{
				stop();
				moveTo(new Location(x, y));
			}
		}
	}
	public void dealDamage(Thing target)
	{
		target.receiveDamage(damage);
	}
	public void act() { moving = true; }
	public void attack() { attacking = true; }
	public void setLeader(Thing thing) { leader = thing; }
	public Location getCenter() { return center; }
	public Thing getLeader() { return leader; }
	public double getVision() { return super.getVision(); }
	public double[] getVector() { return vector; }
	public boolean isMoving() { return moving; }
	public double getDirection() { return direction; }
	public Location getTarget() { return target; }
	public Map getMap() { return super.getMap(); }
	public boolean isAttacking() { return attacking; }
	public double getRadius() { return radius; }
	public double getSpeed() { return speed; }
	public int getDamage() { return damage; }
}

