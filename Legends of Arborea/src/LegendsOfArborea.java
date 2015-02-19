
public class LegendsOfArborea {

	public static void main(String[] args) {
		System.out.println("Set-up game environment:");
		Grid grid = new Grid();
		int[] from = {1,2};
		int[] to = {2,2};
		grid.moveUnit(from, to);
		grid.attackUnit(from, to);
	}
}
