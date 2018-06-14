import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.application.Application;
import java.awt.geom.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import java.util.Scanner;
import javafx.stage.Screen;
import javafx.scene.control.Button;
import java.awt.event.MouseListener;
import java.awt.MouseInfo;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javafx.scene.text.*;

public class Runner extends Application implements EventHandler<InputEvent>
{
	GraphicsContext gc;
	Image unit = new Image("trooper.jpg");
	Image trooper;
	Image img;
	int[] canvasSize = {1200, 700};
	int gridSize = 100;
	AnimateObjects animate;
	Map map;
	Color[] color = {Color.BLUE, Color.RED, Color.YELLOW};
	int players = 0;
	int x = 0;
	int y = 0;
	Scanner reader = new Scanner(System.in);
	String temp;
	int counter = 0;
	int[] point1 = {600, 250}; //new int[2];
	int[] point2 = new int[2];
	boolean holdingDown = false;
	boolean primary = false;
	boolean secondary = false;
	Stage stage;
	int border = 2;
	Player player;
	Player player2;
	int units = 0;
	int[] screenDimensions = new int[2];
	boolean fullscreen = true;
	int grids = 4;
	int[] consoleDimensions = {900, 250};
	int[] consoleGridDimensions = {consoleDimensions[0] / grids, consoleDimensions[1] / grids};
	int[] consoleMargins = {7, 8};
	boolean attackCommand = false;
	int timer = 0;

	public static void main(String[] args)
	{
		launch();
	}
	public void drawMap()
	{
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, screenDimensions[0], screenDimensions[1]);

		gc.setFill(Color.GREEN);
		gc.fillRect(0, 0, map.getX(), map.getY());

		gc.setFill(Color.WHITE);

		for(int x = 0; x < map.getX()/gridSize; x++)
			gc.fillRect(x * gridSize, 0, 1, map.getY());

