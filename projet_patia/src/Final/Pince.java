package Final;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;



public class Pince {

	static EV3LargeRegulatedMotor pince = new EV3LargeRegulatedMotor(MotorPort.D);
	static int speed = 500;
	static int delayPince = 3000;	

	
	public StateP state;
	
	public Pince(StateP s) {
		state = s;
		pince.setSpeed(speed);
		if (s == StateP.CLOSE)
			this.OuvrirPince();
	}
	
	public void OuvrirPince() {
		if (state !=StateP.OPEN) {
			pince.forward();
			state = StateP.MOVING;
			Delay.msDelay(delayPince);
			pince.stop();
			state = StateP.OPEN;
		}
	}
	
	public void FermePince() {
		if (state != StateP.CLOSE) {
			pince.backward();
			state = StateP.MOVING;
			Delay.msDelay(delayPince);
			pince.stop();
			state = StateP.CLOSE;
		}
	}
	
	public int GetSpeed() {
		return pince.getSpeed();
	}
	
}
