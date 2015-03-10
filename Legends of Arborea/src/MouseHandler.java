import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {
	Tile currentTile;
	Tile selectedTile;
	Point currenTileCoords;
	Point HEXSTART;// = new Point((int)(WIDTH*0.1), (int)(HEIGHT*0.28));
	Point HEXSIZE;// = new Point(44,44);
	MouseEvent event;
	Grid grid;
	Unit currentUnit;
	
	public MouseHandler(Grid grid1,Point HEXSIZE1, Point HEXSTART1){
		grid = grid1;
		HEXSTART = HEXSTART1;
		HEXSIZE = HEXSIZE1;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int pixelX = e.getX();
		int pixelY = e.getY();
		currentTile = grid.getTile(pixelToHex(pixelX, pixelY).x, pixelToHex(pixelX, pixelY).y);
		selectedTile = currentTile; 
		if(currentTile!=null){
			currentUnit = currentTile.unit;
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		event = e;
		int pixelX = e.getX();
		int pixelY = e.getY();
		currenTileCoords = pixelToHex(pixelX, pixelY);

		// TODO Auto-generated method stub
		
	}
	public Point hexToPixel(int q, int r){
		float pixelx = (float) (HEXSIZE.x * 3f/2f * q) +5;
	    float pixely = (float) (HEXSIZE.y * Math.sqrt(3f) * (r + (q/2f))); 
	    return new Point((int)pixelx, (int)pixely);
	}
	
	public float[] hexToCube(float q, float r){
		float[] cubeCoords = new float[3];
		cubeCoords[0] = q;
		cubeCoords[1] = r;
		cubeCoords[2] = -q-r;
		return cubeCoords;
	}
    
	public int[] cubeRound(float x, float z, float y){
		int[] roundedCube = new int[3];
		int rx = (int)x;
		int ry = (int)y;
		int rz = (int)z;
		
		float diffX = Math.abs(rx - x);
		float diffY = Math.abs(ry - y);
		float diffZ = Math.abs(rz - z);
		
		if(diffX > diffY && diffX > diffZ){
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
	    		
	public Point pixelToHex(int x, int y){
		y = (y-HEXSTART.y)+5;// -(HEXSIZE.y/3);
		x = (x-HEXSTART.x)+5; //-(HEXSIZE.x/2);
		float hexQ = (x * (2f/3f) / HEXSIZE.x);
		float hexR = ((-x / 3f) + ((float)Math.sqrt(3f)/3f) * y) / HEXSIZE.y;
		float[] cubeCoords = hexToCube(hexQ, hexR);
		int[] roundedCoords = cubeRound(cubeCoords[0],cubeCoords[1],cubeCoords[2]);
		
		return new Point(roundedCoords[0]-4,roundedCoords[1]);
	}
	
	/*
	 * Convert the coordinate of a tile to a string, so it
	 * can be used as key to access a tile in the hashmap
	 */

}