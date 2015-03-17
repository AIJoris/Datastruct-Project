import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * The Tile class represents a hexagon. It takes a location and unit.
 */
public class Tile {
	int x,y;
	Unit unit;
	String key;
	ArrayList<Tile> adjacentTiles, legalMoves;
	Point location;
	boolean searching, target, option;
	int buffer;
	
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
		key = toKey(x,y);		
		location = new Point(x,y);
		searching = false;
		target = false;
		option = false;
	}
	
	/*
	 * Initialize the tile by calculating the initial buffer and adjacent tiles
	 */
	public void adjacentTiles(HashMap<String, Tile> gridMap) {
		adjacentTiles = new ArrayList<Tile>();
		// Generate adjacent tiles
		int[] xMoves = {x-1, x-1, x, x, x+1, x+1};
		int[] yMoves = {y, y+1, y-1, y+1, y-1, y};
		for (int i = 0; i < 6; i++) {
			if (gridMap.get(toKey(xMoves[i], yMoves[i])) != null) {
				adjacentTiles.add(gridMap.get(toKey(xMoves[i], yMoves[i])));
			}
		}
	}
	
	/*
	 * This method checks if and how many friendly units 
	 * are present at tiles nearby, and calculates the buffer 
	 */
	public int getBuffer(String team) {
		buffer = 0;
		// Specify who are friendly and who are hostile
		String friendlyGeneral;
		String friendlyInfantry;
		String hostileGeneral;
		String hostileInfantry;
		if (team.equals("Humans")) {
			friendlyGeneral = "General";
			friendlyInfantry = "Swordsman";
			hostileGeneral = "Orc";
			hostileInfantry = "Goblin";
		}
		else {
			friendlyGeneral = "Orc";
			friendlyInfantry = "Goblin";
			hostileGeneral = "General";
			hostileInfantry = "Swordsman";
		}
		
		// Build up buffer based on hostile/friendly units nearby
		int nrTiles = adjacentTiles.size();
		for (int i = 0; i < nrTiles; i++) {
			if (adjacentTiles.get(i) != null & adjacentTiles.get(i).unit != null) {
				String unitName = adjacentTiles.get(i).unit.name;
				if (unitName.equals(friendlyGeneral)){
					buffer += 2;
				}
				else if (unitName.equals(friendlyInfantry)){
					buffer += 1;
				}
				else if (unitName.equals(hostileGeneral)) {
					buffer -= 2;
				}
				else if (unitName.equals(hostileInfantry)) {
					buffer -= 1;
				}
			}
		}
		return buffer;
	}

	
	/*
	 * This method calculates all possible legal moves from a position (x,y)
	 */
	public ArrayList<Tile> legalMoves() {			
		// Generate all adjacent tiles and check which ones make a legal move
		ArrayList<Tile> legalMoves = new ArrayList<Tile>();
		for (Tile adjacentTile : adjacentTiles) {			
			// Check if the tiles exist
			if (adjacentTile == null) {
				continue;
			}
			
			// Check if there is a unit at the start position and no unit on the goal position
			if (unit == null || adjacentTile.unit != null) {
				continue;
			}
			
			// The move is legal, so add it to the list
			legalMoves.add(adjacentTile);
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
	 * Convert the coordinate of a tile to a string, so it
	 * can be used as key to access a tile in the hashmap
	 */
	public String toKey(int x, int y) {
		return new Integer(x).toString() + new Integer(y).toString();
	}

	/*
	 * This method checks if a tile is a legal move
	 */
	public boolean isLegal(Tile tile) {
		try {
			return legalMoves().contains(tile);
		} 
		catch (NullPointerException e){
			return false;
		}
	}
	
	/*
	 * Converts axial coordinates to cube coordinates
	 */
	public int[] hexToCube(int q, int r) {
		int[] cubeCoords = new int[3];
		cubeCoords[0] = q;
		cubeCoords[1] = r;
		cubeCoords[2] = -q-r;
		return cubeCoords;
	}
	
	/*
	 * Returns the distance to a given tile
	 */
	public int distanceTo(Tile endTile) {
		int[] startCoords = hexToCube(this.x, this.y);
		int[] endCoords = hexToCube(endTile.x, endTile.y);
		return (Math.abs(startCoords[0] - endCoords[0]) + Math.abs(startCoords[1] - endCoords[1]) + Math.abs(startCoords[2] - endCoords[2]))/2;
		
	}
	
	/*
	 * Returns the closest hostiles
	 */
	public ArrayList<Unit> getClosestHostiles(ArrayList<Unit> hostiles) {
		ArrayList<Unit> closestHostiles = new ArrayList<Unit>();
		int bestDistance = 999;
		for (Unit hostile : hostiles) {
			if (this.distanceTo(hostile.tile) < bestDistance) {
				bestDistance = this.distanceTo(hostile.tile);
			}
		}
		for (Unit hostile : hostiles) {
			if (this.distanceTo(hostile.tile) == bestDistance) {
				closestHostiles.add(hostile);
			}
		}
		return closestHostiles;	
	}	
}