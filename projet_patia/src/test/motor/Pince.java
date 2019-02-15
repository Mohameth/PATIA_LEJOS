package test.motor;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Pince {

	public static void main(String[] args) {
		 EV3LargeRegulatedMotor pince = new EV3LargeRegulatedMotor(MotorPort.D);
	     pince.setSpeed(500);
//	     pince.setStallThreshold(10, 10);
	     
	     LCD.drawString("Down : fermer", 0, 0);
	     LCD.drawString("Up   : ouvrir", 0, 1);
	     LCD.drawString("Right: stop", 0, 2);
	     LCD.drawString("Left : Quitter", 0, 3);
	     
	     EV3 ev3brick = (EV3) BrickFinder.getLocal();
	     Keys buttons = ev3brick.getKeys();
	     
	     boolean fini = false;
	     while (!fini) {
	    	 switch(Button.waitForAnyPress()) {
	    		 case (Button.ID_DOWN) :
	    			
	    			pince.backward();
	    		 	
//	    		 	while (buttons.getButtons() != Keys.ID_ESCAPE)
//	    		 	{
//	    	            
//	    	            LCD.clear();
//	    	            if (pince.isStalled())
//	    	            	LCD.drawString("stall",0,5);
//	    	            else
//	    	            	LCD.drawString("not stall",0,5);
//	    		 	}
	    			 break;
	    		 case (Button.ID_UP) :
	    			 pince.forward();
	    		 	 break;
	    		 case (Button.ID_RIGHT):
	    			 pince.stop();
	    		     break;
	    		 case (Button.ID_LEFT) :
	    			 fini = true;
	    		 	 break;
	    	 }
	     }
	     
	     

	}

}
