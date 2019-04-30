package MyNavigator;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class CalibrateDirection {
	EV3 ev3brick;
	Port portS1;
	EV3UltrasonicSensor sonicSensor;
	SampleProvider sonicdistance;
	Tomtom tomtom;
	DistanceSensorThread dist;
	
	public CalibrateDirection(Tomtom t) {
		this.tomtom = t;
		this.ev3brick = (EV3) BrickFinder.getLocal();
		portS1=ev3brick.getPort("S1");
		sonicSensor = new EV3UltrasonicSensor(portS1);
		sonicdistance = sonicSensor.getDistanceMode();
	}
	
	
	private float getDistance() {
		float[] sample = new float[sonicdistance.sampleSize()];
		sonicdistance.fetchSample(sample, 0);
		return sample[0];
	}
	
	public void Calibrate() {
			// initialise an array of floats for fetching samples
			boolean contRotate = true;
			int angle = 5;

			
			float currentDistance = -1.0f;
			float rightDistance = -1.0f;
			float oldDistance = -1.0f;
			while (currentDistance<=0) {
				currentDistance = getDistance();				
			}
			
			this.tomtom.rotateAngle(10);
			while (rightDistance<=0) {
				rightDistance = getDistance();
			}
			
			
			System.out.println("cur distance=" + currentDistance);
			System.out.println("right distance=" + rightDistance);

			if (currentDistance<rightDistance) {
				angle = -2;
			}else {
				angle = 2;
			}
			this.tomtom.rotateAngle(-10);
			
			//System.exit(0);
			oldDistance=currentDistance;
			while(contRotate) {				
				this.tomtom.rotateAngle(angle);
				while (currentDistance<=0) {
					currentDistance = getDistance();
				}
				if (oldDistance<currentDistance) {
					contRotate=false;
				}
				oldDistance=currentDistance;
			}
			this.tomtom.rotateAngle(angle*-1);
			
	}	
}
