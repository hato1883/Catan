package io.github.hato1883.game.board;

import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;
import io.github.hato1883.registries.RegistryManager;
import io.github.hato1883.game.resource.CatanResourceTypes;

import java.util.Map;

// TODO: Move to basemod
public enum CatanTileTypes implements ITileType {
    FOREST("catan:forest", Map.of(CatanResourceTypes.LUMBER, 1)),
    FIELD("catan:field", Map.of(CatanResourceTypes.GRAIN, 1)),
    HILL("catan:hill", Map.of(CatanResourceTypes.BRICK, 1)),
    MOUNTAIN("catan:mountain", Map.of(CatanResourceTypes.ORE, 1)),
    PASTURE("catan:pasture", Map.of(CatanResourceTypes.WOOL, 1)),
    DESERT("catan:desert", Map.of());

    private final String id;
    private final Map<IResourceType, Integer> baseProduction;

    CatanTileTypes(String id, Map<IResourceType, Integer> baseProduction) {
        this.id = id;
        this.baseProduction = baseProduction;
    }

    @Override
    public String getId() { return id; }

    @Override
    public Map<IResourceType, Integer> getBaseProduction() { return baseProduction; }

    public static void registerDefaults() {
        for (ITileType type : values()) {
            RegistryManager.getTileTypeRegistry().register(type.getId(), type);
        }
    }
}
