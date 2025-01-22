package objects;

import java.util.List;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class HiddenTrap extends Objects implements Interactable, Support, Blocked {
	
	public HiddenTrap(Point2D initialPosition) {
		super(initialPosition);
	}
	
	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}

	@Override
	public String getName() {         //a hiddenTrap é uma Wall inicialmente, por isso inicializamo-la apenas com a posição incial
		return "Wall";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	
	@Override
	public void interact() {
		List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Objects);
		
		for(Objects obj : list) {
			if(obj instanceof Manel && Room.isUnderObject(obj.getPosition(), this)) {    //vê se na posição abaixo do JumpMsn está a hiddenTrap. Se estiver, ativa a Trap
				
				ImageGUI.getInstance().removeImage(this);
				GameEngine.getInstance().getRoom().getListObjects().remove(this);
				
				Trap trap = new Trap(this.getPosition());
				ImageGUI.getInstance().addImage(trap);
				GameEngine.getInstance().getRoom().getListObjects().add(trap);
			}
		}
	}
	
}