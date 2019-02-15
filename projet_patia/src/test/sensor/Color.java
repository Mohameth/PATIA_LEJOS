package test.sensor;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.utility.Delay;

public class Color {
	public static void main(String[] args)
    {
        log("Program starts");
        EV3 ev3brick = (EV3) BrickFinder.getLocal();
        ColorSensor sensor = new ColorSensor(SensorPort.S3);
        Keys buttons = ev3brick.getKeys();
        
        while (buttons.getButtons() != Keys.ID_ESCAPE)
        {
            Delay.msDelay(1000);
            LCD.clear();
            LCD.drawInt(sensor.getColorID(),0,5);
        }

        log("Program ends");
    }


    private static void log(String msg)
    {
        System.out.println("log>\t" + msg);
    }
}
