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
	ArrayList<Unit> friendlies;
	String team;
	boolean endTurn;
	
	/*
	 * Constructor initializes grid and mouseHandler
	 */
	public HumanPlayer(Grid newGrid, MouseHandler newMouseHandler) {
		grid = newGrid;
		mouseHandler = newMouseHandler;
	}

	/*
	 * Converts coordinates to string
	 */
	public String toKey(int x, int y) {
		return new Integer(x).toString() + new Integer(y).toString();
	}
	
	public void play() {
		team = grid.team;
		// Check the team and decide who are friendlies
		if (team.equals("Humans")) {
			friendlies = new ArrayList<Unit>(grid.humans);
		}
		else {
			friendlies = new ArrayList<Unit>(grid.beasts);
		}
		
		// For each unit, set moveLeft and attackLeft to true
		resetTurnsLeft(true);
		
		// Loop over the amount of units of the player's team
		endTurn = false;
		playLoop();	
		resetTurnsLeft(false);
		endTurn = false;
	}
	
	private void playLoop() {
		while (!endTurn) {
			// Select the start and end tile
			selectTiles();
			if (endTurn){
				break;
			}
			
			Unit unitSelf = tileSelf.unit;
			// If you pick a unit a unit that has no moves left, pick another one.
			while (!unitSelf.attackLeft && !unitSelf.moveLeft) {
				System.out.println("You have already used this unit");
				grid.message = "used";
				selectTiles();
				if(endTurn){
					break;
				}
			}
			
			// move unit
			if (goalTile.unit == null) {
				move();
				// When a tile has no moves and attacks left, remove it from friendlies
				if (unitSelf.attackLeft == false && unitSelf.moveLeft == false) {
					friendlies.remove(unitSelf);
				}
				playLoop();
				break;
			}
			// attack
			else {
				attack();
				// When a tile has no moves and attacks left, remove it from friendlies
				if (unitSelf.attackLeft == false && unitSelf.moveLeft == false) {
					friendlies.remove(unitSelf);
				}
				playLoop();
			}
		}
	}
	
	/*
	 * This method allows the human player to select a friendly unit
	 * and a goal tile
	 */
	private void selectTiles() {
		// Select a friendly unit
		selectFriendlyUnit();
		
		// Select the goal tile
		boolean goalTileSelected = selectGoalTile();
		while (!goalTileSelected) {
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
			pause(10);
			counter++;
			if (counter > 50) {
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
		mouseHandler.currentTile = null;
		int counter = 0;
		
		// Wait for click
		while (mouseHandler.currentTile == null) {
			pause(10);
			counter++;
			if (counter > 50) {
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
	
	
	/*
	 * This method checks if the player wants to move, and acts accordingly
	 */
	private void move() {
		// Check if the tile has a move left, and if so move
		if (tileSelf.unit.moveLeft) {
			// The move has to be legal
			if (tileSelf.isLegal(goalTile)) {
				System.out.println("Unit on tile " + tileSelf.key + "to tile " + goalTile.key);
				grid.moveUnit(tileSelf, goalTile);
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
		if (grid.attackIsPossible(tileSelf.unit, goalTile.unit)) {
			if (tileSelf.unit.attackLeft) {
				grid.attackUnit(tileSelf.unit, goalTile.unit);
				tileSelf.unit.attackLeft = false;
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
	private void resetTurnsLeft(Boolean toBoolean) {
		// If you are playing with the humans, reset the humans
		if (team.equals("Humans")) {
			for (Unit unit : grid.humans){
				unit.moveLeft = toBoolean;
				unit.attackLeft = toBoolean;
			}
		}
		
		// If you are playing with the beasts, reset the beasts
		else {
			for (Unit unit : grid.beasts){
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
