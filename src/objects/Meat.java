package objects;

import java.util.List;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Meat extends Objects implements Interactable, Tick{

	private int ticksPassed = 0;   
	
	public Meat(Point2D initialPosition) {
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
		return "GoodMeat";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	
	@Override
	public void interact() {
		List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Manel);
		
		for(Objects obj : list) {
			if(obj.getPosition().equals(this.getPosition())) {
				
				((Manel)obj).setHealth(100);       //restabelece a vida do JumpMan
				
				ImageGUI.getInstance().setStatusMessage("JumpMan resablished his Stamina!!");
				ImageGUI.getInstance().removeImage(this); 
		        GameEngine.getInstance().getRoom().getListObjects().remove(this);
			}
		}
	}
	
	
	@Override
	public void tick() { 
		ticksPassed++;
			
		if(ticksPassed >= 10) {          //se passarem 10 ticks de relógio, a carne apodrece
			GameEngine.getInstance().getRoom().getListObjects().remove(this);
			ImageGUI.getInstance().removeImage(this);
			
			BadMeat badMeat = new BadMeat(this.getPosition());
			GameEngine.getInstance().getRoom().getListObjects().add(badMeat);
			ImageGUI.getInstance().addImage(badMeat);
		}
	}
	
}