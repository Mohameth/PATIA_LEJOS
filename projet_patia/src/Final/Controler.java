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
	private Change_Repere transform;

	public Controler() {
		this.nav = new MyNavigator();
		this.pince = new Pince(StateP.CLOSE);
		this.touch = new TouchSensor();
		this.cam = new Cam_Palet();
		this.transform = new Change_Repere();
	}
	
	public void StartProg(int numPalet) {
//		LCD.drawString("press to start", 0, 0);
//		Button.waitForAnyPress();
//		LCD.clear();
		
		ArrayList<Point> palets = cam.GetPaletList();
		Point p = new Point(0,0);
		if (! palets.isEmpty()) {
			p = palets.get(0);
			p = this.transform.getPoint(p);
			LCD.drawString("go to" + p.x +"," + p.y, 0, 0);
			
			this.GoToPalet(p);
			p = this.transform.getPoint(palets.get(1));
			this.GoToPalet(p);
		}
		
	}
	
	public void rammenePalet() {
		pince.FermePince();
		nav.goTo(-10, 0);
		while(nav.isMoving());
		pince.OuvrirPince();
		nav.DeposePalet();
		nav.setCoord(0,0);
		//nav.goTo(0, 0);
	}
	
	public void GoToPalet(Point p) {
		nav.goTo(p.x,p.y);
		
		while (nav.isMoving() && !touch.isPressed());
		if (nav.isMoving()) {
			nav.stop();
			//nav.setCoord(p.x, p.y);
			this.rammenePalet();
		} else {
			//nav.setCoord(p.x, p.y);
			LCD.clear();
			LCD.drawString("palet non detecte", 1, 1);
		}
	}
	
	public void CloseProg() {
		this.pince.FermePince();
		this.nav.stop();
	}
}
