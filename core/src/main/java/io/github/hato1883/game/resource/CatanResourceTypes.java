package io.github.hato1883.game.resource;

import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.registries.RegistryManager;

/**
 * Enum representing the resource types found on the board.
 */
// TODO: Move to basemod
public enum CatanResourceTypes implements IResourceType {
    GRAIN("catan:grain", "Grain", ""),
    BRICK("catan:brick", "Brick", ""),
    LUMBER("catan:lumber", "Lumber", ""),
    ORE("catan:ore", "Ore", ""),
    WOOL("catan:wool", "Wool", "");

    private final String desc;
    private final String id;
    private final String name;

    CatanResourceTypes(String id, String name, String desc) {
        this.desc = desc;
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    public static void registerDefaults() {
        for (IResourceType type : values()) {
            RegistryManager.getResourceTypeRegistry().register(type.getId(), type);
        }
    }
}
