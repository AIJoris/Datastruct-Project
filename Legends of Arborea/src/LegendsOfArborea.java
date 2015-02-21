
public class LegendsOfArborea {

	public static void main(String[] args) {
		System.out.println("Set-up game environment:");
		Grid grid = new Grid();
		boolean move = grid.moveUnit(-4,1, -4,2);
		System.out.println(move);

	}
}
