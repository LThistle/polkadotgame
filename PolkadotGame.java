import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;

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
      polkadotFrame.setCursor( polkadotFrame.getToolkit().createCustomCursor(
                   new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(),null));
   }
}

class PolkadotPanel extends JPanel
{
   private static final int FRAME = 800;
   private BufferedImage myImage;
   private Graphics myBuffer;
   private MouseDot myMouse;
   private ArrayList<Polkadot> myDots = new ArrayList<Polkadot>();
   private Timer t;
   private Timer t2;
   
   public PolkadotPanel()
   {
      myImage = new BufferedImage(FRAME, FRAME, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
      myMouse = new MouseDot(50,1,1,randomColor());
      
      for(int i=0; i<10; i++)
      {
         myDots.add(new Polkadot(Math.random()*100,(int)(Math.random()*800),(int)(Math.random()*800),randomColor()));
      }
      
      t = new Timer(1, new Listener());
      t.start();
      
      t2 = new Timer(500, new ExitListener());
      t2.start();
   }
   
   public void paintComponent(Graphics g)
   {
      g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
   }
   
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {     
         myBuffer.setColor(Color.WHITE); 
         myBuffer.fillRect(0,0,FRAME,FRAME);
         
         myMouse.updatePos(getMouseX(),getMouseY());
         myMouse.drawme(myBuffer);
         
         Iterator<Polkadot> myIter = myDots.iterator();
      
         while (myIter.hasNext()) {
            Polkadot p = myIter.next();
            p.move();
            p.drawme(myBuffer);
            int val = myMouse.check(p);
            if(val==2)
            {
               myIter.remove();
            }
            else if(val==1)
            {
               t.stop();
               t2.stop();
            }
         }     
      
         repaint();
      }
   }
   
   private class ExitListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {     
         myDots.add(new Polkadot(Math.random()*100,(int)(Math.random()*800),(int)(Math.random()*800),randomColor()));
         
         Iterator<Polkadot> myIter = myDots.iterator();
      
         while (myIter.hasNext()) {
            Polkadot p = myIter.next();      
            if(hasExited(p))
               myIter.remove();
         }
      }
   }
   
   private int getMouseX()
   {
      Container container = getParent();
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
      Container container = getParent();
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
   
   private Color randomColor()
   {
      int r = (int)(Math.random()*190) + 66;
      int g = (int)(Math.random()*190) + 66;
      int b = (int)(Math.random()*190) + 66;
      Color myColor = new Color(r,g,b);
      return myColor;
   }
   
   private boolean hasExited(Polkadot p)
   {
      if(p.getX()-p.getRadius()>=800)
         return true;
      if(p.getX()+p.getRadius()<=0)
         return true;
      if(p.getY()-p.getRadius()>=800)
         return true;
      if(p.getY()+p.getRadius()<=0)
         return true;
      return false;
   }
}

class Polkadot extends Circle
{
   private int directionX;
   private int directionY;
   
   public Polkadot(double size, int x, int y, Color c)
   {
      super(size,x,y,c);
      do
      {
         directionX = (int)(Math.random()*4)-2;
         directionY = (int)(Math.random()*4)-2;
      } while(Math.abs(directionX)==0 && Math.abs(directionY)==0);
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
      updateSize((getDiameter()+2));
   }
   
   public int check(Polkadot p)
   {
      
      double distance = Math.sqrt((Math.pow(getX()-p.getX(),2) + Math.pow(getY()-p.getY(),2)));
      if(distance<=getRadius()+p.getRadius())
      {
         if(getRadius()>p.getRadius())
         {
            eatDot();
            return 2;
         }
         else
            return 1;
      }
      return 0;
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