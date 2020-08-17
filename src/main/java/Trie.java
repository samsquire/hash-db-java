import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        HashMap<Character, TrieNode> children = root.children;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            TrieNode t;
            if (children.containsKey(c)) {
                t = children.get(c);
            } else {
                t = new TrieNode(c);
                children.put(c, t);
            }

            children = t.children;

            //set leaf node
            if (i == word.length() - 1) {
                t.isLeaf = true;
                t.value = value;
                t.key = word;
                t.sortKey = sortKey;
            }
        }
    }



    public List<TrieNode> searchNode(String str) {
        Map<Character, TrieNode> children = root.children;
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
            descend(results, children);
        }

        return results;
    }

    private void descend(List<TrieNode> results, Map<Character, TrieNode> children) {
        for (TrieNode t : children.values()) {
            if (t.isLeaf) {
                results.add(t);
            } else {
                descend(results, t.children);
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