package GUI;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Tile extends Paintable {
	
	public static final int SIDE_LENGTH = 128;
	
	private boolean isInBounds;
	
	public Tile(BufferedImage i, boolean t, int x, int y) {
		super(i, x, y);
		this.isInBounds = t;
		setImg(Paintable.enlargeImg(this.getImg(), 2.0));
	}
	
	public boolean isInBounds() {
		return isInBounds;
	}

}
