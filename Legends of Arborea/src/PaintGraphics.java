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
		input = classLoader.getResourceAsStream("heart.png");
		Image heart = ImageIO.read(input);
//		input = classLoader.getResourceAsStream("heart.png");
//		Image heart = ImageIO.read(input);
//		input = classLoader.getResourceAsStream("heart.png");
//		Image heart = ImageIO.read(input);
//		input = classLoader.getResourceAsStream("heart.png");
//		Image heart = ImageIO.read(input);
//		input = classLoader.getResourceAsStream("heart.png");
//		Image heart = ImageIO.read(input);
		
		Map<String, Image> characters = new HashMap<>();
		characters.put("General", general);
		characters.put("Goblin", goblin);
		characters.put("Orc", orc);
		characters.put("Swordsman", swordsman);
		int imWidth = general.getWidth(this);
		
		// Left panel
		try{
			g.drawImage(characters.get(mouseHandler.currentUnit.name),(HEIGHT/2)-imWidth, HEXSTART.y, this);
//				mouseHandler.currentUnit.hitPoints
			int xOffset = (HEIGHT/2)-imWidth;
			Point location = new Point(xOffset, HEXSTART.y + 70);
			int hitPoints = mouseHandler.currentUnit.hitPoints;
			for (int i = 0; i < hitPoints; i++) {
				if(i< 5){
					g.drawImage(heart,location.x+(i*35), location.y, this);
				} else {
					g.drawImage(heart,location.x+((i-5)*35), location.y+35, this);
				}

			}
			
		} catch (NullPointerException e){}
		// Right panel
		try {
			g.drawImage(characters.get(grid.getTile(mouseHandler.currenTileCoords.x, mouseHandler.currenTileCoords.y).unit.name),(WIDTH/2)+ imWidth, HEXSTART.y, this);
			int xOffset = (WIDTH/2)+ imWidth;
			Point location = new Point(xOffset, HEXSTART.y + 70);
			int hitPoints = grid.getTile(mouseHandler.currenTileCoords.x, mouseHandler.currenTileCoords.y).unit.hitPoints;
			for (int i = 0; i < hitPoints; i++) {
				if(i< 5){
					g.drawImage(heart,location.x+(i*35), location.y, this);
				} else {
					g.drawImage(heart,location.x+((i-5)*35), location.y+35, this);
				}
				
			}
        } catch (NullPointerException e) {}
		
		// Update panel on the bottom
		
		
		
	}
	
	public void drawBoard(Graphics g){
//		// OUTER
		Color color = new Color(0, 100,0);
		Point location = new Point(0,0);
//		location.move(HEXSTART.x, HEXSTART.y);
//		for(int j = 0; j < 5; j++){
//			for(int i = 0; i < 5+j; i++){
//				drawHexagon(g, location, HEXSIZE.x, HEXSIZE.y, color);
//				int a = location.x;
//				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
//				location.move(a ,b);
//			}
//			int a = location.x + (int)(HEXSIZE.x*1.5);
//			int b = HEXSTART.y - (int)(0.5 *((j+1)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
//			location.move(a ,b);
//		}
//		
//		int b1 = HEXSTART.y - (int)(((1.5)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
//		location.move(location.x ,b1);
//		
//		for(int j = 4; j > 0; j--){
//			for(int i = 5; i > 1-j; i--){
//				drawHexagon(g, location, HEXSIZE.x, HEXSIZE.y, color);
//				int a = location.x;
//				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
//				location.move(a ,b);
//			}
//			int a = location.x + (int)(HEXSIZE.x*1.5);
//			int b = HEXSTART.y + (int)(0.5 *((2-j)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
//			location.move(a ,b);
//		}
		
		
		// INNER *************
		color = new Color(0, 180,0);
		location = new Point(0,0);
		location.move(HEXSTART.x + 2, HEXSTART.y + 2);
		for(int j = 0; j < 5; j++){
			for(int i = 0; i < 5+j; i++){
				if( mouseHandler.selectedTile != null && mouseHandler.pixelToHex(location.x, location.y).equals(mouseHandler.selectedTile.location)){
					color = new Color(0, 70,0);
				}
				if( mouseHandler.selectedTile != null){
					for (int k = 0; k < 6; k++) {
						try { 
							if(mouseHandler.selectedTile.adjacentTiles.get(k).location.equals(mouseHandler.pixelToHex(location.x, location.y))){
								if(mouseHandler.selectedTile.adjacentTiles.get(k)!= null){
									if(mouseHandler.selectedTile.isLegal(mouseHandler.selectedTile.adjacentTiles.get(k))){
										color = new Color(0, 130,0);
									}
								}
							}
						} catch (IndexOutOfBoundsException e){}
					} 
				}
				if (mouseHandler.pixelToHex(location.x, location.y).equals(mouseHandler.currenTileCoords)){
					color = new Color(0, 230,0);
				}
				drawHexagon(g, location, HEXSIZE.x-4, HEXSIZE.y-4, color);
				color = new Color(0, 180,0);
				int a = location.x;
				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
				location.move(a ,b);
			}
			int a = location.x + (int)(HEXSIZE.x*1.5);
			int b = HEXSTART.y - (int)(0.5 *((j+1)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
			location.move(a ,b);
		}
		
		int b1 = HEXSTART.y - (int)(((1.5)*(Math.sqrt(3f) / 2f *(HEXSIZE.y*2f))));
		location.move(location.x ,b1);
		
		for(int j = 4; j > 0; j--){
			for(int i = 5; i > 1-j; i--){
				if( mouseHandler.selectedTile != null && mouseHandler.pixelToHex(location.x, location.y).equals(mouseHandler.selectedTile.location)){
					color = new Color(0, 70,0);
				}
				
				if( mouseHandler.selectedTile != null){
					for (int k = 0; k < 6; k++) {
						try { 
							if(mouseHandler.selectedTile.adjacentTiles.get(k).location.equals(mouseHandler.pixelToHex(location.x, location.y))){
								if(mouseHandler.selectedTile.adjacentTiles.get(k)!= null){
									if(mouseHandler.selectedTile.isLegal(mouseHandler.selectedTile.adjacentTiles.get(k))){
										color = new Color(0, 130,0);
									}
									if(mouseHandler.selectedTile.isHostile(mouseHandler.selectedTile.adjacentTiles.get(k))){
										color = new Color(80, 0,0);
									}
								}
							}
						} catch (IndexOutOfBoundsException e){}
					} 
				}
				
				if (mouseHandler.pixelToHex(location.x, location.y).equals(mouseHandler.currenTileCoords)){
					color = new Color(0, 230,0);
				}
				
				drawHexagon(g, location, HEXSIZE.x-4, HEXSIZE.y-4, color);
				color = new Color(0, 180,0);
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




