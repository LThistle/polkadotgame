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
      polkadotFrame.setContentPane(new Menu(polkadotFrame));
      polkadotFrame.setVisible(true);
   }
}

class Menu extends JPanel
{
   private JFrame myFrame;
   public Menu(JFrame superFrame)
   {
      myFrame = superFrame;
      JButton myButton = new JButton("Start!");
      myButton.addActionListener(new Listener());
      add(myButton);
   }
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {   
         myFrame.setContentPane(new PolkadotPanel(myFrame));   
         myFrame.setVisible(true);      
      }
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
   private JLabel myScore;
   private boolean isAlive = true;
   
   public PolkadotPanel(JFrame superFrame)
   {
      setCursor(getToolkit().createCustomCursor(
                   new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(),null));
      myImage = new BufferedImage(FRAME, FRAME, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
      myMouse = new MouseDot(25,1,1,randomColor());
      
      setLayout(null);
      myScore = new JLabel("0");
      myScore.setHorizontalAlignment(SwingConstants.RIGHT);
      myScore.setFont((new java.awt.Font("SansSerif", 1, 100)));
      myScore.setBounds(475, 0, 300, 100);   
      add(myScore);
   
      
      t = new Timer(1, new Listener());
      t.start();
      
      t2 = new Timer(500, new UpdateListener());
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
               myScore.setText(""+myMouse.getEaten());
            }
            else if(val==1)
            {
               t.stop();
               t2.stop();
               isAlive = false;
            }
         }     
         repaint();
      }
   }
   
   private class UpdateListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {     
         spawn();
         
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
      if(mouseX>=800)
         return 800;
      if(mouseX<=0)
         return 0;
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
      if(mouseY>=800)
         return 800;
      if(mouseY<=0)
         return 0;
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
      if(p.getX()-p.getRadius()>=850)
         return true;
      if(p.getX()+p.getRadius()<=-50)
         return true;
      if(p.getY()-p.getRadius()>=850)
         return true;
      if(p.getY()+p.getRadius()<=-50)
         return true;
      return false;
   }
   
   private void spawn()
   {
      //left side
      double size = Math.random()*myMouse.getDiameter() + myMouse.getDiameter()/1.25;
      int x = 0-(int)(size/2);
      int y = (int)(Math.random()*800);      
      myDots.add(new Polkadot(size,x,y,randomColor()));
      //right side
      size = Math.random()*myMouse.getDiameter() + myMouse.getDiameter()/1.25;
      x = 800+(int)(size/2);
      y = (int)(Math.random()*800);
      myDots.add(new Polkadot(size,x,y,randomColor()));      
      //top
      size = Math.random()*myMouse.getDiameter() + myMouse.getDiameter()/1.25;
      x = (int)(Math.random()*800);
      y = 0-(int)(size/2);
      myDots.add(new Polkadot(size,x,y,randomColor()));
      //bottom
      size = Math.random()*myMouse.getDiameter() + myMouse.getDiameter()/1.25;
      x = (int)(Math.random()*800);
      y = 800+(int)(size/2);
      myDots.add(new Polkadot(size,x,y,randomColor()));
   }
}

class Polkadot extends Circle
{
   private double directionX;
   private double directionY;
   
   public Polkadot(double size, int x, int y, Color c)
   {
      super(size,x,y,c);
      do
      {
         directionX = Math.random()*4-2;
         directionY = Math.random()*4-2;
      } while(getDist(x,y,400,400)<=getDist(x+(int)directionX,y+(int)directionY,400,400));
   }
   
   public void move()
   {
      updatePos(getX()+(int)directionX,getY()+(int)directionY);
   }
   
   public double getDist(int x1, int y1, int x2, int y2)
   {
      return Math.sqrt((Math.pow(x1-x2,2) + Math.pow(y1-y2,2)));
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
   
   public int getEaten()
   {
      return eatenDots;
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