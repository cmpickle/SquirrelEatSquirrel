package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Grass {
	private static Random rand = new Random();
	private BufferedImage grass;
	public int x;
	public int y;
	Rectangle area = new Rectangle();
	
	public Grass()
	{
		try
		{
			this.grass = ImageIO.read(new File("res/grass" + rand.nextInt(4) + ".png"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		this.x = rand.nextInt();
		this.y = rand.nextInt();
		area = new Rectangle(x, y, grass.getWidth(), grass.getHeight());
	}
	
	public Grass(int x, int y)
	{
		try
		{
			this.grass = ImageIO.read(new File("res/grass" + rand.nextInt(4) + ".png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.x = x;
		this.y = y;
		area = new Rectangle(x, y, x+grass.getWidth(), y+grass.getHeight());
	}
	
	public void paintComponent(Graphics2D g2d)
	{
		g2d.drawImage(grass, getX(), getY(), getWidth(), getHeight(), null);
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
		return grass.getWidth();
	}
	
	private int getHeight()
	{
		return grass.getHeight();
	}
	
	public Rectangle getArea()
	{
		return new Rectangle(getX(), getY(), grass.getWidth(), grass.getHeight());
	}
}
