package objects;

import java.util.Random;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Gorila extends Character implements Damageable, Villain, Tick {

	private Random random;
	
	public Gorila(Point2D initialPosition, int health, int powerAttack) {
		super(initialPosition, health, powerAttack);
		this.random = new Random();
	}
	
	
	public Point2D getPosition() {
		return super.getPosition();
	}
	
	public void setPosition(Point2D position) {
		super.setPosition(position);
	}
	
	@Override
	public String getName() {
		return "DonkeyKong";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	public int getHealth() {
		return super.getHealth();
	}
	
	public void setHealth(int health) {
		super.setHealth(health);
	}

	public int getPowerAttack() {
		return super.getPowerAttack();
	}
	
	
	@Override
	public void takeDamage(Character c) {
		int health = c.getHealth();
		health -= this.getPowerAttack();
		c.setHealth(health);
		
		ImageGUI.getInstance().setStatusMessage(c.getName() + " was atacked! Life: " + health + "/100");
		
		if(((Character) c).getHealth() <= 0) {
			if(c instanceof Manel) {
				((Manel) c).takeLife();
			}
			/*else {    //caso em que o DonkeyKong ataca o outro DonkeyKong, código que ficará comentado como descrito no relatório
				ImageGUI.getInstance().removeImage(c);
				GameEngine.getInstance().getRoom().getListObjects().remove(c);
				ImageGUI.getInstance().setStatusMessage(c.getName() + " killed!");
			}*/
		}
	}
	
	
	@Override
	public void tick() {    //cria uma banana a cada tick de relógio na posição onde o DonkeyKong se encontra
		double rate = 0.10 + (GameEngine.getInstance().getRoom().getCurrentRoom() * 0.10);  //aumenta o número de bananas que caem por nível
		
		if(Math.random() < rate) {
			Banana banana = new Banana(this.getPosition());
			ImageGUI.getInstance().addImage(banana);
			GameEngine.getInstance().getRoom().getListObjects().add(banana);
		}	
	}
	
	
	@Override
	public void moveVillain() {
		int direction = random.nextInt(2);  
		Point2D novaPosicao = getPosition();
		Point2D posicaoJumpMan = GameEngine.getInstance().getRoom().getManelPosition();
		
		double chase = 0.10 + (GameEngine.getInstance().getRoom().getCurrentRoom() * 0.10);     //o DonkeyKong aumenta a perseguição ao JumpMan, ou seja, nos níveis mais avançados aproxima-se progressivamente do Jump-Man.
		
		if(Math.random() < chase) {
			novaPosicao = chaseTarget(novaPosicao, posicaoJumpMan);
		}else {
			switch(direction) {
			
			case 0: novaPosicao = getPosition().plus(Direction.LEFT.asVector());
					break;
					
			case 1:	novaPosicao = getPosition().plus(Direction.RIGHT.asVector());
					break;
			}
		}

		
		if(ImageGUI.getInstance().isWithinBounds(novaPosicao.scaleIsotropical(49)) && !GameEngine.getInstance().getRoom().isBlocked(novaPosicao)) {   //não sai da sala e não passa por cima dos objetos que implementam a interface Blocked
			setPosition(novaPosicao);
		}
	}
	
	private Point2D chaseTarget(Point2D attacker, Point2D target) {   //função que vê a posição do DonkeyKong em relação à posição do JumpMan
		int dx = target.getX() - attacker.getX();     //vê a posição horizontal de cada um, já que o DonkeyKong só se movimenta na horizontal
		if(dx > 0) {  //se a diferença for positiva, significa que o JumpMan está à direita do Donkey Kong e por isso o Donkey move-se para a direita
			return attacker.plus(Direction.RIGHT.asVector());
		}else {               //se a diferença for negativa, significa que o JumpMan está à esquerda do Donkey Kong e por isso o Donkey move-se para a esquerda
			return attacker.plus(Direction.LEFT.asVector());
		}
	}
	
	
}	