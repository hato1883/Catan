package io.github.hato1883.modloader.assets;

public enum AssetCategory {
    TILE("tile"),
    BUILDING("building"),
    ROAD("road"),
    PORT("port");


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
            case "tile" -> TILE;
            case "building" -> BUILDING;
            case "road" -> ROAD;
            case "port" -> PORT;
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };
    }
}
