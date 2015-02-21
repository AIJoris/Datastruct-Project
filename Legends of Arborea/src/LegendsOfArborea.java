
public class LegendsOfArborea {

	public static void main(String[] args) {
		System.out.println("Set-up game environment:");
		Grid grid = new Grid();
		
		boolean move = grid.moveUnit(-2,-1, -1,-1);
		
		move = grid.moveUnit(-1,-1, 0,-1);
		move = grid.moveUnit(0,-1, 1,-1);
		move = grid.moveUnit(1,-1, 2,-1);
		System.out.println(grid.getUnit(3,-2).hitPoints);
		boolean attack = grid.attackUnit(2,-1, 3,-2);
		System.out.println(grid.getUnit(3,-2).hitPoints);

	}
}
