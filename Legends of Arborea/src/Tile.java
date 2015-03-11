import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;


public class Tile {
	int x;
	int y;
	Unit unit;
	String team;
	String key;
	ArrayList<Tile> adjacentTiles;
	int buffer;
	ArrayList<Tile> surroundingHostiles;
	ArrayList<Tile> legalMoves;
	Point location;
	boolean moveLeft;
	boolean attackLeft;
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
		
		location = new Point(x,y);
		moveLeft = false;
		attackLeft = false;
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
	public int getBuffer() {
		if (unit != null) {
			buffer = 0;
			// Specify who are friendly and who are hostile
			Unit friendlyGeneralUnit;
			Unit friendlyInfantryUnit;
			Unit hostileGeneralUnit;
			Unit hostileInfantryUnit;
			if (unit.team.equals("Humans")) {
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
			int nrTiles = adjacentTiles.size();
			for (int i = 0; i < nrTiles; i++) {
				if (adjacentTiles.get(i) != null & adjacentTiles.get(i).unit != null) {
					if (adjacentTiles.get(i).unit.name.equals(friendlyGeneralUnit.name)){
						buffer += 2;
					}
					else if (adjacentTiles.get(i).unit.name.equals(friendlyInfantryUnit.name)){
						buffer += 1;
					}
					else if (adjacentTiles.get(i).unit.name.equals(hostileGeneralUnit.name)) {
						buffer -= 2;
					}
					else if (adjacentTiles.get(i).unit.name.equals(hostileInfantryUnit.name)) {
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
	public ArrayList<Tile> surroundingHostiles() {
		surroundingHostiles = new ArrayList<Tile>();
		Unit surroundingUnit;
		
		// Loop over adjacent tiles to find hostile units
		for (Tile adjacentTile : adjacentTiles) {
			if (adjacentTile == null | adjacentTile.unit == null) {
				continue;
			}
			surroundingUnit = adjacentTile.unit; 
			if (!surroundingUnit.team.equals(unit.team)) {
				surroundingHostiles.add(adjacentTile);
			}
		}
		return surroundingHostiles;
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

	public boolean isLegal(Tile move){
		try{
			return legalMoves().contains(move);
		} catch (NullPointerException e){
			return false;
		}
	}
	
	public boolean isHostile(Tile move){
		try{
			return surroundingHostiles().contains(move);
		} catch (NullPointerException e){
			return false;
		}
	}
	
	/** Converts axial coordinates to cube coordinates */
	public int[] hexToCube(int q, int r){
		int[] cubeCoords = new int[3];
		cubeCoords[0] = q;
		cubeCoords[1] = r;
		cubeCoords[2] = -q-r;
		return cubeCoords;
	}
	
	/** Returns the distance to a given tile */
	public int distanceTo(Tile endTile){
		int[] startCoords = hexToCube(this.x, this.y);
		int[] endCoords = hexToCube(endTile.x, endTile.y);
		return (Math.abs(startCoords[0] - endCoords[0]) + Math.abs(startCoords[1] - endCoords[1]) + Math.abs(startCoords[2] - endCoords[2]))/2;
		
	}
	
	/** Returns the closest hostiles */
	public ArrayList<Tile> getClosestHostiles(ArrayList<Tile> hostiles){
		ArrayList<Tile> closestHostiles = new ArrayList<Tile>();
		int bestDistance = 999;
		for (Tile hostile : hostiles) {
			if(this.distanceTo(hostile) < bestDistance){
				bestDistance = this.distanceTo(hostile);
			}
		}
		for (Tile hostile : hostiles) {
			if(this.distanceTo(hostile) == bestDistance){
				closestHostiles.add(hostile);
			}
		}
		return closestHostiles;	
	}
	
//	ArgMax<Double, String> argmax = new Argmax<Double, String>(
//            Double.NEGATIVE_INFINITY, "");
//
//for (String str : stringList) // stringList is probably an array of Strings
//{
//    argmax.update(str, getScore(str));
//}
//
//double maxValue = argmax.getMaxValue();
//String argmaxString = argmax.getArgMax();
	
}