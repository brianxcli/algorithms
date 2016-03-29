package brian.algorithm.AucklandRoadSystem.structs;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;

public class RoadNode {
	public long id;
	public Location location;
	public double lat;
	public double lon;
	public HashSet<Segment> edgesOut;
	public HashSet<Segment> edgesIn;

	public RoadNode(long id, double lat, double lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		
		edgesOut = new HashSet<Segment>();
		edgesIn = new HashSet<Segment>();
	}
	
	public String toString() {
		return new String(id + '|' + location.toString() +
			'|' + "inSize:" + edgesIn.size() + '|' + 
			"outSize:" + edgesOut.size());
	}

	public void draw(Graphics g, double latScale, String roadPrefix, int nodeRadius, long seletedNodeId) {
		Iterator<Segment> segs = edgesOut.iterator();		
		while (segs.hasNext()) {
			segs.next().draw(g, latScale, roadPrefix, nodeRadius, seletedNodeId);
		}
		
		
	}
}
