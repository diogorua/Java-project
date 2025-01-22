package objects;

import java.util.List;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.utils.Point2D;

public class Door extends Objects implements Interactable{
	
	public Door(Point2D initialPosition) {
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
		return "DoorClosed";
	}
	
	@Override
	public int getLayer() {
		return 0;
	}
	

	@Override
	public void interact() {
		List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Objects);
		
		for(Objects obj : list) {
			if(obj instanceof Manel && this.getPosition().equals(obj.getPosition())) {
				GameEngine.getInstance().getRoom().nextLevel();   //se o JumpMan passa na porta, segue para o próximo nível
			}
		}
	}
	
}