public class Location
{
	private int x = 0;
	private int y = 0;
	private double exactX = 0;
	private double exactY = 0;
	public Location() {}
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
		exactX = x * 1.0;
		exactY = y * 1.0;
	}
	public Location(double x, double y)
	{
		exactX = x;
		exactY = y;
		this.x = (int)exactX;
		this.y = (int)exactY;
	}
	public Location getLocationByDistance(double distance, double angle)
	{
		double percentX = Math.cos(Math.toRadians(angle));
		double percentY = -1 * Math.sin(Math.toRadians(angle));

		double newX = percentX * distance;
		double newY = percentY * distance;

		newX += exactX;
		newY += exactY;

		Location output = new Location(newX, newY);
		return output;
	}
	public double getAngleTo(Location target)
	{
		//In degrees
		double opp = this.getExactY() - target.getExactY();
		double hyp = this.distanceFrom(target);
		double radians = Math.asin(opp/hyp);
		double degrees = Math.toDegrees(radians);
		double coterminal = Util.findCotermAngle(degrees);
		if(this.getExactX() > target.getExactX())
			coterminal += 90;
		return coterminal;
	}
	public boolean isValid(Map map) { return !(x > map.getX() || x < 0 || y > map.getY() || y < 0); }
	public double getDifferenceX(Location other) { return exactX - other.getExactX(); }
	public double getDifferenceY(Location other) { return exactY - other.getExactY(); }
	public int getX() { return x; }
	public int getY() { return y; }
	public double getExactX() { return exactX; }
	public double getExactY() { return exactY; }
	public double distanceFrom(Location other) //Works
	{
		double x2 = other.getExactX();
		double y2 = other.getExactY();
		return Math.sqrt(((exactX - x2) * (exactX - x2)) + ((exactY - y2) * (exactY - y2)));
	}
	public String toString() { return "x: " + exactX + "  y: " + exactY; }
}