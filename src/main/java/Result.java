import java.util.Objects;

public class Result {
    private final Object partitionKey;
    private final Object attributes;
    private final String sortKey;
    private Object value;

    public Result(Object partitionKey, Object databaseValue, String sortKey, Object value) {

        this.partitionKey = partitionKey;
        this.attributes = databaseValue;
        this.sortKey = sortKey;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Result{" +
                "partitionKey=" + partitionKey +
                ", attributes=" + attributes +
                ", sortKey='" + sortKey + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(sortKey, result.sortKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortKey);
    }

    public static int Comparator(Result result, Result result1) {
        return result.sortKey.compareTo(result1.sortKey);
    }

    public String getSortKey() {
        return sortKey;
    }
}
