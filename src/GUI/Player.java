package GUI;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends Paintable {

	private BufferedImage stoppedR;
	private BufferedImage stoppedL;
	private BufferedImage moving1R;
	private BufferedImage moving2R;
	private BufferedImage moving1L;
	private BufferedImage moving2L;
	
	private int switchImage;
	private int xDir;
	private int yDir;
	private int speed = 4;
	
	public Player() {
		super(null, 128, 128);
		try {
			stoppedR = ImageIO.read(new File("char.png"));
			moving1R = ImageIO.read(new File("charWalk1.png"));
			moving2R = ImageIO.read(new File("charWalk2.png"));
			stoppedR = Paintable.enlargeImg(stoppedR, 4);
			moving1R = Paintable.enlargeImg(moving1R, 4);
			moving2R = Paintable.enlargeImg(moving2R, 4);
			stoppedL = flipImage(stoppedR);
			moving1L = flipImage(moving1R);
			moving2L = flipImage(moving2R);
			setImg(stoppedR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getXDir() {
		return xDir;
	}
	public int getYDir() {
		return yDir;
	}
	public int getSpeed() {
		return speed;
	}

	public void move(boolean isColliding) {
		if (!isInBounds())
			putInBounds();
		int[] newPos = testMove();
		if (!isColliding && (newPos[0] != this.getX() || newPos[1] != this.getY())) {
			setX(getX() + xDir*speed);
			setY(getY() + yDir*speed);
			if (switchImage == 0) {
				if (this.getImg() == moving1R || this.getImg() == moving1L) {
					if (this.getImg() == moving1R)
						this.setImg(moving2R);
					else if (this.getImg() == moving1L)
						this.setImg(moving2L);
				}
				else if (xDir == 1)
					this.setImg(moving1R);
				else if (xDir == -1)
					this.setImg(moving1L);
				else if (yDir != 0) {
					if (this.getImg() == stoppedR)
						this.setImg(moving1R);
					else if (this.getImg() == stoppedL)
						this.setImg(moving1L);
					else if (this.getImg() == moving2L)
						this.setImg(moving1L);
					else if (this.getImg() == moving2R)
						this.setImg(moving1R);
				}
				switchImage = 20;
			}
			switchImage--;
		} else {
			if (xDir == 1)
				this.setImg(stoppedR);
			else if (xDir == -1)
				this.setImg(stoppedL);
			else if (this.getImg() == moving1R || this.getImg() == moving2R)
				this.setImg(stoppedR);
			else if (this.getImg() == moving1L || this.getImg() == moving2L)
				this.setImg(stoppedL);
		}
	}
	public int[] testMove() {
		int xTemp = getX() + xDir*speed;
		int yTemp = getY() + yDir*speed;
		if (xTemp > 0 && yTemp > 0 && xTemp+getWidth() < TestPanel.getMaxX() && yTemp+getHeight() < TestPanel.getMaxY())
			return new int[] {xTemp, yTemp};
		return new int[] {getX(), getY()};
	}
	public boolean isInBounds() {
		return (getX() > 0 && getY() > 0 && getX() < TestPanel.getMaxX() && getY() < TestPanel.getMaxY());
	}
	public void putInBounds() {
		setX(32);
		setY(32);
	}

	public void moveRight() {
		if (xDir != 1) {
			switchImage = 0;
			xDir = 1;
		}
	}
	public void moveLeft() {
		if (xDir != -1) {
			xDir = -1;
			switchImage = 0;
		}
	}
	public void stopHMovement() {
		if (xDir == 1)
			setImg(stoppedR);
		else
			setImg(stoppedL);
		xDir = 0;
	}
	public void moveUp() {
		yDir = -1;
	}
	public void moveDown() {
		yDir = 1;
	}
	public void stopVMovement() {
		yDir = 0;
	}
	public void speedUp() {
		if (speed < 6)
			speed = speed*2;
	}
	public void slowDown() {
		if (speed > 1)
			speed = speed/2;
	}
	
	@Override
	public boolean collidesWith(Paintable p) {
		return ((p.getX() <= getX()+getWidth() && p.getX()+p.getWidth() >= getX())
				&& (p.getY() <= getY()+getHeight() && p.getY()+p.getHeight() >= getY()+getHeight()-10));
	}
	public boolean willCollideWith(Paintable p) {
		return ((p.getX() <= getX()+getWidth()+speed*xDir && p.getX()+p.getWidth() >= getX()+speed*xDir)
				&& (p.getY() <= getY()+getHeight()+speed*yDir && p.getY()+p.getHeight() >= getY()+getHeight()-5+speed*yDir));
	}

}