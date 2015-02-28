import java.util.ArrayList;
import java.util.Random;

/*
 * This class implements a computer driven player
 */
public class AI {
	String positionSelf;
	Tile tileSelf;
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
			tileSelf = grid.gridMap.get(positionSelf);
//			humansTemp.remove(index);
		}
		else if (playerTeam.equals("Beasts")) {
			index = rand.nextInt(beastsTemp.size());
			positionSelf = beastsTemp.get(index);
			tileSelf = grid.gridMap.get(positionSelf);
//			humansTemp.remove(index);
		}
		x = tileSelf.x;
		y = tileSelf.y;
		
		// Attack a hostile unit if possible
		hostiles = tileSelf.surroundingHostiles(x, y);
		if (!hostiles.isEmpty()) {
			positionHostiles = hostiles.get(rand.nextInt(hostiles.size()));
			x1 = grid.gridMap.get(positionHostiles).x;
			y1 = grid.gridMap.get(positionHostiles).y;
			grid.attackUnit(x,y, x1, y1);
		}
		
		// Make a random move if not possible to attack
		else if (tileSelf.legalMoves().size() > 0){
			// Randomly pick one of the legal moves to be made from (x,y)
			legalMoves = tileSelf.legalMoves();
			toPosition = legalMoves.get(rand.nextInt(legalMoves.size()));
			x1 = grid.gridMap.get(toPosition).x;
			y1 = grid.gridMap.get(toPosition).y;
			
			// Move to the position
			grid.moveUnit(x, y, x1, y1);	
		}
	}
}