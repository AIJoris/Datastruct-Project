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
				Thread.sleep(1000);
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
	 * 	- Wanneer eindigd de AI zijn beurt eigenlijk???
	 */
	public void playIntelligent() {
		ArrayList<Tile> surroundingHostiles;
		ArrayList<Tile> legalMoves;
		ArrayList<Tile> closestHostiles;
		
		// Create a list of all friendlies and enemies
		ArrayList<Tile> allFriendlies = grid.beasts;//new ArrayList<Tile>(grid.beasts);
		ArrayList<Tile> allHostiles = grid.humans;//new ArrayList<Tile>(grid.humans);
		if (team.equals("Humans")) {
			allFriendlies = grid.humans;//new ArrayList<Tile>(grid.humans);
			allHostiles = grid.beasts;//new ArrayList<Tile>(grid.beasts);
		}
		
		Tile targetHostile;
		Tile bestMove;
		// Loop over all friendly units
		for (Tile unitTile : allFriendlies) {		
			// Check if there are any legal moves for the current unit
			legalMoves = unitTile.legalMoves();
			
			
			if (legalMoves.isEmpty()) {
				continue;
			}
			
			bestMove = legalMoves.get(0);
			
			// Get the list of closest hostiles
			closestHostiles = unitTile.getClosestHostiles(allHostiles);
			targetHostile = closestHostiles.get(0);
			System.out.println(closestHostiles);
			System.out.println(targetHostile.unit);
			
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
			
			// Loop over all legal moves for the current unit
			boolean move = false;
			int distanceBeforeMove = unitTile.distanceTo(targetHostile);
			int bufferBeforeMove = unitTile.buffer;
			for (Tile legalMove : legalMoves) {
				// Pause for 0.1 seconds and give the unit about to move a color
				unitTile.attackLeft = true;
				legalMove.moveLeft = true;
				targetHostile.attackLeft = true;
				
				// TODO Units moeten om hun eigen manschappen lopen als ze niet door kunnen lopen naar hun targer en geen bonus buffer geven aan een unit die wel kan aanvallen
				int distanceAfterMove = legalMove.distanceTo(targetHostile);
				int distanceBestMove = bestMove.distanceTo(targetHostile);
				System.out.println("This is a move for unit at " + unitTile.key);
				// If a move brings you closer then without moving, and remains the same or brings you 
				// closer then the current best move, set move to true
				if (distanceAfterMove < distanceBeforeMove) {
					if (distanceAfterMove <= distanceBestMove) {
						System.out.println("Distance(after, before) smaller, distance(after,best) smaller/equal");
						int bufferAfterMove = legalMove.buffer;
						int bufferBestMove = bestMove.buffer;
						
						// If the buffer increases or stays the same with a move, make it the best current move
//								if (bufferAfterMove < bufferBestMove) {
//									break;
//								}
						
						bestMove = legalMove;
						move = true;
						
					}
				}
				
				// If a move does not bring you closer then you were, but does gain you more buffer
				else if (distanceAfterMove == distanceBeforeMove) {
					
					int bufferAfterMove = legalMove.buffer;
					int bufferBestMove = bestMove.buffer;
					if (bufferAfterMove > bufferBestMove && bufferAfterMove > bufferBeforeMove) {
						System.out.println("Distance(after,before) stays the same, buffer increases");
						bestMove = legalMove;
						move = true;
					}
				}
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					System.err.println(e);
				}
			}
			// TODO check of de weg niet geblokkeerd wordt, oftewel kijk wat de kortste weg is zonder de bezette tiles als possible paths mee te tellen
			
			// Do the best move
			if (move == true) {
				;
			}
		
		// Attack the hostile with the lowest health
			if (!surroundingHostiles.isEmpty()) {
				targetHostile = surroundingHostiles.get(rand.nextInt(surroundingHostiles.size()));
				for (Tile surroundingHostile : surroundingHostiles) {
					if (surroundingHostile.buffer < targetHostile.buffer) {
						targetHostile = surroundingHostile;
					}
					else if (surroundingHostile.buffer == targetHostile.buffer) {
						if (surroundingHostile.unit.hitPoints < targetHostile.unit.hitPoints) {
							targetHostile = surroundingHostile;
						}
					}
				}
				grid.attackUnit(unitTile, targetHostile);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*
	 * This method makes intelligent moves
	 * DINGEN DIE MISSCHIEN BETER KUNNEN:
	 * 	- In plaats van per unit en zet doen, eerst voor alle units alle zetten evalueren, dan de beste zet doen en na elke zet opnieuw alle zetten evalueren en de beste zet doen.
	 * 	- Wanneer eindigd de AI zijn beurt eigenlijk???
	 */
	public void playMultiAgent() {
		ArrayList<Tile> surroundingHostiles;
		ArrayList<Tile> legalMoves;
		ArrayList<Tile> closestHostiles;
		
		// Create a list of all friendlies and enemies
		ArrayList<Tile> allFriendlies = grid.beasts;//new ArrayList<Tile>(grid.beasts);
		ArrayList<Tile> allHostiles = grid.humans;//new ArrayList<Tile>(grid.humans);
		if (team.equals("Humans")) {
			allFriendlies = grid.humans;//new ArrayList<Tile>(grid.humans);
			allHostiles = grid.beasts;//new ArrayList<Tile>(grid.beasts);
		}
		
		Tile targetHostile;
		Tile bestMove;
		// Loop over all friendly units
		for (Tile unitTile : allFriendlies) {
			// Check if there are any legal moves for the current unit
			boolean move = false;
			legalMoves = unitTile.legalMoves();
			if (!legalMoves.isEmpty()) {
				bestMove = legalMoves.get(0);
				
				// Get the list of closest hostiles
				closestHostiles = unitTile.getClosestHostiles(allHostiles);
				targetHostile = closestHostiles.get(0);
				System.out.println(closestHostiles);
				System.out.println(targetHostile.unit);
				
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
				
				// Loop over all legal moves for the current unit
				int distanceBeforeMove = unitTile.distanceTo(targetHostile);
				int bufferBeforeMove = unitTile.buffer;
				for (Tile legalMove : legalMoves) {
					// Pause for 0.1 seconds and give the unit about to move a color
					unitTile.attackLeft = true;
					legalMove.moveLeft = true;
					targetHostile.attackLeft = true;
					
					// TODO Units moeten om hun eigen manschappen lopen als ze niet door kunnen lopen naar hun targer en geen bonus buffer geven aan een unit die wel kan aanvallen
					int distanceAfterMove = legalMove.distanceTo(targetHostile);
					int distanceBestMove = bestMove.distanceTo(targetHostile);
					System.out.println("This is a move for unit at " + unitTile.key);
					
					// If you can't move closer to a hostile, but he is more then 2 tiles away, move anyway
					if (distanceBeforeMove >= 1) {
						if (distanceAfterMove < distanceBestMove) {
							bestMove = legalMove;
							move = true;
							System.out.println("Omsingel");
						}
					}
					
					// If a move brings you closer then without moving, and remains the same or brings you 
					// closer then the current best move, set move to true
					if (distanceAfterMove < distanceBeforeMove) {
						if (distanceAfterMove <= distanceBestMove) {					
							bestMove = legalMove;
							move = true;
						}
					}
					
					// If a move does not bring you closer then you were, but does gain you more buffer
					else if (distanceAfterMove == distanceBeforeMove) {
						
						int bufferAfterMove = legalMove.buffer;
						int bufferBestMove = bestMove.buffer;
						if (bufferAfterMove > bufferBestMove && bufferAfterMove > bufferBeforeMove) {
							System.out.println("Distance(after,before) stays the same, buffer increases");
							bestMove = legalMove;
							move = true;
						}
					}
					
					try {
						Thread.sleep(100);
					}
					catch (InterruptedException e) {
						System.err.println(e);
					}
					legalMove.moveLeft = false;
					targetHostile.attackLeft = false;
				}
				// TODO check of de weg niet geblokkeerd wordt, oftewel kijk wat de kortste weg is zonder de bezette tiles als possible paths mee te tellen
				
				// Do the best move
				unitTile.attackLeft = false;
				if (move == true) {
					grid.moveUnit(unitTile, bestMove);
					unitTile = bestMove;
				}
				
			}
			
			surroundingHostiles = unitTile.surroundingHostiles();
			// Attack the hostile with the lowest health
			if (!surroundingHostiles.isEmpty()) {
				targetHostile = surroundingHostiles.get(rand.nextInt(surroundingHostiles.size()));
				for (Tile surroundingHostile : surroundingHostiles) {
					if (surroundingHostile.buffer < targetHostile.buffer) {
						targetHostile = surroundingHostile;
					}
					else if (surroundingHostile.buffer == targetHostile.buffer) {
						if (surroundingHostile.unit.hitPoints < targetHostile.unit.hitPoints) {
							targetHostile = surroundingHostile;
						}
					}
				}
				grid.attackUnit(unitTile, targetHostile);
			
				

			}
			
		}
	}
	
	
}