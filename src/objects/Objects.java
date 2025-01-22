package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class Objects implements ImageTile {

	private Point2D initialPosition;
	
	public Objects(Point2D initialPosition) {
		this.initialPosition = initialPosition;
	}
	
	
	public Point2D getPosition() {
		return initialPosition;
	}
	
	public void setPosition(Point2D position) {
		this.initialPosition = position;	
	}
	
	public abstract String getName();
	
	public abstract int getLayer();
	
}