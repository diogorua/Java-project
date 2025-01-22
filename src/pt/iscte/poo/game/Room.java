package pt.iscte.poo.game;

import objects.Character;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import objects.Bat;
import objects.Blocked;
import objects.Bomb;
import objects.Damageable;
import objects.Door;
import objects.Floor;
import objects.Gorila;
import objects.HiddenTrap;
import objects.Interactable;
import objects.Manel;
import objects.Meat;
import objects.Objects;
import objects.Princess;
import objects.Stair;
import objects.Support;
import objects.Sword;
import objects.Tick;
import objects.Trap;
import objects.Villain;
import objects.Wall;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Room {
	
	private Manel manel; 
	private int currentRoom = 0;
	private List<Objects> listObjects = new ArrayList<>();
	
	public Room() {
		loadRoom("room"+currentRoom+".txt");
	}
	
	
	private void loadRoom(String ficheiro) {
	
		try {  
			File file = new File("rooms/" + ficheiro);
			
			if(!file.exists()) {
				throw new FileNotFoundException("File " + ficheiro + " not found");
			}
			
			try (Scanner s = new Scanner(file)) {
				
				int y = 0;
				
				while(s.hasNextLine()) {
					
					String line = s.nextLine();
					
					if(line.startsWith("#")) {   //ignora a 1ª linha
						line = s.nextLine();
					}
					
					if(line.isEmpty()) {
						throw new IllegalArgumentException("Falta uma linha inteira no ficheiro");
					}
					
					if(line.length() < 10) {
						System.err.println("There are missing elements in line " + (y+1));
					}
					
					for(int x = 0; x < line.length(); x++) {
						char letra = line.charAt(x);
						
						Objects obj = null;
						Point2D posicao = new Point2D(x,y);
						Objects floor = new Floor(posicao);
							
						//adiciona chão por padrão
						ImageGUI.getInstance().addImage(floor);
						listObjects.add(floor);	
						
						switch(letra) {
						
							case 'H':	
								if(manel == null) {
									this.manel = new Manel(posicao, 100, 15);    //inicializa o JumpMan com a vida cheia, e com um determinado poder de ataque
								}
								
								manel.setPosition(posicao);
								manel.savePosition(posicao);                     //salva a posição incial do Manel
								ImageGUI.getInstance().addImage(manel);
								listObjects.add(manel);
								break;
								
							case 'W':	
								obj = new Wall(posicao);
								break;
								
							case 'B':	
								obj = new Bat(posicao);
								break;
								
							case 'm':	
								obj = new Meat(posicao);
								break;
								
							case 'h':	
								obj = new HiddenTrap(posicao);
								break;
								
							case 'G':	
								obj = new Gorila(posicao, 100, 20);
								break;
								
							case 'b':	
								obj = new Bomb(posicao);
								break;
								
							case '0':	
								obj = new Door(posicao);
								break;
								
							case 't':	
								obj = new Trap(posicao);
								break;
								
							case 'S':	
								obj = new Stair(posicao);
								break;
								
							case 's':	
								obj = new Sword(posicao);
								break;
								
							case 'P':	
								obj = new Princess(posicao);
								break;	
								
							default:                      //se encontra um caracter desconhecido, avisa o utilizador da falha ocorrida
								char c = line.charAt(x);
								if(!isSpace(c)) {
									System.err.println("Invalid char in line " + (y+1) + " and column " + (x+1) + ". Filled with floor");
								}
								break;
						}
						
						if(obj != null) {
							ImageGUI.getInstance().addImage(obj);
							listObjects.add(obj);
						}
					}
					y++;
				}
			}
		
		}catch (FileNotFoundException e1) {
			System.err.println(e1.getMessage());
			try(Scanner s = new Scanner(System.in)){	//lê o novo ficheiro da consola
				System.out.print("Indique o nome do ficheiro: ");
				String newFile = s.nextLine().trim();
				File f = new File("rooms/" + newFile);
				if(!f.exists()){          //se introduzir um novo ficheiro, mas é um ficheiro que não existe, fecha o jogo com erro
					System.err.println("File not found! Aborting...");
					ImageGUI.getInstance().dispose();
					System.exit(1);
				}
				loadRoom(newFile);
			}
		}catch (IllegalArgumentException e2) {
			System.err.println("Erro no ficheiro: " + e2.getMessage());
			ImageGUI.getInstance().dispose();
			System.exit(1);
		}
	}
	
	private boolean isSpace(char c) {
		return c == ' ';
	}
	
	public int getCurrentRoom() {
		return currentRoom;
	}
	
	
	public List<Objects> getListObjects() {
		return listObjects;
	}
	
	
	public List<Objects> getList(Predicate<Objects> pred) {
		List<Objects> newList = new ArrayList<>();
		for(Objects obj : listObjects) {
			if(pred.test(obj)) {
				newList.add(obj);
			}
		}
		return newList;
	}
	
	
	public void moveVillainRandom() {
		List<Objects> list = getList(obj -> obj instanceof Villain);
		Iterator<Objects> it = list.iterator();
	    while(it.hasNext()) {
	    	Objects obj = it.next();
			if (isNextTo(obj.getPosition(), manel)) {
				return;                    //evita que o donkeykong se mova quando o manel está lado a lado 
			}
			((Villain) obj).moveVillain();
	    }
	}
	
	
	public void tick() {
		List<Objects> list = getList(obj -> obj instanceof Tick);
		Iterator<Objects> it = list.iterator();
		while(it.hasNext()) {
			Objects obj = it.next();
			((Tick) obj).tick();
		}
		//ações que acontecem a cada tick de relógio
		moveVillainRandom();
		attackJumpManAndVillain();
		gravity();
		interactObjects();
	}
	
	
	public void attackJumpManAndVillain() {        //ataque de um vilão qualquer ao JumpMan ou a outro vilão
		List<Objects> list = getList(obj -> obj instanceof Villain && obj instanceof Damageable);
		
	    Iterator<Objects> it = list.iterator();
	    while(it.hasNext()) {
	    	Objects obj = it.next();
	    	/*for(Objects object : list) {     //ataque de um Donkey ao outro está comentado como descrito no relatório
	    		if(obj != object) {
	    			performAttack(obj, object);
	    		}else {
	    			break;
	    		}
	    	}*/
	    	performAttack(obj, manel);
	    }
	}
	
	public void attackVillain() {
		List<Objects> list = getList(obj -> obj instanceof Villain && obj instanceof Damageable);
		Iterator<Objects> it = list.iterator();
	    while(it.hasNext()) {
	    	Objects obj = it.next();
	    	performAttack(manel, obj);
	    }
	}
	
	
	public void performAttack(Objects attacker, Objects target) {
        if (isNextTo(attacker.getPosition(), target)) {
            ((Damageable) attacker).takeDamage((Character) target);
        }   
	}
	
	
	public static boolean isNextTo(Point2D position, Objects obj) {
		List<Point2D> np = obj.getPosition().getNeighbourhoodPoints();
		for(Point2D p : np) {
			if(p.equals(position)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean isUnderObject(Point2D position, Objects obj) {
		Point2D newpos = position.plus(Direction.DOWN.asVector());   //vê a posição abaixo do JumpMan
		if(newpos.equals(obj.getPosition())) {    //objetos que servem de suporte ao JumpMan, se a posição abaixo não tiver uma parede ou uma escada ou uma trap, ele cai
			return true;
		}
		return false;
	}
	
	
	public Point2D getManelPosition() {
		return manel.getPosition();
	}
	
	
	public void moveHero(Direction d) {
		manel.moveJumpMan(d);
	}
	
	
	public boolean isBlocked(Point2D position) {
		List<Objects> list = getList(obj -> obj instanceof Blocked);
		for(Objects obj : list) {
			if(obj.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}
	
	
	//define a interação entre o manel e os objetos
	public void interactObjects() {
		List<Objects> list = getList(obj -> obj instanceof Interactable);
		Iterator<Objects> it = list.iterator();
		while(it.hasNext()) {
			Objects obj = it.next();
			((Interactable) obj).interact();
		}
	}
	
	
	public boolean anythingUnder(Point2D position) {
		Point2D newpos = position.plus(Direction.DOWN.asVector());   //vê a posição abaixo do JumpMan
		List<Objects> list = getList(obj -> obj instanceof Support);
		for(Objects obj : list) {
			if(newpos.equals(obj.getPosition())) {    //objetos que servem de suporte ao JumpMan, se a posição abaixo não tiver uma parede ou uma escada ou uma trap, ele cai
				return true;
			}
		}	
		return false;
	}
	
	
	public void gravity() {
		if(!anythingUnder(getManelPosition())) {
			moveHero(Direction.DOWN);
		}
	}
	
	
	public void nextLevel() {
		currentRoom++;
		listObjects.clear();
		ImageGUI.getInstance().clearImages();
		loadRoom("room"+currentRoom+".txt");
	}
	
	
	public void restart() {
		if(manel.getLifes() <= 0) {
			currentRoom = 0;
			manel.setLifes(3);
			listObjects.clear();
			ImageGUI.getInstance().clearImages();
			loadRoom("room0.txt");
			
		}else {
			manel.setPosition(manel.getSavedPosition());
			ImageGUI.getInstance().addImage(manel);
			listObjects.add(manel);
		}
	}
	
	
	public static void top10HighScores(String ficheiro, String name, int time) {
		List<String> highScores = new ArrayList<>();
		
		try {
			File file = new File(ficheiro);
			
			if(!file.exists()) {          //se o ficheiro não existir, cria um novo (esse mesmo ficheiro), e se der erro ao criar lança a exceção e termina o jogo
				System.err.println("File not found! Creating a new one");
				if(!file.createNewFile()) {
					throw new IOException("Error creating new File");
				}
			}
			
			//Ler do arquivo os scores existentes
			try(Scanner s = new Scanner(file)){
				while(s.hasNextLine()) {
					String line = s.nextLine().trim();
					highScores.add(line);
				}
			}	
			
		}catch(IOException e1) {
			System.err.println(e1.getMessage());
			System.exit(1);
		}
		
		//atualizar com o novo score
		highScores.add(name + ", " + time);	
		
		//ordenar a minha lista de highScores, de acordo com um comparador externo, por ordem crescente de tempo
		highScores.sort((s1, s2) -> {
			int time1 = Integer.parseInt(s1.split(",")[1].trim());
	        int time2 = Integer.parseInt(s2.split(",")[1].trim());
	        int compare = Integer.compare(time1, time2);
	        
	        if(compare == 0) {      //Em caso de empate nos tempos, ordena por ordem alfabética
	        	String name1 = s1.split(",")[0].trim();
	        	String name2 = s2.split(",")[0].trim();
	        	return name1.compareToIgnoreCase(name2); 
	        }
	        
	        return compare;
		});
		
		//mostra o top10 highScores
		if(highScores.size() > 10) {
			highScores = highScores.subList(0, 10);
		}
		
		//escrever a lista de scores no arquivo
		try(PrintWriter writer = new PrintWriter(new File(ficheiro))) {
			for(String score : highScores) {
				writer.println(score);
			}
		}catch (FileNotFoundException e1) {
			System.err.println("Problema ao escrever no ficheiro!");
		}
	}
	
	
	public static void displayHighScores(String ficheiro) {
		String scores = "";
		
		try(Scanner scanner = new Scanner(new File(ficheiro))) {
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				scores += line + System.getProperty("line.separator");
			}
		}catch(FileNotFoundException e1) {
			System.err.println("File not found!");
		}
		ImageGUI.getInstance().showMessage("Top10HighScores (em segundos)", scores);
	}
	
}