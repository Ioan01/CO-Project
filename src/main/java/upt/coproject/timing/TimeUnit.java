package upt.coproject.timing;

public enum TimeUnit {
    NANO(1),
    MICRO(1_000),
    MILLI(1_000_000),
    SEC(1_000_000_000);

    private final int value;

    TimeUnit(final int val) {
        value = val;
    }

    public int getValue() { return value; }
}
