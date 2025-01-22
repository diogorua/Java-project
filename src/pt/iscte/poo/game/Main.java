package pt.iscte.poo.game;

import java.io.FileNotFoundException;
import pt.iscte.poo.gui.ImageGUI;

public class Main {

	public static void main(String[] args) throws FileNotFoundException{
		ImageGUI gui = ImageGUI.getInstance();
		GameEngine engine = GameEngine.getInstance();
		gui.setStatusMessage("Good luck!");
		gui.registerObserver(engine);
		gui.go();
	}
	
}