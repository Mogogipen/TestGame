package GUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class TestPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int maxX = 900;
	private static int maxY = 700;
	
	public TestFrame contentPane;
	private Player player;
	private int score;
	
	private HashMap<String, BufferedImage> currentTheme;
	private ArrayList<ArrayList<Tile>> mapTiles = new ArrayList<ArrayList<Tile>>();
	private Coin coin;
	private ArrayList<Coin> coins = new ArrayList<Coin>();
	private ArrayList<Paintable> toDraw;
	
	public TestPanel(TestFrame cp) {
		contentPane = cp;
		player = new Player();
		toDraw = new ArrayList<Paintable>();
		coin = new Coin(0, 0);
		score = 0;
		
		HashMap<String, BufferedImage> initTheme = new HashMap<String, BufferedImage>();
		try {
			BufferedImage ground = ImageIO.read(new File("grass.png"));
			BufferedImage wall = ImageIO.read(new File("water.png"));
			BufferedImage edge = ImageIO.read(new File("cliff.png"));
			initTheme.put("ground", ground);
			initTheme.put("wall", wall);
			initTheme.put("edge", edge);
			currentTheme = initTheme;
			loadMap("map.txt", currentTheme);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Coin c : coins) {
			toDraw.add(c);
		}
		
//		cp.setBounds(maxX, maxY);
		toDraw.add(player);
		setKeys();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		try {
			
			doWindowTranslation(g);

			if (mapTiles != null && !mapTiles.isEmpty()) {
				for (ArrayList<Tile> a : mapTiles) {
					for (Tile t : a) {
						g.drawImage(t.getImg(), t.getX(), t.getY(), null);
					}
				}
			} else {
				for (int i = 0; i*Tile.SIDE_LENGTH < 899; i++) {
					for (int j = 0; j*Tile.SIDE_LENGTH < 899; j++) {
						g.drawImage(currentTheme.get("road"), i*Tile.SIDE_LENGTH, j*Tile.SIDE_LENGTH, null);
						maxX = i*64;
						maxY = j*64;
					}
				}
			}
			
			if (player.getX() > maxX-64) {
				this.mapTiles.clear();
				loadMap("map2.txt", currentTheme);
				player.setX(16);
			} else if (player.getX() < 8) {
				this.mapTiles.clear();
				loadMap("map.txt", currentTheme);
				player.setX(maxX-64);
			}
			
			for (Coin c : coins) {
				if (c.collidesWith(player)) {
					toDraw.remove(c);
					score += c.getValue();
				}
			}
			
			Thread.sleep(10);
			player.move(detectPlayerCollision());
			drawResources(g);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		repaint();
	}
	
	//----------------\\
	// Draw Resources \\
	//----------------\\
	
	//Moves the view window if the player is in the middle if the screen.
	private void doWindowTranslation(Graphics g) {
		int xTransTo = 0;
		int yTransTo = 0;
		
		if (player.getX() < 288);
		else if (player.getX() >= maxX-288)
			xTransTo = -(maxX-576);
		else
			xTransTo = -(player.getX()-288);
		
		if (player.getY() < 256);
		else if (player.getY() >= maxY-256)
			yTransTo = -(maxY-512);
		else
			yTransTo = -(player.getY()-256);
		
		g.translate(xTransTo, yTransTo);
	}

	@SuppressWarnings("unused")
	private void drawResource(Graphics g, Paintable p) {
		g.drawImage(p.getImg(), p.getX(), p.getY(), null);
	}
	private void drawResources(Graphics g) {
		for (Paintable p : toDraw) {
			g.drawImage(p.getImg(), p.getX(), p.getY(), null);
		}
	}
	
	//---------------------\\
	// Collision Detection \\
	//---------------------\\
	
	public boolean detectPlayerCollision() {
		for (ArrayList<Tile> a : mapTiles) {
			for (Tile t : a) {
				if (!t.isInBounds()) {
					if (player.willCollideWith(t))
						return true;
				}
			}
		}
		return false;
	}
	
	//----------\\
	// Load Map \\
	//----------\\
	
	private void loadMap(String filename, HashMap<String,BufferedImage> imgs) {
		try {
			Scanner map = new Scanner(new File(filename));
			int yLine = 0;
			while (map.hasNext()) {
				mapTiles.add(new ArrayList<Tile>());
				char[] line = map.nextLine().toCharArray();
				for (int xLine = 0; xLine < line.length; xLine++) {
					if (line[xLine] == 'O')
						mapTiles.get(yLine).add(new Tile(imgs.get("ground"), true, xLine*Tile.SIDE_LENGTH, yLine*Tile.SIDE_LENGTH));
					else if (line[xLine] == 'X')
						mapTiles.get(yLine).add(new Tile(imgs.get("wall"), false, xLine*Tile.SIDE_LENGTH, yLine*Tile.SIDE_LENGTH));
					else if (line[xLine] == 'C') {
						mapTiles.get(yLine).add(new Tile(imgs.get("ground"), true, xLine*Tile.SIDE_LENGTH, yLine*Tile.SIDE_LENGTH));
						coins.add(new Coin(coin, xLine*Tile.SIDE_LENGTH + (int)(Math.random()*Tile.SIDE_LENGTH), yLine*Tile.SIDE_LENGTH + (int)(Math.random()*Tile.SIDE_LENGTH)));
					} else if (line[xLine] == 'W')
						mapTiles.get(yLine).add(new Tile(imgs.get("edge"), false, xLine*Tile.SIDE_LENGTH, yLine*Tile.SIDE_LENGTH));
					else {
						throw new IllegalArgumentException();
					}
				}
				yLine++;
			}
			map.close();
			maxX = mapTiles.get(0).size()*Tile.SIDE_LENGTH;
			maxY = mapTiles.size()*Tile.SIDE_LENGTH;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//--------------\\
	// Key Bindings \\
	//--------------\\
	
	private void setKeys() {
		//W, A, S, D pressed
		this.getInputMap().put(KeyStroke.getKeyStroke("pressed A"), "moveLeft");
		this.getActionMap().put("moveLeft", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player.moveLeft();
				repaint();
			}
		});
		this.getInputMap().put(KeyStroke.getKeyStroke("pressed E"), "moveRight");
		this.getActionMap().put("moveRight", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player.moveRight();
				repaint();
			}
		});
		this.getInputMap().put(KeyStroke.getKeyStroke("pressed O"), "moveDown");
		this.getActionMap().put("moveDown", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player.moveDown();
				repaint();
			}
		});
		this.getInputMap().put(KeyStroke.getKeyStroke("pressed COMMA"), "moveUp");
		this.getActionMap().put("moveUp", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player.moveUp();
				repaint();
			}
		});
		
		//W, A, S, D released
		this.getInputMap().put(KeyStroke.getKeyStroke("released A"), "stopHMovement");
		this.getInputMap().put(KeyStroke.getKeyStroke("released E"), "stopHMovement");
		AbstractAction stopHMovement = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player.stopHMovement();
				repaint();
			}
		};
		this.getActionMap().put("stopHMovement", stopHMovement);
		this.getInputMap().put(KeyStroke.getKeyStroke("released O"), "stopVMovement");
		this.getInputMap().put(KeyStroke.getKeyStroke("released COMMA"), "stopVMovement");
		AbstractAction stopVMovement = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player.stopVMovement();
				repaint();
			}
		};
		this.getActionMap().put("stopVMovement", stopVMovement);
		
		//Shift
		this.getInputMap().put(KeyStroke.getKeyStroke("L"), "speedUp");
		this.getActionMap().put("speedUp", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player.speedUp();
				System.out.println("speed up");
				repaint();
			}
		});
		this.getInputMap().put(KeyStroke.getKeyStroke("released L"), "slowDown");
		this.getActionMap().put("slowDown", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				player.slowDown();
				System.out.println("slow down");
				repaint();
			}
		});
	}
	
	public static int getMaxX() {
		return maxX;
	}
	public static int getMaxY() {
		return maxY;
	}

}
