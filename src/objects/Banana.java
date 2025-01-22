package objects;

import java.util.List;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Banana extends Objects implements Interactable, Tick {

	private int attackPower;
	
	public Banana(Point2D initialPosition) {
		super(initialPosition);
		this.attackPower = 5;
	}

	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}

	@Override
	public String getName() {
		return "Banana";
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	
	@Override
	public void interact() {
		List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Objects);
		
		for(Objects obj : list) {
			if(obj instanceof Manel && obj.getPosition().equals(this.getPosition())) {
				
				int health = ((Manel) obj).getHealth();
				health -= attackPower;
				((Manel) obj).setHealth(health);
			
				ImageGUI.getInstance().setStatusMessage("JumpMan was hit by a banana! Life: " + ((Manel) obj).getHealth() + "/100");
				ImageGUI.getInstance().removeImage(this); 
				GameEngine.getInstance().getRoom().getListObjects().remove(this);
				
	        	if(((Manel) obj).getHealth() <= 0) {
					((Manel) obj).takeLife();
				}
			}
		}
	}
	
	
	@Override
	public void tick() {    //a banana desce pela sala a cada tick de relógio
		Point2D novaPosicao = this.getPosition().plus(Direction.DOWN.asVector());
		
		if (ImageGUI.getInstance().isWithinBounds(novaPosicao.scaleIsotropical(49))) { // Vê se a nova posição está dentro dos limites
			setPosition(novaPosicao);
		} else {
	    	ImageGUI.getInstance().removeImage(this); 
	    	GameEngine.getInstance().getRoom().getListObjects().remove(this);
	    }
	}
	
}