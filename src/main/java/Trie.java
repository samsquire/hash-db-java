import java.util.*;

/*
This code is based off https://www.programcreek.com/2014/05/leetcode-implement-trie-prefix-tree-java/
But changed to produce collections
 */

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode('_');
    }

    // Inserts a word into the trie.
    public void insert(String word, Object value, String sortKey) {
        Map<Character, TrieNode> children = root.children;
        TrieNode t = null;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);


            if (children.containsKey(c)) {
                t = children.get(c);
            } else {
                t = new TrieNode(c);
                children.put(c, t);
            }

            children = t.children;

            //set leaf node

        }
        t.isLeaf = true;
        t.value = value;
        t.key = word;
        t.sortKey = sortKey;

    }



    public List<TrieNode> searchNode(String str, SortOrder order) {
        TreeMap<Character, TrieNode> children = root.children;
        TrieNode t = null;
        List<TrieNode> results = new ArrayList<TrieNode>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (children.containsKey(c)) {
                t = children.get(c);
                children = t.children;
            } else {
                return null;
            }
        }
        if (t.isLeaf) {
            results.add(t);
        } else {
            if (order == SortOrder.ASCENDING) {
                descendAscending(results, children, order);
            } else {
                descendDescending(results, children, order);
            }
        }

        return results;
    }

    private void descendDescending(List<TrieNode> results, TreeMap<Character,TrieNode> children, SortOrder order) {
        for (TrieNode t : children.descendingMap().values()) {
            if (t.isLeaf) {
                results.add(t);
            } else {
                descendDescending(results, t.children, order);
            }
        }
    }

    private void descendAscending(List<TrieNode> results, Map<Character, TrieNode> children, SortOrder order) {
        for (TrieNode t : children.values()) {
            if (t.isLeaf) {
                results.add(t);
            } else {
                descendAscending(results, t.children, order);
            }
        }
    }

    public boolean has(String key) {
        return get(key) != null;
    }

    public TrieNode get(String key) {
        Map<Character, TrieNode> children = root.children;
        TrieNode t = null;
        List<TrieNode> results = new ArrayList<TrieNode>();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (children.containsKey(c)) {
                t = children.get(c);
                children = t.children;
            } else {
                return null;
            }
        }
        return t;
    }
}