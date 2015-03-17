import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/*
 * The MouseHandler class implements the MouseListen an MouseMotionListener. 
 * It determines the actions that have to be taken when the mouse is moved
 * or clicked at a specific location. This was the game play is provided.
 */
public class MouseHandler implements MouseListener, MouseMotionListener {
	Tile currentTile;
	Tile selectedTile;
	Point currenTileCoords;
	Point HEXSTART;
	Point HEXSIZE;
	MouseEvent event;
	Grid grid;
	Unit currentUnit;
	String button;
	
	/*
	 * The constructor determines some needed dimensions
	 */
	public MouseHandler(Grid grid1,Point HEXSIZE1, Point HEXSTART1){
		grid = grid1;
		HEXSTART = HEXSTART1;
		HEXSIZE = HEXSIZE1;
	}
	
	/*
	 * If the mouse clicked select a tile
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int pixelX = e.getX();
		int pixelY = e.getY();
		currentTile = grid.getTile(pixelToHex(pixelX, pixelY).x, pixelToHex(pixelX, pixelY).y);
		selectedTile = currentTile; 
		if (currentTile!=null) {
			currentUnit = currentTile.unit;
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
	}
	
	/*
	 * When the mouse is moved a hovering effect will show
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		event = e;
		int pixelX = e.getX();
		int pixelY = e.getY();
		currenTileCoords = pixelToHex(pixelX, pixelY);
		
	}
	
	/*
	 * Gets a hexagon's axial coordinate an an input and converts it to a pixel coordinate
	 */
	public Point hexToPixel(int q, int r){
		float pixelx = (float) (HEXSIZE.x * 3f/2f * q) +5;
	    float pixely = (float) (HEXSIZE.y * Math.sqrt(3f) * (r + (q/2f))); 
	    return new Point((int)pixelx, (int)pixely);
	}
	
	/*
	 * Converts axial coordinates to cube coordinates
	 */
	public float[] hexToCube(float q, float r){
		float[] cubeCoords = new float[3];
		cubeCoords[0] = q;
		cubeCoords[1] = r;
		cubeCoords[2] = -q-r;
		return cubeCoords;
	}
    
	/*
	 * Rounds cubic coordinates
	 */
	public int[] cubeRound(float x, float z, float y){
		int[] roundedCube = new int[3];
		int rx = (int)x;
		int ry = (int)y;
		int rz = (int)z;
		
		float diffX = Math.abs(rx - x);
		float diffY = Math.abs(ry - y);
		float diffZ = Math.abs(rz - z);
		
		if (diffX > diffY && diffX > diffZ) {
			rx = -ry-rz;
		} else if(diffY > diffZ){
			ry = -rx-rz;
		} else{
			rz = -rx-ry;
		}
		roundedCube[0] = rx;
		roundedCube[1] = rz;
		roundedCube[2] = ry;

		return roundedCube;
	}
	    		
	/*
	 * This method converts a pixel coordinate to a axial coordinate (does not work properly)
	 */
	public Point pixelToHex(int x, int y){
		// Add offset
		y = (y-HEXSTART.y)+5;
		x = (x-HEXSTART.x)+5; 
		// Calculate unrounder coordinates
		float hexQ = (x * (2f/3f) / HEXSIZE.x);
		float hexR = ((-x / 3f) + ((float)Math.sqrt(3f)/3f) * y) / HEXSIZE.y;
		// Round and return cooridnates
		float[] cubeCoords = hexToCube(hexQ, hexR);
		int[] roundedCoords = cubeRound(cubeCoords[0],cubeCoords[1],cubeCoords[2]);
		return new Point(roundedCoords[0]-4,roundedCoords[1]);
	}

}
