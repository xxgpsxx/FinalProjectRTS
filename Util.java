import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.*;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import java.awt.geom.*;
import javafx.scene.image.*;

public class Util
{
	public static Image rotate(Image image, double rotation)
	{
		ImageView iv = new ImageView(image);
		iv.setRotate((int)rotation);
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		return iv.snapshot(params, null);
	}
	public static double asin(double ratio) { return Math.toDegrees(Math.asin(ratio)); }
	public static double sin(double degrees) { return Math.toDegrees(Math.sin(Math.toRadians(degrees))); }
	public static double cos(double degrees) { return Math.toDegrees(Math.cos(Math.toRadians(degrees))); }
	public static double findCotermAngle(double degrees)
	{
		if(degrees > 360)
			return findCotermAngle(degrees - 360);
		else if(degrees < 0)
			return findCotermAngle(degrees + 360);
		return degrees;
	}
	public static void throwError(String message)
	{
		throw new IllegalArgumentException(message);
	}
	public static Location convertToJava(Location location)
	{
		double newY = location.getExactY() - 700;
		return new Location(location.getExactX(), newY);
	}
	public static Location convertToNorm(Location location)
	{
		double newY = location.getExactY() + 700;
		return new Location(location.getExactX(), newY);
	}
	public static int[] correctCoordinates(int[] point1, int[] point2)
	{
		int[] output = new int[4];

		output[0] = point1[0];
		output[1] = point1[1];

		output[2] = point2[0] - output[0];
		output[3] = point2[1] - output[1];

		if(output[0] > point2[0])
		{
			output[2] = output[0] - point2[0];
			output[0] = point2[0];
		}

		if(output[1] > point2[1])
		{
			output[3] = output[1] - point2[1];
			output[1] = point2[1];
		}

		return output;
	}
	public static boolean intersects(Location loc, Thing thing)
	{
		Rectangle2D.Double box = new Rectangle2D.Double(
			loc.getX(),
			loc.getY(),
			2,
			2);

		Ellipse2D.Double other;
		Rectangle2D.Double otherBox;
		Unit unit;
		Building building;

		if(thing instanceof Unit)
		{
			unit = (Unit)thing;
			other = new Ellipse2D.Double(
				unit.getLocation().getX(),
				unit.getLocation().getY(),
				unit.getRadius(),
				unit.getRadius());

			return other.intersects(box);
		}
		else if(thing instanceof Building)
		{
			building = (Building)thing;
			otherBox = new Rectangle2D.Double(
				building.getLocation().getX(),
				building.getLocation().getY(),
				building.getWidth(),
				building.getHeight());

			return otherBox.intersects(box);
		}
		return false;
	}
	public static void print(int[] arr)
	{
		for(int x : arr)
			System.out.print(x + " ");
		System.out.println();
	}
	public static void print(boolean[] arr)
	{
		for(boolean x : arr)
			System.out.print(x + " ");
		System.out.println();
	}
	public static int rand(int min, int max) { return (int)(Math.random() * (max - min)) + min; }
	public static int closestSquareRoot(int x)
	{
		double sqrt = Math.sqrt(x);
		int rounded = (int)Math.round(sqrt);
		return rounded;
	}
}