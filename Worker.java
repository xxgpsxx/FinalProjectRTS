import java.awt.geom.*;
import javafx.event.*;
import javafx.scene.input.*;
public class Worker extends MeleeUnit
{
	private int cargo = 0;
	private boolean building = false;
	private boolean mining = false;
	private boolean returning = false;
	private int countdown = 60;
	private MineralNode node;
	public Worker(Map map, Location location, int owner)
	{
		super(map, 50, 1, location, owner, 20.0, 5.0, 5, 30.0, 1.0);
	}
	public Worker(Map map, Location location)
	{
		super(map, 50, 1, location, 20.0, 5.0, 5, 30.0, 1.0);
	}
	public Worker(Location location)
	{
		super(50, 1, location, 20.0, 5.0, 5, 30.0, 1.0);
	}
	public Worker(int x, int y)
	{
		super(50, 1, new Location(x, y), 20.0, 5.0, 5, 30.0, 1.0);
	}
	public void immediateStop()
	{
		super.stop();
		building = false;
		mining = false;
		returning = false;
		countdown = 60;
	}
	public boolean inContact(MineralNode node)
	{
		int size = node.getAmount() / 250;

		Rectangle2D.Double nodeBox = new Rectangle2D.Double(
			node.getLocation().getX(),
			node.getLocation().getY(),
			size,
			size);

		Ellipse2D.Double workerBox = new Ellipse2D.Double(
			getLocation().getX(),
			getLocation().getY(),
			getRadius(),
			getRadius());

		return workerBox.intersects(nodeBox);
	}
	public void stop()
	{
		super.reset();
	}
	public int getMaxHealth() { return super.getMaxHealth(); }
	public int mine(MineralNode node) //When the worker is in contact with a mineral node
	{
		if(countdown == 0)
		{
			stop();
			mining = false;
			returning = true;
			countdown = 60;
			cargo = 5;
			node.mine(cargo);
			returnCargo();
			return 0;
		}
		countdown--;
		return countdown;
		//Has an int called wait time that tracks how long it needs to stay at a mineral node to succesfully mine from it
		//Takes 5 minerals at a time
		//after it is done mining, it moves to closest main building (returnCargo)
	}
	public void beginMining(MineralNode node)
	{
		if(this.node != node && node.getWorkers().size() >= 3)
			for(MineralNode mineral : getMap().getMineralNodes())
				if(mineral.getWorkers().size() < 3)
					node = mineral;

		if(node != null && (node.getWorkers().size() < 3 || node == this.node))
		{
			node.add(this);
			super.moveTo(node.getLocation());
			this.node = node;
			mining = true;
			returning = false;
		}
	}
	public void move()
	{
		Player owner = getMap().getPlayers().get(getOwner());
		if(mining)
		{
			if(inContact(node))
				mine(node);

			else
				super.move();
		}
		else if(returning)
		{
			if(super.inContact(owner.getMainBuilding()))
			{
				owner.giveMinerals();
				beginMining(node);
			}
			else
				super.move();
		}
		else
			super.move();



		//Checks to see if it is in contact with either a mineral node or a main building
		//It then acts depending on what it is carrying
		//If it is not touching anything, then it will proceed
	}
	public void returnCargo()
	{
		//Player owner = getMap().getPlayers().get(getOwner());
		Player owner = getMap().getPlayers().get(1);
		super.moveTo(owner.getMainBuilding());
		returning = true;
		mining = false;

		//Moves to closest main building
		//Gives cargo to main building
	}
	public MineralNode closestMineralNode()
	{
		double minDistance = 10000.0;
		MineralNode output = null;
		for(MineralNode node : getMap().getMineralNodes())
		{
			if(getLocation().distanceFrom(node.getLocation()) < minDistance)
			{
				minDistance = getLocation().distanceFrom(node.getLocation());
				output = node;
			}
		}
		return output;
	}
	public void handle(InputEvent input)
	{
		if(input instanceof KeyEvent)
		{
			KeyEvent event = (KeyEvent)input;
			KeyCode code = event.getCode();

			if(code == KeyCode.M)
				beginMining(closestMineralNode());
		}
		else if(input instanceof MouseEvent)
		{
			MouseEvent event = (MouseEvent)input;

			if(event.isSecondaryButtonDown())
			{
				immediateStop();
				int x = (int)event.getX();
				int y = (int)event.getY();

				Location loc = new Location(x, y);

				boolean mineralNode = false;
				MineralNode mineral = null;

				for(MineralNode node : getMap().getMineralNodes())
				{
					if(Util.intersects(loc, node))
					{
						mineralNode = true;
						mineral = node;
					}
				}
				if(mineralNode && mineral != null)
					beginMining(node);
				else
				{
					moveTo(loc);
					move();
				}
			}
		}
	}
	public int setOwner(int x) { return super.setOwner(x); }
	public Map setMap(Map map) { return super.setMap(map); }
	public Map getMap() { return super.getMap(); }
}