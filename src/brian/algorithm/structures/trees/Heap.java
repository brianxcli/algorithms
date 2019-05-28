package brian.algorithm.structures.trees;

import brian.algorithm.exceptions.HeapInitilizationException;

/**
 * Heap structure. Default is max heap for heap sort;
 * default capacity is 16, but can be defined by an 
 * integer array that is passed into or by customized
 * capacity.
 * @author brian
 */
public class Heap {	
	private int[] heap;
	private boolean isMaxHeap;
	private int heapSize;

	public Heap(int[] heap) {
		this(true, heap);
	}

	public Heap(boolean isMaxHeap, int[] heap) {
		this.isMaxHeap = isMaxHeap;
		initializeBlindly(heap);
	}

	private void initializeBlindly(int[] heap) {
		this.heapSize = heap.length;
		this.heap = new int[heapSize + 1];
		
		for (int i = 0; i < heapSize; i++) {
			this.heap[i + 1] = heap[i];
		}
	}
	
	public boolean isMaxHeap() {
		return isMaxHeap;
	}
	
	public boolean isInitialized() {
		return (this.heap != null);
	}
	
	public void initialize(int[] heap) throws HeapInitilizationException {
		if (this.heap != null) {
			throw new HeapInitilizationException("Heap cannot be intilized twice.");
		}
		
		initializeBlindly(heap);
	}
	
	public void heapify(int index) {
		heapify(index, heapSize);
	}
	
	private void heapify(int index, int heapSize) {	
		// using while statement instead of recursion
		while (index != 0 && index <= heapSize) {
			int left = leftChildIndex(index);
			left = (left <= heapSize) ? left : 0;
			
			int right = rightChildIndex(index);
			right = (right <= heapSize) ? right : 0;
			
			int swap = index;
			
			// if it is a max heap, find the largest node;
			// otherwise find the the smallest node.
			if (isMaxHeap) {
				if (left != 0 && heap[left] > heap[index]) {
					swap = left;
				}
				
				if (right != 0 && heap[right] > heap[swap]) {
					swap = right;
				}
			} else {
				if (left != 0 && heap[left] < heap[index]) {
					swap = left;
				}
				
				if (right != 0 && heap[right] < heap[swap]) {
					swap = right;
				}
			}
			
			if (swap != index) {
				swap(swap, index);
				index = swap;
			} else {
				// the heap has met the heap property
				return;
			}
		}		
	}
	
	public void swap(int preIdx, int latIdx) {
		int temp = heap[preIdx];
		heap[preIdx] = heap[latIdx];
		heap[latIdx] = temp;
	}
	
	private int leftChildIndex(int index) {
		int target = index * 2;
		return (target <= heapSize) ? target : 0;
	}
	
	private int rightChildIndex(int index) {
		int target = index * 2 + 1;
		return (target <= heapSize) ? target : 0;
	}
	
	private int parentIndex(int index) {
		return (index / 2);
	}
	
	public int lastNonLeafNodeIndex() {
		return parentIndex(heapSize);
	}
	
	public int firstLeafIndex() {
		return (lastNonLeafNodeIndex() +1);
	}
	
	public void buildHeap() throws HeapInitilizationException {
		if (this.heap == null) {
			throw new HeapInitilizationException("Heap has not been initialized.");
		}
		
		int last = lastNonLeafNodeIndex();		
		for (int i = last; i >= 1; i--) {
			heapify(i);
		}
	}
	
	public int getHeapSize() {
		return heapSize;
	}
	
	public void sort() {
		int size = heapSize;

		while (size >= 2) {
			swap(1, size);
			size--;
			
			// heapify from node all the way down to 
			// one of the leaves along one shortest path
			heapify(1, size);
		}
	}
	
	public void dump() {
		System.out.println();
		for (int i = 1; i <= heapSize; i++) {
			System.out.print(heap[i] + " ");
		}
		
		System.out.println();
	}
}




