
public class Tile {
	int x;
	int y;
	int nr;
	Unit unit;
	String key;
	
	/*
	 * Constructor with only coordinates
	 */
	public Tile(int newX, int newY, int newNr) {
		this(newX, newY, null, newNr);
	}
	
	/*
	 * Constructor that also initialized a unit on the tile
	 */
	public Tile(int newX, int newY, Unit newUnit,int newNr) {
		x = newX;
		y = newY;
		unit = newUnit;
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
	public void removeUnit() {
		unit = null;
	}
	
}
