import java.awt.Color;

public class MemoryCard {
	
	private boolean flippedUp;
	private int posX, posY;
	
	private String back; // Pattern
	private String front; // Icon
	private Color backColor; // Pattern Color
	private Color frontColor; // Icon Color

	private Color colorFrontArray[] = {new Color(255, 180, 180), new Color(255, 255, 180), 
			new Color(190, 255, 255), new Color(180, 255, 180), new Color(255, 180, 240)};
	
	private Color colorBackArray[] = {new Color(155, 80, 80), new Color(155, 155, 80), 
			new Color(90, 155, 155), new Color(80, 155, 80), new Color(155, 80, 140)};
	
	private String iconArray[] = {"cube", "diamond", "heart", "jack", "spade", "waves"};
	private String patternArray[] = {"brick", "round"};

	public MemoryCard(boolean blank)
	{
		if(blank)
		{
			
		} else {
			randomize();
			flippedUp = false;
		}
	}
	
	
	public void randomize()
	{
		//System.out.println("Made New Card");
		
		back = patternArray[(int)(Math.random()*(patternArray.length))];
		front = iconArray[(int)(Math.random()*(iconArray.length))];
		backColor = colorBackArray[(int)(Math.random()*(colorBackArray.length))];
		frontColor = colorFrontArray[(int)(Math.random()*(colorFrontArray.length))];
	}
	
	//////////////////////////////////////////////////////////////////////////
	public void setBack(String str)
	{
		back = str;
	}
	public void setFront(String str)
	{
		front = str;
	}
	public void setBackColor(Color cl)
	{
		backColor = cl;
	}
	public void setFrontColor(Color cl)
	{
		frontColor = cl;
	}
	public void setStatus(boolean boo)
	{
		flippedUp = boo;
	}
	public void setX(int x)
	{
		posX = x;
	}
	public void setY(int y)
	{
		posY = y;
	}
	
	//////////////////////////////////////////////////////////////////////////
	public String getBack()
	{
		return back;
	}
	public String getFront()
	{
		return front;
	}
	public Color getBackColor()
	{
		return backColor;
	}
	public Color getFrontColor()
	{
		return frontColor;
	}
	public boolean getStatus()
	{
		return flippedUp;
	}
	public int getX()
	{
		return posX;
	}
	public int getY()
	{
		return posY;
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	public MemoryCard clone()
	{
		MemoryCard cpy = new MemoryCard(true);
		
		cpy.setBack(back);
		cpy.setBackColor(backColor);
		cpy.setFront(front);
		cpy.setFrontColor(frontColor);
		cpy.setStatus(flippedUp);
		cpy.setX(posX);
		cpy.setY(posY);
		
		return cpy;
	}
	
	public boolean equals(MemoryCard compare)
	{
		if(compare.getStatus() == flippedUp &&
		   compare.getBack().equalsIgnoreCase(back) &&
		   compare.getFront().equalsIgnoreCase(front) &&
		   compare.getBackColor().equals(backColor) && 
		   compare.getFrontColor().equals(frontColor)
				)
			return true;
		
		return false;
	}
	
}
