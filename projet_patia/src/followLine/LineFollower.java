package followLine;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;


public class LineFollower
{
    public static void main(String[] args)
    {
        Controller controller = new Controller(SensorPort.S3, MotorPort.B, MotorPort.A);

        controller.run();
    }
}