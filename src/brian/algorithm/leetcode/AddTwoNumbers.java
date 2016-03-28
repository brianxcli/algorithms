package brian.algorithm.leetcode;

public class AddTwoNumbers {
	private static ListNode HEADER = new ListNode(-1);
	
	public static void main(String[] args) {
		ListNode node1 = toNode(0);
		ListNode node2 = toNode(0);
		ListNode result = addTwoNumbers(node1, node2);
		System.out.println(result);
	}
	
	public AddTwoNumbers() {
		// TODO Auto-generated constructor stub
	}
	
	private static class ListNode {
		public int val;
		public ListNode next;
		public ListNode(int x) { val = x; }
	}
	
	public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode input1 = l1;
		ListNode input2 = l2;
		HEADER.next = null;
		ListNode temp = HEADER;
		boolean overflow = false;
		
		while (input1 != null && input2 != null) {
			int num1 = input1.val;
			int num2 = input2.val;
			int sum = num1 + num2;
			
			if (overflow) {
				sum++;
				overflow = false;
			}
			
			if (sum > 9) {
				overflow = true;
				sum -= 10;
			}
			
			input1.val = sum;
			temp.next = input1;
			temp = temp.next;
			
			if (input1 != null) input1 = input1.next;
			if (input2 != null) input2 = input2.next;
		}
		
		if (input1 == null && input2 == null) {
			if (overflow) {
				temp.next = new ListNode(1);
			}		    
		} else {
		    ListNode longer = (input1 == null) ? input2 : input1;
		    
		    while (longer.next != null) {
		    	if (overflow) {
		    		longer.val++;
		    	}
		    	
		    	if (longer.val > 9) {
		    		longer.val -= 10;		    		
		    	} else {
		    		overflow = false;
		    	}
		    	
		    	temp.next = longer;
		    	longer = longer.next;
		    	temp = temp.next;
		    }
		    
		    temp.next = longer;
		    if (overflow) {
		    	longer.val++;
		    }
		    
		    if (longer.val > 9) {
	    		longer.val -= 10;		    		
	    	} else {
	    		overflow = false;
	    	}
		    
		    if (overflow) {
		    	longer.next = new ListNode(1);
		    }
		}

		return HEADER.next;
    }
	
	private static ListNode toNode(int num) {	
		if (0 <= num && num < 10) {
			return new ListNode(num);
		}
				
		int remains = num / 10;
		int digit = num % 10;
		ListNode node = new ListNode(digit);
		ListNode temp = node;
		
		while (remains != 0) {
			digit = remains % 10;
			remains /= 10;
			temp.next = new ListNode(digit);
			temp = temp.next;
		}

		return node;
	}
}





