package io.github.hato1883.modloader.dependency;

/**
 * Represents the load priority of a mod.
 * Higher priority mods load before lower priority mods,
 * but dependency order always takes precedence.
 */
public enum LoadPriority {
    HIGH(3),
    NORMAL(2),
    LOW(1);

    private final int weight;

    LoadPriority(int weight) {
        this.weight = weight;
    }

    /**
     * Gets the weight of this priority.
     * Higher weight means higher priority.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns true if this priority is higher than the other.
     */
    public boolean isHigherThan(LoadPriority other) {
        return this.getWeight() > other.getWeight();
    }

    /**
     * Returns true if this priority is lower than the other.
     */
    public boolean isLowerThan(LoadPriority other) {
        return this.getWeight() < other.getWeight();
    }

    /**
     * Compares two LoadPriority values based on their weight.
     * Returns positive if a > b, negative if a < b, zero if equal.
     */
    public static int compare(LoadPriority a, LoadPriority b) {
        return Integer.compare(a.getWeight(), b.getWeight());
    }
}
