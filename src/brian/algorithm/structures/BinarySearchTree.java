package brian.algorithm.structures;

public class BinarySearchTree {
	public Node root;

	public BinarySearchTree() {
		
	}
	
	public static class Node {
		public Node parent;
		public Node left;
		public Node right;
		public int key;
	}
	
	public void insert(int value) {
		Node parent = root;
		
		while (true) {
			if (parent == null) {
				break;
			} else if (parent.key <= value) {
				// One benefit of adding the same value into
				// the right sub tree:
				// keeping the tree walk stable, which means
				// nodes with the same value appears in the
				// tree in the same order as they are in the
				// original input.
				if (parent.right != null) {
					parent = parent.right;
				} else {
					break;
				}
			} else if (parent.key > value) {
				if (parent.left != null) {
					parent = parent.left;
				} else {
					break;
				}
			}
		}
		
		Node node = new Node();
		node.key = value;
		
		if (parent == null) {
			root = node;
		} else if (value < parent.key && parent.left == null) {
			parent.left = node;
			node.parent = parent;
		} else if (value >= parent.key && parent.right == null) {
			parent.right = node;
			node.parent = parent;
		} 
	}
	
	public void buildTree(int[] array) {
		for (int element : array) {
			insert(element);
		}
	}
	
	public void inOrderTreeWalk() {
		inOrderTreeWalk(root);
		System.out.println();
	}
	
	private void inOrderTreeWalk(Node node) {
		if (node == null) {
			return;
		}
		
		inOrderTreeWalk(node.left);
		dumpNode(node);
		inOrderTreeWalk(node.right);
	}
	
	private void dumpNode(Node node) {
		System.out.print(node.key + " ");
	}
	
	public Node search(int value) {
		Node node = root;
		
		while (node != null) {
			if (node.key == value) {
				break;
			} else if (node.key < value) {
				node = node.left;
			} else if (node.key > value) {
				node = node.right;
			}
		}
		
		return node;
	}
	
	public Node getMinimum() {
		return getMinimum(root);
	}
	
	private Node getMinimum(Node root) {
		Node parent = root;
		
		if (parent == null) {
			return parent;
		}
		
		while (parent.left != null) {
			parent = parent.left;
		}
		
		return parent;
	}
	
	public Node getMaximum() {
		return getMaximum(root);
	}
	
	private Node getMaximum(Node root) {
		Node parent = root;
		
		if (parent == null) {
			return parent;
		}
		
		while (parent.right != null) {
			parent = parent.right;
		}
		
		return parent;
	}
	
	private Node getSuccessor(Node node) {
		if (node == null) {
			return null;
		}
		
		if (node.right != null) {
			return getMaximum(node.right);
		}
		
		Node current = node;
		Node parent = current.parent;
		while (parent != null && current == parent.right) {
			current = parent;
			parent = parent.parent;
		}
		
		return parent;
	}
	
	private Node getPredecessor(Node node) {
		if (node == null) {
			return null;
		}
		
		if (node.left != null) {
			return getMinimum(node.left);
		}
		
		Node current = node;
		Node parent = current.parent;
		while (parent != null && node == parent.left) {
			current = parent;
			parent = parent.parent;
		}
		
		return parent;
	}
}





