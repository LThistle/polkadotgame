import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class PolkadotGame
{
   public static void main(String[] args)
   {
      JFrame polkadotFrame = new JFrame("Polkadot Game");
      polkadotFrame.setSize(800,800);
      polkadotFrame.setLocation(400,200);
      polkadotFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      polkadotFrame.setContentPane(new PolkadotPanel());
      polkadotFrame.setVisible(true);
   }
}

class PolkadotPanel extends JPanel
{
   private static final int FRAME = 800;
   private BufferedImage myImage;
   private Graphics myBuffer;
   private MouseDot myMouse;
   private Polkadot[] polkadots;
   
   public PolkadotPanel()
   {
      myImage = new BufferedImage(FRAME, FRAME, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
      myMouse = new MouseDot(100,1,1,Color.RED);
      
      polkadots = new Polkadot[10];
      for(int i=0; i<10; i++)
      {
         polkadots[i] = new Polkadot(Math.random()*100,(int)(Math.random()*800),(int)(Math.random()*800),Color.BLUE);
      }
      
      Timer t = new Timer(1, new Listener());
      t.start();
      
   }
   
   public void paintComponent(Graphics g)
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
   
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         myBuffer.setColor(Color.WHITE);    //cover the 
         myBuffer.fillRect(0,0,FRAME,FRAME);
         
         myMouse.updatePos(getMouseX(),getMouseY());
         myMouse.drawme(myBuffer);
         
         
         for(int i=0; i<10; i++)
         {
            polkadots[i].move();
            polkadots[i].drawme(myBuffer);
         }
         repaint();
      }
   }
   
   private int getMouseX()
   {
      Container container = this.getParent();
      Container previous = container;
      while (container != null)
      {
         previous = container;
         container = container.getParent();
      }
      int mouseX = (int)MouseInfo.getPointerInfo().getLocation().getX();   
      mouseX-= previous.getX();
      return mouseX;
   }
   
   private int getMouseY()
   {
      Container container = this.getParent();
      Container previous = container;
      while (container != null)
      {
         previous = container;
         container = container.getParent();
      }
      int mouseY = (int)MouseInfo.getPointerInfo().getLocation().getY();
      mouseY-= previous.getY();
      return mouseY;
   }
}

class Polkadot extends Circle
{
   private int directionX;
   private int directionY;
   
   public Polkadot(double size, int x, int y, Color c)
   {
      super(size,x,y,c);
      directionX = (int)(Math.random()*4)-2;
      directionY = (int)(Math.random()*4)-2;
   }
   
   public void move()
   {
      updatePos(getX()+directionX,getY()+directionY);
   }
}

class MouseDot extends Circle
{
   private int eatenDots = 0;
   
   public MouseDot(double size, int x, int y, Color c)
   {
      super(size,x,y,c);
   }
   
   public void eatDot()
   {
      eatenDots++;
      updateSize((getDiameter()*1.1));
   }
}

class Circle
{
   private double diameter;
   private int myX;  
   private int myY;
   private Color myColor;
   
   public Circle(double size, int x, int y, Color c)
   {
      diameter = size;
      myX = x;
      myY = y;
      myColor = c;
   }
   
   public int getX()
   {
      return myX;
   }
   
   public int getY()
   {
      return myY;
   }
   
   public double getDiameter()
   {
      return diameter;
   }
   
   public double getRadius()
   {
      return getDiameter()/2;
   }
   
   public void updateSize(double d)
   {
      diameter = d;
   }
   
   public void updatePos(int x, int y)
   {
      myX = x;
      myY = y;
   }
   
   public void drawme(Graphics myBuffer) 
   {
      myBuffer.setColor(this.myColor);
      myBuffer.fillOval((int)(getX() - getRadius()), (int)(getY()-getRadius()), (int)getDiameter(), (int)getDiameter());
   }
}