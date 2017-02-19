package brian.algorithm.AucklandRoadSystem.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;

import brian.algorithm.AucklandRoadSystem.structs.Location;
import brian.algorithm.AucklandRoadSystem.structs.Vector;

public class MoveManager {
	public static final int ZOOMLV_1 = 0;
	public static final int ZOOMLV_2 = 1;
	public static final int ZOOMLV_3 = 2;
	public static final int ZOOMLV_4 = 3;
	public static final int ZOOMLV_5 = 4;
	
	/** zoom factor */
	private static final int ZOOM_FAC = 4;
	private int level = ZOOMLV_1;
	
	private static final int MOV_NORTH = 0;
	private static final int MOV_SOUTH = 1;
	private static final int MOV_EAST = 2;
	private static final int MOV_WEST = 3;	
	
	/** record the direction and distance the map
	 * has shifted from the ORIGIN */
	private Vector mapVector = new Vector(0, 0);
	
	private static final MoveManager self = new MoveManager();
	
	public static MoveManager getSelf() {
		return self;
	}
	
	private MoveManager() {
		// do nothing
	}
	
	public void zoom(boolean oneUpLevel, Dimension dimen) {
		if (oneUpLevel && level == ZOOMLV_5) {
			return;
		} else if (!oneUpLevel && level == ZOOMLV_1) {
			return;
		}
		
		if (oneUpLevel) {
			level += 1;
			// change back to map center position, scale it
			// and then translate back to origin position.
			mapVector.dx = -(mapVector.dx - dimen.width / 2);
			mapVector.dy = -(mapVector.dy - dimen.height / 2);
			mapVector.dx = -mapVector.dx * ZOOM_FAC + dimen.width / 2;
			mapVector.dy = -mapVector.dy * ZOOM_FAC + dimen.height / 2;
		} else {
			level -= 1;
			mapVector.dx = -(mapVector.dx - dimen.width / 2);
			mapVector.dy = -(mapVector.dy - dimen.height / 2);
			mapVector.dx = -mapVector.dx / ZOOM_FAC + dimen.width / 2;
			mapVector.dy = -mapVector.dy / ZOOM_FAC + dimen.height / 2;
		}
		
		if (level == ZOOMLV_1) {
			mapVector.dx = 0;
			mapVector.dy = 0;
		}
	}
	
	public void zoomToAucklandCenter(double scale, Dimension dimen) {
		level += 1;
		Point point = Location.AUCKLAND_CENTRE.asPoint(Location.ORIGIN, scale);
		mapVector.dx = -point.x * ZOOM_FAC + dimen.width / 2;
		mapVector.dy = -point.y * ZOOM_FAC + dimen.height / 2;
	}
	
	public int getZoomLevel() {
		return level;
	}
	
	public double getPixelsPerKM() {
		return Math.pow(ZOOM_FAC, level);
	}
	
	public Vector getCurrentMoveVector() {
		return mapVector;
	}
	
	/**
	 * move the map in a given direction at current 
	 * zoom level.
	 * @param direction which direction to move
	 * @param width map width
	 * @param height map height
	 */
	public void move(int direction, int width, int height) {
		if (level == ZOOMLV_1) {
			// do not move the map in the original
			// zoom level.
			return;
		}
		
		// for every move, we just go for one fifth of 
		// length of map width or height
		int pixelMove = 0;
		switch (direction) {
			case MOV_NORTH:
				pixelMove = height / 5;
				mapVector.dy += pixelMove;
				break;
			case MOV_SOUTH:
				pixelMove = height / 5;
				mapVector.dy -= pixelMove;
				break;
			case MOV_EAST:
				pixelMove = width / 5;
				mapVector.dx -= pixelMove;
				break;
			case MOV_WEST:
				pixelMove = width / 5;
				mapVector.dx += pixelMove;
				break;
		}
	}
	
	public void moveByMouseWheel(MouseWheelEvent e, int mapHeight) {
		if (level == ZOOMLV_1) {
			// do not move the map in the original
			// zoom level.
			return;
		}
		
		// for every rotation unit of mouse wheel, we just 
		// go for one tenth of length of map height
		int unit = e.getUnitsToScroll();
		int pixelMove = 0;		
		
		if (unit > 0) {
			pixelMove = mapHeight / 5;
			mapVector.dy -= pixelMove;
		} else {
			pixelMove = mapHeight / 5;
			mapVector.dy += pixelMove;
		}
	}
	
	public void moveByMouseDrag(Vector vector) {
		if (level == ZOOMLV_1) {
			// do not move the map in the original
			// zoom level.
			return;
		}
		
		mapVector.dx += vector.dx;
		mapVector.dy += vector.dy;
	}
}





