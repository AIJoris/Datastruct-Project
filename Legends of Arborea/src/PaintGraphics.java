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
	/* Get screen information*/
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	/* Width and height of application window in pixels */
	static int WIDTH = (int)screenSize.getWidth();
	static int HEIGHT = (int)screenSize.getHeight();
	
	// CONSTRUCTER
	public PaintGraphics(Point HEXSTART1, Point HEXSIZE1, Grid grid1, MouseHandler handler){
		this.HEXSTART = HEXSTART1;
		this.HEXSIZE = HEXSIZE1;
		this.grid = grid1;
		this.mouseHandler = handler;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		drawBoard(g);
		try {
			drawDecor(g);
			addCharacters(g);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		repaint();
    }
	
	public void drawDecor(Graphics g) throws IOException{
		/* Add title */
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("title.png");
		Image title = ImageIO.read(input);
		g.drawImage(title,(int)(WIDTH/4.79), HEIGHT/18, this);
		
		/* add hearts, swords and axes (for hitpoints and life representation) */
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
		
		
		/* Add score boards */
		// Load images
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
		
		// Messages
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
		
//		// load button
//		input = classLoader.getResourceAsStream("endturn.png");
//		Image endTurn = ImageIO.read(input);
//		input = classLoader.getResourceAsStream("endturnSel.png");
//		Image endTurnSel = ImageIO.read(input);
//		
//		Map<String, Image> turnButtons = new HashMap<>();
//		turnButtons.put("endTurn", endTurn);
//		turnButtons.put("endTurnSel", endTurnSel);
		
		// Left panel
		try{
			g.drawImage(characters.get(mouseHandler.currentUnit.name),(HEIGHT/2)-imWidth, HEXSTART.y, this);
			int xOffset = (HEIGHT/2)-imWidth;
			Point location = new Point(xOffset, HEXSTART.y + 70);
			int hitPoints = mouseHandler.currentUnit.hitPoints;
			for (int i = 0; i < hitPoints; i++) {
				if(i< 5){
					g.drawImage(icons.get("heart"),location.x+(i*35), location.y, this);
				} else {
					g.drawImage(icons.get("heart"),location.x+((i-5)*35), location.y+35, this);
				}

			}
			int weaponSkill = mouseHandler.currentUnit.weaponSkill;
			weaponSkill += mouseHandler.selectedTile.getBuffer();
			String weapon = mouseHandler.currentUnit.weapon;
			location = new Point(xOffset, HEXSTART.y + 140);
			for (int i = 0; i < weaponSkill; i++) {
				if(i< 5){
					g.drawImage(icons.get(weapon),location.x+(i*35), location.y, this);
				} else if (i< 10){
					g.drawImage(icons.get(weapon),location.x+((i-5)*35), location.y+35, this);
				} else {
					g.drawImage(icons.get(weapon),location.x+((i-10)*35), location.y+70, this);
				}

			}
			
			
		} catch (NullPointerException e){}
		// Right panel
		try {
			Tile tempTile = grid.getTile(mouseHandler.currenTileCoords.x, mouseHandler.currenTileCoords.y);
			g.drawImage(characters.get(tempTile.unit.name),(WIDTH/2)+ imWidth, HEXSTART.y, this);
			int xOffset = (WIDTH/2)+ imWidth;
			Point location = new Point(xOffset, HEXSTART.y + 70);
			int hitPoints = tempTile.unit.hitPoints;
			for (int i = 0; i < hitPoints; i++) {
				if(i< 5){
					g.drawImage(heart,location.x+(i*35), location.y, this);
				} else {
					g.drawImage(heart,location.x+((i-5)*35), location.y+35, this);
				}
				
			}
			int weaponSkill = tempTile.unit.weaponSkill;
			weaponSkill += tempTile.getBuffer();

			String weapon = tempTile.unit.weapon;
			location = new Point(xOffset, HEXSTART.y + 140);
			for (int i = 0; i < weaponSkill; i++) {
				if(i< 5){
					g.drawImage(icons.get(weapon),location.x+(i*35), location.y, this);
				} else if (i< 10){
					g.drawImage(icons.get(weapon),location.x+((i-5)*35), location.y+35, this);
				} else {
					g.drawImage(icons.get(weapon),location.x+((i-10)*35), location.y+70, this);
				}

			}
        } catch (NullPointerException e) {}
		
		// Update panel on the bottom
		Point messageLocation = new Point((WIDTH/2)-244, HEIGHT/7);
		String foundMessage = grid.message;
		if(foundMessage != null){
			g.drawImage(messages.get(foundMessage),messageLocation.x, messageLocation.y, this);
		}
		
//		// end turn button
//		Point buttonLocation = new Point((WIDTH/2)-180, HEIGHT/4-10);
//		g.drawImage(turnButtons.get(mouseHandler.button),buttonLocation.x, buttonLocation.y, this);
//		turnButtons.get(mouseHandler.button).setToolTipText("This shows up on mouse hover");
		

	}
	
	public void drawBoard(Graphics g){
		// color to default
		Color color = new Color(97, 168,104);
		Tile AdjacentTile;
		Point location = new Point(HEXSTART.x, HEXSTART.y);
		Point hexLocation = new Point(0,0);
		// LEFT SIDE
		for(int j = 0; j < 5; j++){
			for(int i = 0; i < 5+j; i++){
				hexLocation = mouseHandler.pixelToHex(location.x, location.y);
				
				// These define the colors of the tiles under the units of the player whos turn it is
				// if the unit has a move and attack left color is purple
				if(grid.getTile(hexLocation.x, hexLocation.y).attackLeft && grid.getTile(hexLocation.x, hexLocation.y).moveLeft){
					color = new Color(206, 119,206);
				// if the unit has only an attack left color the tile red
				} else if(grid.getTile(hexLocation.x, hexLocation.y).attackLeft){
					color = new Color(201, 116,118);
				// if the unit has only a move left color the tile blue
				} else if(grid.getTile(hexLocation.x, hexLocation.y).moveLeft){
					color = new Color(117, 116,190);
				}
				
				// If the to be colored tile is the selected tile color it distinctively
				if( mouseHandler.selectedTile != null && hexLocation.equals(mouseHandler.selectedTile.location)){
					color = new Color(0, 70,0);
				}
				
				// Adjacent tiles (legal moves and surrounding hostiles)
				if( mouseHandler.selectedTile != null){
					for (int k = 0; k < 6; k++) {
						try {
							AdjacentTile = mouseHandler.selectedTile.adjacentTiles.get(k);
							if(AdjacentTile.location.equals(hexLocation)){
								if(AdjacentTile!= null){
									System.out.println(AdjacentTile.location);
									
									// If adjacent tile is legal give color it dark green
									if(mouseHandler.selectedTile.isLegal(AdjacentTile)){
										color = new Color(0, 130,0);
									}
									// If adjacent tile contain an enemy color it red
									if(mouseHandler.selectedTile.isHostile(AdjacentTile)){
										color = new Color(80, 0,0);
									}
								}
							}
						} catch (IndexOutOfBoundsException | NullPointerException e){}
					} 
				}
				
				// If the mouse is on the to be colored tile color is light green
				if (mouseHandler.pixelToHex(location.x, location.y).equals(mouseHandler.currenTileCoords)){
					color = new Color(154, 224,158);
				}
				
				drawHexagon(g, location, HEXSIZE.x-4, HEXSIZE.y-4, color);
				// Reset color to default
				color = new Color(97, 168,104);
				int a = location.x;
				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
				location.move(a ,b);
			}
			int a = location.x + (int)(HEXSIZE.x*1.5);
			int b = HEXSTART.y - (int)(0.5 *((j+1)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
			location.move(a ,b);
		}
		
		// RIGHT SIDE
		int b1 = HEXSTART.y - (int)(((1.5)*(Math.sqrt(3f) / 2f *(HEXSIZE.y*2f))));
		location.move(location.x ,b1);
		
		for(int j = 4; j > 0; j--){
			for(int i = 5; i > 1-j; i--){
				hexLocation = mouseHandler.pixelToHex(location.x, location.y);
				
				// These define the colors of the tiles under the units of the player whos turn it is
				// if the unit has a move and attack left color is purple
				if(grid.getTile(hexLocation.x, hexLocation.y).attackLeft && grid.getTile(hexLocation.x, hexLocation.y).moveLeft){
					color = new Color(206, 119,206);
				// if the unit has only an attack left color the tile red
				} else if(grid.getTile(hexLocation.x, hexLocation.y).attackLeft){
					color = new Color(201, 116,118);
				// if the unit has only a move left color the tile blue
				} else if(grid.getTile(hexLocation.x, hexLocation.y).moveLeft){
					color = new Color(117, 116,190);
				}
				
				// If the to be colored tile is the selected tile color it distinctively
				if( mouseHandler.selectedTile != null && hexLocation.equals(mouseHandler.selectedTile.location)){
					color = new Color(0, 70,0);
				}
				
				// Adjacent tiles (legal moves and surrounding hostiles)
				if( mouseHandler.selectedTile != null){
					for (int k = 0; k < 6; k++) {
						try {
							AdjacentTile = mouseHandler.selectedTile.adjacentTiles.get(k);
							if(AdjacentTile.location.equals(hexLocation)){
								if(AdjacentTile!= null){
									// If adjacent tile is legal give color it dark green
									if(mouseHandler.selectedTile.isLegal(AdjacentTile)){
										color = new Color(0, 130,0);
									}
									// If adjacent tile contain an enemy color it red
									if(mouseHandler.selectedTile.isHostile(AdjacentTile)){
										color = new Color(80, 0,0);
									}
								}
							}
						} catch (IndexOutOfBoundsException | NullPointerException e){}
					} 
				}
				
				// If the mouse is on the to be colored tile color is light green
				if (mouseHandler.pixelToHex(location.x, location.y).equals(mouseHandler.currenTileCoords)){
					color = new Color(154, 224,158);
				}
				
				drawHexagon(g, location, HEXSIZE.x-4, HEXSIZE.y-4, color);
				color = new Color(97, 168,104);
				int a = location.x;
				int b = location.y + (int)(Math.sqrt(3f) / 2f *(HEXSIZE.y*2f));
				location.move(a ,b);
			}
			int a = location.x + (int)(HEXSIZE.x*1.5);
			int b = HEXSTART.y + (int)(0.5 *((2-j)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
			location.move(a ,b);
		}		
	}
	
	public void addCharacters(Graphics g) throws IOException{
		// Load images
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
		
		HashMap<String,Tile> tiles = grid.gridMap;
		for(Tile tile: tiles.values()){
			if(tile.unit != null){
				String foundCharacter = tile.unit.name;
				if(foundCharacter != null){
					Point location = mouseHandler.hexToPixel(tile.x, tile.y);
					int newLocX = location.x+HEXSTART.x+HEXSIZE.x*5 +2;
					int newLocY = location.y+HEXSTART.y+HEXSIZE.y*2+6;
					g.drawImage(characters.get(foundCharacter), newLocX, newLocY, this);
				    
					
				}
			}
		}
	}
	
	public void drawHexagon(Graphics g, Point center,  int sizeX, int sizeY, Color color){
		Polygon points = new Polygon();
		for(int i = 1; i < 7; i++){
			double angle = i * 2 * Math.PI / 6;
			points.addPoint((int)(center.x + sizeX * Math.cos(angle)) , (int)(center.y + sizeY * Math.sin(angle)));
		}
		g.setColor(color);
		g.fillPolygon(points);
	}
	
	
}




