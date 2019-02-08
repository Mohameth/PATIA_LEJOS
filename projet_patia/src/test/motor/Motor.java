package test.motor;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Motor {

	public static void main(String[] args) {
		 log("Program Starting");

	        EV3LargeRegulatedMotor motord = new EV3LargeRegulatedMotor(MotorPort.A);
	        motord.setSpeed(500);
	        EV3LargeRegulatedMotor motorg = new EV3LargeRegulatedMotor(MotorPort.B);
	        motorg.setSpeed(500);

	        log("Forward motion");
	        motord.forward();
	        motorg.forward();

	        Delay.msDelay(3000);

	        log("Stopping motor");
	        motord.stop();
	        motorg.stop();
	        
	        log("Program Ending");
	    }


	    private static void log(String msg) 
	    { 
	        System.out.println("log>\t" + msg); 
	    }

}
