package GUI;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Coin extends Paintable {
	
	private int value = 1;

	public Coin(int x, int y) {
		super(null, x, y);
		try {
			this.setImg(ImageIO.read(new File("coin.png")));
			this.setImg(Paintable.enlargeImg(this.getImg(), 3.0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Coin(Coin coin) {
		super(coin.getImg(), coin.getX(), coin.getY());
		value = coin.value;
	}
	
	public Coin(Coin coin, int x, int y) {
		super(coin.getImg(), x, y);
		value = coin.value;
	}
	
	public int getValue() {
		return value;
	}
}
