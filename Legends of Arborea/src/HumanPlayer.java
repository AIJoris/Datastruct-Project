import java.util.ArrayList;
import java.util.Random;


public class HumanPlayer {
	String positionSelf;
	Tile tileSelf;
	String goalPosition;
	Tile goalTile;
	Random rand = new Random();
	int x, y, x1, y1;	
	Grid grid;
	MouseHandler mouseHandler;
	ArrayList<String> friendlies;
	String team;
	boolean endTurn = false;
	
	/*
	 * Constructor
	 */
	public HumanPlayer(Grid newGrid, MouseHandler newMouseHandler) {
		grid = newGrid;
		mouseHandler = newMouseHandler;
		team = grid.team;
	}
	
	public String toKey(int x, int y) {
		return new Integer(x).toString() + new Integer(y).toString();
	}
	
	public void play() {
		// Check the team and decide who are friendlies
		if (team.equals("Humans")) {
			friendlies = new ArrayList<String>(grid.humans);
		}
		else {
			friendlies = new ArrayList<String>(grid.beasts);
		}
		
		// For each unit, set moveLeft and attackLeft to true
		resetTurnsLeft();
		
		// Loop over the amount of units of the player's team
		playLoop();	
		endTurn = false;
	}
	
	private void playLoop() {
		while (!friendlies.isEmpty() && endTurn == false) {
			// Select the start and end tile
			selectTiles();
			if(endTurn){
				break;
			}
			while (!tileSelf.attackLeft && !tileSelf.moveLeft) {
				System.out.println("You have already used this unit");
				grid.message = "used";
				selectTiles();
			}
			
			// move
			if (goalTile.unit == null) {
				move();
				// When a tile has no moves and attacks left, remove it from friendlies
				if (tileSelf.attackLeft == false && tileSelf.moveLeft == false) {
					friendlies.remove(toKey(x,y));
				}
				playLoop();
				break;
			}
			// attack
			else {
				attack();
				// When a tile has no moves and attacks left, remove it from friendlies
				if (tileSelf.attackLeft == false && tileSelf.moveLeft == false) {
					friendlies.remove(toKey(x,y));
				}
				playLoop();
			}
		}
	}
	
	/*
	 * This method checks if the player wants to move, and acts accordingly
	 */
	private void move() {
		// Check if the tile has a move left, and if so move
		if (tileSelf.moveLeft) {
			// The move has to be legal
			if (tileSelf.isLegal(goalTile)) {
				grid.moveUnit(x, y, x1, y1);
				goalTile.moveLeft = false;
				if (tileSelf.attackLeft) {
					goalTile.attackLeft = true;
				}
				tileSelf.attackLeft = false;
				tileSelf.moveLeft = false;
			}
		}
		// If the tile has no move left
		else {
			grid.message = "used";
		}
		mouseHandler.selectedTile = null;
	}
	
	/*
	 * This method checks if the player wants to attack, and acts accordingly
	 */
	private void attack() {
		// If the goal tile contains a unit, is possible and still has attack left, then attack
		if (grid.attackIsPossible(x, y, x1, y1)) {
			if (tileSelf.attackLeft) {
				grid.attackUnit(x, y, x1, y1);
				tileSelf.attackLeft = false;
				mouseHandler.selectedTile = null;
			}								
		}
		// If the attack is not possible, the player can again select a unit
		else {
			mouseHandler.selectedTile = null;
			playLoop();
		}	
		
	}
	
	/*
	 * For all tiles with units, set turnsLeft to true
	 */
	private void resetTurnsLeft() {
		// If you are playing with the humans, reset the humans
		if (team.equals("Humans")) {
			for (String unitPosition : grid.humans){
				grid.gridMap.get(unitPosition).moveLeft = true;
				grid.gridMap.get(unitPosition).attackLeft = true;
			}
		}
		
		// If you are playing with the beasts, reset the beasts
		else {
			for (String unitPosition : grid.beasts){
				grid.gridMap.get(unitPosition).moveLeft = true;
				grid.gridMap.get(unitPosition).attackLeft = true;
			}
		}
		
	}
	
	/*
	 * This method allows the human player to select a friendly unit
	 * and a goal tile
	 */
	private void selectTiles() {
//		System.out.println("Select one of your units!");
		// Select a friendly unit
		selectFriendlyUnit();
		
		// Select the goal tile
		boolean goalTileSelected = selectGoalTile();
		while (goalTileSelected == false) {
			goalTileSelected = selectGoalTile();
		}
	}
	/*
	 * This method allows the human player to select a friendly unit
	 */
	private void selectFriendlyUnit() {
		// Wait for a friendly unit to be selected
		mouseHandler.currentUnit = null;
		int counter = 0;
		while (mouseHandler.currentUnit == null || !mouseHandler.currentUnit.team.equals(team)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter++;
			if (counter > 10) {
				grid.message = null;
			}
			if (endTurn) {
				return;
			}
		}
		
		// Select friendly unit
		x = mouseHandler.currentTile.x;
		y = mouseHandler.currentTile.y;
		positionSelf = toKey(x,y);
		tileSelf = grid.gridMap.get(positionSelf);
	}
	
	/*
	 * This method allows the human player to select a goal tile,
	 * and if he/she again selects a friendly unit, the original friendly 
	 * gets replaced and another goal tile can be selected.
	 */
	private boolean selectGoalTile() {
//		System.out.println("Select goal tile");
		mouseHandler.currentTile = null;
		int counter = 0;
		
		// Wait for click
		while (mouseHandler.currentTile == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter++;
			if (counter > 10) {
				grid.message = null;
			}
			if (endTurn) {
				return true;
			}
		}
		// Check if a friendly unit is again selected, and if so, change it to the new unit
		if (mouseHandler.currentUnit != null) {
			if (mouseHandler.currentUnit.team.equals(team)) {
				x = mouseHandler.currentTile.x;
				y = mouseHandler.currentTile.y;
				positionSelf = toKey(x,y);
				tileSelf = grid.gridMap.get(positionSelf);
				return false;
			}
		}
		// Select a goal tile/unit
		x1 = mouseHandler.currentTile.x;
		y1 = mouseHandler.currentTile.y;
		goalPosition = toKey(x1,y1);
		goalTile = grid.gridMap.get(goalPosition);
		return true;
	}
	
}
