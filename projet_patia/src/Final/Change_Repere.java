package Final;

import java.awt.Point;

public class Change_Repere {
	
	public Change_Repere() {
		
	}
	
	public Point getPoint(Point p) {
		// translate
		Point temp = new Point();
		temp.x = p.x - 100;
		temp.y = p.y - 30;
		
		// rotate :
		Point res = new Point();
		
//		res.x = (int) (temp.x * Math.cos(90) - temp.y * Math.sin(90));
//		res.y = (int) (temp.x * Math.sin(90) - temp.y * Math.cos(90));
		res.x = temp.y;
		res.y = -temp.x;
		
		return res;
		
	}
}
