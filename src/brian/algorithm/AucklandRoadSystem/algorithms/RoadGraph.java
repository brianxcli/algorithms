package brian.algorithm.AucklandRoadSystem.algorithms;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import brian.algorithm.AucklandRoadSystem.structs.*;

public class RoadGraph {
	/**
	 * find node instances by their ids.
	 */
	private HashMap<Long, RoadNode> nodeMap;
	
	/**
	 * graph data structure using lists.
	 */
	private HashMap<Long, ArrayList<RoadNode>> graph;
	
	/**
	 * find road instances by their ids.
	 */
	private HashMap<Long, Road> roadIdMap;
	
	private HashMap<String, Road> roadNameMap;
	
	private HashMap<Long, Polygon> polygonMap;
	
	private static RoadGraph ins = new RoadGraph();
	
	private RoadGraph() {
		nodeMap = new HashMap<Long, RoadNode>();
		graph = new HashMap<Long, ArrayList<RoadNode>>();
		roadIdMap = new HashMap<Long, Road>();
		roadNameMap = new HashMap<String, Road>();
		polygonMap = new HashMap<Long, Polygon>();
	}
	
	public static RoadGraph getInstance() {
		return ins;
	}
	
	public void addNode(long id, double lat, double lon) {		
		RoadNode node = null;
		
		if (nodeMap.containsKey(id)) {
			node = nodeMap.get(id);			
		} else {
			node = new RoadNode(id, lat, lon);
			nodeMap.put(id, node);
		}
		
		if (!graph.containsKey(id)) {
			graph.put(id, new ArrayList<RoadNode>());
		}
	}
	
	/**
	 * Must be called after right the time when all nodes
	 * are read from node data files, and right after
	 * the Auckland City Region is confirmed.
	 */
	public void confirmAllNodeLocation() {
		Iterator<RoadNode> nodes = nodeMap.values().iterator();
		while (nodes.hasNext()) {
			RoadNode node = nodes.next();
			node.location = Location.newFromLatLon(node.lat, node.lon);
		}
	}
	
	public ArrayList<RoadNode> getDirectedAdjacent(long id) {
		return graph.get(id);
	}
	
	public RoadNode getNode(long id) {
		return nodeMap.get(id);
	}
	
	public HashMap<Long, RoadNode> getAllNodes() {
		return nodeMap;
	}
	
	public void addRoad(Road road) {
		if (!roadIdMap.containsKey(road.id)) {
			roadIdMap.put(road.id, road);
		}
		
		String name = road.label;
		if (!roadNameMap.containsKey(name)) {
			roadNameMap.put(name, road);
		}
	}

	public Road getRoad(long id) {
		return roadIdMap.get(id);
	}
	
	public boolean containsRoad(String name) {
		return roadNameMap.containsKey(name);
	}
	
	public void addPolygon(long id, Polygon polygon) {
		polygonMap.put(id, polygon);
	}
	
	public HashMap<Long, Polygon> getPolygons() {
		return polygonMap;
	}
	
	/**
	 * Must be called after all the nodes and road information
	 * being loaded into the graph.
	 * @param seg
	 * @throws Exception
	 */
	public void addSegment(Segment seg) throws RuntimeException {
		boolean isMissingStart = false;
		
		long startId = seg.startNodeId;
		if (nodeMap.containsKey(startId)) {			
			RoadNode node = nodeMap.get(startId);
			if (node != null) {
				node.edgesOut.add(seg);
			}
			
			// Check whether the corresponding road 
			// is a two-way road.
			long roadId = seg.roadId;
			Road road = null;
			if (roadIdMap.containsKey(roadId)) {
				road = roadIdMap.get(roadId);
			}
			
			if (road != null && (road.oneway == 1)) {
				node.edgesIn.add(seg);
			}
		} else {
			isMissingStart = true;
		}
		
		if (isMissingStart) {
			throw new RuntimeException("No starting node has been added: id = " + seg.startNodeId);
		}
	}

	public void dump() {
		int nodeCnt = 1;
		
		Iterator<Entry<Long, RoadNode>> iterator = nodeMap.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<Long, RoadNode> entry = iterator.next();
			RoadNode node = entry.getValue();
			
			StringBuilder builder = new StringBuilder();
			for (Segment seg : node.edgesOut) {
				builder.append(seg.toString());
				builder.append(" ");
			}
			
			System.out.println("node " + nodeCnt + ": " + node.toString() + "=>" + builder.toString());
			nodeCnt++;
		}
	}

	/**
	 * Draw the graph
	 * @param g Graphics instance
	 * @param scale how many pixels per kilometer
	 * @param roadPrefix prefix in search box to be highlighted
	 * @param nodeRadius how big the node is to be drawn
	 * @param seletedNodeId id of the node to be highlighted
	 */
	public void draw(Graphics g, double latScale, String roadPrefix, int nodeRadius, long seletedNodeId) {
		Iterator<RoadNode> nodes = nodeMap.values().iterator();
		while (nodes.hasNext()) {
			RoadNode node = nodes.next();
			node.draw(g, latScale, roadPrefix, nodeRadius, seletedNodeId);
		}
		
		Iterator<Polygon> poly = polygonMap.values().iterator();
		while (poly.hasNext()) {
			Polygon polygon = poly.next();
			polygon.draw(g, latScale);
		}
	}
}




