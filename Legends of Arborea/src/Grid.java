import java.util.ArrayList;
import java.util.HashMap;

/* 
 * This class implements the hex-grid
 */
public class Grid {
	HashMap<String,Tile> grid;
	String key;
	String team;
	int skillAttacker;
	int skillDefender;
	double hitChance;
	ArrayList<String> beasts = new ArrayList<String>();
	ArrayList<String> humans = new ArrayList<String>();
	
	
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
		String[] goblins = {toKey(-4,1), toKey(-3,0), toKey(-3,1), toKey(-3,2), toKey(-3,3), toKey(-3,4), toKey(-2,4), toKey(-2,-1)};
		
		// Place all units on their tiles
		for (String coord : generals) {
			grid.get(coord).addUnit(new General());
			humans.add(coord);
		}
		for (String coord : swordsmen) {
			grid.get(coord).addUnit(new Swordsman());
			humans.add(coord);
		}
		for (String coord : orcs) {
			grid.get(coord).addUnit(new Orc());
			beasts.add(coord);
		}
		for (String coord : goblins) {
			grid.get(coord).addUnit(new Goblin());
			beasts.add(coord);
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
		try {
			Unit unit = grid.get(toKey(x, y)).unit;
			return unit;
		}
		catch (NullPointerException e){
			return null;
		}
		
	}
	
	/*
	 * This method removes a unit from the grid
	 */
	public void removeUnit(int x, int y) {
		grid.get(toKey(x,y)).removeUnit();
		
		// Remove the unit also from the lists of units
		if (team.equals("Humans")) {
			beasts.remove(toKey(x,y));
			return;
		}
		else if (team.equals("Beasts")) {
			humans.remove(toKey(x, y));
			return;
		}
	}
	
	/*
	 * Move a unit to a specified position
	 */
	public boolean moveUnit(int x, int y, int x1, int y1) {			
		// Move unit if the move is legal and the goal tile is not occupied
		if (isLegalMove(x, y, x1, y1) == true) {
			grid.get(toKey(x1,y1)).unit = grid.get(toKey(x,y)).unit;
			grid.get(toKey(x,y)).unit = null;	
			
			// Update the lists containing all the units
			if (team.equals("Humans")) {
				humans.set(humans.indexOf(toKey(x,y)), toKey(x1,y1));
			}
			else if (team.equals("Beasts")) {
				beasts.set(beasts.indexOf(toKey(x,y)), toKey(x1,y1));
			}
			return true;
		}
		// If the move isn't legal, return false
		System.out.println("This is not a legal move!");
		return false;
	}
	
	/*
	 * Attack a unit with another unit
	 */
	public boolean attackUnit(int x, int y , int x1, int y1) {
		// Check if it is possible to attack
		if (attackIsPossible(x,y,x1,y1) == false) {
			return false;
		}
		
		// Get weapon skills
		skillAttacker = getUnit(x,y).weaponSkill + getBuffer(x,y);
		skillDefender = getUnit(x1,y1).weaponSkill + getBuffer(x1,y1);
		hitChance = 1 / (1 + Math.exp(0.4 * (skillAttacker - skillDefender)));
		
		// Attack the defender
		if (Math.random() <= hitChance ) {
			getUnit(x1,y1).hitPoints -= 1;
			System.out.println("BOOM in the balls!");
			
			// Remove the unit if he died
			if (getUnit(x1, y1).hitPoints == 0) {
				removeUnit(x1,y1);
			}
			return true;
		}
		System.out.println("Ha, you missed!");
		return false;
	}
	
	/*
	 * This method implements a couple of checks explained above each check
	 * to make sure it is possible to attack the unit at (x1,y1) with unit (x,y)
	 */
	public boolean attackIsPossible(int x, int y, int x1, int y1) {
		// Check if the tiles exist
		if (getTile(x1, y1) == null || getTile(x,y) == null) {
			return false;
		}		
		
		// Check if the attacker exists and is friendly
		if (getUnit(x,y) == null || !getUnit(x,y).team.equals(team)) {
			System.out.println("There must be a (friendly) unit to attack with!");
			return false;
		}
		
		// Check if there is a unit to attack
		if (getUnit(x1,y1) == null) {
			System.out.println("Stop attacking air");
			return false;
		}
		
		// Check if the defender is friendly or hostile
		if (getUnit(x1,y1).team.equals(team)) {
			System.out.println("Friendly fire!");
			return false;
		}
		return true;
	}
	
