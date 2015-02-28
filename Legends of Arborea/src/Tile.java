import java.util.ArrayList;
import java.util.HashMap;


public class Tile {
	int x;
	int y;
	Unit unit;
	String team;
	String key;
	Tile[] adjacentTiles = new Tile[6];
	int buffer;
	ArrayList<String> surroundingHostiles;
	ArrayList<String> legalMoves;
	
	/*
	 * Constructor with only coordinates
	 */
	public Tile(int newX, int newY) {
		this(newX, newY, null);
	}
	
	/*
	 * Constructor that also initialized a unit on the tile
	 */
	public Tile(int newX, int newY, Unit newUnit) {
		x = newX;
		y = newY;
		unit = newUnit;	
		if (unit != null) {
			team = unit.team;
		}
		
	}
	
	/*
	 * Initialize the tile by calculating the initial buffer and adjacent tiles
	 */
	public void adjacentTiles(HashMap<String, Tile> gridMap) {
		// Generate adjacent tiles
		int[] xMoves = {x-1, x-1, x, x, x+1, x+1};
		int[] yMoves = {y, y+1, y-1, y+1, y-1, y};
		for (int i = 0; i < 6; i++) {
			if(gridMap.get(toKey(xMoves[i], yMoves[i])) != null){
				adjacentTiles[i] =  gridMap.get(toKey(xMoves[i], yMoves[i]));
			}
		}
	}
	
	/*
	 * This method checks if and how many friendly units 
	 * are present at tiles nearby, and calculates the buffer 
	 */
	public int getBuffer() {
		if (unit == null) {
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
			
			// Build up buffer based on hostile/friendly units nearby
			for (int i = 0; i < 6; i++) {
				if (adjacentTiles[i] != null & adjacentTiles[i].unit != null) {
					if (adjacentTiles[i].unit.name.equals(friendlyGeneralUnit.name)){
						buffer += 2;
					}
					else if (adjacentTiles[i].unit.name.equals(friendlyInfantryUnit.name)){
						buffer += 1;
					}
					else if (adjacentTiles[i].unit.name.equals(hostileGeneralUnit.name)) {
						buffer -= 2;
					}
					else if (adjacentTiles[i].unit.name.equals(hostileInfantryUnit.name)) {
						buffer -= 1;
					}
				}
			}
			return buffer;
		}
		return 0;
	}

	/*
	 * This method returns all hostile forces around a position
	 */
	public ArrayList<String> surroundingHostiles(int x, int y) {
		surroundingHostiles = new ArrayList<String>();
		Unit surroundingUnit;
		
		// Loop over adjacent tiles to find hostile units
		for (int i = 0; i < 6; i++) {
			surroundingUnit = adjacentTiles[i].unit; 
			if (surroundingUnit != null) {
				if (!surroundingUnit.team.equals(team)) {
					surroundingHostiles.add(toKey(adjacentTiles[i].x, adjacentTiles[i].y));
				}
			}
		}
		return surroundingHostiles;
	}
	
	/*
	 * This method calculates all possible legal moves from a position (x,y)
	 */
	public ArrayList<String> legalMoves() {			
		// Generate all adjacent tiles and check which ones make a legal move
		ArrayList<String> legalMoves = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {			
			// Check if the tiles exist
			if (adjacentTiles[i] == null) {
				continue;
			}
			
			// Check if there is a unit at the start position and no unit on the goal position
			if (unit == null || adjacentTiles[i].unit != null) {
				continue;
			}
			
			// The move is legal, so add it to the list
			legalMoves.add(toKey(adjacentTiles[i].x, adjacentTiles[i].y));
			
		}
		return legalMoves;
	}
	
	/*
	 * Add a unit to this tile
	 */
	public void addUnit(Unit newUnit) {
		unit = newUnit;
	}
	
	/*
	 * Remove the unit currently on this tile
	 */
	public void removeUnit(Grid grid) {
		unit = null;
		// Remove the unit also from the lists of units
				if (team.equals("Humans")) {
					grid.beasts.remove(toKey(x,y));
					return;
				}
				else if (team.equals("Beasts")) {
					grid.humans.remove(toKey(x, y));
					return;
				}
	}
	
	/*
	 * Convert the coordinate of a tile to a string, so it
	 * can be used as key to access a tile in the hashmap
	 */
	public String toKey(int x, int y) {
		return new Integer(x).toString() + new Integer(y).toString();
	}
	
}