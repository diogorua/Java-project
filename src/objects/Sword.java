package objects;

import java.util.List;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Sword extends Objects implements Interactable {

	private int initialPower;
	
	public Sword(Point2D initialPosition) {
		super(initialPosition);
		this.initialPower = 10;
	}
	
	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}
	
	@Override
	public String getName() {
		return "Sword";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	public int getInitialPower() {
		return initialPower;
	}
	
	
	@Override
	public void interact() {
		List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Objects);
		
		for(Objects obj : list) {
			if(obj.getPosition().equals(this.getPosition()) && obj instanceof Manel) {
				
				int power = ((Manel) obj).getPowerAttack();
				power += this.getInitialPower();
				((Manel) obj).setPowerAttack(power);
				
				ImageGUI.getInstance().setStatusMessage("JumpMan caught the sword! Damage level has increased by " + this.getInitialPower() + " and is now " + power);
				ImageGUI.getInstance().removeImage(this); 
		        GameEngine.getInstance().getRoom().getListObjects().remove(this);
			}
		}
	}
	
}