import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class TrieNode {
    public String key;
    public String sortKey;
    char c;
    public Object value = null;
    TreeMap<Character, TrieNode> children = new TreeMap<Character, TrieNode>();
    boolean isLeaf;

    public TrieNode(char c, Object value) {
        this.c = c;
        this.value = value;
    }

    public TrieNode(char c){
        this.c = c;
    }
}
