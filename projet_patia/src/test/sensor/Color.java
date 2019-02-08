package test.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.utility.Delay;

public class Color {
	public static void main(String[] args)
    {
        log("Program starts");

        ColorSensor sensor = new ColorSensor(SensorPort.S3);

        for (int ii = 0; ii < 10; ii++)
        {
            Delay.msDelay(1500);

            log("On Path: " + sensor.onPath());
        }

        log("Program ends");
    }


    private static void log(String msg)
    {
        System.out.println("log>\t" + msg);
    }
}
