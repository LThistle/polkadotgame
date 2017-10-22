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
   
}

class MouseDot extends Circle
{
   public MouseDot(int size, int x, int y, Color color)
   {
      super(size,x,y,color);
   }
}

class Circle
{
   private double diameter;
   private double myX;  
   private double myY;
   private Color myColor;
   
   public Circle(double size, double x, double y, Color color)
   {
      diameter = size;
      myX = x;
      myY = y;
      myColor = color;
   }
   
   public double getX()
   {
      return myX;
   }
   
   public double getY()
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
   
   public void drawme(Graphics myBuffer) 
   {
      myBuffer.setColor(this.myColor);
      myBuffer.fillOval((int)(getX() - getRadius()), (int)(getY()-getRadius()), (int)getDiameter(), (int)getDiameter());
   }
}