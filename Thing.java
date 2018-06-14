public class Thing
{
	private int health = 0;
	private int armor = 0;
	private Boolean alive = true;
	private int owner = 0;
	private Location location = new Location();
	private Map map;
	private double vision;
	private int maxHealth;
	public Thing()
	{
	}
	public Thing(Map map, int health, int armor, Location location, int owner, double vision)
	{
		this.map = map;
		this.health = health;
		this.armor = armor;
		this.owner = owner;
		this.location = location;
		this.vision = vision;
		setMaxHealth();
	}
	public Thing(Map map, int health, int armor, Location location, double vision)
	{
		this.map = map;
		this.health = health;
		this.armor = armor;
		this.location = location;
		this.vision = vision;
		setMaxHealth();
	}
	public Thing(int health, int armor, Location location, double vision)
	{
		this.health = health;
		this.armor = armor;
		this.location = location;
		this.vision = vision;
		setMaxHealth();
	}
	public Location getCenter()
	{
		if(this instanceof Building)
			return ((Building)this).getCenter();
		else if(this instanceof Unit)
			return ((Unit)this).getCenter();
		else
			return location;
	}
	public void setMaxHealth()
	{
		maxHealth = health;
	}
	public int getMaxHealth() { return maxHealth; }
	public double getVision() { return vision; }
	public int setOwner(int x) { owner = x; return x; }
	public Map setMap(Map map) { this.map = map; return map; }
	public Location teleportTo(Location location) { this.location = location; return location; }
	public Map getMap() { return map; }
	public int getOwner() { return owner; }
	public Location getLocation() { return location; }
	public int getHealth() { return health; }
	public int getArmor() { return armor; }
	public int receiveDamage(int damage)
	{
		//health = health - (damage - armor);
		damage = damage - armor;
		if(damage < 0)
			damage = 0;
		health = health - damage;
		if(health <= 0)
		{
			if(this instanceof Building)
				getMap().remove((Building)this);

			else if(this instanceof Unit)
				getMap().remove((Unit)this);

		}

		return health;
	}
}
