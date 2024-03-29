package GUI;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Paintable {
	
	private int xPos;
	private int yPos;
	private int height;
	private int width;
	private BufferedImage img;
	
	//Constructor
	public Paintable(BufferedImage i, int x, int y) {
		setX(x);
		setY(y);
		setImg(i);
	}
	
	//-------------------//
	// Getters & Setters //
	//-------------------//
	
	public int getX() {
		return xPos;
	}
	public void setX(int x) {
		xPos = x;
	}
	public int getY() {
		return yPos;
	}
	public void setY(int y) {
		yPos = y;
	}
	public int getHeight() {
		return height;
	}
	protected void setHeight(int h) {
		height = h;
	}
	public int getWidth() {
		return width;
	}
	public BufferedImage getImg() {
		return img;
	}
	public void setImg(BufferedImage i) {
		img = i;
		if (i != null) {
			height = i.getHeight();
			width = i.getWidth();
		}
	}
	
	//******************************
	
	//Collision detection
	public boolean collidesWith(Paintable p) {
		return ((p.xPos <= xPos+width && p.xPos+p.width >= xPos)
				&& (p.yPos <= yPos+height && p.yPos+p.height >= yPos));
	}
	
	//--------------------//
	// Image Manipulation //
	//--------------------//
	
	public static BufferedImage enlargeImg(BufferedImage img, double factor) { //Make a temporary image and draw it onto the result buffered image.
		Image temp = img.getScaledInstance((int)(img.getWidth()*factor), (int)(img.getHeight()*factor), Image.SCALE_FAST);
		BufferedImage result = new BufferedImage((int)(img.getWidth()*factor), (int)(img.getHeight()*factor), BufferedImage.TYPE_INT_ARGB);
		result.getGraphics().drawImage(temp, 0, 0, null);
		return result;
	}
	public static BufferedImage flipImageH(BufferedImage image){ //Draw the old buffered image onto the result buffered image backwards (column 1 to column n, column 2 to column n-1, ... , column n to column 1).
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    for (int i=0;i<image.getWidth();i++) {
			for (int j=0;j<image.getHeight();j++) {
				int tmp = image.getRGB(i, j);
				
				result.setRGB(image.getWidth()-i-1, j, tmp);
				result.setRGB(i, j, image.getRGB(image.getWidth()-i-1, j));
			}
	    }
	    return result;
	}

}
