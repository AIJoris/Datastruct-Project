/* 
 * This class implements the hex-grid
 */
public class Grid {
	
	/*
	 * Grid constructor which initializes the grid and 
	 * places the units according to the startposition
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
	}
	
	/* 
	 * Place all units on the grid in the right starting position
	 */
	public void placeUnits() {
		System.out.println("Create the startingposition on the grid");
		General general = new General();
		Swordsman swordsman = new Swordsman();
		Orc orc = new Orc();
		Goblin goblin = new Goblin();
	}
	
	/*
	 * This method returns the unit currently at the specified position
	 */
	public Unit getUnit(Object position) {
		Goblin goblin = new Goblin();
		return goblin;
	}
	
	/*
	 * Move a unit to a specified position
	 */
	public boolean moveUnit(Object from, Object to) {
		System.out.println("Move the unit at position 1 to position 2");
		return true;
	}
	
	/*
	 * Attack a unit with another unit
	 */
	
	public boolean attackUnit(Object from, Object to) {
		System.out.println("Attack the unit at position 2 from position 1");
		return true;
	}
	
	/*
	 * Remove a unit from a specified position
	 */
	public void removeUnit(Object position) {
		System.out.println("Unit at position ... removed");
	}
	
}
