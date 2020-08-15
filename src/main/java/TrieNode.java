import java.util.HashMap;

class TrieNode {
    public String key;
    char c;
    public Object value = null;
    HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
    boolean isLeaf;

    public TrieNode(char c, Object value) {
        this.c = c;
        this.value = value;
    }

    public TrieNode(char c){
        this.c = c;
    }
}
