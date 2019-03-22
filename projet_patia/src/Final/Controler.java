package Final;

import java.awt.Point;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Pose;

public class Controler {

	private MyNavigator nav;
	private Pince pince;
	private TouchSensor touch;
	private Cam_Palet cam;

	public Controler() {
		this.nav = new MyNavigator();
		this.pince = new Pince(StateP.CLOSE);
		this.touch = new TouchSensor();
		this.cam = new Cam_Palet();
	}
	
	public void StartProg() {
		LCD.drawString("press to start", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		
		nav.setCoord(-100,30);
		
		ArrayList<Point> palets = cam.GetPaletList();
		Point p = new Point(0,0);
		if (! palets.isEmpty()) {
			p = palets.get(0);
			nav.goTo(-p.x,p.y);
		}
		while (nav.isMoving() && !touch.isPressed());
		if (nav.isMoving()) {
			nav.stop();
			nav.setCoord(-p.x, p.y);
			this.rammenePalet();
		} else {
			LCD.drawString("palet non detecte", 1, 1);
		}
		LCD.drawString("At x:" + nav.getCoord().getX() + " y:" + nav.getCoord().getY(), 0, 0);
		Button.waitForAnyPress();
	}
	
	public void rammenePalet() {
		pince.FermePince();
		nav.goTo(-100, -30);
		while(nav.isMoving());
		pince.OuvrirPince();
		nav.DeposePalet();
		nav.setCoord(-100,30);
		nav.goTo(-100, 30);
	}
	
	public void CloseProg() {
		this.pince.FermePince();
		this.nav.stop();
	}
}
