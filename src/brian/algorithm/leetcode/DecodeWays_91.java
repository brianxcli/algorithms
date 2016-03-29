package brian.algorithm.leetcode;

public class DecodeWays_91 {
	private static String test = "101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010";
	private static String test1 = "1010";
	private static String test2 = "0";
	private static String test3 = "7206";
	private static String test4 = "217";
	private static String test5 = "284278238490238490238490";
	
	public static void main(String[] args) {
		DecodeWays_91 decodeWay = new DecodeWays_91();
		int ways = decodeWay.numDecodings(test3);
		System.out.println(ways);
	}
	
    public int numDecodings(String s) {
    	if (s == null || s.length() == 0) {
    		return 0;
    	}
    	
    	int smaller = 0;
        int bigger = 0;
    	
    	int length = s.length();
    	int lastIdx = length - 1;
    	smaller = decodeOneDigit(s.charAt(lastIdx));   	
    	if (length == 1) {
    		return smaller;
    	} 
    	
    	int lastButOneIdx = lastIdx - 1;
    	bigger = decodeTwoDigits(s, lastButOneIdx);
    	if (length == 2) {
    		return bigger;
    	}
    	
    	int loopFirst = lastButOneIdx - 1;
    	for (int i = loopFirst; i >= 0; i--) {
    		char ch = s.charAt(i);
    		int temp = 0;
    		
    		if (ch == '0') {
    			temp = 0;
    		} else {
    			int firstTwo = Integer.parseInt(s.substring(i, i + 2));
    			if (firstTwo <= 26) {
    				if (firstTwo == 10 || firstTwo == 20) {
    					temp = smaller;
    				} else {
    					temp = 
    				}
    			} else {
    				temp = bigger;
    			}
    		}
    	}
    	
    	
    	
    	
    	
    	

        
        for (int i = lastSnd - 1; i >= 0; i--) {
        	char ch = input[i];
        	int temp = 0;
        	
        	if (ch == '0') {
        		temp = 0;
        	} else if (ch == '1') {
        		temp = decodeOneDigit(ch) * (bigger + smaller);
        	} else if (ch == '2') {
        		char next = input[i + 1];
        		if (next == '0') {
        			temp = smaller;
        		} else if ('1' <= next && next <= '6') {
        			temp = decodeOneDigit(ch) * (bigger + smaller);
        		} else {
        			temp = bigger;
        		}
        	} else {
        		temp = bigger;
        	}

        	smaller = bigger;
        	bigger = temp;
        }
        
        return bigger;
    }
    
    private int decodeOneDigit(char ch) {
    	if ('1' <= ch && ch <= '9') {
    		return 1;
    	} else {
    		return 0;
    	}
    }
    
    private int decodeTwoDigits(String s, int idx) {
    	int value = Integer.parseInt(s.substring(idx, idx + 2));
    	if (value <= 26) {
    		if (value == 10 || value == 20) {
    			return 1;
    		} else {
    			return 2;
    		}
    	} else {
    		return 1;
    	}
    }
}