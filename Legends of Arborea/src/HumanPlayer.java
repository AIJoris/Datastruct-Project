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
	
	public HumanPlayer(Grid newGrid, MouseHandler newMouseHandler) {
		grid = newGrid;
		mouseHandler = newMouseHandler;
	}
	
	public String toKey(int x, int y) {
		return new Integer(x).toString() + new Integer(y).toString();
	}
	
	public void play() {
//		ArrayList<String> humansTemp = new ArrayList<String>(grid.humans);
//		ArrayList<String> beastsTemp = new ArrayList<String>(grid.beasts);
		
		for (int i = 0; i < 10; i++) {
			// Wait for a mouse click
			mouseHandler.currentUnit = null;
			System.out.println("Select a unit");
			while (mouseHandler.currentUnit == null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Select a unit
			x = mouseHandler.currentTile.x;
			y = mouseHandler.currentTile.y;
			positionSelf = toKey(x,y);
			tileSelf = grid.gridMap.get(positionSelf);
			
			// Wait for a second mouse click
			System.out.println("Select goal tile");
			mouseHandler.currentTile = null;
			while (mouseHandler.currentTile == null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Select a goal tile/unit
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
}