	/*
	 * Convert the coordinate of a tile to a string, so it
	 * can be used as key to access a tile in the hashmap
	 */
	public String toKey(int x, int y) {
		return new Integer(x).toString() + new Integer(y).toString();
	}
	
	/*
	 * This method calculates if the move is legal
	 */
	public boolean isLegalMove(int x, int y, int x1, int y1) {	
		ArrayList<String> legalMoves = legalMoves(x,y);
		if (legalMoves.contains(toKey(x1,y1))) {
			return true;
		}
		return false;
	}
	
	/*
	 * This method checks if and how many friendly units 
	 * are present at tiles nearby, and calculates the buffer 
	 */
	public int getBuffer(int x, int y) {
		// Specify who are friendly and who are hostile
		Unit friendlyGeneralUnit;
		Unit friendlyInfantryUnit;
		Unit hostileGeneralUnit;
		Unit hostileInfantryUnit;
		if (team.equals("Humans")) {
			friendlyGeneralUnit = new General();
			friendlyInfantryUnit = new Swordsman();
			hostileGeneralUnit = new Orc();
			hostileInfantryUnit = new Goblin();
		}
		else {
			friendlyGeneralUnit = new Orc();
			friendlyInfantryUnit = new Goblin();
			hostileGeneralUnit = new General();
			hostileInfantryUnit = new Swordsman();
		}
		
		// Lists with adjacent tiles
		int[] xMoves = {x-1, x-1, x, x, x+1, x+1};
		int[] yMoves = {y, y+1, y-1, y+1, y-1, y};
		
		Unit unit;
		int buffer = 0;
		// Loop over adjacent tiles
		for (int i = 0; i < 6; i++) {
			unit = getUnit(xMoves[i], yMoves[i]); 
			if (unit != null) {
				if (unit.name.equals(friendlyGeneralUnit.name)){
					buffer += 2;
				}
				else if (unit.name.equals(friendlyInfantryUnit.name)){
					buffer += 1;
				}
				else if (unit.name.equals(hostileGeneralUnit.name)) {
					buffer -= 2;
				}
				else if (unit.name.equals(hostileInfantryUnit.name)) {
					buffer -= 1;
				}
			}
		}
		return buffer;
	}

	/*
	 * This method returns all hostile forces around a position
	 */
	public ArrayList<String> allHostiles(int x, int y) {
		ArrayList<String> hostiles = new ArrayList<String>();
		
		// Lists with adjacent tiles
		int[] xMoves = {x-1, x-1, x, x, x+1, x+1};
		int[] yMoves = {y, y+1, y-1, y+1, y-1, y};
		Unit unit;
		
		// Loop over adjacent tiles to find hostile units
		for (int i = 0; i < 6; i++) {
			unit = getUnit(xMoves[i], yMoves[i]); 
			if (unit != null) {
				if (!unit.team.equals(team)) {
					hostiles.add(toKey(xMoves[i], yMoves[i]));
				}
			}
		}
		return hostiles;
	}
	
	/*
	 * This method calculates all possible legal moves from a position (x,y)
	 */
	public ArrayList<String> legalMoves(int x, int y) {	
		// Lists with adjacent tiles
		int[] xMoves = {x-1, x-1, x, x, x+1, x+1};
		int[] yMoves = {y, y+1, y-1, y+1, y-1, y};
		
		// Generate all adjacent tiles and check which ones make a legal move
		int x1;
		int y1;
		ArrayList<String> legalMoves = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			x1 = xMoves[i];
			y1 = yMoves[i];
			
			// Check if the tiles exist
			if (getTile(x1, y1) == null || getTile(x,y) == null) {
				continue;
			}
			
			// Check if there is a unit at the start position and no unit on the goal position
			if (getUnit(x,y) == null || getUnit(x1,y1) != null) {
				continue;
			}
			
			// The move is legal, so add it to the list
			legalMoves.add(toKey(x1,y1));
			
		}
		return legalMoves;
	}
}
