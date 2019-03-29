package test;

import Final.TouchSensor;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.utility.Delay;
import lejos.utility.TextMenu;


public class NavigatorTest {
	static EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(
			MotorPort.B);
	static EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(
			MotorPort.A);
	static EV3LargeRegulatedMotor pince = new EV3LargeRegulatedMotor(MotorPort.D);
    
	static TouchSensor touch = new TouchSensor();
	
	static int delayPince = 3000;

	public static void main(String[] args) throws InterruptedException {

		// menu options for displaying navigator controlling
		String[] menuItems = new String[1];
		menuItems[0] = "Task 1: Navigator";

		// display menu
		TextMenu menu = new TextMenu(menuItems);
		menu.setTitle("NavRobot");

		// setup the wheel diameter of left (and right) motor in centimeters,
		// i.e. 5.5 cm
		// the offset number is the distance between the center of wheel to
		// the center of robot, i.e. half of track width
		Wheel wheel1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.6).offset(-7);
		Wheel wheel2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.6).offset(7);

		pince.setSpeed(500);
		
		pince.forward();
		Delay.msDelay(delayPince);
		pince.stop();
		
		// set up the chassis type, i.e. Differential pilot
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 },
				WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot ev3robot = new MovePilot(chassis);

		Navigator navbot = new Navigator(ev3robot);

		// run routine based on menu choice
		switch (menu.select()) {
		case 0:
			navigate(navbot);
			break;
		}

	}

	// navigate()
	// demo navigating using way points

	private static void navigate(Navigator nav) {

		// get EV3 brick
		EV3 ev3brick = (EV3) BrickFinder.getLocal();
		
		// instantized LCD class for displaying and Keys class for buttons
		Keys buttons = ev3brick.getKeys();
		TextLCD lcddisplay = ev3brick.getTextLCD();
		
		// set up way points
		Waypoint wp1 = new Waypoint(0, 10);
		Waypoint wp2 = new Waypoint(0, 0);
		Waypoint wp3 = new Waypoint(-50, 50);

		// clear menu
		lcddisplay.clear(); 

		// display current location
		lcddisplay.drawString("At 0, 0", 0, 0);
		lcddisplay.drawString("Press ENTER", 0, 2);
		lcddisplay.drawString("to continue", 0, 3);

		
//	-----> x
//	|
//	|
//	V y
		
		
		// wait until ENTER key is pressed
		while (buttons.waitForAnyPress() != Keys.ID_ENTER) {
			Thread.yield();
		}

		// navigate to way point one, display new location
		lcddisplay.clear();
		nav.goTo(wp1);
		lcddisplay.drawString("At 30, 30", 0, 0);
		lcddisplay.drawString("Press ENTER", 0, 2);
		lcddisplay.drawString("to continue", 0, 3);

		while ( !touch.isPressed()) {
			LCD.drawString("Palet ndetecté", 0, 4);
		}
		nav.stop();
		pince.backward();
		Delay.msDelay(delayPince);
		pince.stop();
		
		// wait until ENTER key is pressed
		while (buttons.waitForAnyPress() != Keys.ID_ENTER) {
			
			Thread.yield();
		}

		// navigate to way point two, display new location
		lcddisplay.clear();
		nav.goTo(wp2);
		lcddisplay.drawString("At 0, 0", 0, 0);
		lcddisplay.drawString("Press ENTER", 0, 2);
		lcddisplay.drawString("to continue", 0, 3);

		// wait until ENTER key is pressed
		while (buttons.waitForAnyPress() != Keys.ID_ENTER)
			Thread.yield();

		// navigate to way point 3, display new location
		lcddisplay.clear();
		nav.goTo(wp3);
		lcddisplay.drawString("At -30, 30", 0, 0);
		lcddisplay.drawString("Press ENTER", 0, 2);
		lcddisplay.drawString("to continue", 0, 3);

		// wait until ENTER key is pressed
		while (buttons.waitForAnyPress() != Keys.ID_ENTER)
			Thread.yield();

		// navigate to way point 2, display new location
		lcddisplay.clear();
		nav.goTo(wp2);
		lcddisplay.drawString("At 0, 0", 0, 0);
		lcddisplay.drawString("Press ENTER", 0, 2);
		lcddisplay.drawString("to exit", 0, 3);

		// wait until ENTER key is pressed
		while (buttons.waitForAnyPress() != Keys.ID_ENTER)
			Thread.yield();
	}
}
