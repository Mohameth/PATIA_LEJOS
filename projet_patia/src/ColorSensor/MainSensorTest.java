package ColorSensor;

import Final.ColorSensorThread;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class MainSensorTest {
	private static ColorSensorThread mysensor;

	public static void main(String[] args) {
		mysensor = new ColorSensorThread();
		mysensor.start();
		while (!Button.ESCAPE.isDown()) {			
			//void
		}
		System.exit(0);
	}
}
