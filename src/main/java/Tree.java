import java.util.ArrayList;
import java.util.List;

public class Tree<V extends Comparable> {
    public String key;
    public V value;
    public Tree left;
    public Tree right;

    public Tree(String key, V value) {
        this.key = key;
        this.value = value;
    }
    public void insert(String key, V value) {
        if (this.left != null && key.compareTo(this.key) <= 0) {
            this.left.insert(key, value);
        } else if (this.right != null && key.compareTo(this.key) > 0) {
            this.right.insert(key, value);
        } else if (key.compareTo(this.key) <= 0) {
            this.left = new Tree<V>(key, value);
        } else if (key.compareTo(this.key) > 0) {
            this.right = new Tree<V>(key, value);
        } else {
            this.key = key;
            this.value = value;
        }
    }
    public List<Tree<V>> walkBetween(String startFrom, String stop) {
        List<Tree<V>> results = new ArrayList<>();
        if (this.left != null) {
            this.left.walkBetween(results, startFrom, stop);
        }
        if (this.key.compareTo(startFrom) >= 0 && this.key.compareTo(stop) <= 0) {
            results.add(this);
        }
        if (this.right != null) {
            this.right.walkBetween(results, startFrom, stop);
        }
        return results;
    }

    private List<Tree<V>> walkBetween(List<Tree<V>> results, String startFrom, String stop) {
        if (this.left != null) {
            this.left.walkBetween(results, startFrom, stop);
        }
        if (this.key.compareTo(startFrom) >= 0 && this.key.compareTo(stop) <= 0) {
            results.add(this);
        }
        if (this.right != null) {
            this.right.walkBetween(results, startFrom, stop);
        }
        return results;
    }
}
