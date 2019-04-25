package Final;

import java.awt.Point;

public class Change_Repere {
	
	private String cote;
	private int x;
	private int y;
	
	public Change_Repere() {
		cote = "g";
		x = 99;
		y = 28;
	}
	
	public Point getPoint(Point p) {
		Point res = new Point();
		if (cote == "g") {
			// translate
			Point temp = new Point();
			temp.x = p.x - x;
			temp.y = p.y - y;
			
			// rotate :
			
	//		res.x = (int) (temp.x * Math.cos(90) - temp.y * Math.sin(90));
	//		res.y = (int) (temp.x * Math.sin(90) - temp.y * Math.cos(90));
			res.x = temp.y;
			res.y = -temp.x;
		} else {
			Point temp = new Point();
			temp.x = p.x - x;
			temp.y = p.y - y;
			
			res.x = -temp.y;
			res.y = -temp.x;
		}
		
		return res;
		
	}
	
	public void setCote(String c) {
		this.cote = c;
	}
	
	public void sety(int y) {
		this.y = y;
	}
	
	public void setx(int x) {
		this.x = x;
	}
}
