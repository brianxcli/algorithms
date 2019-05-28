package brian.algorithm.sorting;

import brian.algorithm.exceptions.HeapInitilizationException;
import brian.algorithm.structures.trees.Heap;

public class HeapSort {
	private static int[] test1 = {5, 13, 2, 25, 7, 17, 20, 8, 4};
	
	private static int[] test2 = { 
			482, 538, 519, 532, 335, 
			458, 526, 484, 342, 357, 
			353, 502, 461, 326, 434, 
			331, 526, 343, 388, 531
	};
	
	private static int[] test3 = { 
			746, 755, 827, 771, 919, 
			861, 754, 799, 900, 857, 
			933, 868, 942, 854, 924, 
			769, 819, 927, 892, 842
	};
	
	public static void main(String[] args) {
		Heap heap = new Heap(test1);
		
		try {
			heap.buildHeap();
		} catch (HeapInitilizationException e) {
			e.printStackTrace();
		}
		
		heap.sort();
		heap.dump();
	}
}
