package MyNavigator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import Final.IOFile;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class DistanceSensorThread extends Thread {
	EV3 ev3brick;
	Port portS1;
	EV3UltrasonicSensor sonicSensor;
	SampleProvider sonicdistance;
	private List<MyObserver> obs = new ArrayList<>();
	private float oldDistance;
	private float curDistance;
	private boolean exit = false;
	
	public DistanceSensorThread() {
		this.ev3brick = (EV3) BrickFinder.getLocal();
		portS1=ev3brick.getPort("S1");
		sonicSensor = new EV3UltrasonicSensor(portS1);
		sonicdistance = sonicSensor.getDistanceMode();
		oldDistance=-1.0f;
		curDistance=-1.0f;
	}
	
	public void addObserver(MyObserver channel) {
	    this.obs.add(channel);
	}

	public void removeObserver(MyObserver channel) {
	    this.obs.remove(channel);
	}
	
	public void notifyAll(String newCol) {
        for (MyObserver channel : this.obs) {
            channel.update(newCol);
        }
    }
	
	public float getCurrentDistance() {
		float[] sample = new float[sonicdistance.sampleSize()];
		sonicdistance.fetchSample(sample, 0);
		return sample[0];
	}

	public boolean isCurrentMin() {
		boolean res;
		oldDistance=curDistance;
		float[] sample = new float[sonicdistance.sampleSize()];
		sonicdistance.fetchSample(sample, 0);
		curDistance = sample[0];
		if (curDistance>oldDistance) {
			res= true;
		}else {
			res= false;
		}
		return res;
	}
		
	public void run() {
		exit = false;
		while (!exit) {
			Delay.msDelay(0);
			if (isCurrentMin()) {
				//notify main
				this.notifyAll("stopRotate");
			}
		}
	}
	
	public void stopMe(){
        exit = true;
    }
}
