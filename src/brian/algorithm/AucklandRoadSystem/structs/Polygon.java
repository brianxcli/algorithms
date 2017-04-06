package brian.algorithm.AucklandRoadSystem.structs;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import brian.algorithm.AucklandRoadSystem.utils.MoveManager;

public class Polygon {
	public int type;
	public int endLevel;
	public int cityIndex;
	public ArrayList<Location> data;
	public String label;
	
	public void draw(Graphics g, double scale) {
		if (data == null || data.size() < 2) {
			return;
		}

		Location first = data.get(0);
		
		for (int i = 1; i < data.size(); i++) {
			Location sec = data.get(i);
			Point pStart = first.asPoint(Location.ORIGIN, scale);		
			Point pEnd = sec.asPoint(Location.ORIGIN, scale);
			
			Vector vector = MoveManager.getSelf().getCurrentMoveVector();	
			pStart.translate(vector.dx, vector.dy);
			pEnd.translate(vector.dx, vector.dy);
			
			g.drawLine(pStart.x, pStart.y, pEnd.x, pEnd.y);
			first = sec;
		}
	}
}
