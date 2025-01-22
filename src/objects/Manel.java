package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Manel extends Character implements Damageable, Tick {
	
	private boolean hasBomb = false;
	private Point2D savePosition;
	private int lifes = 3;
	
	public Manel(Point2D initialPosition, int health, int powerAttack){
		super(initialPosition, health, powerAttack);
	}

	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}

	@Override
	public String getName() {
		return "JumpMan";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	public void setHasBomb(boolean value) {
		hasBomb = value;
	}
	
	public void savePosition(Point2D position) {
		savePosition = position;
	}
	
	public Point2D getSavedPosition() {
		return savePosition;
	}

	public int getHealth() {
		return super.getHealth();
	}
	
	public void setHealth(int health) {
		super.setHealth(health);
	}
	
	public int getLifes() {
		return lifes;
	}
	
	public void setLifes(int life) {
		this.lifes = life;
	}
	
	public int getPowerAttack() {
		return super.getPowerAttack();
	}

	public void setPowerAttack(int powerAttack) {
		super.setPowerAttack(powerAttack);
	}

	public void moveJumpMan(Direction d) {
		setPosition(getPosition().plus(d.asVector()));
	}
	
	public void takeLife () {   //função que define sempre o que se sucede quando a vida do JumpMan chega ao fim, evitando código repetido nas outras classes dos objetos que atacam o JumpMan
		if(lifes > 0) {	
			lifes --;
			this.setLifes(lifes);
			this.setHealth(100);
			this.setPowerAttack(15);   //quando o JumpMan perde uma vida (mas ainda não perdeu as 3), optámos por retirar o poder da espada que aumentava o seu poder de ataque 
			hasBomb = false;          //se o JumpMan perde uma vida, mesmo que já tenha apanhado a bomba, não vai conseguir plantá-la na room, ou seja, perde a bomba sempre que morre
			ImageGUI.getInstance().setStatusMessage("JumpMan lost a life. Life remains: " + this.getLifes());
		}
		
		if(lifes == 0){
			ImageGUI.getInstance().setStatusMessage("JumpMan killed. New game started. Life remains 3");
			this.setHealth(100);
			this.setPowerAttack(15);
			hasBomb = false;
		}
		
		ImageGUI.getInstance().removeImage(this);
		GameEngine.getInstance().getRoom().getListObjects().remove(this);
		GameEngine.getInstance().getRoom().restart();
	}
	
	
	@Override
	public void takeDamage(Character c) {
		int health = c.getHealth();
		health -= this.getPowerAttack();
		c.setHealth(health);
		
		ImageGUI.getInstance().setStatusMessage(c.getName() + " was atacked! Life: " + c.getHealth() + "/100");
		
		if(c.getHealth() <= 0) {
			ImageGUI.getInstance().removeImage(c);
			GameEngine.getInstance().getRoom().getListObjects().remove(c);
			ImageGUI.getInstance().setStatusMessage(c.getName() + " killed!");
		}
	}
	
	
	@Override
	public void tick() {
		int k = ImageGUI.getInstance().keyPressed();
		
		if(k == 66) {  //se pressionar a tecla 'b', larga a bomba (apenas se já a tivesse apanhado antes)
			plantBomb();
		}
		
	}
	
	private void plantBomb() {
		if(!hasBomb) {          //se não apanhou a bomba, não consegue plantá-la na sala
			return;
		}
		
		Bomb bomb = new Bomb(this.getPosition());
		ImageGUI.getInstance().addImage(bomb);
		GameEngine.getInstance().getRoom().getListObjects().add(bomb);
		
		bomb.setPosition(this.getPosition());
		hasBomb = false;
		bomb.setFirstCatch(false);    //se o JumpMan planta a bomba a 1 vez, já não a vai voltar a apanhar (torna-se instransponível)
		bomb.setDrop(true);
	}
	
}