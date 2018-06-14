import java.util.ArrayList;

public class Game
{
	private ArrayList<Player> players = new ArrayList<Player>();
	public Game()
	{
	}
	public Player addPlayer(Player player) { players.add(player); return player; }
	public Player getPlayerByNumber(int number)
	{
		for(Player player : players)
			if(player.getNumber() == number)
				return player;
		return null;
	}
}