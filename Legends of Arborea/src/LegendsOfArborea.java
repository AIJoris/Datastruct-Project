// Bijhouden wie waar staat (onderscheid infantry/generals??), heb ik nu in arraylist gedaan (slim)?
// Moet er nog een if in voor een negatieve hitchance?
// Functie allHostiles die kijkt of er hostiles in de buurt zijn, kan in/samen met buffer/legalmoves? en returned arraylist (slim?)
// isPossible en legalmove samenvoegen?
// Je kunt ipv hostiles zoeken om een positie ook kijken naar de lijst met units van het andere team en kijken of er units van jou in de buurt zijn, die returnen en dan attacken (AI probleem)
// Het opslaan van de coordinaten in strings begint onhandig te worden omdat je niet makkelijk terug kunt converten naar ints
// Iets nieuws geprobeerd: de method die alle legal moves returned die stopt er geen keys in maar gewoon x,y achter elkaar
// een position class maken is misschien toch wel handig omdat je nu op een een string terugkrijgt en die niet kunt converten naar de ints (komt door mogelijke minnen)
// Je kunt aanvallen van overal
// q learning / value iteration


// Unit moet niet moven als zijn buffer verkleint (doen ze nu wel, dus die threshold is niet voldoende) (parhfinding)
// Als de buffer van een unit heel laag is (bv 0-3), dan moet hij zijn buffer zsm vergoten, zo voorkom je isolatie





import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class LegendsOfArborea implements ActionListener{
	/* Get screen information*/
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	/* Width and height of application window in pixels */
	static int WIDTH = (int)screenSize.getWidth();
	static int HEIGHT = (int)screenSize.getHeight();
	
	static HumanPlayer player3;
	static HumanPlayer player4;
	/* Size of the sides of the hexagon */
	// kan niet width meer an length want dan gaat ie spacen met de select
	public static final Point HEXSIZE  = new Point(44,44);
	/* Place to start drawing the board */
	private static final Point HEXSTART =  new Point(WIDTH/2 - (7*HEXSIZE.x), HEIGHT/2-(int)((Math.sqrt(3f)*HEXSIZE.y-60)));
	Polygon points;
	PaintGraphics graphics;
	public static MouseHandler mouseHandler;
	
	
	/* 
	 * Main method
	 */	
	public static void main(String[] args) {
		// Play the game
		int win = 0;
		for (int i = 0; i < 1; i++) {
			// Set up the game environment
			Grid grid = new Grid();
			new LegendsOfArborea(grid);
			
			// Define the starting team
			String startingTeam = "Humans";
			grid.team = startingTeam;
			if (playGame(grid).equals("Humans")) {
				win++;
			}
		}
		System.out.println("Intelligent win " + win + "times out of 10000 games");
	}
	
	/*
	 * Legends of Arborea contructor
	 */
	public LegendsOfArborea(Grid grid) {
		JFrame frame = new JFrame("Legends of Arborea");
		JButton endTurnButton = new JButton("End turn");
		Container con = frame.getContentPane();
		Image img;
		try {
			img = ImageIO.read(getClass().getResource("endturn.png"));
			endTurnButton.setIcon(new ImageIcon(img));
			endTurnButton.setPressedIcon(new ImageIcon(img));
			img = ImageIO.read(getClass().getResource("endturnSel.png"));
			endTurnButton.setRolloverEnabled(true);
			endTurnButton.setRolloverIcon(new ImageIcon(img));
		} catch (IOException e1) {
			endTurnButton.setBounds(HEXSTART.x - 350, HEXSTART.y+350, 180, 35);
		}
		endTurnButton.setBounds(HEXSTART.x - 400, HEXSTART.y+320, 199, 35);
		endTurnButton.setBackground(new Color(0,0,0));
		endTurnButton.setBorder(BorderFactory.createEmptyBorder());
		
		con.setBackground(new Color(0,0,0));
		mouseHandler = new MouseHandler(grid, HEXSIZE, HEXSTART);
		frame.addMouseListener(mouseHandler);
		frame.addMouseMotionListener(mouseHandler);
		
		graphics = new PaintGraphics(HEXSTART, HEXSIZE, grid, mouseHandler);
		frame.setPreferredSize(new Dimension(300,400));
		frame.setMinimumSize(new Dimension(300,400));
		endTurnButton.setVisible(true);
		graphics.add(endTurnButton);
		graphics.add(new JPanel());

		frame.add(graphics);

		endTurnButton.addActionListener(this);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);;
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
		frame.addWindowListener( new WindowAdapter() {
	    	 public void windowClosing ( WindowEvent e ) {
	    	 	System.exit(0); 
	    	 }
	    });
	}
	//static class Action1 implements ActionListener {   
	//	@Override
		public void actionPerformed(ActionEvent e) {
			player3.endTurn = true;	
			player4.endTurn = true;
		}
	//}   
	
	/*
	 * This method plays the game
	 */
	private static String playGame(Grid grid) {
		// Create the AI's
		AI player1 = new AI(grid, "Humans");
		AI player2 = new AI(grid, "Beasts");
		player3 = new HumanPlayer(grid, mouseHandler);
		player4 = new HumanPlayer(grid, mouseHandler);
		int turn = 0;
		
		// Main game loop
		while (!grid.humans.isEmpty() & !grid.beasts.isEmpty()) {
			// Player 1:
			if (grid.team.equals("Humans")) {
				System.out.println("Humans: ");
				player1.playIntelligent();
			}
			
			// Player 2:
			else if (grid.team.equals("Beasts")) {
				System.out.println("Beasts");
				player2.playIntelligent();
			}
			
			changeTeam(grid);
			turn++;
		}
		System.out.println(turn);
		System.out.println("The " + grid.team + " lose!");
		return grid.team;
	}
	
	/*
	 * This method updates the turn and switches teams
	 */
	private static void changeTeam(Grid grid) {
		// Change team
		if (grid.team.equals("Humans")) {
			grid.team = "Beasts";
		}
		else if (grid.team.equals("Beasts")) {
			grid.team = "Humans";
		}
	}
	
}
