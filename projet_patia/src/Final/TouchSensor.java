package Final;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;


public class TouchSensor extends EV3TouchSensor
{
	private static Port p = SensorPort.S4;
	
    public TouchSensor()
    {
        super(p);
    }

    public boolean isPressed()
    {
        float[] sample = new float[1];
        fetchSample(sample, 0);

        return sample[0] != 0;
    }
}
