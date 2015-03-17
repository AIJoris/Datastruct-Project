import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/* sets up graphics */
public class PaintGraphics extends JComponent{
	private static final long serialVersionUID = 1L;
	Point HEXSTART;
	Point HEXSIZE;
	Grid grid;
	MouseHandler mouseHandler;
	Graphics g;
	// Get screen information
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// Width and height of application window in pixels 
	static int WIDTH = (int)screenSize.getWidth();
	static int HEIGHT = (int)screenSize.getHeight();
	
	/* Constructor: initialize variables */
	public PaintGraphics(Point HEXSTART1, Point HEXSIZE1, Grid grid1, MouseHandler handler) {
		this.HEXSTART = HEXSTART1;
		this.HEXSIZE = HEXSIZE1;
		this.grid = grid1;
		this.mouseHandler = handler;
	}
	
	/* Call the drawing methods */
	public void paint(Graphics g) {
		super.paint(g);
		drawBoard(g);
		try {
			drawDecor(g);
			drawUnits(g);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		repaint();
    }
	
	/* This method draws the title, background and character information screens*/
	public void drawDecor(Graphics g) throws IOException{
		// Add Title
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("title.png");
		Image title = ImageIO.read(input);
		g.drawImage(title,(int)(WIDTH/4.79), HEIGHT/18, this);
		
		// Load images of hearts, swords and axes (for hit points and life representation)
		input = classLoader.getResourceAsStream("heart.png");
		Image heart = ImageIO.read(input);
		input = classLoader.getResourceAsStream("axe.png");
		Image axe = ImageIO.read(input);
		input = classLoader.getResourceAsStream("sword.png");
		Image sword = ImageIO.read(input);
		
		Map<String, Image> icons = new HashMap<>();
		icons.put("heart", heart);
		icons.put("axe", axe);
		icons.put("sword", sword);
		
		// Add score boards 
		// Load images of the characters
		input = classLoader.getResourceAsStream("genOverview.png");
		Image general = ImageIO.read(input);
		input = classLoader.getResourceAsStream("gobOverview.png");
		Image goblin = ImageIO.read(input);
		input = classLoader.getResourceAsStream("orcOverview.png");
		Image orc = ImageIO.read(input);
		input = classLoader.getResourceAsStream("smOverview.png");
		Image swordsman = ImageIO.read(input);
		
		Map<String, Image> characters = new HashMap<>();
		characters.put("General", general);
		characters.put("Goblin", goblin);
		characters.put("Orc", orc);
		characters.put("Swordsman", swordsman);
		int imWidth = general.getWidth(this);
		
		// Load images of the Messages
		input = classLoader.getResourceAsStream("enemyOutofReach.png");
		Image reach = ImageIO.read(input);
		input = classLoader.getResourceAsStream("unitUsed.png");
		Image used = ImageIO.read(input);
		input = classLoader.getResourceAsStream("missed.png");
		Image missed = ImageIO.read(input);
		input = classLoader.getResourceAsStream("boom.png");
		Image boom = ImageIO.read(input);
		input = classLoader.getResourceAsStream("friendly.png");
		Image friendly = ImageIO.read(input);
		
		Map<String, Image> messages = new HashMap<>();
		messages.put("reach", reach);
		messages.put("used", used);
		messages.put("missed", missed);
		messages.put("boom", boom);
		messages.put("friendly", friendly);
		
		// Construct left panel
		try{
			// Draw name
			g.drawImage(characters.get(mouseHandler.currentUnit.name),(HEIGHT/2)-imWidth, HEXSTART.y, this);
			
			// Get hit points and draw corresponding amount of hearts
			int xOffset = (HEIGHT/2)-imWidth;
			Point location = new Point(xOffset, HEXSTART.y + 70);
			int hitPoints = mouseHandler.currentUnit.hitPoints;
			for (int i = 0; i < hitPoints; i++) {
				if (i< 5) {
					g.drawImage(icons.get("heart"),location.x+(i*35), location.y, this);
				} else {
					g.drawImage(icons.get("heart"),location.x+((i-5)*35), location.y+35, this);
				}
			}
			
			// Get weapon skill and draw corresponding amount of weapons
			int weaponSkill = mouseHandler.currentUnit.weaponSkill;
			weaponSkill += mouseHandler.selectedTile.unit.getBuffer();
			String weapon = mouseHandler.currentUnit.weapon;
			location = new Point(xOffset, HEXSTART.y + 140);
			for (int i = 0; i < weaponSkill; i++) {
				if (i< 5) {
					g.drawImage(icons.get(weapon),location.x+(i*35), location.y, this);
				} else if (i< 10) {
					g.drawImage(icons.get(weapon),location.x+((i-5)*35), location.y+35, this);
				} else {
					g.drawImage(icons.get(weapon),location.x+((i-10)*35), location.y+70, this);
				}
			}
		} catch (NullPointerException e) {}
		
		// Construct right panel
		try {
			Tile tempTile = grid.getTile(mouseHandler.currenTileCoords.x, mouseHandler.currenTileCoords.y);
			// Draw name 
			g.drawImage(characters.get(tempTile.unit.name),(WIDTH/2)+ imWidth, HEXSTART.y, this);
			// Get hit points and draw corresponding amount of hearts 
			int xOffset = (WIDTH/2)+ imWidth;
			Point location = new Point(xOffset, HEXSTART.y + 70);
			int hitPoints = tempTile.unit.hitPoints;
			for (int i = 0; i < hitPoints; i++) {
				if (i< 5) {
					g.drawImage(heart,location.x+(i*35), location.y, this);
				} else {
					g.drawImage(heart,location.x+((i-5)*35), location.y+35, this);
				}
			}
			
			// Get weapon skill and draw corresponding amount of weapons 
			int weaponSkill = tempTile.unit.weaponSkill;
			weaponSkill += tempTile.unit.getBuffer();
			String weapon = tempTile.unit.weapon;
			location = new Point(xOffset, HEXSTART.y + 140);
			for (int i = 0; i < weaponSkill; i++) {
				if (i< 5) {
					g.drawImage(icons.get(weapon),location.x+(i*35), location.y, this);
				} else if (i< 10) {
					g.drawImage(icons.get(weapon),location.x+((i-5)*35), location.y+35, this);
				} else {
					g.drawImage(icons.get(weapon),location.x+((i-10)*35), location.y+70, this);
				}
			}
        } catch (NullPointerException e) {}
		
		// Update message panel
		Point messageLocation = new Point((WIDTH/2)-244, HEIGHT/7);
		String foundMessage = grid.message;
		if (foundMessage != null) {
			g.drawImage(messages.get(foundMessage),messageLocation.x, messageLocation.y, this);
		}
	}
	
	// This method draws the hexagonal board
	public void drawBoard(Graphics g) {
		// The default tile color 
		Color color = new Color(97, 168,104);
		Tile AdjacentTile;
		Point location = new Point(HEXSTART.x, HEXSTART.y);
		Point hexLocation = new Point(0,0);
		Unit unit;
		// Draw left side of the board 
		// Loop horizontally
		for (int j = 0; j < 5; j++) {
			// Loop vertically
			for (int i = 0; i < 5+j; i++) {
				hexLocation = mouseHandler.pixelToHex(location.x, location.y);
				unit = grid.getTile(hexLocation.x, hexLocation.y).unit;
				if (unit != null) {
					// The following statements define the color of the to be painted tile. The color indicates the state of the tile 
					// if the unit has a move and attack left color is purple 
					if ((unit.attackLeft && unit.moveLeft) || unit.tile.searching) {
						color = new Color(206, 119,206);
					// if the unit has only an attack left color the tile red
					} 
					else if (unit.attackLeft || unit.tile.target) {
						color = new Color(201, 116,118);
					// if the unit has only a move left color the tile blue
					} 
					else if (unit.moveLeft || unit.tile.option) {
						color = new Color(117, 116,190);
					}
					// If the to be colored tile is the selected tile color it dark green
					if (mouseHandler.selectedTile != null && hexLocation.equals(mouseHandler.selectedTile.location)) {
						color = new Color(0, 70,0);
					}
				}
				
				// Loop over adjacent tiles and color the tiles corresponding to their state
				if (mouseHandler.selectedTile != null) {
					for (int k = 0; k < 6; k++) {
						try {
							AdjacentTile = mouseHandler.selectedTile.adjacentTiles.get(k);
							if (AdjacentTile.location.equals(hexLocation))  {
								if (AdjacentTile!= null) {
									// If adjacent tile is a legal move give color it green
									if (mouseHandler.selectedTile.isLegal(AdjacentTile)) {
										color = new Color(0, 130,0);
									}
									// If adjacent tile contains an enemy color it red
									if (mouseHandler.selectedTile.unit.isHostile(AdjacentTile)) {
										color = new Color(80, 0,0);
									}
								}
							}
						} catch (IndexOutOfBoundsException | NullPointerException e) {}
					} 
				}
				
				// If the mouse is on the to be colored tile color it light green
				if (mouseHandler.pixelToHex(location.x, location.y).equals(mouseHandler.currenTileCoords)) {
					color = new Color(154, 255,158);
				}
				
				// Draw the hexagon (tile) using the appropriate color 
				drawHexagon(g, location, HEXSIZE.x-4, HEXSIZE.y-4, color);
				// Reset color to default 
				color = new Color(97, 168,104);
				int a = location.x;
				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
				// Move to the next tile (vertically)
				location.move(a ,b);
			}
			// Move to the next tile (horizontally)
			int a = location.x + (int)(HEXSIZE.x*1.5);
			int b = HEXSTART.y - (int)(0.5 *((j+1)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
			location.move(a ,b);
		}
		
		// Draw right side of the board 
		int b1 = HEXSTART.y - (int)(((1.5)*(Math.sqrt(3f) / 2f *(HEXSIZE.y*2f))));
		location.move(location.x ,b1);
		// Loop horizontally 
		for(int j = 4; j > 0; j--) {
			// Loop vertically
			for(int i = 5; i > 1-j; i--) {
				hexLocation = mouseHandler.pixelToHex(location.x, location.y);
				unit = grid.getTile(hexLocation.x, hexLocation.y).unit;
				if (unit != null) {
					// The following statements define the color of the to be painted tile. The color indicates the state of the tile 
					// if the unit has a move and attack left color is purple 
					if (unit.attackLeft && unit.moveLeft) {
						color = new Color(206, 119,206);
					// if the unit has only an attack left color the tile red
					} else if (unit.attackLeft) {
						color = new Color(201, 116,118);
					// if the unit has only a move left color the tile blue
					} else if (unit.moveLeft) {
						color = new Color(117, 116,190);
					}
					// If the to be colored tile is the selected tile color it dark green
					if (mouseHandler.selectedTile != null && hexLocation.equals(mouseHandler.selectedTile.location)) {
						color = new Color(0, 70,0);
					}
				}
				// Loop over adjacent tiles and color the tiles corresponding to their state
				if (mouseHandler.selectedTile != null) {
					for (int k = 0; k < 6; k++) {
						try {
							AdjacentTile = mouseHandler.selectedTile.adjacentTiles.get(k);
							if (AdjacentTile.location.equals(hexLocation))  {
								if (AdjacentTile!= null) {
									// If adjacent tile is a legal move give color it green
									if (mouseHandler.selectedTile.isLegal(AdjacentTile)) {
										color = new Color(0, 130,0);
									}
									// If adjacent tile contains an enemy color it red
									if (mouseHandler.selectedTile.unit.isHostile(AdjacentTile)) {
										color = new Color(80, 0,0);
									}
								}
							}
						} catch (IndexOutOfBoundsException | NullPointerException e) {}
					} 
				}
				
				// If the mouse is on the to be colored tile color it light green
				if (mouseHandler.pixelToHex(location.x, location.y).equals(mouseHandler.currenTileCoords)) {
					color = new Color(154, 224,158);
				}
				
				// Draw the hexagon (tile) using the appropriate color 
				drawHexagon(g, location, HEXSIZE.x-4, HEXSIZE.y-4, color);
				// Reset color to default 
				color = new Color(97, 168,104);
				int a = location.x;
				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
				// Move to the next tile (vertically)
				location.move(a ,b);
			}
			// Move to the next tile (horizontally)
			int a = location.x + (int)(HEXSIZE.x*1.5);
			int b = HEXSTART.y + (int)(0.5 *((2-j)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
			location.move(a ,b);
		}		
	}
	
	/* This method draws the units on the board */
	public void drawUnits(Graphics g) throws IOException{
		// Load the images
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("General.png");
		Image general = ImageIO.read(input);
		input = classLoader.getResourceAsStream("Goblin.png");
		Image goblin = ImageIO.read(input);
		input = classLoader.getResourceAsStream("Orc.png");
		Image orc = ImageIO.read(input);
		input = classLoader.getResourceAsStream("Swordsman.png");
		Image swordsman = ImageIO.read(input);
		
		Map<String, Image> characters = new HashMap<>();
		characters.put("General", general);
		characters.put("Goblin", goblin);
		characters.put("Orc", orc);
		characters.put("Swordsman", swordsman);
		
		// Loop over over tiles and draw the unit if present
		HashMap<String,Tile> tiles = grid.gridMap;
		for (Tile tile: tiles.values()) {
			if (tile.unit != null) {
				String foundCharacter = tile.unit.name;
				if (foundCharacter != null) {
					Point location = mouseHandler.hexToPixel(tile.x, tile.y);
					int newLocX = location.x+HEXSTART.x+HEXSIZE.x*5 +2;
					int newLocY = location.y+HEXSTART.y+HEXSIZE.y*2+6;
					g.drawImage(characters.get(foundCharacter), newLocX, newLocY, this);
				}
			}
		}
	}
	
	// This method takes a size and location (among other specifications) and draws a hexagon
	public void drawHexagon(Graphics g, Point center,  int sizeX, int sizeY, Color color) {
		Polygon points = new Polygon();
		for (int i = 1; i < 7; i++) {
			double angle = i * 2 * Math.PI / 6;
			points.addPoint((int)(center.x + sizeX * Math.cos(angle)) , (int)(center.y + sizeY * Math.sin(angle)));
		}
		g.setColor(color);
		g.fillPolygon(points);
	}
}