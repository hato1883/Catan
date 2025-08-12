package io.github.hato1883.modloader.assets;

public final class Identifier {
    private final String modId;
    private final String itemId;

    private Identifier(String modId, String itemId) {
        this.modId = modId;
        this.itemId = itemId;
    }

    public static Identifier of(String modId, String itemId) {
        return new Identifier(modId, itemId);
    }

    @Override
    public String toString() {
        return modId + ":" + itemId;
    }
}

