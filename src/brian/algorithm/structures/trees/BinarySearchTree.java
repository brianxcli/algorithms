package brian.algorithm.structures.trees;

/**
 * A binary search tree structure which holds 
 * only different integer values as keys
 * @author Xiaochen
 */
public class BinarySearchTree {
	public Node root;

	public BinarySearchTree() {
		
	}
	
	public static class Node {
		public Node parent;
		public Node left;
		public Node right;
		public int key;
		
		public boolean hasParent() {
			return parent != null;
		}
		
		public Node parent() {
			return parent;
		}
		
		public Node left() {
			return left;
		}
		
		public Node right() {
			return right;
		}
		
		public boolean hasLeft() {
			return left != null;
		}
		
		public boolean hasRight() {
			return right != null;
		}
		
		public boolean isLeft() {
			return hasParent() && (parent.left == this);
		}
		
		public boolean isRight() {
			return hasParent() && (parent.right == this);
		}
	}
	
	public Node insert(int value) {
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
		
		return node;
	}
	
	private void insert(Node parent, Node node) {
		if (parent.key < node.key) {
			if (parent.right == null) {
				parent.right = node;
			} else {
				insert(parent.right, node);
			}
		} else if (parent.key > node.key) {
			if (parent.left == null) {
				parent.left = node;
			} else {
				insert(parent.left, node);
			}
		}
	}
	
	public Node insert_recursive(int value) {
		Node node = new Node();
		node.key = value;
		
		if (root == null) {
			root = node;
		} else {
			insert(root, node);
		}
		
		return node;
	}
	
	public void buildTree(int[] array) {
		for (int element : array) {
			insert(element);
		}
	}
	
	public void deleteNode(Node node) {
		if (node == null) {
			return;
		}
		
		if (node.left == null) {
			transplant(node, node.right);
		} else if (node.right == null) {
			transplant(node, node.left);
		} else {
			// Node target is the node with the smallest
			// key value in the node's right subtree,
			// meaning it is the successor of node if
			// node has the right subtree.
			// More efficient in this way
			Node target = getMinimum(node.right);
			
			if (target.parent != node) {
				// When target is not the right child of node,
				// we should first construct a new right subtree rooted
				// at target
				transplant(target, target.right);
				target.right = node.right;
				target.right.parent = target;
			}
			
			// replace node with the new tree root at target,
			// and then move the left subtree
			transplant(node, target);
			target.left = node.left;
			target.left.parent = target.left;
		}
	}
	
	/**
	 * Replaces one subtree as a child of its parent with another subtree.
	 * When it replaces the subtree rooted at node origin with the subtree 
	 * rooted at node target, node origin's parent becomes node target's 
	 * parent, and node origin's parent ends up having node target as its 
	 * appropriate child.
	 * @param origin
	 * @param target
	 */
	public void transplant(Node origin, Node target) {
		if (origin.parent == null) {
			// node origin is the root node
			root = target;
		} else if (origin == origin.parent.left) {
			// if origin is the left child of its parent
			origin.parent.left = target;
		} else {
			// if not the left child, it must the right child
			origin.parent.right = target;
		}
		
		if (target != null) {
			target.parent = origin.parent;
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
	
	public void inOrderWalkNonRecursive() {
		Node current = getMinimum(root);
		
		while (true) {	
			dumpNode(current);
			
			if (current.hasRight()) {
				current = getMinimum(current.right());
			} else {
				while (current.hasParent() && current.isRight()) {
					current = current.parent();
				}
				
				if (!current.hasParent()) {
					return;
				}
				
				if (current.isLeft()) {
					current = current.parent();
				}
			}
		}
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





