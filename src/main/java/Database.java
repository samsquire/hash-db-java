import java.util.*;

public class Database {
    private Map<String, Object> db = new HashMap<>();
    private Trie sortIndex = new Trie();
    private Map<String, Trie> secondaryIndexes = new HashMap<String, Trie>();

    public static void main(String[] args) {
        Database db = new Database();

        db.store("main", "user#samsquire", "following#dinar", List.of("Messages 1"), null);
        db.store("main", "user#samsquire", "following#someonelse", List.of("Messages 2"), null);
        db.store("main", "user#samsquire", "message#2020-08-01T14:39", List.of("Messages 1"), null);
        db.store("main", "user#samsquire", "message#2020-07-01T14:39", List.of("Messages 2"), null);
        db.store("main", "user#samsquire", "message#2020-06-01T09:30", List.of("Messages 3"), null);
        db.store("main", "user#samsquire", "message#2020-06-01T14:39", List.of("Messages 4"), null);
        db.store("main", "user#dinar", "message#2020-06-01T14:39", List.of("Messages 5"), null);

        db.store("index1", "user#dinar", "message#2020-06-01T14:39",
                Map.of("messages", List.of("Messages 6"),
                        "readstatus", "READ"),
                "readstatus");
        db.store("index1", "user#dinar", "message#2020-07-01T14:39",
                Map.of("messages", List.of("Messages 7"),
                        "readstatus", "UNREAD"),
                "readstatus");

        List<Result> following = db.begins_with("user#samsquire", "following", SortOrder.ASCENDING);
        System.out.println("Following samsquire");
        for (Result result : following) {
            System.out.println(result);
        }
        System.out.println("Query between");
        for (Result result : db.query_between("user#samsquire", "message#2020-06-01", "message#2020-07-01", SortOrder.ASCENDING)) {

            System.out.println(result);
        }

        System.out.println("Messages sent by users");
        for (Result result : db.pk_begins_with("message", "user", SortOrder.DESCENDING)) {
            System.out.println(result);
        }

        System.out.println("Read messages by dinar");
        List<Result> read = db.begins_with("index1", "user#dinar", "READ", SortOrder.ASCENDING);
        for (Result result : read) {
            System.out.println(result);
        }

        System.out.println("Users with UNREAD messages");
        List<Result> usersWithUnread = db.pk_begins_with("index1", "UNREAD", "user", SortOrder.ASCENDING);
        for (Result result : usersWithUnread) {
            System.out.println(result);
        }

    }

    private List<Result> query_between(String partitionKey, String fromKey, String toKey, SortOrder order) {
        List<Result> results = new ArrayList<Result>();

        for (TrieNode trieNode : sortIndex.searchNode(partitionKey + ":" + fromKey, order)) {
            results.add(new Result(trieNode.value, db.get(trieNode.key), trieNode.sortKey, trieNode.value));
        }
        for (TrieNode trieNode : sortIndex.searchNode(partitionKey + ":" + toKey, order)) {
            results.add(new Result(trieNode.value, db.get(trieNode.key), trieNode.sortKey, trieNode.value));
        }
        return results;
    }

    private List<Result> pk_begins_with(String partitionKey, String sortKey, SortOrder order) {
        List<Result> results = new ArrayList<Result>();
        for (TrieNode trieNode : sortIndex.searchNode(partitionKey, order)) {
            for (TrieNode result : ((Trie)trieNode.value).searchNode(sortKey, order)) {
                results.add(new Result(result.value, db.get(result.value), trieNode.sortKey, result.value));
            }
        }
        return results;
    }


    private List<Result> pk_begins_with(String indexName, String partitionKey, String sortKey, SortOrder order) {
        Trie secondaryIndex = secondaryIndexes.get(indexName);
        List<Result> results = new ArrayList<Result>();
        for (TrieNode trieNode : secondaryIndex.searchNode(partitionKey, order)) {
            for (TrieNode result : ((Trie)trieNode.value).searchNode(sortKey, order)) {
                results.add(new Result(result.key, db.get(result.value), trieNode.sortKey, result.value));
            }
        }
        return results;
    }

    private List<Result> begins_with(String partitionKey, String query, SortOrder order) {
        List<Result> results = new ArrayList<>();
        for (TrieNode trieNode : sortIndex.searchNode(partitionKey + ":" + query, order)) {
            results.add(new Result(trieNode.value, db.get(trieNode.key), trieNode.sortKey, trieNode.value));
        }
        return results;
    }

    private List<Result> begins_with(String indexName, String partitionKey, String query, SortOrder order) {
        List<Result> results = new ArrayList<>();
        List<TrieNode> trieNodes = secondaryIndexes.get(indexName).searchNode(partitionKey + ":" + query, order);
        if (trieNodes == null) {
            return results;
        }
        for (TrieNode trieNode : trieNodes) {
            results.add(new Result(trieNode.key, db.get(trieNode.value), trieNode.sortKey, trieNode.value));
        }
        return results;
    }

    public String store(String indexName, String partitionKey, String sortKey, Object value, String indexKey) {
        if (db.containsKey(partitionKey + ":" + value)) {
            return "ALREADY_STORED";
        }
        db.put(partitionKey + ":" + sortKey, value);
        if (indexName.equals("main")) {
            sortIndex.insert(partitionKey + ":" + sortKey, sortKey, sortKey);
            if (!sortIndex.has(sortKey)) {
                sortIndex.insert(sortKey, new Trie(), sortKey);
            }
            ((Trie) sortIndex.get(sortKey).value).insert(partitionKey, partitionKey + ":" + sortKey, sortKey);
        } else {
            Map<String, Object> attributes = (Map<String, Object>) value;
            Trie secondaryIndex = null;
            if (!secondaryIndexes.containsKey(indexName)) {
                secondaryIndexes.put(indexName, new Trie());
            }
            secondaryIndex = secondaryIndexes.get(indexName);
            String indexingOn = (String) attributes.get(indexKey);
            secondaryIndex.insert(partitionKey + ":" + indexingOn, partitionKey + ":" + sortKey, sortKey);

            // inverted index
            if (!secondaryIndex.has(indexingOn)) {
                secondaryIndex.insert(indexingOn, new Trie(), indexingOn);
            }
            ((Trie) secondaryIndex.get(indexingOn).value).insert(partitionKey, partitionKey + ":" + sortKey, sortKey);
        }

        return "STORED";
    }


}