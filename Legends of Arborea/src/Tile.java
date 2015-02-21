
public class Tile {
	int x;
	int y;
	Unit unit;
	
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
