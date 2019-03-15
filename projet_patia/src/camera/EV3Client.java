package camera;


import java.net.DatagramPacket;
import java.net.DatagramSocket;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;

public class EV3Client {
  public static void main(String args[]) 
  {
    try 
    {
      int port = 8888;
      
      EV3 ev3brick = (EV3) BrickFinder.getLocal();
      Sound.setVolume(10);
      Sound.beep();

      // Create a socket to listen on the port.
      DatagramSocket dsocket = new DatagramSocket(port);

      // Create a buffer to read datagrams into. If a
      // packet is larger than this buffer, the
      // excess will simply be discarded!
      byte[] buffer = new byte[2048];

      // Create a packet to receive data into the buffer
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

      // Now loop forever, waiting to receive packets and printing them.
      boolean again = true;
      while (again) 
      {
        // Wait to receive a datagram
        dsocket.receive(packet);

        // Convert the contents to a string, and display them
        String msg = new String(buffer, 0, packet.getLength());
        //System.out.println(packet.getAddress().getHostName() + ": "
        //    + msg);
        
        String[] palets = msg.split("\n");
        
        for (int i = 0; i < palets.length; i++) 
        {
        	String[] coord = palets[i].split(";");
        	int x = Integer.parseInt(coord[1]);
        	int y = Integer.parseInt(coord[2]);
        
        	System.out.println();
        	LCD.clear();
        	LCD.drawString("                                                                     ", 0, i);
        	LCD.drawString("x: " + Integer.toString(x) + " y: " + Integer.toString(y) , 0, i);
        }
        

        // Reset the length of the packet before reusing it.
        packet.setLength(buffer.length);
        
        if(Button.ESCAPE.isDown()) {
			again = false;
		}
      }
     
    } 
    catch (Exception e) 
    {
      System.err.println(e);
    }
  }
}
           