package obs_color;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class MainSensorTest {
	private static col_sensor_thread mysensor;

	public static void main(String[] args) {
		mysensor = new col_sensor_thread();
		mysensor.start();
		while (!Button.ESCAPE.isDown()) {			
			//void
		}
		System.exit(0);
	}
}
