package objects;

import pt.iscte.poo.utils.Point2D;

public class Wall extends Objects implements Blocked, Support {

	
	public Wall(Point2D initialPosition) {
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
		return "Wall";
	}

	@Override
	public int getLayer() {
		return 0;
	}

}