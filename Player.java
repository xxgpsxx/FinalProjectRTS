import java.util.ArrayList;
import javafx.event.*;
import javafx.scene.input.*;
public class Player
{
	private int number;
	private ArrayList <Building> buildings = new ArrayList <Building>();
	//private ArrayList <MainBuilding> mainBuildings = new ArrayList <MainBuilding>();
	private ArrayList <Unit> armyUnits = new ArrayList <Unit>();
	private ArrayList <Worker> workers = new ArrayList <Worker>();
	private int minerals;
	private int[] supply = new int[2];
	private ArrayList <Thing> selection = new ArrayList <Thing>();
	private ArrayList <ArrayList <Thing>> controlGroups = new ArrayList <ArrayList <Thing>>();
	private Map map;
	private String name;
	private MainBuilding main;
	public Player(int number, Map map)
	{
		this.map = map;
		for(int i = 0; i < map.getPlayers().size(); i++)
			if(map.getPlayers().get(i).getNumber() == number)
				Util.throwError("Another player already exists with same number");
		this.number = number;
		name = "Player " + number;

		initializeControlGroups();
	}
	public Player(int number, Map map, String name)
	{
		this.map = map;
		for(int i = 0; i < map.getPlayers().size(); i++)
			if(map.getPlayers().get(i).getNumber() == number)
				Util.throwError("Another player already exists with same number");
		this.name = name;

		initializeControlGroups();
	}
	public MainBuilding getMainBuilding() { return main; }
	private void initializeControlGroups()
	{
		for(int i = 0; i < 10; i++)
			controlGroups.add(new ArrayList <Thing>());
	}
	public ArrayList <Thing> getControlGroup(int number) { return controlGroups.get(number); }
	public ArrayList <Thing> addToControlGroup(int number, Thing thing)
	{
		controlGroups.get(number).add(thing);
		return controlGroups.get(number);
	}
	public ArrayList <Thing> addToControlGroup(int number, ArrayList <Thing> things)
	{
		for(Thing thing : things)
			controlGroups.get(number).add(thing);
		return controlGroups.get(number);
	}
	public ArrayList <Thing> clearControlGroup(int number)
	{
		ArrayList <Thing> temp = controlGroups.get(number);
		controlGroups.get(number).clear();
		return temp;
	}
	public ArrayList <Thing> createControlGroup(int number, Thing thing)
	{
		clearControlGroup(number);
		addToControlGroup(number, thing);
		return controlGroups.get(number);
	}
	public ArrayList <Thing> createControlGroup(int number, ArrayList <Thing> things)
	{
		clearControlGroup(number);
		addToControlGroup(number, things);
		return controlGroups.get(number);
	}
	public int giveMinerals()
	{
		minerals += 5;
		return minerals;
	}
	public Building give(Building building)
	{
		if(building instanceof MainBuilding)
			main = (MainBuilding)building;
		building.setOwner(number);
		building.setMap(map);
		buildings.add(building);
		map.add(building);
		return building;
	}
	public Unit give(Unit unit)
	{
		unit.setOwner(number);
		unit.setMap(map);
		if(!(unit instanceof Worker))
			armyUnits.add(unit);
		else
			workers.add((Worker)(unit));
		map.add(unit);
		return unit;
	}
	public boolean owns(Thing thing)
	{
		if(thing instanceof Worker)
		{
			thing = (Worker)thing;
			for(Worker worker : workers)
				if(worker == thing)
					return true;
		}
		else if(thing instanceof Unit)
		{
			thing = (Unit)thing;
			for(Unit unit : armyUnits)
				if(unit == thing)
					return true;
		}
		else if(thing instanceof Building)
		{
			thing = (Building)thing;
			for(Building building : buildings)
				if(building == thing)
					return true;
		}
		return false;
	}
	public ArrayList <Thing> addToSelection(Thing thing)
	{
		selection.add(thing);
		return selection;
	}
	public boolean inSelection(Thing thing)
	{
		for(Thing object : selection)
			if(object.equals(thing))
				return true;
		return false;
	}
	public ArrayList <Thing> clearSelection()
	{
		ArrayList <Thing> temp = selection;
		selection = new ArrayList <Thing>();
		return temp;
	}
	public ArrayList <Thing> setSelection(ArrayList <Thing> things)
	{
		ArrayList <Thing> temp = selection;
		selection = new ArrayList <Thing>();
		selection = things;
		return temp;
	}
	public void handle(InputEvent input)
	{
		if(input instanceof KeyEvent)
		{
			KeyEvent event = (KeyEvent)input;
			KeyCode key = event.getCode();
			char c = ' ';
			try
			{
				c = event.getText().charAt(0);
			}
			catch(Exception e)
			{
			}
			int keyCode = (int)c;

			if(keyCode > 47 && keyCode < 58)
			{
				int number = keyCode - 48;
				if(event.isControlDown())
					createControlGroup(number, selection);

				else if(event.isShiftDown())
					addToControlGroup(number, selection);

				else
					setSelection(controlGroups.get(number));
			}

			else if(key == KeyCode.SPACE)
			{
				for(Unit unit : armyUnits)
					selection.add(unit);
			}
		}
	}
	public int spend(int x) { minerals = minerals - x; return minerals; }
	public String getName() { return name; }
	public Map getMap() { return map; }
	public int getNumber() { return number; }
	public ArrayList <Building> getBuildings() { return buildings; }
	public ArrayList <Unit> getArmyUnits() { return armyUnits; }
	public ArrayList <Worker> getWorkers() { return workers; }
	public int getMinerals() { return minerals; }
	public int getAvailableSupply() { return supply[1]; }
	public int getUsedSupply() { return supply[0]; }
	public ArrayList <Thing> getSelection() { return selection; }
	public void remove(Thing thing)
	{
		if(thing instanceof Building)
			buildings.remove(buildings.indexOf((Building)thing));

		else if(thing instanceof Worker)
			workers.remove(workers.indexOf((Worker)thing));

		else if(thing instanceof Unit)
			armyUnits.remove(armyUnits.indexOf((Unit)thing));
	}
}
