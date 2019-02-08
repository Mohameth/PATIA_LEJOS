package test.hello;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class hello_World {

	public static void main(String[] args) {
		LCD.drawString("Hello World",2,4);
		Button.waitForAnyPress();
	}

}
