package test.motor;

import Final.Pince;
import Final.StateP;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class PinceTes {

	public static void main(String[] args) {
		Pince p = new Pince(StateP.CLOSE);
		
		LCD.drawInt(p.GetSpeed(), 5, 5);
		LCD.drawString("pince ouvert", 1, 1);
		LCD.drawString("appuyer pour fermer", 1, 2);
		Button.waitForAnyPress();
		p.OuvrirPince();
		p.FermePince();
		LCD.drawString("pince fermer", 1, 1);
		LCD.drawString("appuyer pour ouvrir", 1, 2);
		Button.waitForAnyPress();
		
		p.OuvrirPince();
		LCD.drawString("pince ouvert", 1, 1);
		LCD.drawString("appuyer pour fermer", 1, 2);
		Button.waitForAnyPress();
		
		p.FermePince();
			
	}

}
