package GUI;

import java.awt.Image;
import java.awt.image.BufferedImage;

//*****--------------------------------------------------------------*****//
// This is the Tile class, it is capable of being painted, and colliding. //
// This makes up the background of the game shows where the player can go.//
//*****--------------------------------------------------------------*****//

public class Tile extends Paintable {
	
	public static final int SIDE_LENGTH = 128;
	
	private boolean isInBounds; //True if the tile is a wall
	
	//Constructor
	public Tile(BufferedImage i, boolean t, int x, int y) {
		super(i, x, y);
		this.isInBounds = t;
		setImg(Paintable.enlargeImg(this.getImg(), 2.0));
	}
	
	//Getter
	public boolean isInBounds() {
		return isInBounds;
	}

}
