package objects;

import java.util.Random;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import java.util.List;

public class Bat extends Objects implements Interactable, Villain {
	
	private Random random;
	private int powerAttack;
	
	public Bat(Point2D initialPosition) {
		super(initialPosition);
		this.random = new Random();
		this.powerAttack = 20;
	}
	
	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}
	
	@Override
	public String getName() {
		return "Bat";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}
	
	
	@Override
	public void interact() {
		List<Objects> list = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Objects);
		
		for(Objects obj : list) {
			if(obj instanceof Manel && Room.isNextTo(this.getPosition(), obj)) {
				
				int health = ((Manel) obj).getHealth();
				health -= this.powerAttack;
				((Manel) obj).setHealth(health);
					
				ImageGUI.getInstance().setStatusMessage("JumpMan was atacked! Life: " + ((Manel) obj).getHealth() + "/100");
				ImageGUI.getInstance().removeImage(this); 
			    GameEngine.getInstance().getRoom().getListObjects().remove(this);
		        
		        if(((Manel) obj).getHealth() <= 0) {
					((Manel) obj).takeLife();
				}
			}
		}
	}
	
	
	@Override
	public void moveVillain() {   //sempre que encontrar umas escadas, o morcego desce automaticamente
		boolean goDown = false;
		Point2D novaPosicao = getPosition();
	    List<Objects> stairs = GameEngine.getInstance().getRoom().getList(obj -> obj instanceof Stair);
	    
	    Point2D abaixo = getPosition().plus(Direction.DOWN.asVector());

	    for (Objects obj : stairs) {
	        if (Room.isUnderObject(novaPosicao, obj)) {    //verifica se existem escadas na posição abaixo do morcego
	            goDown = true;
	            break;                       //assim que encontra umas escadas abaixo da posição do morcego, interrompe o ciclo 
	        }
	    }
	    
	    if(goDown) {  
	    	novaPosicao = abaixo;
	    }else {
	    	int direction = random.nextInt(2); 
		    switch (direction) {
		    
		        case 0:
		            novaPosicao = getPosition().plus(Direction.LEFT.asVector());
		            break;
		            
		        case 1:
		            novaPosicao = getPosition().plus(Direction.RIGHT.asVector());
		            break;
		    }
	    }

	    if (ImageGUI.getInstance().isWithinBounds(novaPosicao.scaleIsotropical(49)) && !GameEngine.getInstance().getRoom().isBlocked(novaPosicao)) {
	        setPosition(novaPosicao);
	    }
	}
}