package io.github.hato1883.api.mod.load.asset;

public enum AssetCategory {
    TILE("tiles"),
    BUILDING("buildings"),
    ROAD("roads"),
    PORT("ports");


    private final String category;

    AssetCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the String version category of this AssetCategory.
     * Used to locate assets
     */
    public String getCategory() {
        return category;
    }

    public static AssetCategory of(String category) {
        return switch (category) {
            case "tiles" -> TILE;
            case "buildings" -> BUILDING;
            case "roads" -> ROAD;
            case "ports" -> PORT;
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };
    }
}
