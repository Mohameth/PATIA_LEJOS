package test.motor;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Pince {

	public static void main(String[] args) {
		 EV3LargeRegulatedMotor pince = new EV3LargeRegulatedMotor(MotorPort.D);
	     pince.setSpeed(500);
	     
	     LCD.drawString("Down : fermer", 0, 0);
	     LCD.drawString("Up   : ouvrir", 0, 1);
	     LCD.drawString("Right: stop", 0, 2);
	     LCD.drawString("Left : Quitter", 0, 3);
	     
	     
	     boolean fini = false;
	     while (!fini) {
	    	 switch(Button.waitForAnyPress()) {
	    		 case (Button.ID_DOWN) :
	    			 pince.backward();
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
