package MyNavigator;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.awt.Point;
import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;

public class Cam_Palet {
  
	private DatagramSocket dsocket;
	
	public Cam_Palet() {
 
		int port = 8888;
      
      	// Create a socket to listen on the port.
      	try {
    	  dsocket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int getPaletnumber() {
		
      // Create a buffer to read datagrams into. If a
      // packet is larger than this buffer, the
      // excess will simply be discarded!
      byte[] buffer = new byte[2048];

      // Create a packet to receive data into the buffer
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

      // Wait to receive a datagram
      try {
		dsocket.receive(packet);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return 0;
	}

	  // Convert the contents to a string, and display them
	  String msg = new String(buffer, 0, packet.getLength());
	  //System.out.println(packet.getAddress().getHostName() + ": "
	  //    + msg);
        
      String[] palets = msg.split("\n");

      // Reset the length of the packet before reusing it.
      packet.setLength(buffer.length);

      return palets.length;
	}
	
	public ArrayList<Point> GetPaletList() {
		// Create a buffer to read datagrams into. If a
		// packet is larger than this buffer, the
		// excess will simply be discarded!
		byte[] buffer = new byte[2048];

		// Create a packet to receive data into the buffer
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		// Wait to receive a datagram
		try {
			dsocket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<Point>();
		}

		// Convert the contents to a string, and display them
		String msg = new String(buffer, 0, packet.getLength());
		//System.out.println(packet.getAddress().getHostName() + ": "
		//    + msg);
        
		String[] palets = msg.split("\n");
	    
		ArrayList<Point> res = new ArrayList<>();
	    for (int i = 0; i < palets.length; i++) {
        	String[] coord = palets[i].split(";");
        	int x = Integer.parseInt(coord[1]);
        	int y = Integer.parseInt(coord[2]);
        
        	res.add(new Point(x,y));
        }
        

        // Reset the length of the packet before reusing it.
        packet.setLength(buffer.length);
        
        return res;
        
      }
     
}
           