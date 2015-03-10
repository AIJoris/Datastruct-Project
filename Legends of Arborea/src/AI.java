import java.util.ArrayList;
import java.util.Random;

/*
 * This class implements a computer driven player
 */
public class AI {
	String positionSelf;
	Tile tileSelf;
	String positionHostile;
	String toPosition;
	ArrayList<String> hostiles;
	ArrayList<String> legalMoves;
	Random rand = new Random();
	int x;
	int y;
	int x1;
	int y1;
	Grid grid;
	String team;
	
	/*
	 * Constructor
	 */
	public AI(Grid newGrid, String newPlayerTeam) {
		grid = newGrid;
		team = newPlayerTeam;
	}
	
	/*
	 * Make a random move
	 */
	public void playRandom() {
		ArrayList<String> humansTemp = new ArrayList<String>(grid.humans);
		ArrayList<String> beastsTemp = new ArrayList<String>(grid.beasts);
		int index;

		// Do 1 action with every unit
		resetTurnsLeft();
		while (!humansTemp.isEmpty() && !beastsTemp.isEmpty()) {
			// Get a random friendly unit at a position
			if (team.equals("Humans")) {
				index = rand.nextInt(humansTemp.size());
				positionSelf = humansTemp.get(index);
				tileSelf = grid.gridMap.get(positionSelf);
				// Remove the index so this unit will not be chosen again
				humansTemp.remove(index);
			}
			else if (team.equals("Beasts")) {
				index = rand.nextInt(beastsTemp.size());
				positionSelf = beastsTemp.get(index);
				tileSelf = grid.gridMap.get(positionSelf);
				beastsTemp.remove(index);
				// Remove the index so this unit will not be chosen again
			}
			x = tileSelf.x;
			y = tileSelf.y;
			
			// Attack a hostile unit if possible		
			hostiles = tileSelf.surroundingHostiles();
			if (!hostiles.isEmpty()) {
				positionHostile = hostiles.get(rand.nextInt(hostiles.size()));
				x1 = grid.gridMap.get(positionHostile).x;
				y1 = grid.gridMap.get(positionHostile).y;
				grid.attackUnit(x,y, x1, y1);
				tileSelf.turnsLeft = false;
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
				tileSelf.turnsLeft = false;
				grid.getTile(x1, y1).turnsLeft = false;
			}
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				System.err.println(e);
			}
			grid.message = null;
		}
	}
	
	/*
	 * For all tiles with units, set turnsLeft to true
	 */
	private void resetTurnsLeft() {
		if (team.equals("Humans")) {
			for (String unitPosition : grid.humans){
				grid.gridMap.get(unitPosition).turnsLeft = true;
			}
		}
		else {
			for (String unitPosition : grid.beasts){
				grid.gridMap.get(unitPosition).turnsLeft = true;
			}
		}
		
	}
	
	public void play() {
		
	}
	
	public int minimax() {
		return 0;
	}
}