import java.util.ArrayList;
import java.util.Random;

/*
 * This class implements a computer driven players
 */
public class AI {
	String positionSelf;
	Tile toTile;
	ArrayList<Unit> hostiles;
	Random rand = new Random();
	Grid grid;
	String team;
	boolean startFormation;
	
	/*
	 * Constructor: Initializes the to be used grid and team
	 */
	public AI(Grid newGrid, String newPlayerTeam) {
		grid = newGrid;
		team = newPlayerTeam;
		startFormation = true;
	}
	
	/*
	 * This method makes a move using the multi agent AI. It loops over all units and makes the best move possible based
	 * on the closest hostile with the lowest health and buffer. A good move is defined by the decrease of distance to the
	 * hostile, and an increase of buffer.
	 */
	public void playMultiAgent() {
		ArrayList<Unit> surroundingHostiles;
		ArrayList<Tile> legalMoves;
		ArrayList<Unit> closestHostiles;
		
		// Create a list of all friendlies and enemies
		ArrayList<Unit> allFriendlies = grid.humans;
		ArrayList<Unit> allHostiles = grid.beasts;
		if (team.equals("Beasts")) {
			allFriendlies = grid.beasts;
			allHostiles = grid.humans;
			// First move for beasts is hardcoded to get a good starting formation (the humans starting formation is good already)
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
		
		Unit targetHostile;
		Tile bestMove;
		// Loop over all friendly units
		for (Unit unit : allFriendlies) {
			if (allHostiles.isEmpty()) {
				break;
			}
			
			// Check if there are any legal moves for the current unit
			boolean move = false;
			legalMoves = unit.tile.legalMoves();
			if (!legalMoves.isEmpty()) {
				// Initialize best move randomly
				bestMove = legalMoves.get(rand.nextInt(legalMoves.size()));
				
				// Get the list of closest hostiles
				closestHostiles = unit.tile.getClosestHostiles(allHostiles);
				targetHostile = closestHostiles.get(rand.nextInt(closestHostiles.size()));
				
				// Pick the target based on which one has the lowest buffer and health
				for (Unit closeHostile : closestHostiles) {
					if (closeHostile.getBuffer() < targetHostile.getBuffer()) {
						targetHostile = closeHostile;
					}
					else if (closeHostile.getBuffer() == targetHostile.getBuffer()) {
						if (closeHostile.hitPoints < targetHostile.hitPoints) {
							targetHostile = closeHostile;
						}
					}
				}
				
				// Loop over all legal moves for the current unit
				int distanceBeforeMove = unit.tile.distanceTo(targetHostile.tile);
				int bufferBeforeMove = unit.getBuffer();
				for (Tile legalMove : legalMoves) {
					// Pause for 0.1 seconds and give the unit about to move a color
					unit.tile.searching = true;
					legalMove.option = true;
					targetHostile.tile.target = true;
					
					int distanceAfterMove = legalMove.distanceTo(targetHostile.tile);
					int distanceBestMove = bestMove.distanceTo(targetHostile.tile);
					
					// If you can't move closer to a hostile, but he is more than 2 tiles away, move anyway (prevents line formations)
					if (distanceBeforeMove > 1) {
						if (distanceAfterMove <= distanceBestMove) {
							bestMove = legalMove;
							move = true;
						}
					}
					
					// If a move brings you closer and remains the same or brings you 
					// closer than the current best move, set move to true and update best move
					if (distanceAfterMove < distanceBeforeMove) {
						if (distanceAfterMove <= distanceBestMove) {					
							bestMove = legalMove;
							move = true;
						}
					}
					
					// If a move does not bring you closer than you were, but does gain you more buffer or the
					// buffer remains the same move anyway (by always moving even when your position does not
					// get better you make space for other units to come help)
					else if (distanceAfterMove == distanceBeforeMove && distanceAfterMove <= distanceBestMove) {
						int bufferAfterMove = legalMove.getBuffer(team);
						int bufferBestMove = bestMove.getBuffer(team);
						if (bufferAfterMove >= bufferBestMove && bufferAfterMove >= bufferBeforeMove) {
							bestMove = legalMove;
							move = true;
						}
					}
					pause(300);
					legalMove.option = false;
					targetHostile.tile.target = false;
				}
				
				// Do the best move
				unit.tile.searching = false;
				if (move == true) {
					grid.moveUnit(unit.tile, bestMove);
				}
				
			}
			
			surroundingHostiles = unit.surroundingHostiles();
			// Attack the hostile with the lowest health
			if (!surroundingHostiles.isEmpty()) {
				targetHostile = surroundingHostiles.get(rand.nextInt(surroundingHostiles.size()));
				// Loop over surrounding hostiles
				for (Unit surroundingHostile : surroundingHostiles) {
					if (surroundingHostile.getBuffer() < targetHostile.getBuffer()) {
						targetHostile = surroundingHostile;
					}
					else if (surroundingHostile.getBuffer() == targetHostile.getBuffer()) {
						if (surroundingHostile.hitPoints < targetHostile.hitPoints) {
							targetHostile = surroundingHostile;
						}
					}
				}
				grid.attackUnit(unit, targetHostile);
			}
			
		}
	}
	
	/*
	 * This method extends the multi agent approach calculating all moves in advance and choosing the highest
	 * ranked move.
	 */
	public void playMultiAdvanced() {
		// Create a list of all friendlies and enemies
		ArrayList<Unit> allFriendlies = grid.humans;
		ArrayList<Unit> allHostiles = grid.beasts;
		
		if (team.equals("Beasts")) {
			allFriendlies = grid.beasts;
			allHostiles = grid.humans;
			// First move for beasts is hardcoded to get a good starting formation (the humans starting formation is good already)
			if (startFormation == true) {
				pause(500);
				grid.moveUnit(grid.getTile(-3,-1), grid.getTile(-2, -2));
				pause(300);
				grid.moveUnit(grid.getTile(-3,0), grid.getTile(-2, 0));
				pause(300);
				grid.moveUnit(grid.getTile(-3,1), grid.getTile(-2, 1));
				pause(300);
				grid.moveUnit(grid.getTile(-3,2), grid.getTile(-2, 2));
				pause(300);
				grid.moveUnit(grid.getTile(-3,3), grid.getTile(-2, 3));
				pause(300);
				grid.moveUnit(grid.getTile(-4,4), grid.getTile(-3, 3));	
				pause(300);
				grid.moveUnit(grid.getTile(-4,1), grid.getTile(-3, 0));	
				pause(300);
				
				startFormation = false;
				return;
			}
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
			pause(300);
			
			// Do the highest ranked move
			if (highestRankedMove.rank < 1) {
				break;
			}
			
			ArrayList<Unit> surroundingHostiles;
			Unit targetHostile;
			
			// Loop over all friendly units
			for (Unit unit : allFriendlies) {
				surroundingHostiles = unit.surroundingHostiles();
				// Attack the hostile with the lowest buffer and health
				if (!surroundingHostiles.isEmpty() && unit.attackLeft) {
					targetHostile = surroundingHostiles.get(rand.nextInt(surroundingHostiles.size()));
					for (Unit surroundingHostile : surroundingHostiles) {
						if (surroundingHostile.getBuffer() < targetHostile.getBuffer()) {
							targetHostile = surroundingHostile;
						}
						else if (surroundingHostile.getBuffer() == targetHostile.getBuffer()) {
							if (surroundingHostile.hitPoints < targetHostile.hitPoints) {
								targetHostile = surroundingHostile;
							}
						}
					}
					// Always attack first before moving any units if the buffer is significantly high
					if (unit.getBuffer() > 3){
						grid.attackUnit(unit, targetHostile);
					}
				}
			}
			grid.moveUnit(highestRankedMove.startTile, highestRankedMove.goalTile);
			
			
			// Update ranked list of all possible moves for every unit
			rankedMoves = evaluateMoves(allFriendlies, allHostiles);
		}
		
		ArrayList<Unit> surroundingHostiles;
		Unit targetHostile;
		
		// After moving all units, attack with all units (which have not attacked yet)
		for (Unit unit : allFriendlies) {
			surroundingHostiles = unit.surroundingHostiles();
			// Attack the hostile with the lowest health/buffer
			if (!surroundingHostiles.isEmpty() && unit.attackLeft) {
				targetHostile = surroundingHostiles.get(rand.nextInt(surroundingHostiles.size()));
				for (Unit surroundingHostile : surroundingHostiles) {
					if (surroundingHostile.getBuffer() < targetHostile.getBuffer()) {
						targetHostile = surroundingHostile;
					}
					else if (surroundingHostile.getBuffer() == targetHostile.getBuffer()) {
						if (surroundingHostile.hitPoints < targetHostile.hitPoints) {
							targetHostile = surroundingHostile;
						}
					}
				}
				grid.attackUnit(unit, targetHostile);
			}
		}
		resetTurnsLeft(false);
	}
	
	/*
	 * This method generates all legal moves for every unit, and returns an arrayList of ranked moves
	 */
	private ArrayList<RankedMove> evaluateMoves(ArrayList<Unit> allFriendlies, ArrayList<Unit> allHostiles) {
		ArrayList<Tile> legalMoves;
		Tile bestMove;
		ArrayList<Unit> closestHostiles;
		Unit targetHostile;
		ArrayList<RankedMove> rankedMoves = new ArrayList<RankedMove>();
		
		// Loop over all friendly units
		for (Unit unit : allFriendlies) {
			// Check if there are any legal moves for the current unit
			legalMoves = unit.tile.legalMoves();
			if (!legalMoves.isEmpty() && unit.moveLeft) {
				bestMove = legalMoves.get(rand.nextInt(legalMoves.size()));
				
				// Get the list of closest hostiles
				closestHostiles = unit.tile.getClosestHostiles(allHostiles);
				targetHostile = closestHostiles.get(rand.nextInt(closestHostiles.size()));
				
				// Pick the target based on which one has the lowest buffer and health
				for (Unit closeHostile : closestHostiles) {
					if (closeHostile.getBuffer() < targetHostile.getBuffer()) {
						targetHostile = closeHostile;
					}
					else if (closeHostile.getBuffer() == targetHostile.getBuffer()) {
						if (closeHostile.hitPoints < targetHostile.hitPoints) {
							targetHostile = closeHostile;
						}
					}
				}
				
				// Loop over all legal moves for the current unit
				int distanceBeforeMove = unit.tile.distanceTo(targetHostile.tile);
				int bufferBeforeMove = unit.getBuffer();
				for (Tile legalMove : legalMoves) {					
					
					int distanceAfterMove = legalMove.distanceTo(targetHostile.tile);
					int distanceBestMove = bestMove.distanceTo(targetHostile.tile);					
					// If you can't move closer to a hostile, but he is more than 1 tile away, move anyway
					if (distanceBeforeMove > 1 && distanceBeforeMove < 4) {
						if (distanceAfterMove <= distanceBestMove) {
							bestMove = legalMove;
						}
					}
					
					// If a move brings you closer than without moving, and remains the same or brings you 
					// closer then the current best move, set move to true
					if (distanceAfterMove < distanceBeforeMove) {
						if (distanceAfterMove <= distanceBestMove) {					
							bestMove = legalMove;
						}
					}
					
					// If a move does not bring you closer then you were, but does gain you more buffer
					else if (distanceAfterMove == distanceBeforeMove && distanceAfterMove <= distanceBestMove) {
						int bufferAfterMove = legalMove.getBuffer(team);
						int bufferBestMove = bestMove.getBuffer(team);
						
						if (bufferAfterMove >= bufferBestMove && bufferAfterMove >= bufferBeforeMove) {
							bestMove = legalMove;
						}
					}
					
				}
				
				// rank the best move
				int distanceBestMove = bestMove.distanceTo(targetHostile.tile);
				int bufferBestMove = bestMove.getBuffer(team);
				int rankDis = 0;
				
				// If the distance remains the same, add 1 to rank
				if ((distanceBeforeMove - distanceBestMove) == 0) {
					rankDis = 2;
				}
				
				// If the distance decreases, add 2 to the rank
				else if ((distanceBeforeMove - distanceBestMove) > 0) {
					rankDis = 3;
				}
				
				// If you are far away from the target always keep moving (except if you are very far away)
				else if (distanceBeforeMove > 1 && distanceBeforeMove < 4) {
					rankDis = 2;
				}
				
				// Increase or decrease the rank depending on the buffer
				double rankBuffer = (0.1 * (bufferBestMove - bufferBeforeMove));
						
				// Add all gained buffer points to the rank multiplied by 0.1
				double rank =  rankDis + rankBuffer;
				RankedMove rankedMove = new RankedMove(unit.tile, bestMove, rank);
				rankedMoves.add(rankedMove);	
			}
		}
		return rankedMoves;
	}
	
	/*
	 * For all tiles with units, set turnsLeft to true
	 */
	private void resetTurnsLeft(Boolean toBoolean) {
		if (team.equals("Humans")) {
			for (Unit unit : grid.humans) {
				unit.moveLeft = toBoolean;
				unit.attackLeft = toBoolean;
			}
		}
		else {
			for (Unit unit : grid.beasts) {
				unit.moveLeft = toBoolean;
				unit.attackLeft = toBoolean;
			}
		}
		
	}
	
	/*
	 * This method pauses the game for an amount of ms
	 */
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		}
		catch (InterruptedException e) {
		}
	}
	
}