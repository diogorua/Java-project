package objects;

import pt.iscte.poo.utils.Point2D;

public class Stair extends Objects implements Support {

	
	public Stair(Point2D initialPosition) {
		super(initialPosition);
	}
	
	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}

	@Override
	public String getName() {
		return "Stairs";
	}

	@Override
	public int getLayer() {
		return 0;
	}

}