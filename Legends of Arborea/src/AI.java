import java.util.ArrayList;
import java.util.Random;

/*
 * This class implements a computer driven player
 */
public class AI {
	String positionSelf;
	String positionHostiles;
	String toPosition;
	ArrayList<String> hostiles;
	ArrayList<String> legalMoves;
	Random rand = new Random();
	int x;
	int y;
	int x1;
	int y1;
	Grid grid;
	String playerTeam;
	
	/*
	 * Constructor
	 */
	public AI(Grid newGrid, String newPlayerTeam) {
		grid = newGrid;
		playerTeam = newPlayerTeam;
	}
	
	/*
	 * Make a random move
	 */
	public void play() {
		ArrayList<String> humansTemp = grid.humans;
		ArrayList<String> beastsTemp = grid.beasts;
		int index;
		
		// hier een forloop die elke unit een zet laat doen
		
		// Get a random friendly unit at a position
		if (playerTeam.equals("Humans")) {
			index = rand.nextInt(humansTemp.size());
			positionSelf = humansTemp.get(index);
//			humansTemp.remove(index);
		}
		else if (playerTeam.equals("Beasts")) {
			index = rand.nextInt(beastsTemp.size());
			positionSelf = beastsTemp.get(index);
//			humansTemp.remove(index);
		}
		x = grid.grid.get(positionSelf).x;
		y = grid.grid.get(positionSelf).y;
		
		// Attack a hostile unit if possible
		hostiles = grid.allHostiles(x, y);
		if (!hostiles.isEmpty()) {
			positionHostiles = hostiles.get(rand.nextInt(hostiles.size()));
			x1 = grid.grid.get(positionHostiles).x;
			y1 = grid.grid.get(positionHostiles).y;
			grid.attackUnit(x,y, x1, y1);
		}
		
		// Make a random move if not possible to attack
		else if (grid.legalMoves(x,y).size() > 0){
			// Randomly pick one of the legal moves to be made from (x,y)
			legalMoves = grid.legalMoves(x,y);
			toPosition = legalMoves.get(rand.nextInt(legalMoves.size()));
			x1 = grid.grid.get(toPosition).x;
			y1 = grid.grid.get(toPosition).y;
			
			// Move to the position
			grid.moveUnit(x, y, x1, y1);	
		}
	}
}
