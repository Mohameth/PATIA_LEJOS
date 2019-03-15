package Final;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.lcd.LCD;

public class Controler {

	private MyNavigator nav;
	private Pince pince;
	private TouchSensor touch;

	public Controler() {
		this.nav = new MyNavigator();
		this.pince = new Pince(StateP.CLOSE);
		this.touch = new TouchSensor();
	}
	
	public void StartProg() {
		
		nav.goTo(100, 0);
		while (nav.isMoving() && !touch.isPressed());
		if (nav.isMoving()) {
			nav.stop();
			this.rammenePalet();
		} else {
			LCD.drawString("palet non detecte", 1, 1);
		}
		Button.waitForAnyPress();
	}
	
	public void rammenePalet() {
		pince.FermePince();
		nav.goTo(-10, 0);
		while(nav.isMoving());
		pince.OuvrirPince();
		nav.goTo(0, 0);
	}
	
	public void CloseProg() {
		this.pince.FermePince();
		this.nav.stop();
	}
}
