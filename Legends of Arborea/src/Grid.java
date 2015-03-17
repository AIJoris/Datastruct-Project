import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* 
 * This class implements the hex-grid data structure
 */
public class Grid {
	HashMap<String,Tile> gridMap;
	int nrTiles = 61;
	String key;
	String team;
	int skillAttacker;
	int skillDefender;
	double hitChance;
	ArrayList<Unit> beasts;
	ArrayList<Unit> humans;
	String message = null;
	
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
		gridMap = new HashMap<String, Tile>(100);
		
		// Fill the first half of the grid by looping over axial coordinates
		for (int x = -4; x <= 0; x++) {
			for (int y = -4 - x; y <= 4; y++) {
				gridMap.put(toKey(x,y), new Tile(x,y));
			}
		}
		
		// Fill the second half of the grid by looping over axial coordinates
		for (int x = 1; x <= 4; x++) {
			for (int y = -4; y <= 4 - x; y++) {
				gridMap.put(toKey(x,y), new Tile(x,y));
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
		
		// Keep a list of humans and beasts
		humans = new ArrayList<Unit>();
		beasts = new ArrayList<Unit>();
		
		Tile tempTile;
		Unit tempUnit;
		// Place all units on their tiles
		for (String coord : generals) {
			tempTile = gridMap.get(coord);
			tempUnit = new General(tempTile);
			tempTile.addUnit(tempUnit);
			humans.add(tempUnit);
		}
		for (String coord : swordsmen) {
			tempTile = gridMap.get(coord);
			tempUnit = new Swordsman(tempTile);
			tempTile.addUnit(tempUnit);
			humans.add(tempUnit);
		}
		for (String coord : orcs) {
			tempTile = gridMap.get(coord);
			tempUnit = new Orc(tempTile);
			tempTile.addUnit(tempUnit);
			beasts.add(tempUnit);
		}
		for (String coord : goblins) {
			tempTile = gridMap.get(coord);
			tempUnit = new Goblin(tempTile);
			tempTile.addUnit(tempUnit);
			beasts.add(tempUnit);
		}
		
		// Initialize the tiles by calculating adjacent tiles and buffers for every tile
		for (Map.Entry<String, Tile> entry : gridMap.entrySet()) {
			entry.getValue().adjacentTiles(gridMap);
		}
	}
	
	/*
	 * Get the tile at the specific position
	 */
	public Tile getTile(int x, int y) {
		key = toKey(x,y);
		return gridMap.get(key);
	}
	
	/*
	 * Move a unit to a specified position
	 */
	public boolean moveUnit(Tile startTile, Tile goalTile) {			
		// Move unit if the move is legal and the goal tile is not occupied
		if (startTile.legalMoves().contains(goalTile)) {						
			// Move the unit
			goalTile.unit = startTile.unit;
			goalTile.unit.tile = goalTile;
			startTile.unit = null;
			goalTile.unit.moveLeft = false;
			
			return true;
		}
		// If the move isn't legal, return false
		System.out.println("This is not a legal move!");
		return false;
	}
	
	/*
	 * Attack a unit with another unit
	 */
	public boolean attackUnit(Unit unitSelf, Unit unitHostile) {	
		// Check if it is possible to attack
		if (!attackIsPossible(unitSelf, unitHostile)) {
			return false;
		}
		
		// Get weapon skills
		skillAttacker = unitSelf.weaponSkill + unitSelf.getBuffer();
		skillDefender = unitHostile.weaponSkill + unitHostile.getBuffer();
		hitChance = 1 / (1 + Math.exp(-0.4 * (skillAttacker - skillDefender)));
		
		// Attack the hostile
		if (Math.random() <= hitChance ) {
			unitHostile.hitPoints -= 1;
			message = "boom";
			
			// Remove the unit if he died
			if (unitHostile.hitPoints == 0) {
				unitHostile.tile.unit = null;
				
				// Remove the unit also from the lists of units
				if (team.equals("Humans")) {
					beasts.remove(unitHostile);
				}
				else if (team.equals("Beasts")) {
					humans.remove(unitHostile);
				}
			}
			return true;
		}
		message = "missed";
		return false;
	}
	
	/*
	 * This method implements a couple of checks explained above each check
	 * to make sure it is possible to attack the unit at (x1,y1) with unit (x,y)
	 */
	public boolean attackIsPossible(Unit unitSelf, Unit unitHostile) {
		// Check if the units exist
		if (unitHostile == null || unitSelf == null) {
			return false;
		}	
		
		// Check if the tile is adjacent
		if (!unitSelf.tile.adjacentTiles.contains(unitHostile.tile)) {
			System.out.println("Your tool of death is not long enough for this attack.");
			message = "reach";
			return false;
		}
		
		// Check if the attacker exists and is friendly
		if (unitSelf == null || !unitSelf.team.equals(team)) {
			System.out.println("There must be a (friendly) unit to attack with!");
			return false;
		}		
		
		// Check if the defender is friendly or hostile
		if (unitHostile.team.equals(unitSelf.team)) {
			System.out.println("Friendly fire!");
			message = "friendly";
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
	
	
}