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
	Tile toTile;
	ArrayList<Unit> hostiles;
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
	
//	/*
//	 * Make a random move
//	 */
//	public void playRandom() {
//		ArrayList<Unit> humansTemp = new ArrayList<Unit>(grid.humans);
//		ArrayList<Unit> beastsTemp = new ArrayList<Unit>(grid.beasts);
//		ArrayList<Tile> legalMoves;
//		int index;
//
//		// Do 1 action with every unit
//		resetTurnsLeft(true);
//		while (!humansTemp.isEmpty() && !beastsTemp.isEmpty()) {
//			// Get a random friendly unit at a position
//			if (team.equals("Humans")) {
//				index = rand.nextInt(humansTemp.size());
//				tileSelf = humansTemp.get(index);
//				// Remove the index so this unit will not be chosen again
//				humansTemp.remove(index);
//			}
//			else if (team.equals("Beasts")) {
//				index = rand.nextInt(beastsTemp.size());
//				tileSelf = beastsTemp.get(index);
//				beastsTemp.remove(index);
//				// Remove the index so this unit will not be chosen again
//			}
//			
//			// Attack a hostile unit if possible		
//			hostiles = tileSelf.surroundingHostiles();
//			if (!hostiles.isEmpty()) {
//				tileHostile = hostiles.get(rand.nextInt(hostiles.size()));
//				grid.attackUnit(tileSelf, tileHostile);
//				tileSelf.attackLeft = false;
//			}
//			
//			// Make a random move if not possible to attack
//			else if (tileSelf.legalMoves().size() > 0) {
//				// Randomly pick one of the legal moves to be made from (x,y)
//				legalMoves = tileSelf.legalMoves();
//				toTile = legalMoves.get(rand.nextInt(legalMoves.size()));
//				
//				// Move to the position
//				grid.moveUnit(tileSelf,tileHostile);	
//				tileSelf.moveLeft = false;
//				if (tileSelf.attackLeft) {
//					toTile.attackLeft = true;
//				}
//				tileSelf.attackLeft = false;
//				toTile.moveLeft = false;
//			}
//			pause(0)
//			grid.message = null;
//		}
//		resetTurnsLeft(false);
//	}
	
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
	 * This method makes intelligent moves
	 * DINGEN DIE MISSCHIEN BETER KUNNEN:
	 * 	- In plaats van per unit en zet doen, eerst voor alle units alle zetten evalueren, dan de beste zet doen en na elke zet opnieuw alle zetten evalueren en de beste zet doen.
	 * 	- Wanneer eindigd de AI zijn beurt eigenlijk???
	 */
	public void playIntelligent() {
		// Create a list of all friendlies and enemies
		ArrayList<Unit> allFriendlies = grid.humans;
		ArrayList<Unit> allHostiles = grid.beasts;
		
		if (team.equals("Beasts")) {
			allFriendlies = grid.beasts;
			allHostiles = grid.humans;
			// First move is hardcoded
			if (startFormation == true) {
				pause(500);
				grid.moveUnit(grid.getTile(-3,-1), grid.getTile(-2, -2));
				grid.moveUnit(grid.getTile(-3,0), grid.getTile(-2, 0));
				grid.moveUnit(grid.getTile(-3,1), grid.getTile(-2, 1));
				grid.moveUnit(grid.getTile(-3,2), grid.getTile(-2, 2));
				grid.moveUnit(grid.getTile(-3,3), grid.getTile(-2, 3));
				grid.moveUnit(grid.getTile(-4,4), grid.getTile(-3, 3));	
				grid.moveUnit(grid.getTile(-4,1), grid.getTile(-3, 0));	
				
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
			
			// Loop over 
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
					
					if (unit.getBuffer() > 3){
						grid.attackUnit(unit, targetHostile);
					}
				}
			}
			
			grid.moveUnit(highestRankedMove.startTile, highestRankedMove.goalTile);
			
			
			// Get a ranked list of all possible moves for every unit
			rankedMoves = evaluateMoves(allFriendlies, allHostiles);
		}
		
		ArrayList<Unit> surroundingHostiles;
		Unit targetHostile;
		
		// After moving all units, attack with all units
		for (Unit unit : allFriendlies) {
			surroundingHostiles = unit.surroundingHostiles();
			// Attack the hostile with the lowest health
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
					
					// If a move brings you closer then without moving, and remains the same or brings you 
					// closer then the current best move, set move to true
					if (distanceAfterMove < distanceBeforeMove) {
						if (distanceAfterMove <= distanceBestMove) {					
							bestMove = legalMove;
						}
					}
					
					// If a move does not bring you closer then you were, but does gain you more buffer
					else if (distanceAfterMove == distanceBeforeMove) {
						//Add placeholders to calculate the buffer
						int bufferAfterMove = legalMove.getBuffer(team);
						int bufferBestMove = bestMove.getBuffer(team);
						
						if (bufferAfterMove > bufferBestMove && bufferAfterMove > bufferBeforeMove) {
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
				
				else if (distanceBeforeMove > 1 && distanceBeforeMove < 4) {
					rankDis = 2;
				}
				
				// Increase of decrease the rank depending on the buffer
				double rankBuffer = (0.1 * (bufferBestMove - bufferBeforeMove));
						
				// Add all gained buffer points to the rank multiplied by 0.1
				double rank =  rankDis + rankBuffer;
				RankedMove rankedMove = new RankedMove(unit.tile, bestMove, rank);
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
		ArrayList<Unit> surroundingHostiles;
		ArrayList<Tile> legalMoves;
		ArrayList<Unit> closestHostiles;
		
		// Create a list of all friendlies and enemies
		ArrayList<Unit> allFriendlies = grid.humans;
		ArrayList<Unit> allHostiles = grid.beasts;
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
					else if (distanceAfterMove == distanceBeforeMove && distanceAfterMove <= distanceBestMove) {
						// Add placeholders to calculate the buffer
						int bufferAfterMove = legalMove.unit.getBuffer();
						int bufferBestMove = bestMove.unit.getBuffer();
						
						if (bufferAfterMove >= bufferBestMove && bufferAfterMove >= bufferBeforeMove) {
							bestMove = legalMove;
							move = true;
						}
					}
					pause(0);
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
			//TODO attack in aparte functie zetten
			// Attack the hostile with the lowest health
			if (!surroundingHostiles.isEmpty()) {
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