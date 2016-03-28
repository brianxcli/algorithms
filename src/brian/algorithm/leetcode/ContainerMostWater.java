package brian.algorithm.leetcode;

public class ContainerMostWater {
	public static void main(String[] args) {
		ContainerMostWater container = new ContainerMostWater();
		System.out.println(container.maxArea(test));
		String a = "asdfsdfsdfs";
	}
	
	private static int[] test;
	
	public ContainerMostWater() {
		test = new int[15000];
		for (int i = 0; i < 15000; i++) {
			test[i] = i;
		}
	}

	public int maxArea(int[] height) {
        int size = height.length;
        int max = 0;
                
        for (int i = 0; i < size; i++) {
        	int start = 0;
            int end = size - 1;
            
        	for (int j = i - 1; j >= 0; j--) {
        		if (height[i] <= height[j]) {
        			start = j;
        		}
        	}
        	
        	for (int j = i + 1; j <= size - 1; j++) {
        		if (height[i] <= height[j]) {
        			end = j;
        		}
        	}
        	
        	int area = height[i] * (height[end] - height[start]);
        	if (max < area) {
        		max = area;
        	}
        }
        
        return max;
	}
}