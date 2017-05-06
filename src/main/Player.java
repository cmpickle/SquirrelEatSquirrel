package main;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Player {
	private BufferedImage squirrel;
	private BufferedImage squirrelLeft;
	
	private boolean left = true;
	private int size = GUI.startSize;
	public int x = GUI.camerax + GUI.HALF_WINWIDTH;
	public int y = GUI.cameray + GUI.HALF_WINHEIGHT;
	public int bounce = 0;
	private int bounceRate = 20;
	private int bounceHeight = 25;
	public int health = GUI.MaxHealth;
	
	public boolean moveLeft = false;
	public boolean moveRight = false;
	public boolean moveUp = false;
	public boolean moveDown = false;
	
	public Player()
	{
		try
		{
			squirrel = ImageIO.read(new File("res/squirrel3.png"));
			squirrelLeft = ImageIO.read(new File("res/squirrel3Left.png"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		this.x -= squirrel.getWidth()/2;
		this.y -= squirrel.getHeight()/2;
	}
	
	public void paint(Graphics2D g2d)
	{
		if(left)
			g2d.drawImage(squirrelLeft, getX(), getY()-getBounceAmount(bounce,bounceRate,bounceHeight), getWidth(), getHeight(), null);
		else
			g2d.drawImage(squirrel, getX(), getY()-getBounceAmount(bounce,bounceRate,bounceHeight), getWidth(), getHeight(), null);
	}
	
	public int getX()
	{
		return this.x-GUI.camerax;
	}
	
	public int getY()
	{
		return this.y-GUI.cameray;
	}
	
	public int getWidth()
	{
		return squirrel.getWidth() * size;
	}
	
	public int getHeight()
	{
		return squirrel.getHeight() * size;
	}
	
	public void move(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			moveDown=false;
			moveUp=true;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			moveUp=false;
			moveDown=true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			moveLeft=true;
			moveRight=false;
			left=true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			moveRight=true;
			moveLeft=false;
			left=false;
		}
	}
	
	public void stop(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_UP)
			moveUp=false;
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			moveDown=false;
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			moveLeft=false;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			moveRight=false;
	}
	
	private static int getBounceAmount(int currentBounce, int bounceRate, int bounceHeight)
	{
		return (int) (Math.sin((Math.PI/(float)bounceRate)*currentBounce)*bounceHeight);
	}
}
