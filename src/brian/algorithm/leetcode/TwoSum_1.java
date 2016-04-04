package brian.algorithm.leetcode;

/**
 * Given an array of integers, return indices of the two 
 * numbers such that they add up to a specific target.
 * 
 * You may assume that each input would have exactly one solution.
 * 
 * Example:
 * 
 * Given nums = [2, 7, 11, 15], target = 9,
 * Because nums[0] + nums[1] = 2 + 7 = 9,
 * return [0, 1].
 * 
 * UPDATE (2016/2/13): 
 * The return format had been changed to zero-based indices. 
 * Please read the above updated description carefully.
 * @author Xiaochen
 *
 */

public class TwoSum_1 {
	public int[] twoSum(int[] nums, int target) {
		// Whatever the two numbers are, say a + b = c,
		// c/2 must be the middle point between a and b.
		// So a and b have the same distance to c/2.
		// Here, variable half is such c/2 value.
        double half = ((double) target) / 2;
        
        // indices mean distances, and values
        // mean the indices in the input int array nums
        int[] store = new int[99999];
        
        int size = nums.length;
        for (int i = 0; i < size; i++) {
            double distance = Math.abs(nums[i] - half);
            int index = distanceToIndex(distance);
            
            if (store[index] != 0) {
                if (nums[store[index] - 1] + nums[i] != target) {
                    continue;
                } else {
                    int[] ret = new int[2];
                    ret[0] = store[index];
            		ret[1] = i + 1;
            		return ret;
                }
            } else {
            	// this is because the original answer is to 
            	// be 1 based, but now it has changed to be
            	// zero based.
            	// store[index] = i + 1;
            	store[index] = i;
            }
        }
        
        return null;
    }
    
    private int distanceToIndex(double distance) {
        return (int) (distance * 2);
    }
}
