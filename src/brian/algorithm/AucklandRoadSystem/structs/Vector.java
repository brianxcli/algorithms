package brian.algorithm.AucklandRoadSystem.structs;

/**
 * Used to record a move direction on the map in pixels.
 * The origin is Location.ORIGIN, which is the center
 * point of Auckland City.
 * @author brian-pc
 */
public class Vector {
	public int dx;
	public int dy;
	
	public Vector() {
		
	}
	
	public Vector(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
}
