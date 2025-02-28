	/**************************************************************
	 * Joseph Coats
	 * QU - Infinity Games
	 * Example 1: "Pong" - based on Atari PONG (1972)
	 * Resources: 
	 * 		YouTube, BrandonioProductions, "Making a Platformer With Java"
	 **************************************************************/	

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;



public class MemoryCards 
extends Canvas
implements KeyListener, Runnable, MouseListener
{
	boolean running = true;
	private Graphics bufferGraphics = null;
	private BufferStrategy bufferStrategy = null;
	
	private Thread thread;
	
	private BufferedImage backgroundImage, cardBack, timerCard;
	private BufferedImage patterns[] = new BufferedImage[2];
	private BufferedImage icons[] = new BufferedImage[6];
	
	private int backgroundGen, sub, cardPosX, cardPosY, mouseX, mouseY, cardsFlipped = 0, totalCardsFlipped = 0, timer = 200;
	private int selectedCards[][] = new int[2][2];
	private final int cardSizeX = (int)(106*1.5), cardSizeY = (int)(137*1.5);
	private final int BACKGROUND_SIZE, DECK_X = 4, DECK_Y = 3, TIMER_LOAD = 150;
	
	MemoryCard cards[][] = new MemoryCard[DECK_X][DECK_Y];
	MemoryCard tempCard = new MemoryCard(true);
	
	private final int centerDisplacement;
	private boolean displayCards = true;

	

	/**************************************************************
	 * This constructor and paint method initialize the program 
	 * and its buffers.
	 **************************************************************/	
	public MemoryCards(Dimension size)
	{
		this.setPreferredSize(size);
		this.addKeyListener(this);
		this.addMouseListener(this);
		
		if(size.width > size.height)
		{
			BACKGROUND_SIZE = size.width / 100;
		}
		else
		{
			BACKGROUND_SIZE = size.height / 100;
		}
		
		centerDisplacement = ((size.width - 16)/2) - ((int)(cardSizeX*1.5*DECK_X)/2);
		//System.out.println(centerDisplacement + "   " + size.width);
		randomizeCards();
		
		
		// Attempting to load the image files.
		try {
			backgroundImage = ImageIO.read(getClass().getResource("/MemoryCardBackground.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			cardBack = ImageIO.read(getClass().getResource("/MemoryCardBack.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			patterns[0] = ImageIO.read(getClass().getResource("/PatternBrick.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			patterns[1] = ImageIO.read(getClass().getResource("/PatternRound.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		///////////////////////////////////////////////////////////////////////////
		try {
			icons[0] = ImageIO.read(getClass().getResource("/MemoryCardCube.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			icons[1] = ImageIO.read(getClass().getResource("/MemoryCardDiamond.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			icons[2] = ImageIO.read(getClass().getResource("/MemoryCardHeart.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			icons[3] = ImageIO.read(getClass().getResource("/MemoryCardJack.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			icons[4] = ImageIO.read(getClass().getResource("/MemoryCardSpade.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		try {
			icons[5] = ImageIO.read(getClass().getResource("/MemoryCardWaves.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			timerCard = ImageIO.read(getClass().getResource("/MemoryCardTimer.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.thread = new Thread(this);
		
	}
	
	public void paint(Graphics g)
	{
		if(bufferStrategy == null)
		{
			this.createBufferStrategy(2);
			bufferStrategy = this.getBufferStrategy();
			bufferGraphics = bufferStrategy.getDrawGraphics();
			this.thread.start();
		}
	}
	/**************************************************************
	 * This method calls DoLogic, Draw, & DrawBackbufferToScreen 
	 * for every frame.
	 **************************************************************/	
	@Override
	public void run() 
	{	
		while(running)
		{
			//System.out.println(cards[0][0].getBack());
			if(timer > 0)
				timer--;
			else
				DoLogic();
			
			Draw();
			DrawBackbufferToScreen();

			Thread.currentThread();
			try
			{
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	} // End of Running
	
	/**************************************************************
	 * This method runs the logic of the program for every frame.
	 **************************************************************/
	public void DoLogic()
	{
		
		displayCards = false;
		
    	if(cardsFlipped >= 2)
    	{
    		cardsFlipped = 0;
    		if(!( cards[selectedCards[0][0]][selectedCards[0][1]].equals(cards[selectedCards[1][0]][selectedCards[1][1]])) )
	    	{
	    		cards[selectedCards[0][0]][selectedCards[0][1]].setStatus(false);
	    		cards[selectedCards[1][0]][selectedCards[1][1]].setStatus(false);
	    	}
    		else
    		{
	    		totalCardsFlipped += 2;
	    		System.out.println(totalCardsFlipped);
    		}
    	}
		
    	if( totalCardsFlipped >= (DECK_X * DECK_Y) )
    	{
    		System.out.println("Reset");
    		totalCardsFlipped = 0;
    		clearCards();
    		randomizeCards();
    		displayCards = true;
    		timer = 200;
    	}
    	    	
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**************************************************************
	 * The first method draws the objects to the back buffer,
	 * once rendering is finished, it is then displayed to the 
	 * primary buffer.
	 **************************************************************/
	public void Draw()
	{
		bufferGraphics = bufferStrategy.getDrawGraphics();
		try
		{
			this.setBackground(Color.BLUE);
			this.setForeground(new Color(170, 170, 170));
			bufferGraphics.clearRect(0, 0, this.getSize().width, this.getSize().height);

			Graphics2D bufferGraphics2D = (Graphics2D)bufferGraphics;
						
			for(sub = 10; sub >= 0; sub--)
			{
				backgroundGen = 0;
				while(backgroundGen < BACKGROUND_SIZE*2)
				{
					bufferGraphics2D.drawImage(backgroundImage, backgroundGen*100, sub*100, 100, 100, null);				
					backgroundGen++;
				}
			}
			
			
			if(timer > 0) // This could be optimized/expanded upon.
			{
				AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(20*Math.sin(timer*0.1)), timerCard.getWidth()/2, timerCard.getHeight()/2);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	
				bufferGraphics2D.drawImage(op.filter(timerCard, null),  50, 50, (int)(cardSizeX*0.8), (int)(cardSizeY*0.8), null);
			}
			
			for(int xPos = 0; xPos < DECK_X; xPos++)
			{
				for(int yPos = 0; yPos < DECK_Y; yPos++) 
				{
				  if(cards[xPos][yPos] != null)
				  {
					//System.out.println( xPos + " " + yPos);
					cardPosX = centerDisplacement +((int)(xPos*cardSizeX * 1.5))+50;
					cardPosY = (int)(yPos*(cardSizeY * 1.2))+100;
					cards[xPos][yPos].setX(cardPosX);
					cards[xPos][yPos].setY(cardPosY);

					
					if((cards[xPos][yPos].getStatus() || displayCards) && timer <= TIMER_LOAD)
					{
						//System.out.println(this.getSize().height);
						//System.out.print( " " + yPos );
						if(cards[xPos][yPos].getBack().equalsIgnoreCase("round"))
							bufferGraphics2D.drawImage(patterns[1], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
						if(cards[xPos][yPos].getBack().equalsIgnoreCase("brick"))
							bufferGraphics2D.drawImage(patterns[0], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
						
						bufferGraphics2D.setXORMode(cards[xPos][yPos].getBackColor());
						bufferGraphics2D.fillRect(cardPosX, cardPosY, cardSizeX, cardSizeY);
						bufferGraphics2D.setPaintMode();
	
						//System.out.println(" -=--=- " + cards[xPos][yPos].getFront() + " " + cards[xPos][yPos].getFrontColor() + " -=--=- ");
						//System.out.println();
						switch(cards[xPos][yPos].getFront())
						{
							case "cube":
							{
								//System.out.print("cube ");
								bufferGraphics2D.drawImage(icons[0], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "diamond":
							{
								//System.out.print("diamond ");
								bufferGraphics2D.drawImage(icons[1], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "heart":
							{
								//System.out.print("heart ");
								bufferGraphics2D.drawImage(icons[2], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "jack":
							{
								//System.out.print("jack ");
								bufferGraphics2D.drawImage(icons[3], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "spade":
							{
								//System.out.print("spade ");
								bufferGraphics2D.drawImage(icons[4], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "waves":
							{
								//System.out.print("waves ");
								bufferGraphics2D.drawImage(icons[5], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
						}
						bufferGraphics2D.setXORMode(cards[xPos][yPos].getFrontColor());
						switch(cards[xPos][yPos].getFront())
						{
							case "cube":
							{
								//System.out.print("cube ");
								bufferGraphics2D.drawImage(icons[0], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "diamond":
							{
								//System.out.print("diamond ");
								bufferGraphics2D.drawImage(icons[1], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "heart":
							{
								//System.out.print("heart ");
								bufferGraphics2D.drawImage(icons[2], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "jack":
							{
								//System.out.print("jack ");
								bufferGraphics2D.drawImage(icons[3], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "spade":
							{
								//System.out.print("spade ");
								bufferGraphics2D.drawImage(icons[4], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
							case "waves":
							{
								//System.out.print("waves ");
								bufferGraphics2D.drawImage(icons[5], cardPosX, cardPosY, cardSizeX, cardSizeY, null);
								break;
							}
						}
						bufferGraphics2D.setPaintMode();
					} // End of Status
					else
						bufferGraphics2D.drawImage(cardBack, cardPosX, cardPosY, cardSizeX, cardSizeY, null);
				  } // End of IF
				} // Inner FOR

			} // Outer FOR

				
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			bufferGraphics.dispose();
		}
	}
	public void DrawBackbufferToScreen()
	{
		bufferStrategy.show();
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void clearCards()
	{
		for(int arr = 0; arr < DECK_X; arr++)
		{
			for(int arr2 = 0; arr2 < DECK_Y; arr2++)
			{
				cards[arr][arr2] = null;
			}
		}
	}
	
	private void randomizeCards()
	{
		MemoryCard pairs[] = new MemoryCard[6];
		
		for(int sub = 0; sub < 6; sub++)
		{
			int xPos = (int)(Math.random()*DECK_X), yPos = (int)(Math.random()*DECK_Y);
			//System.out.println(xPos + yPos);
			
			if(cards[xPos][yPos] == null)
			{
				cards[xPos][yPos] = new MemoryCard(false); // Blank Cards: False
				pairs[sub] = cards[xPos][yPos];
			}
			else
				sub--;
		} // 6 Cards are generated.
		
		
		// Now to pair them.
		sub = 0;
		//System.out.println();
		for(int arr = 0; arr < DECK_X; arr++)
		{
			for(int arr2 = 0; arr2 < DECK_Y; arr2++)
			{
				if(cards[arr][arr2] == null)
				{
					cards[arr][arr2] = pairs[sub++].clone();
					//System.out.print(" " + sub + " ");
				}
			}
		}
		
		//displayCards();
	}
	
	private void randomizeAllCards()
	{
		for(int arr = 0; arr < DECK_X; arr++)
		{
			for(int arr2 = 0; arr2 < DECK_Y; arr2++)
			{
				System.out.print(arr + " ");
				cards[arr][arr2] = new MemoryCard(false);
				System.out.println(cards[arr][arr2].getFront());
				System.out.println(cards[arr][arr2].getFrontColor());
			}
		}
		//displayCards();
	}
	
	private void displayCards()
	{
		for(int arr = 0; arr < DECK_X; arr++)
		{
			for(int arr2 = 0; arr2 < DECK_Y; arr2++)
			{
				System.out.print(cards[arr][arr2].getFront() + " ");

			}
			System.out.println();
		}
	}
	
	/**************************************************************
	 * These methods are triggered by key events.
	 * They are unused in this example.
	 **************************************************************/
	@Override
	public void keyPressed(KeyEvent arg0) 
	{

	}
	@Override
	public void keyReleased(KeyEvent arg0)  // Does not count shift
	{
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) 
	{

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	  if( timer <= 0 )
	  {
		mouseX = e.getX();
		mouseY = e.getY();
		int refinedX, refinedY;
	    
	    //System.out.print(mouseX+","+mouseY + " | " + (centerDisplacement) + "   " + this.getSize().width);
	    //System.out.println(" | " + ((this.getSize().width/2) - ((int)(cardSizeX*1.5*DECK_X)/2)));

		if(  mouseY > cards[0][0].getY() && mouseX > cards[0][0].getX() )
		{
			refinedX = ( (int)((mouseX - centerDisplacement -50)/(cardSizeX * 1.5) ));
			refinedY = (int)((mouseY - 100)/(cardSizeY * 1.2));
		    
		    if(refinedX < DECK_X && refinedY < DECK_Y && refinedX >= 0 && refinedY >= 0 &&
		    		mouseX <= (cards[refinedX][refinedY].getX()+cardSizeX) &&
		    		mouseY <= (cards[refinedX][refinedY].getY()+cardSizeY)
		      )
		    {
		    	if(!cards[refinedX][refinedY].getStatus())
		    	{
			    	cards[refinedX][refinedY].setStatus(true);
			    	cardsFlipped++;
			    	//System.out.println("Revealed");
			    	
			    	if(cardsFlipped <= 2)
			    	{
				    	selectedCards[cardsFlipped-1][0] = refinedX;
				    	selectedCards[cardsFlipped-1][1] = refinedY;
				    	timer = 50;
			    	}
		    	}
		    }


		    //System.out.print(refinedX+","+refinedY);
		    //System.out.print(" | "+mouseX+","+mouseY);
		    //System.out.println(" | "+(cards[refinedX][refinedY].getX()+cardSizeX)+
		    //		","+(cards[refinedX][refinedY].getY()+cardSizeY) );

		}
	  }
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
