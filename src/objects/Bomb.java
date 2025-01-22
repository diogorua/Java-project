package objects;

import java.util.List;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Bomb extends Objects implements Interactable, Tick {

	private boolean firstCatch = true;
	private boolean dropped = false;
	private int ticksPassed = 0;
	
	public Bomb(Point2D initialPosition) {
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
		return "Bomb";
	}

	@Override 
	public int getLayer() {
		return 0;
	}
	
	public void setFirstCatch(boolean value) {
		firstCatch = value;
	}
	
	public void setDrop(boolean value) {
		dropped = value;
	}
	
	
	@Override
	public void interact() {
		if(firstCatch) {     //1ª vez que o JumpMan apanha a bomba
			List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Manel);
			
			for(Objects obj : list) {
				if(obj.getPosition().equals(this.getPosition())) {
					((Manel) obj).setHasBomb(true);   //já apanhou a bomba
					ImageGUI.getInstance().removeImage(this); 
					GameEngine.getInstance().getRoom().getListObjects().remove(this);
					firstCatch = false;
					break;
				}
			}
			
		}else {  //quando o JumpMan larga a bomba (firstCatch já é false)
			List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Character);
			
			for(Objects obj : list) {
				if(obj instanceof Manel && obj.getPosition().equals(this.getPosition())) {  //alterar depois mas por enquanto fica assim (se não tivesse este código, quando o manel largava a bomba ele desaparecia)
					if(dropped) {
						setDrop(false);
						continue;
					}
				}
				
				if(obj.getPosition().equals(this.getPosition())) {  //quando o manel larga a bomba, se um dos characters pisar na bomba antes dela explodir (antes dos 5 ticks do relógio), morre e a bomba explode
					GameEngine.getInstance().getRoom().getListObjects().remove(obj);
					ImageGUI.getInstance().removeImage(obj); 
					ImageGUI.getInstance().setStatusMessage(obj.getName() + " killed");
					explode();     //chamamos a função explode outra vez mas ela não vai entrar no for uma vez que não respeita a condição do isNear
					if(obj instanceof Manel) {
						((Manel) obj).takeLife();
					}
					return;
				}
			}
			
		}
	}
	
	
	@Override
	public void tick() {  //Explode 5 tics do relógio depois de largada
		if(!firstCatch) {
			ticksPassed++;
			if(ticksPassed >= 5) {
				explode();
			}
		}
	}
	
	
	public void explode() {
		List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Objects);
		
		for(Objects obj : list) { //vê todos os objetos que num raio de 1 quadrado estão ao pé da bomba depois dela ser largada (desaparecem todos os objetos nesse raio de 1 quadrado, exceto as escadas e paredes)
			if(!(obj instanceof Wall) && !(obj instanceof Stair) && !(obj instanceof Floor)) {
				
				if(isNear(this.getPosition(), obj)) {	
				
					if(obj instanceof Manel) {
						((Manel) obj).takeLife();
					
					}else {                                               //no caso dos outros objetos, são removidos do jogo caso sejam apanhados pela bomba
						GameEngine.getInstance().getRoom().getListObjects().remove(obj);
						ImageGUI.getInstance().removeImage(obj);
						if(obj instanceof Character) {
							ImageGUI.getInstance().setStatusMessage(obj.getName() + " killed");
						}
					}
				}
			}
		}
		//quando a bomba explode, remove a bomba do jogo
		GameEngine.getInstance().getRoom().getListObjects().remove(this);
		ImageGUI.getInstance().removeImage(this);
		
	}
	
	
	public boolean isNear(Point2D position, Objects obj) {  //assim que o JumpMan larga a Bomba, explode todos os objetos que estão num raio de 1 quadrado
		if(position.distanceTo(obj.getPosition()) == 1) {
			return true;
		}
		return false;
	}
	
}