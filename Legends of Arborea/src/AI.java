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
	Random rand = new Random();
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
		ArrayList<Tile> legalMoves;
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
			
			// Attack a hostile unit if possible		
			hostiles = tileSelf.surroundingHostiles();
			if (!hostiles.isEmpty()) {
				tileHostile = hostiles.get(rand.nextInt(hostiles.size()));
				grid.attackUnit(tileSelf, tileHostile);
				tileSelf.attackLeft = false;
			}
			
			// Make a random move if not possible to attack
			else if (tileSelf.legalMoves().size() > 0) {
				// Randomly pick one of the legal moves to be made from (x,y)
				legalMoves = tileSelf.legalMoves();
				toTile = legalMoves.get(rand.nextInt(legalMoves.size()));
				
				// Move to the position
				grid.moveUnit(tileSelf,tileHostile);	
				tileSelf.moveLeft = false;
				if (tileSelf.attackLeft) {
					toTile.attackLeft = true;
				}
				tileSelf.attackLeft = false;
				toTile.moveLeft = false;
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
			for (Tile unitTile : grid.humans) {
				unitTile.moveLeft = toBoolean;
				unitTile.attackLeft = toBoolean;
			}
		}
		else {
			for (Tile unitTile : grid.beasts) {
				unitTile.moveLeft = toBoolean;
				unitTile.attackLeft = toBoolean;
			}
		}
		
	}
	
	
	/*
	 * This method makes intelligent moves
	 * DINGEN DIE MISSCHIEN BETER KUNNEN:
	 * 	- In plaats van per unit en zet doen, eerst voor alle units alle zetten evalueren, dan de beste zet doen en na elke zet opnieuw alle zetten evalueren en de beste zet doen.
	 */
	public void playIntelligent() {
		// Create a list of all friendlies and enemies
		ArrayList<Tile> allFriendlies = new ArrayList<Tile>(grid.beasts);
		ArrayList<Tile> allHostiles = new ArrayList<Tile>(grid.humans);
		ArrayList<Tile> surroundingHostiles;
		ArrayList<Tile> legalMoves;
		ArrayList<Tile> closestHostiles;
		if (team.equals("Humans")) {
			allFriendlies = new ArrayList<Tile>(grid.humans);
			allHostiles = new ArrayList<Tile>(grid.beasts);
		}
		else {
			System.out.println("beaststurn");
		}
		
		Tile targetHostile;
		Tile bestMove;
		// Loop over all friendly units
		for (Tile unitTile : allFriendlies) {
			// Pause for 0.1 seconds and give the unit about to move a color
			unitTile.attackLeft = true;
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				System.err.println(e);
			}
			// Choose tactics
			surroundingHostiles = unitTile.surroundingHostiles();
			String tactic = chooseTactic(unitTile, surroundingHostiles);
			
			switch (tactic) {
			case "move":
				// Check if there are any legal moves for the current unit
				legalMoves = unitTile.legalMoves();
				if (!legalMoves.isEmpty()) {
					bestMove = legalMoves.get(0);
					
					// Loop over all legal moves for the current unit
					boolean move = false;
					for (Tile legalMove : legalMoves) {
						// Get the list of closest hostiles
						closestHostiles = legalMove.getClosestHostiles(allHostiles);
						targetHostile = closestHostiles.get(0);
						
						// Pick the target based on which one has the lowest buffer and health
						for (Tile closeHostile : closestHostiles) {
							if (closeHostile.buffer < targetHostile.buffer) {
								targetHostile = closeHostile;
							}
							else if (closeHostile.buffer == targetHostile.buffer) {
								if (closeHostile.unit.hitPoints < targetHostile.unit.hitPoints) {
									targetHostile = closeHostile;
								}
							}
						}
						
						// TODO ergens gaat het mis en soms gaan ze achteruit, terwijl je wil dat ze of vooruit gaan, of afstand hetzelfde houden en buffer vergroten
						//TODO even kijken welke hostile er nou getarget wordt
						
						int distanceBeforeMove = unitTile.distanceTo(targetHostile);
						int distanceAfterMove = legalMove.distanceTo(targetHostile);
						int distanceBestMove = bestMove.distanceTo(targetHostile);
						System.out.println("This is a move for unit at " + unitTile.key);
						// If a move brings you closer then without moving, and remains the same or brings you 
						// closer then the current best move, set move to true
						if (distanceAfterMove < distanceBeforeMove && distanceAfterMove <= distanceBestMove) {
							System.out.println("Distance(after, before) smaller, distance(after,best) smaller/equal");
							int bufferAfterMove = legalMove.buffer;
							int bufferBestMove = bestMove.buffer;
							
							// If the buffer increases or stays the same with a move, make it the best current move
							if (bufferAfterMove >= bufferBestMove) {
								System.out.println("Buffer increases or stays the same");
								bestMove = legalMove;
							}
							move = true;
						}
						
						// If a move does not bring you closer then you were, but does gain you more buffer
						else if (distanceAfterMove == distanceBeforeMove) {
							int bufferBeforeMove = unitTile.buffer;
							int bufferAfterMove = legalMove.buffer;
							int bufferBestMove = bestMove.buffer;
							if (bufferAfterMove > bufferBestMove && bufferAfterMove > bufferBeforeMove) {
								System.out.println("Distance(after,before) stays the same, buffer increases");
								bestMove = legalMove;
								move = true;
							}
						}
					}
					// TODO check of de weg niet geblokkeerd wordt, oftewel kijk wat de kortste weg is zonder de bezette tiles als possible paths mee te tellen
					// TODO Als de afstand tot een hostile niet kleiner wordt door een move, dan niet moven of zorgen dat je buffer vergroot GEDAAN
					// TODO Als je kunt kiezen tussen een x aantal moves waarbij de afstand gelijk afneemt, neem dan degene die je buffer maximaliseerd GEDAAN
					
					// Do the best move
					if (move == true) {
						grid.moveUnit(unitTile, bestMove);
					}
				}
				
				// If there are no legal moves to be done, break.
				break;
			
			// Attack the hostile with the lowest health
			case "attack":
				if (!surroundingHostiles.isEmpty()) {
					tileHostile = surroundingHostiles.get(rand.nextInt(surroundingHostiles.size()));
					grid.attackUnit(unitTile, tileHostile);
				}
				break;
			
			case "reinforce":
				

			}
			
			unitTile.attackLeft = false;
		}
	}
	
	/*
	 * Picks out the most fruitful tactic for a give tile/unit combination (here comes the real AI)
	 */
	public String chooseTactic(Tile ownTile, ArrayList<Tile> surroundingHostiles) {
		String tactic = "move";
		if (!surroundingHostiles.isEmpty()) {
			tactic = "attack";
		}
		return tactic;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}