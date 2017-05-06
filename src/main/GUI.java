package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JPanel {

	private static final long serialVersionUID = -693036000167528691L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int HALF_WINWIDTH = (WIDTH/2);
	public static final int HALF_WINHEIGHT = (HEIGHT/2);
	public static final int FPS = 30;
	
	public static int cameraSlack = 90;
	public static int moveRate = 6;
	public static int bounceRate = 3;
	public static int bounceHeight = 30;
	public static int startSize = 2;
	public static int winSize = 300;
	public static int invulnTime = 2;
	public static boolean invulnMode = false;
	public static int invulnStartTime = 0;
	public static boolean gameOverMode = false;
	public static int gameOverStartTime = 0;
	public static boolean winMode = false;
	public static int gameOverTime = 4;
	public static int MaxHealth = 3;
	
	public static int numGrass = 80;
	public static int numEnemy = 20;
	public static int squirrelMinSpeed = 1;
	public static int squirrelMaxSpeed = 4;
	public static int dirChangeFreq = 2;
	public static boolean left = true;
	
	public static int camerax = 0;
	public static int cameray = 0;
	public static Rectangle screen = new Rectangle(0, 0, WIDTH, HEIGHT);
	
	public static Player player = new Player();
	public static ArrayList<Grass> grassObjs = new ArrayList<Grass>();
//	public static Grass grass = new Grass();	//For some reason this variable fixes the null pointer exception...
	public static ArrayList<Enemy> enemyObjs = new ArrayList<Enemy>();
	public static Random rand = new Random();
	
	public GUI()
	{
		KeyListener listener = new MyKeyListener();
		addKeyListener(listener);
		setFocusable(true);
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		JFrame frame = new JFrame("Alpha Squirrel");
		GUI gui = new GUI();
		gui.setBackground(Color.green);
		frame.add(gui);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		for(int i = 0; i < 10; i++)
		{
			GUI.grassObjs.add(new Grass(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)));
		}
		
		while(true)
		{
			if(invulnMode && System.currentTimeMillis() - invulnStartTime > invulnTime)
				invulnMode = false;
			
			while(grassObjs.size() < numGrass)
				grassObjs.add(makeNewGrass(camerax, cameray));
			while(enemyObjs.size() < numEnemy)
				enemyObjs.add(makeNewEnemy(camerax, cameray));
			
			if(!gameOverMode)
			{
				if(player.moveUp)
					player.y -= moveRate;
				if(player.moveDown)
					player.y += moveRate;
				if(player.moveLeft)
					player.x -= moveRate;
				if(player.moveRight)
					player.x += moveRate;
				
				if((player.moveLeft || player.moveRight || player.moveUp || player.moveDown) || player.bounce != 0)
					player.bounce += 1;
				
				if(player.bounce > bounceRate)
					player.bounce = 0;
			}
			
			int playerCenterX = player.x + player.getWidth()/2;
			int playerCenterY = player.y + player.getHeight()/2;
			if((camerax + HALF_WINWIDTH) - playerCenterX > cameraSlack)
				camerax = playerCenterX + cameraSlack - HALF_WINWIDTH;
			else if(playerCenterX - (camerax + HALF_WINWIDTH) > cameraSlack)
				camerax = playerCenterX - cameraSlack - HALF_WINWIDTH;
			if((cameray + HALF_WINHEIGHT) - playerCenterY > cameraSlack)
				cameray = playerCenterY + cameraSlack - HALF_WINHEIGHT;
			else if(playerCenterY - (cameray + HALF_WINHEIGHT) > cameraSlack)
				cameray = playerCenterY - cameraSlack - HALF_WINHEIGHT;
			
			
//			screen = new Rectangle(0, 0, WIDTH, HEIGHT);
//			screen.x=camerax;
//			screen.y=cameray;
//			System.out.println(screen);
//			System.out.println("player " + player.x + " " + player.y);
//			System.out.println("camera " + camerax + " " + cameray);
			
			for(int i = 0; i < enemyObjs.size(); i++)
			{
				enemyObjs.get(i).x += enemyObjs.get(i).moveX;
				enemyObjs.get(i).y += enemyObjs.get(i).moveY;
				enemyObjs.get(i).bounce += 1;
				if(enemyObjs.get(i).bounce > enemyObjs.get(i).bounceRate)
					enemyObjs.get(i).bounce = 0;
				
				if(rand.nextInt(100)<dirChangeFreq)
				{
					enemyObjs.get(i).moveX = getRandomVelocity();
					enemyObjs.get(i).moveY = getRandomVelocity();
				}
			}
			
			for(int i = 0; i < grassObjs.size(); i++)
				if(isOutsideActiveArea(camerax, cameray, grassObjs.get(i).getArea()))
					grassObjs.remove(i);
			for(int i = 0; i < enemyObjs.size(); i++)
				if(isOutsideActiveArea(camerax, cameray, enemyObjs.get(i).getArea()))
					enemyObjs.remove(i);
			
			gui.repaint();
			Thread.sleep(10);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for(int i = 0; i < grassObjs.size(); i++)
		{
			if(screen.intersects(grassObjs.get(i).getArea()))
				grassObjs.get(i).paintComponent(g2d);				
		}
//		g2d.draw(screen);
//		g2d.drawRect(-WIDTH, -HEIGHT, 3*WIDTH, 3*HEIGHT);
		boolean flashIsOn = Math.round((System.currentTimeMillis()*10)%2) == 1;
		if(!gameOverMode && !(invulnMode && flashIsOn))
			player.paint(g2d);
		for(int i = 0; i < enemyObjs.size(); i++)
		{
			if(screen.intersects(enemyObjs.get(i).getArea()))
				enemyObjs.get(i).paint(g2d);
		}
	}
	
	public static int getRandomVelocity()
	{
		int speed;
		speed = rand.nextInt(squirrelMaxSpeed);
		if(rand.nextInt(2)==0)
			return speed;
		else
			return -speed;
	}
	
	public static boolean isOutsideActiveArea(int cameraX, int cameraY, Rectangle obj)
	{
		int boundsLeftEdge = -WIDTH;
		int boundsTopEdge = -HEIGHT;
		Rectangle activeArea = new Rectangle(boundsLeftEdge, boundsTopEdge, WIDTH*3, HEIGHT*3);
		return !activeArea.intersects(obj);
	}
	
	public static Grass makeNewGrass(int camerax, int cameray)
	{
		Point point = getRandomOffCameraPosition(camerax, cameray);
		Grass grass = new Grass(point.x, point.y);
		return grass;
	}
	
	public static Enemy makeNewEnemy(int camerax, int cameray)
	{
		Point point = getRandomOffCameraPosition(camerax, cameray);
		Enemy enemy = new Enemy(point.x, point.y);
		return enemy;
	}
	
	public static Point getRandomOffCameraPosition(int camerax, int cameray)
	{
		while(true)
		{
			int x = rand.nextInt(3*WIDTH)-WIDTH+camerax;
			int y = rand.nextInt(3*HEIGHT)-HEIGHT+cameray;
			if(!screen.contains(new Point(x,y)))
			{
//				System.out.println("off camera position: " + x + " , " + y);
				return new Point(x,y);
			}
		}
	}
	
	public class MyKeyListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent e)
		{
			player.move(e);
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			player.stop(e);
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
			
		}	
	}
}
