package brian.algorithm.sorting;

import brian.algorithm.structures.trees.BinarySearchTree;

public class BinarySearch {
	private static int[] test1 = {126, -32, 17, 5, 0, 13, 2, 25, 7, 20, 8, 4, 13, 756, 5, 113, -5, 923, 0};
	
	public static void main(String[] args) {
		BinarySearchTree tree = new BinarySearchTree();
		
		tree.buildTree(test1);
		System.out.println("in-order walk recursive begins");
		tree.inOrderTreeWalk();
		
		System.out.println();
		System.out.println("in-order walk non-recursive begins");
		tree.inOrderWalkNonRecursive();
		//System.out.println("min: " + tree.getMinimum().key);
		//System.out.println("max: " + tree.getMaximum().key);
	}
}
