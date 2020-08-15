import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private Map<String, Object> db = new HashMap<>();
    private Trie sortIndex = new Trie();

    public static void main(String[] args) {
        Database db = new Database();

        db.store("user#samsquire", "following#dinar", List.of("Messages 1"));
        db.store("user#samsquire", "following#someonelse", List.of("Messages 2"));
        db.store("user#samsquire", "message#2020-08-01T14:39", List.of("Messages 1"));
        db.store("user#samsquire", "message#2020-07-01T14:39", List.of("Messages 2"));
        db.store("user#samsquire", "message#2020-06-01T09:30", List.of("Messages 3"));
        db.store("user#samsquire", "message#2020-06-01T14:39", List.of("Messages 4"));
        db.store("user#dinar", "message#2020-06-01T14:39", List.of("Messages 5"));

        List<TrieNode> following = db.begins_with("user#samsquire", "following");
        System.out.println("Printing results");
        for (TrieNode result : following) {
            System.out.println(result.value);
        }
        System.out.println("Messages sent by users");
        for (Result result : db.pk_begins_with("message", "user")) {
            System.out.println(result);
        }
    }

    private List<Result> pk_begins_with(String partitionKey, String sortKey) {
        List<Result> results = new ArrayList<Result>();
        for (TrieNode trieNode : sortIndex.searchNode(partitionKey)) {
            for (TrieNode result : ((Trie)trieNode.value).searchNode(sortKey)) {
                results.add(new Result(result.value, db.get(result.value)));
            }
        }
        return results;
    }

    private List<TrieNode> begins_with(String partitionKey, String query) {
        return sortIndex.searchNode(partitionKey + ":" + query);
    }

    public void store(String partitionKey, String sortKey, Object value) {
        db.put(partitionKey + ":" + sortKey, value);
        sortIndex.insert(partitionKey + ":" + sortKey, sortKey);
        if (!sortIndex.has(sortKey)) {
            sortIndex.insert(sortKey, new Trie());
        }
        ((Trie)sortIndex.get(sortKey).value).insert(partitionKey, partitionKey + ":" + sortKey);
    }


}