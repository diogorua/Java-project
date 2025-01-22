package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer {
	
	private Room currentRoom;
	private static GameEngine INSTANCE;
	private int lastTickProcessed = 0;
	
	public GameEngine() {
		currentRoom = new Room();
		ImageGUI.getInstance().update();
	}
	
	//segue o padrão singleton como sugerido
	public static GameEngine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameEngine();
        }
        return INSTANCE;
    }
	
	
    public Room getRoom() {
        return currentRoom;
    }
	
    
	@Override
	public void update(Observed source) {
		
		//verifica se alguma tecla foi pressionada
		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();   //vai saber qual a tecla pressionada
			System.out.println("Keypressed " + k);
			
			Point2D posicaoAtualManel = currentRoom.getManelPosition();    //vemos qual é a posição atual do manel
			
			if (Direction.isDirection(k)) {    //saber se corresponde a uma direção válida
				Direction direcao = Direction.directionFor(k);
				Point2D novaPosicao = posicaoAtualManel.plus(direcao.asVector());
				
				if(ImageGUI.getInstance().isWithinBounds(novaPosicao.scaleIsotropical(49))) {    //limitar as posições do manel, para ele não desaparecer da sala
					if(!currentRoom.isBlocked(novaPosicao)) {   //não passar por certos obstáculos definidos nessa função
						currentRoom.moveHero(direcao);
						currentRoom.interactObjects();
						currentRoom.attackVillain();  //se o manel se mover para a posição do vilão, ataca esse vilão
					}
				}
			}
		}
		
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		
		ImageGUI.getInstance().update();
	}
	
	private void processTick() {
		System.out.println("Tic Tac : " + lastTickProcessed);
		lastTickProcessed++;
		currentRoom.tick();    
	}
	
}