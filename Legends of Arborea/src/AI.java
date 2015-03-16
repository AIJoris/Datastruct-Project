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
	boolean startFormation;
	
	/*
	 * Constructor
	 */
	public AI(Grid newGrid, String newPlayerTeam) {
		grid = newGrid;
		team = newPlayerTeam;
		startFormation = true;
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
				Thread.sleep(0);
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
		// Create a list of all friendlies and enemies
		ArrayList<Tile> allFriendlies = grid.humans;
		ArrayList<Tile> allHostiles = grid.beasts;
		if (team.equals("Beasts")) {
			allFriendlies = grid.beasts;
			allHostiles = grid.humans;
			// First move for beasts
//			if (startFormation == true) {
//				try {
//					Thread.sleep(500);
//				}
//				catch (InterruptedException e) {
//					System.err.println(e);
//				}
//				grid.moveUnit(grid.getTile(-3,-1), grid.getTile(-2, -2));
//				grid.moveUnit(grid.getTile(-3,0), grid.getTile(-2, 0));
//				grid.moveUnit(grid.getTile(-3,1), grid.getTile(-2, 1));
//				grid.moveUnit(grid.getTile(-3,2), grid.getTile(-2, 2));
//				grid.moveUnit(grid.getTile(-3,3), grid.getTile(-2, 3));
//				grid.moveUnit(grid.getTile(-4,4), grid.getTile(-3, 3));	
//				grid.moveUnit(grid.getTile(-4,1), grid.getTile(-3, 0));	
//				
//				startFormation = false;
//				return;
//			}
		}
		
		// Get a ranked list of all possible moves for every unit
		resetTurnsLeft(true);
		ArrayList<RankedMove> rankedMoves = evaluateMoves(allFriendlies, allHostiles);
		while (!rankedMoves.isEmpty()) {
			// Pick the highest ranked move
			int idx = rand.nextInt(rankedMoves.size());
			double highestRank = rankedMoves.get(idx).rank;
			RankedMove highestRankedMove = rankedMoves.get(idx);
			for (RankedMove rankedMove : rankedMoves) {
				if (rankedMove.rank > highestRank) {
					highestRankedMove = rankedMove;
					highestRank = rankedMove.rank;
				}
				
			}
			
			//pause
			try {
				Thread.sleep(0);
			}
			catch (InterruptedException e) {
				System.err.println(e);
			}
			
			// Do the highest ranked move
			if (highestRankedMove.rank < 1) {
				break;
			}
			grid.moveUnit(highestRankedMove.startTile, highestRankedMove.goalTile);
			highestRankedMove.startTile.attackLeft = false;
			highestRankedMove.startTile.moveLeft = false;
			highestRankedMove.goalTile.moveLeft = false;
			highestRankedMove.goalTile.attackLeft = true;
			
			// Get a ranked list of all possible moves for every unit
			rankedMoves = evaluateMoves(allFriendlies, allHostiles);
		}
		
		ArrayList<Tile> surroundingHostiles;
		Tile targetHostile;
		
		// After moving all units, attack with all units
		for (Tile unitTile : allFriendlies) {
			surroundingHostiles = unitTile.surroundingHostiles();
			// Attack the hostile with the lowest health
			if (!surroundingHostiles.isEmpty()) {
				targetHostile = surroundingHostiles.get(rand.nextInt(surroundingHostiles.size()));
				for (Tile surroundingHostile : surroundingHostiles) {
					if (surroundingHostile.getBuffer() < targetHostile.getBuffer()) {
						targetHostile = surroundingHostile;
					}
					else if (surroundingHostile.getBuffer() == targetHostile.getBuffer()) {
						if (surroundingHostile.unit.hitPoints < targetHostile.unit.hitPoints) {
							targetHostile = surroundingHostile;
						}
					}
				}
				grid.attackUnit(unitTile, targetHostile);
			}
		}
		
		resetTurnsLeft(false);
	}
	
	
	private ArrayList<RankedMove> evaluateMoves(ArrayList<Tile> allFriendlies, ArrayList<Tile> allHostiles) {
		ArrayList<Tile> legalMoves;
		Tile bestMove;
		ArrayList<Tile> closestHostiles;
		Tile targetHostile;
		ArrayList<RankedMove> rankedMoves = new ArrayList<RankedMove>();
		
		// Loop over all friendly units
		for (Tile unitTile : allFriendlies) {
			// Check if there are any legal moves for the current unit
			legalMoves = unitTile.legalMoves();
			if (!legalMoves.isEmpty() && unitTile.moveLeft) {
				bestMove = legalMoves.get(rand.nextInt(legalMoves.size()));
				
				// Get the list of closest hostiles
				closestHostiles = unitTile.getClosestHostiles(allHostiles);
				targetHostile = closestHostiles.get(rand.nextInt(closestHostiles.size()));
				
				// Pick the target based on which one has the lowest buffer and health
				for (Tile closeHostile : closestHostiles) {
					if (closeHostile.getBuffer() < targetHostile.getBuffer()) {
						targetHostile = closeHostile;
					}
					else if (closeHostile.getBuffer() == targetHostile.getBuffer()) {
						if (closeHostile.unit.hitPoints < targetHostile.unit.hitPoints) {
							targetHostile = closeHostile;
						}
					}
				}
				
				// Loop over all legal moves for the current unit
				int distanceBeforeMove = unitTile.distanceTo(targetHostile);
				int bufferBeforeMove = unitTile.getBuffer();
				for (Tile legalMove : legalMoves) {					
					
					int distanceAfterMove = legalMove.distanceTo(targetHostile);
					int distanceBestMove = bestMove.distanceTo(targetHostile);					
					// If you can't move closer to a hostile, but he is more than 1 tile away, move anyway
					if (distanceBeforeMove > 1 && distanceBeforeMove < 4) {
						if (distanceAfterMove <= distanceBestMove) {
							bestMove = legalMove;
						}
					}
					
					// If a move brings you closer then without moving, and remains the same or brings you 
					// closer then the current best move, set move to true
					if (distanceAfterMove < distanceBeforeMove) {
						if (distanceAfterMove <= distanceBestMove) {					
							bestMove = legalMove;
						}
					}
					
					// If a move does not bring you closer then you were, but does gain you more buffer
					else if (distanceAfterMove == distanceBeforeMove) {
						int bufferAfterMove = legalMove.getBuffer();
						int bufferBestMove = bestMove.getBuffer();
						if (bufferAfterMove > bufferBestMove && bufferAfterMove > bufferBeforeMove) {
							bestMove = legalMove;
						}
					}
				}
				
				// rank the best move
				int distanceBestMove = bestMove.distanceTo(targetHostile);
				int bufferBestMove = bestMove.getBuffer();
				int rankDis = 0;
				
				// If the distance remains the same, add 1 to rank
				if ((distanceBeforeMove - distanceBestMove) == 0) {
					rankDis = 2;
				}
				
				// If the distance decreases, add 2 to the rank
				else if ((distanceBeforeMove - distanceBestMove) > 0) {
					rankDis = 3;
				}
				
				else if (distanceBeforeMove > 1 && distanceBeforeMove < 4) {
					rankDis = 2;
				}
				
				// Increase of decrease the rank depending on the buffer
				double rankBuffer = (0.1 * (bufferBestMove - bufferBeforeMove));
						
				// Add all gained buffer points to the rank multiplied by 0.1
				double rank =  rankDis + rankBuffer;
				RankedMove rankedMove = new RankedMove(unitTile, bestMove, rank);
				rankedMoves.add(rankedMove);	
				//System.out.println("From " + rankedMove.startTile.key + " to " + rankedMove.goalTile.key + ". Rankdis: " + rankDis + ". RankBuffer: " + rankBuffer);
			}
		}
		return rankedMoves;
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
		ArrayList<Tile> allFriendlies = grid.humans;
		ArrayList<Tile> allHostiles = grid.beasts;
		if (team.equals("Beasts")) {
			allFriendlies = grid.beasts;
			allHostiles = grid.humans;
			// First move for beasts
			if (startFormation == true) {
				grid.moveUnit(grid.getTile(-3,0), grid.getTile(-4, 0));
				grid.moveUnit(grid.getTile(-2,-1), grid.getTile(-3, 0));
				grid.moveUnit(grid.getTile(-3,3), grid.getTile(-4, 3));
				grid.moveUnit(grid.getTile(-4,3), grid.getTile(-4, 2));
				grid.moveUnit(grid.getTile(-3,4), grid.getTile(-4, 3));
				grid.moveUnit(grid.getTile(-2,4), grid.getTile(-3, 4));	
				startFormation = false;
				return;
			}
		}
		
		Tile targetHostile;
		Tile bestMove;
		// Loop over all friendly units
		for (Tile unitTile : allFriendlies) {
			if (allHostiles.isEmpty()) {
				break;
			}
			
			// Check if there are any legal moves for the current unit
			boolean move = false;
			legalMoves = unitTile.legalMoves();
			if (!legalMoves.isEmpty()) {
				bestMove = legalMoves.get(rand.nextInt(legalMoves.size()));
				
				// Get the list of closest hostiles
				closestHostiles = unitTile.getClosestHostiles(allHostiles);
				targetHostile = closestHostiles.get(rand.nextInt(closestHostiles.size()));
				
				// Pick the target based on which one has the lowest buffer and health
				for (Tile closeHostile : closestHostiles) {
					if (closeHostile.getBuffer() < targetHostile.getBuffer()) {
						targetHostile = closeHostile;
					}
					else if (closeHostile.getBuffer() == targetHostile.getBuffer()) {
						if (closeHostile.unit.hitPoints < targetHostile.unit.hitPoints) {
							targetHostile = closeHostile;
						}
					}
				}
				
				// Loop over all legal moves for the current unit
				int distanceBeforeMove = unitTile.distanceTo(targetHostile);
				int bufferBeforeMove = unitTile.getBuffer();
				for (Tile legalMove : legalMoves) {
					// Pause for 0.1 seconds and give the unit about to move a color
					unitTile.attackLeft = true;
					legalMove.moveLeft = true;
					targetHostile.attackLeft = true;
					
					// TODO Units moeten om hun eigen manschappen lopen als ze niet door kunnen lopen naar hun targer en geen bonus buffer geven aan een unit die wel kan aanvallen
					int distanceAfterMove = legalMove.distanceTo(targetHostile);
					int distanceBestMove = bestMove.distanceTo(targetHostile);
					
					// If you can't move closer to a hostile, but he is more then 2 tiles away, move anyway
					if (distanceBeforeMove > 1) {
						if (distanceAfterMove <= distanceBestMove) {
							bestMove = legalMove;
							move = true;
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
						
						int bufferAfterMove = legalMove.getBuffer();
						int bufferBestMove = bestMove.getBuffer();
						if (bufferAfterMove > bufferBestMove && bufferAfterMove > bufferBeforeMove) {
							bestMove = legalMove;
							move = true;
						}
					}
					
					try {
						Thread.sleep(0);
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
					if (surroundingHostile.getBuffer() < targetHostile.getBuffer()) {
						targetHostile = surroundingHostile;
					}
					else if (surroundingHostile.getBuffer() == targetHostile.getBuffer()) {
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