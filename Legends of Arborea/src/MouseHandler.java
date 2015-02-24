import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;



public class MouseHandler implements MouseListener, MouseMotionListener {
	Point currentTile;
	int WIDTH = 1000;
	int HEIGHT = 700;
	Point HEXSTART = new Point((int)(WIDTH*0.1), (int)(HEIGHT*0.28));
	Point HEXSIZE = new Point(44,36);
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int pixelX = e.getX();
		int pixelY = e.getY();
		currentTile = pixelToHex(pixelX, pixelY);
		System.out.println(currentTile.x + " " + currentTile.y);
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
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
//	public Point pixelToHex(int x, int y){
//		int hexX = (int) ((x- HEXSTART.x-HEXSIZE.x*5) * 2/3 / HEXSIZE.x);
//		int hexY = (int) (((-x + HEXSTART.x)*0.333 + Math.sqrt(3)*0.333 * (y-HEXSTART.y-(HEXSIZE.y))) / HEXSIZE.y);
//		return new Point(hexX, hexY);
//	}
//	public Point pixelToHex(int x, int y){
//		x = x-HEXSTART.x - HEXSIZE.x*5;
//		y = y-HEXSTART.y-HEXSIZE.y*2;
//		int hexX = (int) (x * 2/3 / HEXSIZE.x);
//		int hexY = (int) ((-x*(1/3) + Math.sqrt(3)*(1/3) * y) / HEXSIZE.y);
//		System.out.println(y);
//		return new Point(hexX, hexY);
//	}
	public Point pixelToHex(int x, int y){
		x = x-5-HEXSTART.x - HEXSIZE.x*5;
		y = y-HEXSTART.y-(int)(HEXSIZE.y*2.5);
		int hexX = (int) (x / 2*3 / HEXSIZE.x);
		int hexY = (int) ((0.5/x - y) / Math.sqrt(3)  / HEXSIZE.y);
		return new Point(hexX, hexY);
	}
}