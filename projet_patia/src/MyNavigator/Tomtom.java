package MyNavigator;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Vector;

public class Tomtom {
	
	static EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(
			MotorPort.B);
	static EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(
			MotorPort.A);
	
	private MovePilot ev3robot;
	private double distanceOveride = 2.5;
	private double currentX= 30; //centre depart
	private double currentY= 110;
	private double extX =currentX + 50 ; ///direction depart
	private double extY = currentY + 0;

	public Tomtom() {
		Wheel wheel1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.6).offset(-6);
		Wheel wheel2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.6).offset(6);
		
		// set up the chassis type, i.e. Differential pilot
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 },
				WheeledChassis.TYPE_DIFFERENTIAL);
		this.ev3robot = new MovePilot(chassis);
		this.ev3robot.setAngularSpeed(90);
		this.ev3robot.setLinearSpeed(20);
		
	}
	
	public void rotateAngle(double angle) {
		this.ev3robot.rotate(angle);
	}
	
	public void goTravel(double distance) {
		this.ev3robot.travel(distance);	
	}
	
	public void goTo(double x, double y) {
		double angle = getAngle(this.currentX,this.currentY,x,y);
		if (y>this.currentY) {
			angle=-angle;
		}
		System.out.println("Angle: " + angle);
		this.rotateAngle(angle);
		double distance = this.calculateDistanceBetweenPoints(this.currentX, this.currentY, x, y)*distanceOveride;
		this.setExtCoord(x, y, distance);
		this.goTravel(distance);
		this.currentX = x;
		this.currentY = y;
	}
	
	private void setExtCoord(double x, double y, double distance) {
		double lengthAB = distance;
		this.extX = x + (x - this.currentX) / lengthAB * 50;
		this.extY = y + (y - this.currentY) / lengthAB * 50;
	}
	
	public double calculateDistanceBetweenPoints(
		double x1, 
		double y1, 
		double x2, 
		double y2) {       
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
	
	public double getAngle(double robX, double robY,double palX, double palY) {
		//a = (p1.x - p2.x, p1.y - p2.y)
		//b = (p1.x - p3.x, p1.y - p3.y)
		
		double robExtX = this.extX;
		double robExtY = this.extY;

//		
//		p1[0] = robX
//		p1[1] = robY
//		
//		p0[0] = palX
//		p0[1] = palY
//		
//		p2[0] = robExtX
//		p2[1] = robExtY
//		
		double a = Math.pow(robX-palX,2) + Math.pow(robY-palY,2);
		double b = Math.pow(robX-robExtX,2) + Math.pow(robY-robExtY,2);
		double c = Math.pow(robExtX-palX,2) + Math.pow(robExtY-palY,2);
		return Math.acos( (a+b-c) / Math.sqrt(4.0*a*b) ) * 180.0/Math.PI;
				  
	}
	
	

}

