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