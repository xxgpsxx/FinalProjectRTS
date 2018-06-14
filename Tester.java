import java.util.ArrayList;
public class Tester
{
	public Tester()
	{
		Longbowman x = new Longbowman(new Location(0, 0));
		System.out.println(x.getClass().getName());
	}
	public static void main(String args[])
	{
		Tester app = new Tester();
	}
}
