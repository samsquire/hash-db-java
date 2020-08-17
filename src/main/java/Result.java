public class Result {
    private final Object partitionKey;
    private final Object attributes;
    private final String sortKey;

    public Result(Object partitionKey, Object value, String sortKey) {

        this.partitionKey = partitionKey;
        this.attributes = value;
        this.sortKey = sortKey;
    }

    @Override
    public String toString() {
        return "Result{" +
                "partitionKey=" + partitionKey +
                ", attributes=" + attributes +
                ", sortKey='" + sortKey + '\'' +
                '}';
    }
}
