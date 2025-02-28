	/**************************************************************
	 * Joseph Coats
	 * QU - Infinity Games
	 * Example 2: "Memory Cards"
	 * Resources: 
	 * 		YouTube, BrandonioProductions, "Making a Platformer With Java"
	 **************************************************************/	

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameWindow 
extends JFrame
{

	/**************************************************************
	 * This constructor creates the window frame using the given
	 * name provided in the below main method.
	 **************************************************************/	
	public GameWindow(String name)
	{
		super(name);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension windowSize = new Dimension(1600, 1000); // 800 600
		this.setSize(windowSize);
	}
	
	public static void main(String[] args)
	{
		GameWindow window = new GameWindow("Memory Cards");
		

		Container contentPane = window.getContentPane();
		contentPane.setLayout(new GridLayout(1, 1));
		
		MemoryCards edit = new MemoryCards(window.getSize());
		contentPane.add(edit);
		window.setVisible(true);
	}
	
}
