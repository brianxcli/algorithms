package brian.algorithm.leetcode;

public class ReverseInteger {
	public static void main(String[] args) {
		ReverseInteger tool = new ReverseInteger();
		// int result = reverse(Integer.MIN_VALUE);
		int result = tool.reverse(-1000000003);
	}
	
	private int[] DIGITS = new int[10];
    
    public ReverseInteger() {
        for (int i = 0; i < DIGITS.length; i++) {
            DIGITS[i] = -1;
        }
    } 
	
	public int reverse(int x) {
		if (x == Integer.MIN_VALUE) {
			return 0;
		}
		
		boolean isPositive = (x >= 0);
		
		long start = System.nanoTime();
		
		int residual = isPositive ? x : -x;
		int digit = -1;
		int idx = 0;
		
		while (residual != 0) {
			digit = residual % 10;
			residual /= 10;
			DIGITS[idx] = digit;
			idx++;
		}
		
		long getDigits = System.nanoTime();
		System.out.println(getDigits - start);
		
		long result = 0;
		long loopstart = System.nanoTime();
		for (int i = 0; i < 10; i++) {
			if (DIGITS[i] == -1) {
				break;
			}
			
			result = result * 10 + DIGITS[i];			
			if (result > Integer.MAX_VALUE) {
			    return 0;
			}
			
			//long loopend = System.nanoTime();
			//System.out.println(loopend - loopstart);
		}
		
		System.out.println(System.nanoTime() - loopstart);
		
		return isPositive ? (int)result : -(int)result;
    }
}





