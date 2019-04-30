package Final;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.Pose;

public class Controler implements MyObserver{

	private MyNavigator nav;
	private Pince pince;
	private TouchSensor touch;
	private Cam_Palet cam;
	private Change_Repere transform;
	private static ColorSensorThread mysensor;
	private HashMap<String,Point> lineCoord;
	private ArrayList<Point> palets;
	private int current;

	public Controler() {
		this.nav = new MyNavigator();
		this.pince = new Pince(StateP.CLOSE);
		this.touch = new TouchSensor();
		this.cam = new Cam_Palet();
		this.transform = new Change_Repere();
		mysensor = new ColorSensorThread();
		
		mysensor.addObserver(this);
		mysensor.start(); //start color detector
		
		lineCoord = new HashMap<>();
		lineCoord.put("red", new Point(157,0));
		lineCoord.put("green", new Point(0,85));
		lineCoord.put("blue", new Point(0,215));
		lineCoord.put("yellow", new Point(50,0));
		
		palets = cam.GetPaletList();
		current = 0;
	}
	
	
	
	public void StartProg() {
		LCD.drawString("press to start", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		//nav.rotate90();
		
		Point p = new Point(0,0);
		if (! palets.isEmpty()) {
			p = palets.get(current);
			p = this.transform.getPoint(p);
			
			LCD.drawString("1e Palet : go to" + p.x +"," + p.y, 0, 0);
			
			this.FirstPalet(p);
			current++;
			p = getCible(current);
			while (p != null) {
				p = this.transform.getPoint(p);
				this.GoToPalet(p);
				LCD.drawString(current + "e Palet : go to" + p.x +"," + p.y, 0, 0);
				current++;
				p = getCible(current);
			}
		}
		
	}
	
	public void rammenePalet(Point p) {
		pince.FermePince();
		nav.goTo(-5, 0,180);
		while(nav.isMoving());
		float head = (float) getAngle(p);
		LCD.drawInt((int) head, 5, 6);
		//nav.setCoord(-10, 0, head+180);
		pince.OuvrirPince();
		nav.DeposePalet();
	}
	
	
	public Point getCible(int current) {
		if (this.palets.size()>current)
			return this.palets.get(current);
		else
			return null;
	}
	
	public double getAngle(Point p) { 
		double Np = Math.sqrt(p.x*p.x + p.y*p.y);
		return Math.toDegrees(Math.acos(p.x/Np)); 
	}
	
	public double getDist(Point p) {
		return Math.sqrt(p.x*p.x + p.y*p.y);
	}

	public void FirstPalet(Point p) {

		// calcul d'angle et de distance
		int head = (int) getAngle(p);
		int dist = (int) getDist(p);
		if (p.y < 0)
			head = -head;
		
		// Deplacement vers le palet
		nav.travel(dist, head);
		pince.FermePince();
		// Deplacement vers le camps "adverse"
		nav.travel(0, -head);
		nav.travel(25, 90);
		nav.travel(240-p.x+10, -90);
		// depose le palet
		pince.OuvrirPince();
		nav.travel(-10, 0);
		nav.travel(0, 180);
		
		// pos final 270,palet.x-25
		this.transform.setCote("d");
		this.transform.setx(palets.get(current).x-25);
		this.transform.sety(285);
	}
	
	public void GoToPalet(Point p) {
		// calcul d'angle et de distance
		int head = (int) getAngle(p);
		System.out.println(getAngle(p));
		int dist = (int) getDist(p);
		if (p.y < 0)
			head = -head;

		// Deplacement vers le palet
		nav.travel(dist, head);
		pince.FermePince();
		
		// Retour case départ
		nav.travel(-dist, 0);
		nav.travel(0, -head);
		
		// depose le palet
		nav.travel(10, 180);
		pince.OuvrirPince();
		nav.travel(-10,0);
		nav.travel(0, 180);
		
	}
	
	public void CloseProg() {
		this.pince.FermePince();
		this.nav.stop();

		this.mysensor.stopMe();
	}


	@Override
	public void update(String newCol) {
		//notifier le navigator ici
//		Point p = lineCoord.get(newCol);
//		if (p != null) {
//			if (p.x == 0) {
//				p = transform.getPoint(p);
//				p.x = (int) nav.getCoord().getX();
//				nav.stop();
//				nav.setCoord(p.x, p.y,nav.getCoord().getHeading());
//				nav.resume();
//			} else {
//				p = transform.getPoint(p);
//				p.y = (int) nav.getCoord().getY();
//				nav.stop();
//				nav.setCoord(p.x, p.y,nav.getCoord().getHeading());
//				nav.resume();
//			}
//		}
	}
}
