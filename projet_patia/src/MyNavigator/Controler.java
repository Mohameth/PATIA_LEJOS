package MyNavigator;

import java.awt.Point;
import java.util.ArrayList;

import Final.Cam_Palet;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;

public class Controler implements MyObserver {

	private Cam_Palet cam;
	private Tomtom t;
	private StateRobot state;
	private ColorSensorThread mysensor;
	
	public Controler() {
		
		
		
		this.t = new Tomtom();
		
		this.cam = new Cam_Palet();
		
		this.state=StateRobot.OPENPINCE;
		
		mysensor = new ColorSensorThread();
		mysensor.addObserver(this);
		mysensor.start(); //start color detector
		
		StartProg();
	}
	
	
	public void StartProg() {
		System.out.println("State:" + this.state);
		switch (state) {
		case OPENPINCE :
			System.out.println("ouvir peince");
			state =StateRobot.GOTOPALET;
			break;
		case GOTOPALET:
			System.out.println("Avance jusqu'au palet");
			if (goToPalet()){
				state = StateRobot.CLOSEPINCE;
			}
			break;
		case CLOSEPINCE:
			System.out.println("Fermer peince");
			state = StateRobot.EVITE;
			break;
		case EVITE:
			System.out.println("Evite");
			state =StateRobot.GOTOGOAL;
			break;
		case GOTOGOAL:
			System.out.println("GotoGoal");
			state =StateRobot.OPENPINCEGOAL;
			break;
		case OPENPINCEGOAL:
			System.out.println("OpenPince");
			state =StateRobot.GOBACKWARD;
			break;
		case GOBACKWARD:
			System.out.println("Recule un peu");
			state =StateRobot.GOTOPALET;
			break;
		default:
			break;
		}
		
		StartProg();
	
	}
	
	
	private boolean goToPalet() {
		ArrayList<Point> palets = cam.GetPaletList();
		Point p = new Point(0,0);
		if (! palets.isEmpty()) {
			p = palets.get(0);
			LCD.drawString("go to" + p.x +"," + p.y, 0, 0);
			t.goTo(p.getX(), p.getY());
			return true;
		}else {
			return false;
		}
	}
	
	
	
	public void update(String newCol) {
		//notifier le navigator ici
		if (newCol=="red") {
			System.exit(0);
		}
	}
	
	
	
	
}
