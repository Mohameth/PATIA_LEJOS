package MyNavigator;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;

public class MyNav {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Sound.setVolume(10);
		Sound.beep();
		
		Controler c = new Controler();
		
		EV3 ev3brick = (EV3) BrickFinder.getLocal();
		Keys buttons = ev3brick.getKeys();
		buttons.waitForAnyPress();
		
		c.StartProg();
		
	}
}