		for(int y = 0; y < map.getY()/gridSize; y++)
			gc.fillRect(0, y * gridSize, map.getX(), 1);
	}
	public void start(Stage stage)
	{
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[1];
		screenDimensions[0] = gd.getDisplayMode().getWidth();
		screenDimensions[1] = gd.getDisplayMode().getHeight();

		map = new Map();

		Player neutral = new Player(0, map, "neutral");
		Player player1 = new Player(1, map);
		player2 = new Player(2, map);

		map.add(neutral);
		map.add(player1);
		map.add(player2);

		player = player1;

		stage.setTitle("Basic Starcraft");
		Group root = new Group();

		Canvas canvas;
		if(fullscreen)
		{
			canvas = new Canvas(screenDimensions[0], screenDimensions[1]);
			canvasSize[0] = screenDimensions[0];
			canvasSize[1] = screenDimensions[1];
		}
		else
			canvas = new Canvas(canvasSize[0], canvasSize[1]);

		map.setX(canvasSize[0]);
		map.setY(canvasSize[1] - (85 + consoleDimensions[1]));

		root.getChildren().add(canvas);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		scene.addEventHandler(KeyEvent.KEY_PRESSED,this);
		scene.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent)
			{
				int x = (int)mouseEvent.getX();
				int y = (int)mouseEvent.getY();

				if(player.getSelection().size() > 0)
				{
					for(Thing thing : player.getSelection())
					{

						if(thing instanceof Worker)
							((Worker)thing).handle(mouseEvent);

						else if(thing instanceof MainBuilding)
							((MainBuilding)thing).handle(mouseEvent);
					}
				}

				if(mouseEvent.isPrimaryButtonDown())
				{
					System.out.println("Primary");
					primary = true;
					secondary = false;
					if(!mouseEvent.isShiftDown() && !attackCommand)
						player.clearSelection();

					point1[0] = x;
					point1[1] = y;

					holdingDown = true;

					if(attackCommand)
					{
						for(Thing thing : player.getSelection())
						{
							if(thing instanceof Unit)
							{
								((Unit)thing).attack(); //sets attacking to true
								((Unit)thing).moveTo(x, y);
							}
						}
					}

					attackCommand = false;
				}
				else if(mouseEvent.isSecondaryButtonDown())
				{
					ArrayList <Unit> unitSelection = new ArrayList <Unit>();

					for(Thing thing : player.getSelection())
						if(thing instanceof Unit)
							unitSelection.add((Unit)thing);

					int amount = unitSelection.size();

					if(amount > 0)
					{
						int cols = Util.closestSquareRoot(amount);
						int rows = amount / cols;
						if(amount % cols != 0)
							rows++;

						double largestRadius = 0.0;

						for(Unit unit : unitSelection)
							if(unit.getRadius() > largestRadius)
								largestRadius = unit.getRadius();

						int counter = 0;
						for(int i = 0; i < rows; i++)
						{
							for(int j = 0; j < cols; j++)
							{
								if(counter < amount)
								{
									int spotsFromMiddle = j - cols;

									double x1 = x - (i * (largestRadius + 5));
									double y1 = (spotsFromMiddle * (largestRadius + 5)) + y;
									unitSelection.get(counter).stop();
									unitSelection.get(counter).moveTo(new Location(x1, y1));
									counter++;
								}
							}
						}
					}


					attackCommand = false;
					secondary = true;
					primary = false;
				}
			}
		});

		stage.setX(-1928.0);
		stage.setY(-207.0);

		gc = canvas.getGraphicsContext2D();

		for(int i = 0; i < units; i++)
			player1.give(new MeleeUnit(125, 2, new Location(Util.rand(0, canvasSize[0] - 60), Util.rand(0, canvasSize[1] - 60)), 7.0, 5.0, 23, 30.0, 1.0));

		MainBuilding first = new MainBuilding(new Location(200, (map.getY() / 2) - 75));
		player.give(first);

		for(int i = 0; i < 20; i++)
		{
			int x;
			int y;
			while(true)
			{
				boolean valid = true;
				x = Util.rand(50, 125);
				y = Util.rand(first.getLocation().getY() - 100, first.getLocation().getY() + 100);

				Rectangle2D.Double box = new Rectangle2D.Double(
					x,
					y,
					20,
					20);

				for(MineralNode node : map.getMineralNodes())
				{
					Rectangle2D.Double nodeBox = new Rectangle2D.Double(
						node.getLocation().getX(),
						node.getLocation().getY(),
						node.getWidth(),
						node.getHeight());

					valid = !(box.intersects(nodeBox));
				}

				if(valid)
					break;
			}
			Location loc = new Location(x, y);

			map.add(new MineralNode(loc));
		}

		for(int i = 0; i < 60; i++)
		{
			int x = Util.rand(100, 200);
			int y = Util.rand(first.getLocation().getY(), first.getLocation().getY() + first.getHeight());
			Location loc = new Location(x, y);
			Worker worker = new Worker(x, y);
			player.give(worker);
			//worker.moveTo(map.getMineralNodes().get(0));
			worker.beginMining(map.getMineralNodes().get(0));
		}

		first.setSpawnLocation();



		spawnEnemyBase();

		stage.show();
		this.stage = stage;

		animate = new AnimateObjects();
		animate.start();
	}
	public void spawnEnemyBase()
	{
		player2.give(new Goal(screenDimensions[0] - 250, (screenDimensions[1]/2 - consoleDimensions[1])));
		int x = 10000;

		while(true)
		{
			Unit output = null;
			int unit = Util.rand(1, 10);
			Location spawnLocation = new Location(
				Util.rand(1000, 1500),
				Util.rand(0, map.getY() - 100));

			switch(unit)
			{
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
				case(9):
					output = new Giant(spawnLocation);
					break;
			}

			player2.give(output);
			x -= MainBuilding.unitCosts[unit];
			if(x <= 0)
				break;
		}

		player2.give(new Swordsman(new Location(1400, 500)));

		for(Unit unit : player2.getArmyUnits())
			unit.attack();
	}
	public class AnimateObjects extends AnimationTimer
	{
		public void handle(long now)
		{
			point2[0] = (int)MouseInfo.getPointerInfo().getLocation().getX() - (int)stage.getX() - 5;
			point2[1] = (int)MouseInfo.getPointerInfo().getLocation().getY() - (int)stage.getY() - 25;

			System.out.println(map.getGoal());

			gc.clearRect(0, 0, canvasSize[0], canvasSize[1]);
			drawMap();

			gc.setFill(Color.rgb(225, 223, 0));
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);

			for(MineralNode node : map.getMineralNodes())
			{
				int size = node.getAmount() / 250;

				gc.fillRect(
					node.getLocation().getX(),
					node.getLocation().getY(),
					size,
					size);
			}

			if(player.getMainBuilding().isProducing())
				player.getMainBuilding().produce();

			for(Player player : map.getPlayers())
			{
				gc.setFill(color[player.getNumber()]);
				for(Building building : player.getBuildings())
				{
					gc.setFill(color[player.getNumber()]);
					gc.fillRect(
						building.getLocation().getX(),
						building.getLocation().getY(),
						building.getWidth(),
						building.getHeight());

					gc.setFill(Color.BLACK);
					gc.fillRect(
						building.getLocation().getX() - 2,
						building.getLocation().getY() - 15,
						(building.getWidth()) + 4,
						14);

					gc.setFill(Color.BLUE);
					gc.fillRect(
						building.getLocation().getX() + 1,
						building.getLocation().getY() - 13,
						((building.getWidth()) - 2),
						10);

					double health = building.getHealth();
					double maxHealth = building.getMaxHealth();
					double percentageLeft = health/maxHealth;
					double percentageLost = (maxHealth - health)/maxHealth;

					gc.setFill(Color.RED);
					gc.fillRect(
						(building.getLocation().getX() + 1) + (percentageLeft * ((building.getWidth()) - 2)),
						building.getLocation().getY() - 13,
						(percentageLost * ((building.getWidth()) - 2)),
						10);

					if(player.inSelection(building))
					{
						for(int i = 0; i < border; i++)
							gc.strokeRect(
								building.getLocation().getX() + i,
								building.getLocation().getY() + i,
								building.getWidth() - (2 * i),
								building.getHeight() - (2 * i));

						gc.setStroke(Color.BLACK);

						gc.strokeLine(
							building.getCenter().getX(),
							building.getCenter().getY(),
							((MainBuilding)building).getRallyPoint().getX(),
							((MainBuilding)building).getRallyPoint().getY());

						gc.setStroke(Color.ORANGE);

						gc.strokeLine(
							building.getCenter().getX(),
							building.getCenter().getY(),
							((MainBuilding)building).getWorkerRallyPoint().getX(),
							((MainBuilding)building).getWorkerRallyPoint().getY());


					}
				}

				gc.setStroke(Color.BLACK);

				for(Worker worker : player.getWorkers())
				{
					gc.setFill(color[player.getNumber()]);
					if(worker.isMoving())
						worker.move();

					gc.fillOval(
						worker.getLocation().getX(),
						worker.getLocation().getY(),
						worker.getRadius(),
						worker.getRadius());

					if(player.inSelection(worker))
					{
						for(int i = 0; i < border; i++)
							gc.strokeOval(
								worker.getLocation().getX() + i,
								worker.getLocation().getY() + i,
								worker.getRadius() - (2 * i),
								worker.getRadius() - (2 * i));
					}
				}

				for(Unit unit : player.getArmyUnits())
				{

					//System.out.println("MOVING: " + unit.isMoving());
					//System.out.println("ATTACKING: " + unit.isAttacking());
					if(unit.isMoving() || unit.isAttacking())
					{
						if(unit.isPatrolling())
							unit.station();

						else if(unit.isAttacking())
						{
							if(unit instanceof MeleeUnit)
								((MeleeUnit)unit).attackMove();

							else if(unit instanceof RangedUnit)
								((RangedUnit)unit).attackMove();

							else
								unit.move();
						}

						else
							unit.move();
					}

					gc.setFill(Color.BLACK);
					gc.fillRect(
						unit.getLocation().getX() - 2,
						unit.getLocation().getY() - 15,
						unit.getRadius() + 4,
						14);

					gc.setFill(Color.BLUE);
					gc.fillRect(
						unit.getLocation().getX() + 1,
						unit.getLocation().getY() - 13,
						((unit.getRadius()) - 2),
						10);

					double health = unit.getHealth();
					double maxHealth = unit.getMaxHealth();
					double percentageLeft = health/maxHealth;
					double percentageLost = (maxHealth - health)/maxHealth;

					gc.setFill(Color.RED);
					gc.fillRect(
						(unit.getLocation().getX() + 1) + (percentageLeft * (unit.getRadius() - 2)),
						unit.getLocation().getY() - 13,
						(percentageLost * (unit.getRadius() - 2)),
						10);

					gc.setFill(color[player.getNumber()]);

					gc.fillOval(
						unit.getLocation().getX(),
						unit.getLocation().getY(),
						unit.getRadius(),
						unit.getRadius());

					if(player.inSelection(unit))
					{
						gc.setStroke(Color.BLACK);
						for(int i = 0; i < border; i++)
							gc.strokeOval(
								unit.getLocation().getX() + i,
								unit.getLocation().getY() + i,
								unit.getRadius() - (2 * i),
								unit.getRadius() - (2 * i));
					}

					gc.setFill(Color.BLACK);
					gc.setStroke(Color.WHITE);
					Font font = Font.font("Times New Roman", FontWeight.NORMAL, (int)(unit.getRadius()/1.25));
					gc.setFont(font);
					gc.fillText(
						unit.getClass().getName().substring(0, 2),
						unit.getLocation().getX(),
					unit.getLocation().getY() + unit.getRadius()/2 + 10);

					timer++;
				}
			}

			gc.setFill(Color.BLACK);
			gc.setStroke(Color.BLACK);

			int[] parameters;

			if(holdingDown)
			{
				gc.setLineWidth(3);
				parameters = Util.correctCoordinates(point1, point2);

				gc.strokeRect(
					parameters[0],
					parameters[1],
					parameters[2],
					parameters[3]);
			}

			//Drawing the console
			drawConsole();

			//Drawing Text
			gc.setFill(Color.BLACK);
			Font font = Font.font("Times New Roman", FontWeight.NORMAL, 24);
			gc.setFont(font);
			gc.fillText("Minerals: " + player.getMinerals(), 25, 25, 200);

			gc.setFill(Color.WHITE);

			gc.fillText("Time: " + timer/100, 5, map.getY() + 25);
			gc.fillText("Enemy Units Remaining: " + player2.getArmyUnits().size(), 5, map.getY() + 50);
			try
			{
				gc.fillText("Objective Health Remaining: " + player2.getBuildings().get(0).getHealth(), 5, map.getY() + 75);
			}
			catch(Exception e)
			{
				gc.fillText("Objective Health Remaining: " + 0, 5, map.getY() + 75);
			}
		}
		public void drawConsole()
		{
			gc.setStroke(Color.WHITE);
			gc.setFill(Color.WHITE);
			gc.setLineWidth(5);

			//Smaller square
			gc.strokeRect(
				canvasSize[0] - (10 + consoleDimensions[0]),
				canvasSize[1] - (75 + consoleDimensions[1]),
				consoleDimensions[0],
				consoleDimensions[1]);

			gc.strokeRect(
				canvasSize[0] - (20 + consoleDimensions[0]),
				canvasSize[1] - (85 + consoleDimensions[1]),
				consoleDimensions[0] + 20,
				consoleDimensions[1] + 20);

			for(int i = 1; i <= 4; i++)
			{
				gc.strokeRect(
					canvasSize[0] - (10 + consoleDimensions[0]),
					(canvasSize[1] - (75 + consoleDimensions[1])) + (i * consoleGridDimensions[1]),
					consoleDimensions[0],
					1);

				gc.strokeRect(
					(canvasSize[0] - (10 + consoleDimensions[0])) + (i * consoleGridDimensions[0]),
					canvasSize[1] - (75 + consoleDimensions[1]),
					1,
					consoleDimensions[1]);
			}


			double barLength = consoleGridDimensions[0] - (consoleMargins[0] * 2);
			gc.setLineWidth(2);
			//Drawing progress grids for main building
			if(true)//player.inSelection(player.getMainBuilding()))
			{
				int counter = 0;

				for(int j = 0; j < 4; j++)
				{
					for(int i = 0; i < 4; i++)
					{
						if(counter < 9)
						{
							int[] boxOneCoordinates = {
								canvasSize[0] - (10 + consoleDimensions[0]),
								canvasSize[1] - (75 + consoleDimensions[1])};

							int[] currentBoxCoordinates = {
								boxOneCoordinates[0] + consoleMargins[0] + (i * consoleGridDimensions[0]),
								boxOneCoordinates[1] + consoleMargins[1] + (j * consoleGridDimensions[1])};

							if(player.getMainBuilding().isProducing(counter))
							{

								gc.setFill(Color.WHITE);
								gc.fillRect(
									currentBoxCoordinates[0],
									currentBoxCoordinates[1],
									barLength,
									12);

								gc.setFill(Color.AQUAMARINE);
								gc.fillRect(
									currentBoxCoordinates[0] + 1,
									currentBoxCoordinates[1] + 1,
									(((double)(player.getMainBuilding().getProductionCounter(counter)))/
									((double)(player.getMainBuilding().getProductionTime(counter)))) * barLength,
									10);

								gc.setStroke(Color.WHITE);
								gc.setFill(Color.WHITE);
								Font font = Font.font("Times New Roman", FontWeight.NORMAL, 30);
								gc.setFont(font);

								//Amount being produced
								gc.fillText(
									"" + player.getMainBuilding().productionQueue(counter),
									currentBoxCoordinates[0] + consoleGridDimensions[0] - (consoleMargins[0] + 30),
									currentBoxCoordinates[1] + 44);
							}
							//Cost

							Font font = Font.font("Times New Roman", FontWeight.NORMAL, 30);
							gc.setFont(font);
							gc.setFill(Color.WHITE);
							gc.setStroke(Color.WHITE);
							gc.fillText(
								"" + player.getMainBuilding().getUnitCost(counter),
								currentBoxCoordinates[0] + consoleMargins[0] + 100,
								currentBoxCoordinates[1] + 44);

							String name = "";
							switch(counter)
							{
								case(0):
									name = "Worker";
									break;
								case(1):
									name = "Swordsman";
									break;
								case(2):
									name = "Archer";
									break;
								case(3):
									name = "Crossbowman";
									break;
								case(4):
									name = "Longbowman";
									break;
								case(5):
									name = "Spearman";
									break;
								case(6):
									name = "Horseback Rider";
									break;
								case(7):
									name = "Sniper";
									break;
								case(8):
									name = "Giant";
									break;
							}


								//Name
								gc.fillText(
									name,
									currentBoxCoordinates[0] + consoleMargins[0],
									currentBoxCoordinates[1] + 44,
									100);

							counter++;
						}
					}
				}
			}
		}
	}
	public void handle(final InputEvent input)
	{
		if(input instanceof KeyEvent)
		{
			attackCommand = false;
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

			for(Thing thing : player.getSelection())
			{
				if(thing instanceof Worker)
					((Worker)thing).handle(event);

				else if(thing instanceof Unit)
					((Unit)thing).handle(event);

				else if(thing instanceof MainBuilding)
					((MainBuilding)thing).handle(event);
			}

			if(key == KeyCode.T)
			{
				System.out.println("Attack Command");
				attackCommand = true;
			}

			else if(keyCode > 47 && keyCode < 58)
				player.handle(event);

			else
				player.handle(event);
		}
		if(input instanceof MouseEvent) //Tracks when the key is lifted
		{
			int[] parameters = Util.correctCoordinates(point1, point2);

			if(primary)
			{
				holdingDown = false;

				Rectangle2D.Double selectionRect = new Rectangle2D.Double(
					parameters[0],
					parameters[1],
					parameters[2],
					parameters[3]);

				for(Worker worker : player.getWorkers())
				{
					Ellipse2D.Double object = new Ellipse2D.Double(
						worker.getLocation().getX(),
						worker.getLocation().getY(),
						worker.getRadius(),
						worker.getRadius());

					if(object.intersects(selectionRect))
						player.addToSelection(worker);
				}

				for(Unit unit : player.getArmyUnits())
				{
					Rectangle2D.Double object = new Rectangle2D.Double(
						unit.getLocation().getX(),
						unit.getLocation().getY(),
						unit.getRadius() * 2,
						unit.getRadius() * 2);

					if(selectionRect.intersects(object))
						player.addToSelection(unit);
				}

				for(Building building : player.getBuildings())
				{
					Rectangle2D.Double object = new Rectangle2D.Double(
						building.getLocation().getX(),
						building.getLocation().getY(),
						building.getWidth(),
						building.getHeight());

					if(selectionRect.intersects(object))
						player.addToSelection(building);
				}
			}
		}
	}
}
