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
import javafx.geometry.Rectangle2D;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;

public class Example extends Application implements EventHandler<InputEvent>
{
	GraphicsContext gc;
	Image trooper;
	int x = 0;
	int y = 0;
	AnimateObjects animate;

	public static void main(String[] args)
	{
		launch();
	}
	public void start(Stage stage)
	{
		URL resource = getClass().getResource("test.wav");
		AudioClip clip = new AudioClip(resource.toString());
		clip.play();

		stage.setTitle("Final Project Title");
		Group root = new Group();

		Canvas canvas = new Canvas(800, 400);
		root.getChildren().add(canvas);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		scene.addEventHandler(KeyEvent.KEY_PRESSED,this);
		scene.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

		gc = canvas.getGraphicsContext2D();
		trooper = new Image("trooper.jpg");
		gc.drawImage(trooper, 0, 0);

		animate = new AnimateObjects();
		animate.start();

		stage.show();
	}
	public class AnimateObjects extends AnimationTimer
	{
		public void handle(long now)
		{
			//x+=1;
			gc.drawImage(trooper, x, y);
			Rectangle2D rect2 = new Rectangle2D(x, y, trooper.getWidth(), trooper.getHeight());
			Rectangle2D rect1 = new Rectangle2D(400, 100, 100, 100);
			gc.fillRect(400, 100, 100, 100);
			if(rect1.intersects(rect2))
				System.out.println("hit");
		}
	}
	public void handle(final InputEvent event)
	{
		if(true)
		{
			if(event instanceof KeyEvent)
			{
				if(((KeyEvent)event).getCode() == KeyCode.LEFT || ((KeyEvent)event).getCode() == KeyCode.A)
					x -= 5;
				else if(((KeyEvent)event).getCode() == KeyCode.RIGHT || ((KeyEvent)event).getCode() == KeyCode.D)
					x += 5;
				else if(((KeyEvent)event).getCode() == KeyCode.UP || ((KeyEvent)event).getCode() == KeyCode.W)
					y -= 5;
				else if(((KeyEvent)event).getCode() == KeyCode.DOWN || ((KeyEvent)event).getCode() == KeyCode.S)
					y += 5;
			}
			if (event instanceof MouseEvent)
			{
				System.out.println(((MouseEvent)event).getX());
				System.out.println(((MouseEvent)event).getY());
			}
			//x+=1;
			gc.drawImage(trooper, x, y);
			Rectangle2D rect2 = new Rectangle2D(x, y, trooper.getWidth(), trooper.getHeight());
			Rectangle2D rect1 = new Rectangle2D(400, 100, 100, 100);
			gc.fillRect(400, 100, 100, 100);
			if (rect1.intersects(rect2))
				System.out.println("hit");
		}
		else
		{
			gc.setFill( Color.YELLOW);
			gc.setStroke( Color.BLACK );
			gc.setLineWidth(1);
			Font font = Font.font("Arial", FontWeight.NORMAL, 48);
			gc.setFont(font);
			gc.fillText("Game Over", 100, 50 );
			gc.strokeText("Game Over", 100, 50 );
		}
	}
}
