package objects;

import pt.iscte.poo.utils.Point2D;

public abstract class Character extends Objects {

	private int health;
	private int powerAttack;
	
	public Character(Point2D initialPosition, int health , int powerAttack) {
		super(initialPosition);
		this.health = health;
		this.powerAttack = powerAttack;
	}
	
	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getPowerAttack() {
		return this.powerAttack;
	}
	
	public void setPowerAttack(int powerAttack) {
		this.powerAttack = powerAttack;
	}
	
}