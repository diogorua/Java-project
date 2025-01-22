package objects;

import java.util.List;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Princess extends Objects implements Interactable {
	
	
	public Princess(Point2D initialPosition) {
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
		return "Princess";
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
				int ticks = ImageGUI.getInstance().getTicks();
				ImageGUI.getInstance().showMessage("You won!", "You rescued the Princess! Congratulations!");
				String name = ImageGUI.getInstance().showInputDialog("Nome", "Insira o nome do jogador: ");
				Room.top10HighScores("highScores.txt", name, ticks);
				Room.displayHighScores("highScores.txt");
				System.exit(0);             //termina o jogo se o JumpMan chegar à princesa
			}
		}
	}
	
}