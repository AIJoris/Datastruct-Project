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
		resetTurnsLeft();
		// Loop over the amount of units of the player's team
		while (!friendlies.isEmpty() | endTurn == true) {
			// Select the start and end tile
			selectTiles();
			if (!tileSelf.attackLeft & !tileSelf.moveLeft) {
				System.out.println("You have already used this unit");
				grid.message = "used";
				selectTiles();
			}
			
			// If the goal tile contains a unit, attack that unit
			if (goalTile.unit != null) {
				if (grid.attackIsPossible(x, y, x1, y1) == true) {
					if (tileSelf.attackLeft) {
						grid.attackUnit(x, y, x1, y1);
						tileSelf.attackLeft = false;
					}
					else {
						grid.message = "attackLeft";
					}
								
				}
				else {
					selectTiles();
				}
				
			}
			if (tileSelf.moveLeft) {
				grid.moveUnit(x, y, x1, y1);
				goalTile.moveLeft = false;
				tileSelf.attackLeft = false;
				tileSelf.moveLeft = false;
			}
			else {
				grid.message = "moveLeft";
			}
			if (!tileSelf.attackLeft & !tileSelf.moveLeft) {
				friendlies.remove(toKey(x,y));
			}
		}	
	}
	
	/*
	 * For all tiles with units, set turnsLeft to true
	 */
	private void resetTurnsLeft() {
		if (team.equals("Humans")) {
			friendlies = new ArrayList<String>(grid.humans);
		}
		else {
			friendlies = new ArrayList<String>(grid.beasts);
		}
		for (String unitPosition : grid.humans){
			grid.gridMap.get(unitPosition).moveLeft = true;
			grid.gridMap.get(unitPosition).attackLeft = true;
		}
	}
	
	/*
	 * This method allows the human player to select a friendly unit
	 * and a goal tile
	 */
	private void selectTiles() {
		System.out.println("Select one of your units!");
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
		System.out.println("Select goal tile");
		mouseHandler.currentTile = null;
		int counter = 0;
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
