package test;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import test.motor.DifferentialDrive;

public class WallDetector {

	public static void main(String[] args) {
		// get EV3 brick
		EV3 ev3brick = (EV3) BrickFinder.getLocal();

		// LCD class for displaying and Keys class for buttons
		Keys buttons = ev3brick.getKeys();
		TextLCD lcddisplay = ev3brick.getTextLCD();

		// block the thread until a button is pressed
		buttons.waitForAnyPress();

		// get a port instance
		Port portS1 = ev3brick.getPort("S1");

		// Get an instance of the Ultrasonic EV3 sensor
		EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(portS1);

		// get an instance of this sensor in measurement mode
		SampleProvider sonicdistance = sonicSensor.getDistanceMode();

		// initialise an array of floats for fetching samples
		float[] sample = new float[sonicdistance.sampleSize()];

		// fetch a sample
		sonicdistance.fetchSample(sample, 0);
		
		DifferentialDrive drive = new DifferentialDrive(MotorPort.A, MotorPort.B);

        drive.forward();

        boolean fini = false;
		while (buttons.getButtons() != Keys.ID_ESCAPE && !fini) {

			sonicdistance.fetchSample(sample, 0);
			lcddisplay.clear();
			lcddisplay.drawString("distance: " + sample[0], 0, 1);
			if (sample[0] < 0.1) {
				fini = true;
				drive.stop();
			}
				
				
			Delay.msDelay(100);

		}

	}

}
