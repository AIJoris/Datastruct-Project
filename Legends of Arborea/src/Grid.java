import java.awt.List;

/* 
 * This class implements the hex-grid
 */
public class Grid {
	
	/*
	 * Constructor for the grid
	 */
	public Grid() {
		System.out.println("Initialize the grid");
	}
	
	public boolean move(Object unit, Object position) {
		System.out.println("Move");
		return true;
	}
	
	/* 
	 * Place all units on the grid in the right starting postion
	 */
	public void placeUnits(){
		System.out.println("Create the startingposition on the grid");
		General general = new General();
		Swordsman swordsman = new Swordsman();
		Orc orc = new Orc();
		Goblin goblin = new Goblin();
	}
}
