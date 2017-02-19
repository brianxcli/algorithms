package brian.algorithm.AucklandRoadSystem.structs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import brian.algorithm.AucklandRoadSystem.algorithms.RoadGraph;
import brian.algorithm.AucklandRoadSystem.utils.MoveManager;

/**
 * Part of the road connected by two nodes.
 * @author brian-pc
 */
public class Segment {
	public double length;
	public long roadId;
	public long startNodeId;
	public long endNodeId;
	
	/**
	 * Locations alongside which the segment goes.
	 */
	public ArrayList<Location> coor = new ArrayList<Location>();
	
	public String toString() {
		String res = "id:" + roadId + "(";		 		
		for (Location loc : coor) {
			res += loc.x + ":" + loc.y + " ";
		}		
		res += ")";		
		return res;
	}

	public void draw(Graphics g, double scale, String roadPrefix, int nodeRadius, long seletedNodeId) {
		RoadGraph graph = RoadGraph.getInstance();
		
		// We red the roads whose labels start with the 
		// same prefix as searched.
		String roadLabel =  graph.getRoad(roadId).label;
		Color color = g.getColor();
		
		if (!roadPrefix.equals("")) {
			if (graph.containsRoad(roadPrefix)) {
				// when there is a full completion of searched
				// prefix, we only highlight the fully-matched
				// road.
				if (roadLabel.equalsIgnoreCase(roadPrefix)) {
					g.setColor(Color.RED);
				}
			} else if (roadLabel.startsWith(roadPrefix)) {
				// when there is no full completion, we highlight
				// all roads whose names start with the prefix
				g.setColor(Color.RED);
			}
		}
		
		RoadNode start = graph.getNode(startNodeId);
		RoadNode end = graph.getNode(endNodeId);

		Point pStart = start.location.asPoint(Location.ORIGIN, scale);		
		Point pEnd = end.location.asPoint(Location.ORIGIN, scale);

		Vector vector = MoveManager.getSelf().getCurrentMoveVector();
		
		pStart.translate(vector.dx, vector.dy);
		pEnd.translate(vector.dx, vector.dy);
		
		// coordinates include the starting and ending nodes,
		// and we do not need to pay attention to it when drawing
		Point pTemp = null;
		for (Location loc : coor) {
			if (pTemp == null) {
				pTemp = loc.asPoint(Location.ORIGIN, scale);
				pTemp.translate(vector.dx, vector.dy);
			} else {
				Point pCurrent = loc.asPoint(Location.ORIGIN, scale);
				pCurrent.translate(vector.dx, vector.dy);
				
				g.drawLine(pTemp.x, pTemp.y, pCurrent.x, pCurrent.y);
				pTemp = pCurrent;
			}			
		}

		g.setColor(color);
		
		// draw starting and ending nodes.
		// we always draw the starting node for a segment;
		// we only draw the ending node when there is no
		// other segments starting from the ending node.
		int zoomLevel = MoveManager.getSelf().getZoomLevel();
		if (zoomLevel != MoveManager.ZOOMLV_1 && zoomLevel != MoveManager.ZOOMLV_2) {
			int diameter = 2 * nodeRadius;
			
			color = g.getColor();
			if (startNodeId == seletedNodeId) {
				System.out.println("start node seleted:" + start.id + " point:" + pStart);
				g.setColor(Color.RED);
			}
			
			if (zoomLevel >= MoveManager.ZOOMLV_4) {
				char[] id = String.valueOf(start.id).toCharArray();
				g.drawChars(id, 0, id.length, pStart.x - nodeRadius, pStart.y - nodeRadius - 4);
			}
			
			g.fillArc(pStart.x - nodeRadius, pStart.y - nodeRadius, diameter, diameter, 0, 360);
			
			g.setColor(color);
			ArrayList<RoadNode> dAdjacent = RoadGraph.getInstance().getDirectedAdjacent(endNodeId);
			if (dAdjacent.size() == 0) {
				if (endNodeId == seletedNodeId) {
					System.out.println("end node seleted:" + start.id + " point:" + pStart);
					g.setColor(Color.RED);
				}
				
				if (zoomLevel >= MoveManager.ZOOMLV_4) {
					char[] id = String.valueOf(end.id).toCharArray();
					g.drawChars(id, 0, id.length, pEnd.x - nodeRadius, pEnd.y - nodeRadius - 4);
				}
				
				g.fillArc(pEnd.x - nodeRadius, pEnd.y - nodeRadius, diameter, diameter, 0, 360);
			}
			
			g.setColor(color);
		}
	}
}





