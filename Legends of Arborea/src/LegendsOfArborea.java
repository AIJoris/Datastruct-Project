// Bijhouden wie waar staat (onderscheid infantry/generals??), heb ik nu in arraylist gedaan (slim)?
// Elke turn het team updaten
import java.util.ArrayList;
// Moet er nog een if in voor een negatieve hitchance?
// Functie allHostiles die kijkt of er hostiles in de buurt zijn, kan in/samen met buffer/legalmoves? en returned arraylist (slim?)
// isPossible en legalmove samenvoegen?
// Je kunt ipv hostiles zoeken om een positie ook kijken naar de lijst met units van het andere team en kijken of er units van jou in de buurt zijn, die returnen en dan attacken (AI probleem)
// Het opslaan van de coordinaten in strings begint onhandig te worden omdat je niet makkelijk terug kunt converten naar ints
// Iets nieuws geprobeerd: de method die alle legal moves returned die stopt er geen keys in maar gewoon x,y achter elkaar
import java.util.Random;

public class LegendsOfArborea {

	public static void main(String[] args) {
		// Set up the game environment
		Grid grid = new Grid();
		
		// Play the game
		grid.team = "Humans";
		playGame(grid);
		
	}
	
	/*
	 * This method runs the game
	 */
	private static void playGame(Grid grid) {
		// Main game loop
		int turn = 0;
		String positionSelf;
		String positionHostile;
		Unit unit;
		ArrayList<String> hostiles;
		ArrayList<Integer> legalMoves;
		int x;
		int y;
		int x1;
		int y1;
		int size;
		while (!grid.humans.isEmpty() || !grid.beasts.isEmpty()) {
			Random rand = new Random();
			// Player 1:
			if (grid.team.equals("Humans")) {
				// Get a random friendly unit at a position
				positionSelf = grid.humans.get(rand.nextInt(grid.humans.size()));
				x = grid.grid.get(positionSelf).x;
				y = grid.grid.get(positionSelf).y;

				// Attack hostile unit if possible
				hostiles = grid.allHostiles(x, y);
				if (!hostiles.isEmpty()) {
					positionHostile = hostiles.get(0);
					x1 = grid.grid.get(positionHostile).x;
					y1 = grid.grid.get(positionHostile).y;
					grid.attackUnit(x,y, x1, y1);		
					System.out.println(grid.humans.size());
					System.out.println(grid.beasts.size());
				}
				
				// Make a random move if not possible to attack
				else {
					legalMoves = grid.legalMoves(x,y);
					boolean move = grid.moveUnit(x, y, legalMoves.get(0), legalMoves.get(1));
					int i = 1;
					size = legalMoves.size()-2;
					while (move == false && i < size) {
						move = grid.moveUnit(x, y, legalMoves.get(i), legalMoves.get(i+1));
						i += 2;
						
					}					
				}
			}
			
			// Player 2:
			else if (grid.team.equals("Beasts")) {
				// Get a random friendly unit at a position
				positionSelf = grid.beasts.get(rand.nextInt(grid.beasts.size()));
				x = grid.grid.get(positionSelf).x;
				y = grid.grid.get(positionSelf).y;
				
				// Attack hostile unit if possible
				hostiles = grid.allHostiles(x, y);
				if (!hostiles.isEmpty()) {
					positionHostile = hostiles.get(0);
					x1 = grid.grid.get(positionHostile).x;
					y1 = grid.grid.get(positionHostile).y;
					grid.attackUnit(x,y, x1, y1);
				}
				
				// Make a random move if not possible to attack
				else {
					legalMoves = grid.legalMoves(x,y);
					boolean move = grid.moveUnit(x, y, legalMoves.get(0), legalMoves.get(1));
					int i = 1;
					size = legalMoves.size()-2;
					while (move == false && i < size) {
						move = grid.moveUnit(x, y, legalMoves.get(i), legalMoves.get(i+1));
						i += 2;
						
					}					
				}
			}
			
			// Change team
			if (grid.team.equals("Humans")) {
				grid.team = "Beasts";
			}
			else if (grid.team.equals("Beasts")) {
				grid.team = "Humans";
			}
			
			// Update turn
			turn ++;
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		System.out.println(turn);
		System.out.println(grid.team);
	}
	
}
