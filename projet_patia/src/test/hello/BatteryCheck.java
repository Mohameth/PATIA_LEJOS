package test.hello;

import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class BatteryCheck {

	public static void main(String[] args) {
		
		LCD.drawString("Battery :" + (int) (Battery.getBatteryCurrent()*100) + "%", 0, 4);
		Button.waitForAnyPress();
	}

}
