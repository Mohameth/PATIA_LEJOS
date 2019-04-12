package HelloWorld;
import java.util.Arrays;

import ColorCal.IOFile;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.internal.ev3.EV3Battery;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class ColorCalFile {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		
		
		EV3 ev3brick = (EV3) BrickFinder.getLocal();
		Sound.setVolume(10);
		Sound.beep();
		Keys buttons = ev3brick.getKeys();
		
		
		 float[][] sampleList;
		 String[] listOfColors = {"red","green","blue","yellow","white","gray","black"};
		 IOFile save = new IOFile();
		try {
			
			
			
			
					
			Port port = LocalEV3.get().getPort("S3");
			EV3ColorSensor colorSensor = new EV3ColorSensor(port);
			SampleProvider average = new MeanFilter(colorSensor.getRGBMode(), 1);
			colorSensor.setFloodlight(Color.WHITE);
			
			
			
			
			System.out.println("UP calibrate");
			System.out.println("DOWN detectcolor");
			buttons.waitForAnyPress();
			
			if (buttons.getButtons()==Keys.ID_UP) {
				
				save.initOutputFile();
				sampleList = new float[listOfColors.length][average.sampleSize()];

				for (int i=0; i<listOfColors.length;i++) {
					
					System.out.println("Press enter to calibrate:" + listOfColors[i] );
					Button.ENTER.waitForPressAndRelease();
					sampleList[i] = new float[average.sampleSize()];
					average.fetchSample(sampleList[i], 0);
					save.saveIntofile(listOfColors[i], Arrays.toString(sampleList[i]));
				}
			}
			save.initInputFile();
			boolean again = true;
			
			while (again) {
				float[] sample = new float[average.sampleSize()];
				System.out.println("\nPress enter to detect a color...");
				Button.ENTER.waitForPressAndRelease();
				average.fetchSample(sample, 0);
				double minscal = Double.MAX_VALUE;
				String color = "";
				
				
				for (int j=0; j<listOfColors.length;j++) {
				
					//double scalaire = ColorCalFile.scalaire(sample, sampleList[j]);
					float[] tmp = save.getFloatArrayFromKey(listOfColors[j]);
					//String tmp = save.getValFromKey(listOfColors[j]);
					System.out.println(tmp);
					double scalaire = ColorCalFile.scalaire(sample, tmp );
					if (scalaire < minscal) {
						minscal = scalaire;
						color = listOfColors[j];
					}
				}
				
				System.out.println("The color is " + color + " \n");
				System.out.println("Press ENTER to continue \n");
				System.out.println("ESCAPE to exit");
				Button.waitForAnyPress();
				if(Button.ESCAPE.isDown()) {
					colorSensor.setFloodlight(false);
					again = false;
				}
			}
			
//			float[] a =  save.getFloatArrayFromKey(listOfColors[j]);
//			System.out.println(a.length);
//			System.out.println(a[0]+","+a[1]+" "+a[2]+"\n");
//
//			 a =  save.getFloatArrayFromKey("green");
//			System.out.println(a.length);
//			System.out.println(a[0]+","+a[1]+" "+a[2]);
//			
//			Button.waitForAnyPress();
			colorSensor.close();
			
		} catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);
		}
	}
	
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}

}
