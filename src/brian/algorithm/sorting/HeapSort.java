package brian.algorithm.sorting;

import brian.algorithm.exceptions.HeapInitilizationException;
import brian.algorithm.structures.trees.Heap;

public class HeapSort {
	private static int[] test1 = {5, 13, 2, 25, 7, 17, 20, 8, 4};
	
	public static void main(String[] args) {
		Heap heap = new Heap(test1);
		
		try {
			heap.buildHeap();
		} catch (HeapInitilizationException e) {
			e.printStackTrace();
		}
		
		heap.sort();
	}
}
