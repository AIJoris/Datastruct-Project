import java.awt.Point;
import java.util.HashMap;

/* 
 * This class implements the hex-grid
 */
public class Grid {
	HashMap<String,Tile> grid;
	String key;
	
	/*
	 * Grid constructor which initializes the grid and 
	 * places the units according to the start position
	 */
	public Grid() {
		this.initializeGrid();
		this.placeUnits();
	}
	
	/* 
	 * Initialize the grid
	 */
	public void initializeGrid() {
		System.out.println("Initialize the grid");
				
		// Create a grid object
		grid = new HashMap<String, Tile>(100);
		
		// Fill the first half of the grid
		for (int x = -4; x <= 0; x++) {
			for (int y = -4 - x; y <= 4; y++) {
				grid.put(toKey(x,y), new Tile(x,y));
			}
		}
		
		// Fill the second half of the grid
		for (int x = 1; x <= 4; x++) {
			for (int y = -4; y <= 4 - x; y++) {
				grid.put(toKey(x,y), new Tile(x,y));
			}
		}
	}
	
	/* 
	 * Place all units on the grid in the right starting position
	 */
	public void placeUnits() {
		System.out.println("Create the startingposition on the grid");
		
		// Specify which tiles have which units at the starting position
		String[] generals = {toKey(4,-4), toKey(4,-1), toKey(3,1)};
		String[] swordsmen = {toKey(3,-4), toKey(3,-3), toKey(3,-2), toKey(3,-1), toKey(3,0), toKey(4,-3)};
		String[] orcs = {toKey(-4,4), toKey(-3,-1)};
		String[] goblins = {toKey(-4,1), toKey(-3,0), toKey(-3,1), toKey(-3,2), toKey(-3,3), toKey(-3,4), toKey(-2,4), toKey(-2,1)};
		
		// Place all units on their tiles
		for (String coord : generals) {
			grid.get(coord).addUnit(new General());
		}
		for (String coord : swordsmen) {
			grid.get(coord).addUnit(new Swordsman());
		}
		for (String coord : orcs) {
			grid.get(coord).addUnit(new Orc());
		}
		for (String coord : goblins) {
			grid.get(coord).addUnit(new Goblin());
		}
	}
	
	/*
	 * Get the tile at the specific position
	 */
	public Tile getTile(int x, int y) {
		key = toKey(x,y);
		return grid.get(key);
	}
	
	/*
	 * This method returns the unit currently at the specified position
	 */
	public Unit getUnit(int x, int y) {
		Unit unit = grid.get(toKey(x, y)).unit;
		return unit;
	}
	
	/*
	 * Move a unit to a specified position
	 */
	public boolean moveUnit(int x, int y, int x1, int y1) {
		// Check if there is a unit at the start and end coordinates
		if (grid.get(toKey(x,y)).unit == null || getTile(x1, y1) == null || grid.get(toKey(x1,y1)).unit != null) {
			System.out.println(grid.get(toKey(x1,y1)).unit);
			return false;
		}
		
		// Move unit if the move is legal
		if (legalMove(x, y, x1, y1) == true) {
			grid.get(toKey(x1,y1)).unit = grid.get(toKey(x,y)).unit;
			grid.get(toKey(x,y)).unit = null;	
			return true;
		}
		
		return false;
	}
	
	/*
	 * Attack a unit with another unit
	 */
	public boolean attackUnit(Object from, Object to) {
		System.out.println("Attack the unit at position 2 from position 1");
		return true;
	}
	
	
	/*
	 * Convert the coordinate of a tile to a string, so it
	 * can be used as key for the hashmap
	 */
	public String toKey(int x, int y) {
		return new Integer(x).toString() + new Integer(y).toString();
	}
	
	/*
	 * Check if a move is legal
	 */
	public boolean legalMove(int x, int y, int x1, int y1) {
		// Lists with adjacent tiles
		int[] xMoves = {x-1, x-1, x, x, x+1, x+1};
		int[] yMoves = {y, y+1, y-1, y+1, y-1, y};

		for (int i = 0; i < 6; i++) {
			if (xMoves[i] == x1 && yMoves[i] == y1) {
				return true;
			}
		}
		return false;
	}
	
}
