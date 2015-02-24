import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
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
	Point selectedTile;
	MouseHandler mouseHandler;
	
	// CONSTRUCTER
	public PaintGraphics(Point HEXSTART1, Point HEXSIZE1, Grid grid1, Point selectedTileNr, MouseHandler handler){
		this.HEXSTART = HEXSTART1;
		this.HEXSIZE = HEXSIZE1;
		this.grid = grid1;
		this.selectedTile = selectedTileNr;
		this.mouseHandler = handler;
	}
	
	public void paint(Graphics g){
		drawBoard(g);
		try {
			addCharacters(g);
		} catch (IOException e) {
			e.printStackTrace();
		}
		repaint();
    }
	
	public void drawBoard(Graphics g){
		// OUTER
		Color color = new Color(0, 100,0);
		Point location = new Point(0,0);
		location.move(HEXSTART.x, HEXSTART.y);
		for(int j = 0; j < 5; j++){
			for(int i = 0; i < 5+j; i++){
				drawHexagon(g, location, HEXSIZE.x, HEXSIZE.y, color);
				int a = location.x;
				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
				location.move(a ,b);
			}
			int a = location.x + (int)(HEXSIZE.x*1.5);
			int b = HEXSTART.y - (int)(0.5 *((j+1)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
			location.move(a ,b);
		}
		
		int b1 = HEXSTART.y - (int)(((1.5)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
		location.move(location.x ,b1);
		
		for(int j = 4; j > 0; j--){
			for(int i = 5; i > 1-j; i--){
				drawHexagon(g, location, HEXSIZE.x, HEXSIZE.y, color);
				int a = location.x;
				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
				location.move(a ,b);
			}
			int a = location.x + (int)(HEXSIZE.x*1.5);
			int b = HEXSTART.y + (int)(0.5 *((2-j)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
			location.move(a ,b);
		}
		
		
		// INNER *************
		color = new Color(0, 180,0);
		location = new Point(0,0);
		location.move(HEXSTART.x + 2, HEXSTART.y + 2);
		for(int j = 0; j < 5; j++){
			for(int i = 0; i < 5+j; i++){
				if (location == selectedTile){
					color = new Color(0, 230,0);
					System.out.println(selectedTile);
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
		
		b1 = HEXSTART.y - (int)(((1.5)*(Math.sqrt(3) / 2 *(HEXSIZE.y*2))));
		location.move(location.x ,b1);
		
		for(int j = 4; j > 0; j--){
			for(int i = 5; i > 1-j; i--){
				if (location == selectedTile){
					color = new Color(0, 230,0);
					System.out.println(selectedTile);
				}
				drawHexagon(g, location, HEXSIZE.x-4, HEXSIZE.y-4, color);
				color = new Color(0, 180,0);
				int a = location.x;
				int b = location.y + (int)(Math.sqrt(3) / 2 *(HEXSIZE.y*2));
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
		input = classLoader.getResourceAsStream("selected.png");
		Image selected = ImageIO.read(input);
		
		Map<String, Image> characters = new HashMap<>();
		characters.put("General", general);
		characters.put("Goblin", goblin);
		characters.put("Orc", orc);
		characters.put("Swordsman", swordsman);

		HashMap<String,Tile> tiles = grid.grid;
		for(Tile tile: tiles.values()){
			if(tile.unit != null){
				String foundCharacter = tile.unit.name;
				if(foundCharacter != null){
					Point location = hexToPixel(tile.x, tile.y);
					int newLocX = location.x+HEXSTART.x+HEXSIZE.x*5;
					int newLocY = location.y+HEXSTART.y+HEXSIZE.y*2;
					if(tile.unit.selected){
						g.drawImage(selected, newLocX, newLocY, this);
				    }
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
	
	public Point hexToPixel(int x, int y){
		int pixelx = (int) (HEXSIZE.x * 3/2 * x)+5;
	    int pixely = (int) (HEXSIZE.y * Math.sqrt(3) * (y + x*0.5)); 
	    return new Point(pixelx, pixely);
	}
	
	public Point pixelToHex(int x, int y){
		x = x-5-HEXSTART.x - HEXSIZE.x*5;
		y = y-HEXSTART.y-HEXSIZE.y*2;
		int hexX = (int) (x / 2*3 / HEXSIZE.x);
		int hexY = (int) ((0.5/x - y) / 9  / HEXSIZE.y);
		return new Point(hexX, hexY);
	}

//	function hex_to_pixel(hex):
//	    x = size * 3/2 * hex.q
//	    y = size * sqrt(3) * (hex.r + hex.q/2)
//	    return Point(x, y)
//	    		
//	function pixel_to_hex(x, y):
//	    q = x * 2/3 / size
//	    r = (-x / 3 + sqrt(3)/3 * y) / size
//	    return hex_round(Hex(q, r))
}




