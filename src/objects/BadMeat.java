package objects;

import java.util.List;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class BadMeat extends Objects implements Interactable {

	private int damage;
	
	public BadMeat(Point2D initialPosition) {
		super(initialPosition);
		this.damage = 10;
	}
	
	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}

	public int getDamage() {
		return damage;
	}
	
	@Override
	public String getName() {
		return "BadMeat";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	
	public void interact() {
		List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Objects);
		
		for(Objects obj : list) {
			if(obj instanceof Manel && obj.getPosition().equals(this.getPosition())) {
				
				int health = ((Manel) obj).getHealth();
				health -= this.getDamage();
				((Manel) obj).setHealth(health);
			
				ImageGUI.getInstance().setStatusMessage("JumpMan lost energy: Life: " + ((Manel) obj).getHealth() + "/100");
				ImageGUI.getInstance().removeImage(this); 
				GameEngine.getInstance().getRoom().getListObjects().remove(this);
		        
		        if(((Manel) obj).getHealth() <= 0) {
					((Manel) obj).takeLife();
				}
			}
		}
	}	
}