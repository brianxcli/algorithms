package brian.algorithm.AucklandRoadSystem.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Dictionary that stores road names and facilitates quick
 * user lookup for names using trie data structure.
 * @author brian
 */
public class Dictionary {
	private static class LetterNode {
		public char key;
		// the String value represented by from the
		// root node to this node, including the key
		// of this node.
		public String prefix;
		public HashMap<Character, LetterNode> children;
		
		public LetterNode() {
			children = new HashMap<Character, LetterNode>();
		}
	}
	
	private static final int MAX_NAME = 10;
	
	private LetterNode rootNode;
	private static final Dictionary ins = new Dictionary();
		
	private Dictionary() {
		// default key value of root is a empty character
		rootNode = new LetterNode();
		rootNode.prefix = "";
	}
	
	public static final Dictionary getInstance() {
		return ins;
	}
	
	public void insert(String roadName) {
		int length = roadName.length();
		char[] letters = roadName.toCharArray();		
		LetterNode current = rootNode;
		
		for (int i = 0; i < length; i++) {
			// recursively insert every character of
			// the name with the current node going
			// deep in the tree.
			current = insert(letters, i, current);
		}
	}

	/**
	 * Add a character at the index of 'offset' to the 
	 * current node 'current'
	 * @param letter character array from the road name
	 * @param offset the index of the character to be inserted 
	 * @param current current searching node
	 * @return sub node which has the same key as the inserted
	 * character
	 */
	private LetterNode insert(char[] letter, int offset, LetterNode current) {
		char curChar = letter[offset];
		LetterNode nextChild = null;
		
		if (current.children.containsKey(curChar)) {
			// if the character has been inserted into 
			// current node, just return that node.
			nextChild = current.children.get(curChar);
		} else {
			// if no such character is inserted into 
			// current node, add a new node with the key
			// of the character and return the newly
			// created node.
			LetterNode node = new LetterNode();
			node.key = curChar;
			node.prefix = current.prefix + node.key;
			current.children.put(node.key, node);			
			nextChild = node;
		}
		
		return nextChild;
	}
	
	/**
	 * find the LetterNode that has the given key
	 * @param prefix
	 */
	private LetterNode searchSubNodeByPrefix(String prefix, LetterNode current) {
		char[] keys = prefix.toCharArray();
		LetterNode temp = null;
		int length = keys.length;
		temp = current;
		
		for (int i = 0; i < length; i++) {
			temp = searchSubNodeByKey(keys[i], temp);
		}
		
		return temp;
	}
	
	private LetterNode searchSubNodeByKey(char key, LetterNode current) {
		if (current.children.size() == 0) {
			return null;
		} 
		
		return current.children.get(key);
	}
	
	public HashSet<String> searchNameWithPrefix(String prefix) {
		HashSet<String> result = new HashSet<String>();
		
		if (RoadGraph.getInstance().containsRoad(prefix)) {
			// we also add the prefix if it is also a road name
			result.add(prefix);
		}
		
		LetterNode current = searchSubNodeByPrefix(prefix, rootNode);
		if (current != null) {
			travelNodes(result, current);
		}
		return result;
	}
	
	private void travelNodes(HashSet<String> res, LetterNode current) {
		if (current == null) {
			return;
		}
		
		if (RoadGraph.getInstance().containsRoad(current.prefix)) {
			// check if it is a road name
			res.add(current.prefix);
		}
		
		int size = current.children.size();
		if (size == 0 && !res.contains(current.prefix)) {
			// if no leaf any more, this node stands 
			// for one of the road names
			res.add(current.prefix);
			return;
		}
		
		for (LetterNode now : current.children.values()) {
			LetterNode next = current.children.get(now.key);
			travelNodes(res, next);
		}		
	}
}




