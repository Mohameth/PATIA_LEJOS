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
		
		Wheel wheel1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.6).offset(-6);
		Wheel wheel2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.6).offset(6);
		
		// set up the chassis type, i.e. Differential pilot
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 },
				WheeledChassis.TYPE_DIFFERENTIAL);
		ev3robot = new MovePilot(chassis);
		ev3robot.setAngularSpeed(90);
		ev3robot.setLinearSpeed(20);
		
		
		navbot = new Navigator(ev3robot);
	}
	
	public void goTo(double x,double y) {
		navbot.clearPath();
		current = new Waypoint(x, y);
		
		navbot.goTo(current);
		
	}
	
	public void goTo(float x,float y,float heading) {
		navbot.clearPath();
		
		navbot.goTo(x, y, heading);
	}
	
	public Waypoint GetCurrentDest() {
		return current;
	}
	
	public void stop() {
		navbot.stop();
		navbot.clearPath();
		
	}
	
	public void resume() {
		navbot.goTo(this.current);
	}
	
	public boolean isMoving() {
		return navbot.isMoving();
	}
	
	public void setCoord(float x, float y) {
		Pose p = new Pose();
		p.setLocation(x, y);
		navbot.getPoseProvider().setPose(p);
	}
	
	public void setCoord(float x, float y,float heading) {
		Pose p = new Pose();
		p.setLocation(x, y);
		p.setHeading(heading);
		navbot.getPoseProvider().setPose(p);
	}
	
	public Pose getCoord() {
		return navbot.getPoseProvider().getPose();
	}
	
	public void DeposePalet() {
		float heading = navbot.getPoseProvider().getPose().getHeading();
		ev3robot.travel(-5);
		LCD.drawInt((int) heading, 5, 5);
		ev3robot.rotate(180);
		this.setCoord(0, 0, 0);
		//navbot.goTo(0, 0);
		
	}
	
	public void goToPalet(float x,float y) {
		ev3robot.travel(x+12);
		if (y<0) {
			ev3robot.rotate(-90);
			ev3robot.travel(-1*y);
		}
		else { 
			ev3robot.rotate(90);
			ev3robot.travel(y);
		}
	}
	
	public void goToBase(float x, float y) {
		ev3robot.rotate(180);
		if (y<0) {
			ev3robot.travel(-1*y);
			ev3robot.rotate(90);
		}
		else { 			
			ev3robot.travel(y);
			ev3robot.rotate(-90);
		}
		
		ev3robot.travel(x+12+10);
		
	}
	
	public void rotate90() {
		ev3robot.rotate(90);
	}
	
	public void rotate360() {
		ev3robot.rotate(360);
	}
	
	public void travel(int distance, int angle) {
		ev3robot.rotate(angle);
		ev3robot.travel(distance);
	}
}
