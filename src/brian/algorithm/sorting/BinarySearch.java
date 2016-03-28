package brian.algorithm.sorting;

import brian.algorithm.structures.BinarySearchTree;

public class BinarySearch {
	private static int[] test1 = {5, 13, 2, 25, 7, 17, 5, 20, 8, 4, 756, 113, -5, 0, 923, 25, 8};
	
	public static void main(String[] args) {
		BinarySearchTree tree = new BinarySearchTree();
		
		tree.buildTree(test1);
		tree.inOrderTreeWalk();		
		System.out.println("min: " + tree.getMinimum().key);
		System.out.println("max: " + tree.getMaximum().key);
	}
}
