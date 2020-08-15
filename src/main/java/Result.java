public class Result {
    private final Object sortKey;
    private final Object o;

    public Result(Object sortKey, Object value) {

        this.sortKey = sortKey;
        this.o = value;
    }

    @Override
    public String toString() {
        return "Result{" +
                "sortKey=" + sortKey +
                ", o=" + o +
                '}';
    }
}
