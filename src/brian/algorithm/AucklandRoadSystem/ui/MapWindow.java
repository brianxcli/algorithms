package brian.algorithm.AucklandRoadSystem.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JTextArea;

import brian.Task2.RoadSystem.algorithms.Dictionary;
import brian.Task2.RoadSystem.algorithms.QuadTree;
import brian.Task2.RoadSystem.algorithms.RoadGraph;
import brian.Task2.RoadSystem.structs.Location;
import brian.Task2.RoadSystem.structs.Road;
import brian.Task2.RoadSystem.structs.RoadNode;
import brian.Task2.RoadSystem.structs.Segment;
import brian.Task2.RoadSystem.structs.Vector;
import brian.algorithm.AucklandRoadSystem.utils.DataLoader;
import brian.algorithm.AucklandRoadSystem.utils.DataLoaderListener;
import brian.algorithm.AucklandRoadSystem.utils.MoveManager;

public class MapWindow extends IGUI implements DataLoaderListener {
	private RoadGraph graph;
	private MoveManager moveManager;
	
	private long selectedNodeId;
	
	public MapWindow() {
		graph = RoadGraph.getInstance();
		moveManager = MoveManager.getSelf();
	}
	
	@Override
	protected void redraw(Graphics g) {
		Dimension dimen = getFrameSize();
		double latScale = calculateScale(dimen.width);
		graph.draw(g, latScale, getSearchBox().getText(), getNodeRadius(), selectedNodeId);
	}
	
	private double calculateScale(int width) {
		return (width / Location.getMapLatSpanKm()) *
			MoveManager.getSelf().getPixelsPerKM();
	}
	
	private int getNodeRadius() {
		// we define the current zoom level as the node radius
		int level = MoveManager.getSelf().getZoomLevel();
		int radius = 0;
		switch (level) { 
			case MoveManager.ZOOMLV_1:
			case MoveManager.ZOOMLV_2:
				break;
			case MoveManager.ZOOMLV_3:
			case MoveManager.ZOOMLV_4:
				radius = 2;
				break;
			case MoveManager.ZOOMLV_5:
				radius = 3;
				break;
		}
		return radius;
	}
	
	@Override
	protected void onClick(MouseEvent e) {
		click(e);
	}

	private void click(MouseEvent e) {
		if (MoveManager.getSelf().getZoomLevel() < MoveManager.ZOOMLV_4) {
			// we do not respond to any click event
			// if the zoom level is smaller than LV4
			// which shows insufficient details
			return;
		}
		
		int x = e.getX();
		int y = e.getY();
		System.out.println("click:" + " x=" + x + " y=" + y);
		Vector vector = moveManager.getCurrentMoveVector();
		// Point of current click position
		Point point = new Point(-vector.dx + x, -vector.dy + y);
		System.out.println("touched point:" + point);
	
		selectedNodeId = QuadTree.getInstance().searchNodeId(point);
		RoadNode node = RoadGraph.getInstance().getNode(selectedNodeId);	
		showNodeInfo(getTextOutputArea(), node, selectedNodeId, point);
	}
	
	private void showNodeInfo(JTextArea textarea, RoadNode node, long id, Point source) {
		if (id == -1) {
			textarea.setText("distance:");
			return;
		}
		
		String text = "nodeid:" + node.id;
		
		text = text + "    roads out:";		
		Iterator<Segment> segs = node.edgesOut.iterator();
		while (segs.hasNext()) {
			Segment segment = segs.next();
			Road road = RoadGraph.getInstance().getRoad(segment.roadId);
			text = text + road.id + " " + road.label + ";  ";
		}
		
		textarea.setText(text);
	}
	
	@Override
	protected void onMouseDragged(Vector vector) {
		MoveManager.getSelf().moveByMouseDrag(vector);
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		Dimension dimen = getFrameSize();
		MoveManager.getSelf().moveByMouseWheel(e, dimen.height);
	}
	
	@Override
	protected void onSearch() {		
		String prefix = getSearchBox().getText();		
		if (prefix == null || prefix.equals("")) {
			return;
		}
		
		JTextArea textArea = getTextOutputArea();
		HashSet<String> nameSet = Dictionary.getInstance().searchNameWithPrefix(prefix);
		Iterator<String> names = nameSet.iterator();
		String text = "";
		while (names.hasNext()) {
			String name = names.next();
			text = text + "|" + name;
		}
		textArea.setText(text);
	}
	
	@Override
	protected void onMove(Move m) {
		switch (m) {
			case NORTH:
			case SOUTH:
			case WEST:
			case EAST:
				move(m);
				break;
			case ZOOM_IN:
			case ZOOM_OUT:
				zoom(m);
				break;
		}		
	}

	private void move(Move move) {
		Dimension dimen = getFrameSize();
		MoveManager.getSelf().move(move.ordinal(), dimen.width, dimen.height);
	}
	
	private void zoom(Move move) {
		boolean oneUpLevel = (move == Move.ZOOM_IN);
		MoveManager manager = MoveManager.getSelf();
		int level = manager.getZoomLevel();
		
		Dimension dimen = getFrameSize();
		if (oneUpLevel && level == MoveManager.ZOOMLV_1) {
			// zoom in from level 1 to level 2
			// shows the Auckland City center			
			double scale = calculateScale(dimen.width);
			manager.zoomToAucklandCenter(scale, dimen);
		} else {
			manager.zoom(oneUpLevel, dimen);
		}
	
		rebuildQuadtreeIfNeeded();
	}
	
	/**
	 * Must be called after the map being scaled.
	 * Quadtree is only available in the 4th and
	 * 5th zoom level
	 */
	private void rebuildQuadtreeIfNeeded() {
		if (MoveManager.getSelf().getZoomLevel() < MoveManager.ZOOMLV_4) {
			return;
		}
		
		Dimension dimen = getFrameSize();				
		double scale = calculateScale(dimen.width);
		QuadTree.getInstance().reset(scale);

		Iterator<RoadNode> nodes = RoadGraph.getInstance().getAllNodes().values().iterator();
		while (nodes.hasNext()) {
			RoadNode node = nodes.next();			
			Point point = node.location.asPoint(Location.ORIGIN, scale);
			QuadTree.getInstance().addPoint(point, node.id);
		}
	}
	
	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		DataLoader.loadFiles(this, nodes, roads, segments, polygons);
	}
	
	@Override
	public void onLoadEnd() {
		redraw();
	}

	@Override
	public void onLoadNodeFinish() {
		Location.confirmAucklandCityRegion();
		graph.confirmAllNodeLocation();
	}
}




