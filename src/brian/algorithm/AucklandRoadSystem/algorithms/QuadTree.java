package brian.algorithm.AucklandRoadSystem.algorithms;

import java.awt.Point;
import java.awt.Rectangle;

import brian.algorithm.AucklandRoadSystem.structs.Location;

public class QuadTree {
	private static final double NEAREST_CLICK = 10.0;
	
	private static class QuadNode {
		public Rectangle self;
		public QuadNode[] child;
		public long nodeId;
		public Point point;
		
		public String toString() {
			return "nodeid:" + nodeId + "    point:" + point + 
				"    self:" + self.toString();
		}
	}
	
	private QuadNode root;
	
	private static QuadTree self = new QuadTree();
	private QuadTree() {
		// do nothing
	}
	
	public static QuadTree getInstance() {
		return self;
	}
	
	public void reset(double scale) {
		recycle();
		initRoot(scale);		
	}
	
	private void recycle() {
		if (root == null) {
			return;
		}
		
		recycle(root);
	}
	
	private void initRoot(double scale) {
		root = new QuadNode();
		root.self = getRootRect(scale);
	}
		
	private void recycle(QuadNode node) {
		if (node.child != null) {
			int length = node.child.length;
			for (int i = 0; i < length; i++) {
				recycle(node.child[i]);
			}
			
		}		
		node.child = null;
		node.point = null;
		node = null;
	}
	
	private Rectangle getRootRect(double scale) {
		Rectangle rect = new Rectangle();
		Location location = Location.MOST_EAST_SOUTH;
		Point point = location.asPoint(Location.ORIGIN, scale);
		rect.width = point.x;
		rect.height = point.y;
		return rect;
	}
	
	private void splitQuadNodeAsFourOrTwo(QuadNode node) {
		Rectangle rect = node.self;
		
		if (rect.width < 2 && rect.height < 2) {
			return;
		}
		
		if (node.child == null) {
			if (rect.width >= 2 && rect.height >= 2) {
				node.child = new QuadNode[4];
			} else {
				node.child = new QuadNode[2];
			}			
		}
	
		int centerX = rect.x + rect.width / 2;
		int centerY = rect.y + rect.height / 2;
		
		Rectangle rectangle;
		
		if (rect.width >= 2 && rect.height < 2) {
			rectangle = new Rectangle();
			rectangle.x = rect.x;
			rectangle.y = rect.y;
			rectangle.width = centerX - rect.x;
			rectangle.height = centerY - rect.y;
			node.child[0] = new QuadNode();
			node.child[0].self = rectangle;
			
			rectangle = new Rectangle();
			rectangle.x = centerX + 1;
			rectangle.y = rect.y;
			rectangle.width = rect.x + rect.width - rectangle.x;
			rectangle.height = node.child[0].self.height;
			node.child[1] = new QuadNode();
			node.child[1].self = rectangle;
		} else if (rect.width < 2 && rect.height >= 2) {
			rectangle = new Rectangle();
			rectangle.x = rect.x;
			rectangle.y = rect.y;
			rectangle.width = centerX - rect.x;
			rectangle.height = centerY - rect.y;
			node.child[0] = new QuadNode();
			node.child[0].self = rectangle;
			
			rectangle = new Rectangle();
			rectangle.x = rect.x;
			rectangle.y = centerY + 1;
			rectangle.width = node.child[0].self.width;
			rectangle.height = rect.y + rect.height - rectangle.y;
			node.child[1] = new QuadNode();
			node.child[1].self = rectangle;
		} else if (rect.width >= 2 && rect.height >= 2) {
			rectangle = new Rectangle();
			rectangle.x = rect.x;
			rectangle.y = rect.y;
			rectangle.width = centerX - rect.x;
			rectangle.height = centerY - rect.y;
			node.child[0] = new QuadNode();
			node.child[0].self = rectangle;
			
			rectangle = new Rectangle();
			rectangle.x = centerX + 1;
			rectangle.y = rect.y;
			rectangle.width = rect.x + rect.width - rectangle.x;
			rectangle.height = node.child[0].self.height;
			node.child[1] = new QuadNode();
			node.child[1].self = rectangle;
			
			rectangle = new Rectangle();
			rectangle.x = rect.x;
			rectangle.y = centerY + 1;
			rectangle.width = node.child[0].self.width;
			rectangle.height = rect.y + rect.height - rectangle.y;
			node.child[2] = new QuadNode();
			node.child[2].self = rectangle;
			
			rectangle = new Rectangle();
			rectangle.x = node.child[1].self.x;
			rectangle.y = node.child[2].self.y;
			rectangle.width = node.child[1].self.width;
			rectangle.height = node.child[2].self.height;
			node.child[3] = new QuadNode();
			node.child[3].self = rectangle;
		}		
	}

	private void addPoint(QuadNode node, Point point, long nodeId) {
		if (node == null) {
			return;
		}
		
		//System.out.println(node.toString() + "|" + point.toString() + "nodeid:" + nodeId);
		
		if (!node.self.contains(point)) {
			// current quad tree area doesn't contain
			// the input point
		} else if (node.point == null && node.child == null) {
			// current node area has not saved
			// any rectangle, just store into it
			node.nodeId = nodeId;
			node.point = point;			
		} else {
			long oldId = -1;
			Point oldPoint = null;
			
			if (node.point != null) {
				if (node.point.equals(point)) {
					// we may add the same point for
					// multiple times, just skip it.				
					return;
				} else {
					// if we have come across two different
					// node to be added, we get the old 
					// node and add it to the child node.
					oldId = node.nodeId;
					oldPoint = node.point;
					node.point = null;
					node.nodeId = -1;
				}
			}
		
			if (node.child == null) {
				splitQuadNodeAsFourOrTwo(node);
			}
			
			int length = node.child.length;
			if (oldPoint != null) {
				for (int i = 0; i < length; i++) {
					if (node.child[i].self.contains(oldPoint)) {
						addPoint(node.child[i], oldPoint, oldId);
						break;
					}					
				}
			}			
			
			for (int i = 0; i < length; i++) {
				if (node.child[i].self.contains(point)) {
					addPoint(node.child[i], point, nodeId);
					break;
				}
			}
		}
	}
	
	public void addPoint(Point point, long nodeId) {
		addPoint(root, point, nodeId);
	}
	
	public long searchNodeId(Point point) {	
		QuadNode node = searchNearestNode(root, point);
		System.out.println("searched node:" + node.nodeId + "  " + node.point);
		double distance = node.point.distance(point);
		System.out.println("distance between them:" + distance);
		
		long id = -1;
		if (node != null) {
			id = node.nodeId;
		}
		return id;
	}
	
	private QuadNode searchNearestNode(QuadNode node, Point point) {
		// System.out.println(node.toString());
		
		if (node.point != null && node.child == null) {
			// this area has only one point
			return node;
		} else if (node.point == null && node.child != null) {
			// has sub areas
			QuadNode nearest = null;
			double min = Double.MAX_VALUE;
			
			for (QuadNode sub : node.child) {
				if (sub.point == null && sub.child == null) {
					// no valid point in this area, just skip it.
					continue;
				}
				
				QuadNode subnode = searchNearestNode(sub, point);
				if (subnode == null) {
					continue;
				}
				
				double dis = point.distance(subnode.point);
				if (dis < min) {
					min = dis;
					nearest = subnode;
				}
			}
			
			return nearest;
		} else {
			return null;
		}
	}
}




