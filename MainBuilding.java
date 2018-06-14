import java.util.ArrayList;
import javafx.event.*;
import javafx.scene.input.*;
public class MainBuilding extends Building
{
	private final static int[] size = {75, 75};
	private final static double vision = 50.0;
	private final static int[] productionTimes = {4, 8, 12, 16, 6, 20, 24, 18, 25, 30}; //In seconds
	private int[] productionCounter = {4, 8, 12, 16, 6, 20, 24, 18, 25, 30}; //In seconds
	public static int[] unitCosts = {50, 75, 125, 125, 225, 275, 1, 200, 400, 450};
	private Unit[] units = {
		new Worker(getLocation().getX() + size[0], getLocation().getY() + size[1])
		}; //Write clases
	private int[] producing = new int[10];
	private Location spawnLocation;
	private Location rallyPoint;
	private Location workerRallyPoint;
	private boolean startMine = true;
	public MainBuilding(Map map, Location location, int owner)
	{
		super(map, 1500, 3, location, owner, vision, size[0], size[1]);
		setBuildTimes();
	}
	public MainBuilding(Map map, Location location)
	{
		super(map, 1500, 3, location, vision, size[0], size[1]);
		setBuildTimes();
	}
	public MainBuilding(Location location)
	{
		super(1500, 3, location, vision, size[0], size[1]);
		setBuildTimes();
	}
	public void setSpawnLocation()
	{
		int x = getLocation().getX() + size[0] + 5;
		int y = getLocation().getY() + size[1] + 5;

		int x2 = getLocation().getX() - 5;
		int y2 = getLocation().getY() + size[1] + 5;

		int centerX = getLocation().getX() + (getWidth()/2);
		int centerY = getLocation().getY() + (getHeight()/2);

		spawnLocation = new Location(centerX, centerY);
		rallyPoint = new Location(x, y);
		workerRallyPoint = new Location(x2, y2);
	}
	private void setBuildTimes()
	{
		for(int i = 0; i < productionTimes.length; i++)
			productionTimes[i] *= 60;

		for(int i = 0; i < productionCounter.length; i++)
			productionCounter[i] *= 60;

	}
	public int getUnitCost(int x)
	{
		return unitCosts[x];
	}
	public boolean isProducing()
	{
		for(int unit : producing)
			if(unit > 0)
				return true;
		return false;
	}
	public boolean isProducing(int x)
	{
		return producing[x] > 0;
	}
	public int productionQueue(int x)
	{
		return producing[x];
	}
	public void produce(int x)
	{
		if(!(getMap().getPlayerByNumber(getOwner()).getMinerals() - unitCosts[x] < 0))
		{
			producing[x]++;
			getMap().getPlayerByNumber(getOwner()).spend(unitCosts[x]);
		}
	}
	public void produce()
	{
		for(int i = 0; i < productionCounter.length; i++)
		{
			Unit output = null;
			if(isProducing(i))
			{
				productionCounter[i]--;
				if(productionCounter[i] <= 0)
				{
					switch(i)
					{
						case(0):
							output = new Worker(spawnLocation);
							break;
						case(1):
							output = new Swordsman(spawnLocation);
							break;
						case(2):
							output = new Archer(spawnLocation);
							break;
						case(3):
							output = new Crossbowman(spawnLocation);
							break;
						case(4):
							output = new Longbowman(spawnLocation);
							break;
						case(5):
							output = new Spearman(spawnLocation);
							break;
						case(6):
							output = new Horsebackrider(spawnLocation);
							break;
						case(7):
							output = new Sniper(spawnLocation);
							break;
						case(8):
							output = new Giant(spawnLocation);
							break;
					}
					producing[i]--;
					productionCounter[i] = productionTimes[i];
					getMap().getPlayers().get(1).give(output);
					if(output instanceof Worker && workerRallyPoint != null)
						output.moveTo(workerRallyPoint);
					else
						output.moveTo(rallyPoint);
				}
			}
		}
	}
	public void handle(InputEvent input)
	{
		if(input instanceof KeyEvent)
		{
			KeyEvent event = (KeyEvent)input;
			KeyCode code = event.getCode();

			if(code == KeyCode.Q)
				produce(0);
			else if(code == KeyCode.W)
				produce(1);
			else if(code == KeyCode.E)
				produce(2);
			else if(code == KeyCode.R)
				produce(3);
			else if(code == KeyCode.A)
				produce(4);
			else if(code == KeyCode.S)
				produce(5);
			else if(code == KeyCode.D)
				produce(6);
			else if(code == KeyCode.F)
				produce(7);
			else if(code == KeyCode.Z)
				produce(8);
		}
		else if(input instanceof MouseEvent)
		{
			MouseEvent event = (MouseEvent)input;

			int x = (int)event.getX();
			int y = (int)event.getY();

			Location loc = new Location(x, y);

			if(event.isSecondaryButtonDown() && event.isShiftDown())
			{
				workerRallyPoint = loc;

				startMine = false;

				for(MineralNode node : getMap().getMineralNodes())
				{
					if(Util.intersects(loc, node))
						startMine = true;
				}
			}

			else if(event.isSecondaryButtonDown())
				rallyPoint = loc;
		}

	}
	public void setWorkerRallyPoint(Location location) { workerRallyPoint = location; }
	public void setWorkerRallyPoint(int x, int y) { workerRallyPoint = new Location(x, y); }
	public Location getWorkerRallyPoint() { return workerRallyPoint; }
	public int getProductionCounter(int x) { return productionCounter[x]; }
	public int getProductionTime(int x) { return productionTimes[x]; }
	public Location getRallyPoint() { return rallyPoint; }
	public void setRallyPoint(int x, int y) { rallyPoint = new Location(x, y); }
	public void setRallyPoint(Location location) { rallyPoint = location; }
	public double getVision() { return super.getVision(); }
	public int setOwner(int x) { return super.setOwner(x); }
	public Map setMap(Map map) { return super.setMap(map); }
}
