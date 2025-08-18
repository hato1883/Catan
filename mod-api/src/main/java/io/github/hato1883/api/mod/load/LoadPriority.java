package io.github.hato1883.api.mod.load;

public enum LoadPriority {
    HIGH(3),
    NORMAL(2),
    LOW(1);

    private final int weight;

    LoadPriority(int weight) {
        this.weight = weight;
    }

    /**
     * Return the numerical priority,
     * Higher value means earlier in loading queue,
     * Lower value means later in loading queue.
     * @return Priority
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Compares two priorities.
     * @param other another priority
     * @return true if this priority is strictly higher than the other, else false
     */
    public boolean isHigherThan(LoadPriority other) {
        return this.getWeight() > other.getWeight();
    }

    /**
     * Compares two priorities.
     * @param other another priority
     * @return true if this priority is strictly lower than the other, else false
     */
    public boolean isLowerThan(LoadPriority other) {
        return this.getWeight() < other.getWeight();
    }

    public static int compare(LoadPriority a, LoadPriority b) {
        return Integer.compare(a.getWeight(), b.getWeight());
    }
}
