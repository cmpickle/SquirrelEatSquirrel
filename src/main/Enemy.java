package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

public class Enemy {
	private BufferedImage squirrel;
	private BufferedImage squirrelLeft;
	public int x;
	public int y;
	private int size;
	public int moveX;
	public int moveY;
//	private int width;
//	private int height;
	public int bounce;
	public int bounceRate;
	public int bounceHeight;
	public boolean left = true;
	Rectangle area = new Rectangle();
	Random rand = new Random();
	Random rand2 = new Random();
	
	public Enemy()
	{
		try
		{
			squirrel = ImageIO.read(new File("/res/squirrel3.png"));
			squirrelLeft = ImageIO.read(new File("/res/squirrel3Left.png"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		size = rand.nextInt(6)+1;
//		int multiplier = rand.nextInt(7)+1;
//		this.width = (size) * multiplier;
//		this.height = (size) * multiplier;
		this.moveX = GUI.getRandomVelocity();
		this.moveY = GUI.getRandomVelocity();
		this.left = (moveX < 0)? true:false;
		this.bounce = 0;
		this.bounceRate = rand.nextInt(9)+10;
		this.bounceHeight = rand.nextInt(41)+10;
		this.x = rand.nextInt();
		this.y = rand.nextInt();
		area = new Rectangle(x, y, x+squirrel.getWidth(), y+squirrel.getHeight());
	}
	
	public Enemy(int x, int y)
	{
		try
		{
			squirrel = ImageIO.read(new File("res/squirrel3.png"));
			squirrelLeft = ImageIO.read(new File("res/squirrel3Left.png"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		size = rand2.nextInt(7)+1;
//		int multiplier = rand.nextInt(3)+1;
//		this.width = (size) * multiplier;
//		this.height = (size) * multiplier;
		this.moveX = GUI.getRandomVelocity();
		this.moveY = GUI.getRandomVelocity();
		this.left = (moveX < 0)? true:false;
		this.bounce = 0;
		this.bounceRate = rand.nextInt(9)+10;
		this.bounceHeight = rand.nextInt(41)+10;
		this.x = x;
		this.y = y;
		area = new Rectangle(x, y, x+squirrel.getWidth(), y+squirrel.getHeight());
	}
	
	public void paint(Graphics2D g2d)
	{
		if(left)
			g2d.drawImage(squirrelLeft, getX(), getY()-getBounceAmount(bounce,bounceRate,bounceHeight), getWidth()*size, getHeight()*size, null);
		else
			g2d.drawImage(squirrel, getX(), getY()-getBounceAmount(bounce,bounceRate,bounceHeight), getWidth()*size, getHeight()*size, null);
	}
	
	private int getX()
	{
		return this.x-GUI.camerax;
	}
	
	private int getY()
	{
		return this.y-GUI.cameray;
	}
	
	private int getWidth()
	{
		return squirrel.getWidth();
	}
	
	private int getHeight()
	{
		return squirrel.getHeight();
	}
	
	public Rectangle getArea()
	{
		return new Rectangle(getX(), getY(), squirrel.getWidth(), squirrel.getHeight());
	}
	
	private static int getBounceAmount(int currentBounce, int bounceRate, int bounceHeight)
	{
		return (int) (Math.sin((Math.PI/(float)bounceRate)*currentBounce)*bounceHeight);
	}
}
