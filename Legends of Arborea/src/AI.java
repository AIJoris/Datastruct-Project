import java.util.ArrayList;
import java.util.Random;

/*
 * This class implements a computer driven player
 * 
 * POSSIBLE AI TACTICS:
 * 	- Look x moves ahead separately per unit, attack if possible, if not make a move and then attack if possible, if not move closer to hostile 
 * 	- Calculate closest hostile per unit and move towards them, attack if possible
 * 	- Pick one hostile, and move as close as possible with all units and then attack
 * 	- Minimax, with as goal either maximizing own cumulative weapon skill or minimizing the other player's cumulative weapon skill
 * 	- For all units, calculate possible moves, and if an attack is possible immediately or after 1 move. Pick these units first, move/attack
 * 		with them. After moving/attacking with the first unit, calculate it all again and repeat.
 */
public class AI {
	String positionSelf;
	Tile tileSelf;
	String positionHostile;
	String toPosition;
	ArrayList<String> hostiles;
	ArrayList<String> legalMoves;
	Random rand = new Random();
	int x, y, x1, y1;
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
		resetTurnsLeft(true);
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
				tileSelf.attackLeft = false;
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
				tileSelf.moveLeft = false;
				if (tileSelf.attackLeft) {
					grid.getTile(x1,y1).attackLeft = true;
				}
				tileSelf.attackLeft = false;
				grid.getTile(x1, y1).moveLeft = false;
			}
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				System.err.println(e);
			}
			grid.message = null;
		}
		resetTurnsLeft(false);
	}
	
	/*
	 * For all tiles with units, set turnsLeft to true
	 */
	private void resetTurnsLeft(Boolean toBoolean) {
		if (team.equals("Humans")) {
			for (String unitPosition : grid.humans){
				grid.gridMap.get(unitPosition).moveLeft = toBoolean;
				grid.gridMap.get(unitPosition).attackLeft = toBoolean;
			}
		}
		else {
			for (String unitPosition : grid.beasts){
				grid.gridMap.get(unitPosition).moveLeft = toBoolean;
				grid.gridMap.get(unitPosition).attackLeft = toBoolean;
			}
		}
		
	}
	/*
	 * This method makes intelligent moves
	 */
	public void play() {
		// Get the team you are playing
		if (team.equals("Humans")) {
			ArrayList<String> friendlies = new ArrayList<String>(grid.humans);
		}
		else {
			ArrayList<String> friendlies = new ArrayList<String>(grid.beasts);
		}
		
		// For every unit, check which moves are possible
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}