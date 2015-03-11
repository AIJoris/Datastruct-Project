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
	Tile tileHostile;
	Tile toTile;
	ArrayList<Tile> hostiles;
	ArrayList<Tile> legalMoves;
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
		ArrayList<Tile> humansTemp = new ArrayList<Tile>(grid.humans);
		ArrayList<Tile> beastsTemp = new ArrayList<Tile>(grid.beasts);
		int index;

		// Do 1 action with every unit
		resetTurnsLeft(true);
		while (!humansTemp.isEmpty() && !beastsTemp.isEmpty()) {
			// Get a random friendly unit at a position
			if (team.equals("Humans")) {
				index = rand.nextInt(humansTemp.size());
				tileSelf = humansTemp.get(index);
				// Remove the index so this unit will not be chosen again
				humansTemp.remove(index);
			}
			else if (team.equals("Beasts")) {
				index = rand.nextInt(beastsTemp.size());
				tileSelf = beastsTemp.get(index);
				beastsTemp.remove(index);
				// Remove the index so this unit will not be chosen again
			}
			x = tileSelf.x;
			y = tileSelf.y;
			
			// Attack a hostile unit if possible		
			hostiles = tileSelf.surroundingHostiles();
			if (!hostiles.isEmpty()) {
				tileHostile = hostiles.get(rand.nextInt(hostiles.size()));
				x1 = tileHostile.x;
				y1 = tileHostile.y;
				grid.attackUnit(x,y, x1, y1);
				tileSelf.attackLeft = false;
			}
			
			// Make a random move if not possible to attack
			else if (tileSelf.legalMoves().size() > 0){
				// Randomly pick one of the legal moves to be made from (x,y)
				legalMoves = tileSelf.legalMoves();
				toTile = legalMoves.get(rand.nextInt(legalMoves.size()));
				x1 = toTile.x;
				y1 = toTile.y;
				
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
				Thread.sleep(500);
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
			for (Tile unitTile : grid.humans){
				unitTile.moveLeft = toBoolean;
				unitTile.attackLeft = toBoolean;
			}
		}
		else {
			for (Tile unitTile : grid.beasts){
				unitTile.moveLeft = toBoolean;
				unitTile.attackLeft = toBoolean;
			}
		}
		
	}
	/*
	 * This method makes intelligent moves
	 */
	public void playIntelligent() {
		// Create a list of all friendlies and enemies
		ArrayList<Tile> friendlies = new ArrayList<Tile>(grid.beasts);
		ArrayList<Tile> hostiles = new ArrayList<Tile>(grid.humans);
		if (team.equals("Humans")) {
			friendlies = new ArrayList<Tile>(grid.humans);
			hostiles = new ArrayList<Tile>(grid.beasts);
		}
		
		String closestHostile;
		// Loop over all friendly units
		for (Tile ownTile : friendlies) {
			// Loop over all legal moves for every friendly unit
			for (Tile newTile : ownTile.legalMoves()) {
//				closestHostile = ownTile.calcClosestHostiles(hostiles).get(1);
				
			}
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}