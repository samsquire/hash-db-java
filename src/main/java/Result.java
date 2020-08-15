public class Result {
    private final Object sortKey;
    private final Object attributes;

    public Result(Object sortKey, Object value) {

        this.sortKey = sortKey;
        this.attributes = value;
    }

    @Override
    public String toString() {
        return "Result{" +
                "sortKey=" + sortKey +
                ", attributes=" + attributes +
                '}';
    }
}
