import java.util.ArrayList;
import java.util.Random;


public class HumanPlayer {
	String positionSelf;
	Tile tileSelf;
	String positionGoal;
	Tile goalTile;
	String toPosition;
	ArrayList<String> hostiles;
	ArrayList<String> legalMoves;
	Random rand = new Random();
	int x;
	int y;
	int x1;
	int y1;	
	Grid grid;
	String playerTeam;
	MouseHandler mouseHandler;
	
	public HumanPlayer(Grid newGrid, String newPlayerTeam, MouseHandler newMouseHandler) {
		grid = newGrid;
		playerTeam = newPlayerTeam;
		mouseHandler = newMouseHandler;
	}
	
	public String toKey(int x, int y) {
		return new Integer(x).toString() + new Integer(y).toString();
	}
	
	public void play() {
		// Get a friendly unit at a position specified by the mouse click
		mouseHandler.currentUnit = null;
		System.out.println("Select a unit");
		while (mouseHandler.currentUnit == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		x = mouseHandler.currentTile.x;
		y = mouseHandler.currentTile.y;
		positionSelf = toKey(x,y);
		tileSelf = grid.gridMap.get(positionSelf);
		
		// Specify a goal position with a mouse click
		System.out.println("Select goal tile");
		mouseHandler.currentTile = null;
		while (mouseHandler.currentTile == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		x1 = mouseHandler.currentTile.x;
		y1 = mouseHandler.currentTile.y;
		positionGoal = toKey(x1,y1);
		goalTile = grid.gridMap.get(positionGoal);
		
		// If the goal tile contains a unit, attack that unit
		if (goalTile.unit != null) {
			grid.attackUnit(x, y, x1, y1);
		}
		else {
			grid.moveUnit(x, y, x1, y1);
		}
	}			
}
