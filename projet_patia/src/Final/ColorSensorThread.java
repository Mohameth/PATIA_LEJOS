package Final;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ColorCalibration.ColorCalFile;
import Final.IOFile;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class ColorSensorThread extends Thread {
	private String currentcolor = "gray"; //property
	PropertyChangeSupport pcs = new  PropertyChangeSupport(this);
	
	private IOFile save;
	private final String[] listOfColors = {"red","green","blue","yellow","white","gray","black"};
	private float[][] sampleList;
	private Port port = LocalEV3.get().getPort("S3");
	private EV3ColorSensor colorSensor;
	private SampleProvider average;
	
	public ColorSensorThread() {
		this.colorSensor = new EV3ColorSensor(port);
		this.average = new MeanFilter(colorSensor.getRGBMode(), 1);
		this.colorSensor.setFloodlight(Color.WHITE);

		
		this.save = new IOFile();
		save.initInputFile();
	}
	
	
	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener("colorChange", l);
	}
	
	public void setProperty(String val) {
		String old = currentcolor;
		currentcolor = val;
		pcs.firePropertyChange("colorChange", old, val);
	}
	

	public String getCurrentColor() {
		return currentcolor;
	}
	
	public void run() {
		while (true) {
			Delay.msDelay(50);
			if (isColorDifferent()) {
				//notify main
				this.setProperty(getCurrentColor());
			}
		}
	}
	
	
	public boolean isColorDifferent() {
		float[] sample = new float[average.sampleSize()];
		average.fetchSample(sample, 0);
		double minscal = Double.MAX_VALUE;
		String color = "";
		
		
		for (int j=0; j<listOfColors.length;j++) {
		
			float[] tmp = save.getFloatArrayFromKey(listOfColors[j]);
			double scalaire = ColorCalFile.scalaire(sample, tmp );
			if (scalaire < minscal) {
				minscal = scalaire;
				color = listOfColors[j];
			}
		}
		
		//System.out.println("The color is " + color + " \n");
		
		if (color != currentcolor) {
			this.currentcolor = color;
			return true;
		}else {
			return false;
		}
	}
}
