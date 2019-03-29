package Final;

import java.nio.file.Path;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.utility.Delay;

public class MyNavigator {
	static EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(
			MotorPort.B);
	static EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(
			MotorPort.A);
	
	private MovePilot ev3robot;
	public Navigator navbot;
	
	public Waypoint current;
	
	public MyNavigator() {
		// setup the wheel diameter of left (and right) motor in centimeters,
		// i.e. 5.5 cm
		// the offset number is the distance between the center of wheel to
		// the center of robot, i.e. half of track width
		
		Wheel wheel1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.6).offset(-6.75);
		Wheel wheel2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.6).offset(6.75);
		
		// set up the chassis type, i.e. Differential pilot
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 },
				WheeledChassis.TYPE_DIFFERENTIAL);
		ev3robot = new MovePilot(chassis);
		ev3robot.setAngularSpeed(90);
		
		navbot = new Navigator(ev3robot);
	}
	
	public void goTo(double x,double y) {
		current = new Waypoint(x, y);
	
		navbot.goTo(current);
	}
	
	public Waypoint GetCurrentDest() {
		return current;
	}
	
	public void stop() {
		navbot.stop();
		navbot.clearPath();
		
	}
	
	public boolean isMoving() {
		return navbot.isMoving();
	}
	
	public void setCoord(float x, float y) {
		Pose p = new Pose();
		p.setLocation(x, y);
		navbot.getPoseProvider().setPose(p);
	}
	
	public Pose getCoord() {
		return navbot.getPoseProvider().getPose();
	}
	
	public void DeposePalet() {
		navbot.goTo(0, 0);
	}
	
}
