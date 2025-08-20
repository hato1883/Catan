package io.github.hato1883.api.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.ITileType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ITileTypeRegistry extends IRegistry<ITileType> {

    /**
     * Creates and registers a new tile type with the given identifier and base production.
     *
     * @param id the unique identifier for the tile type
     * @param baseProduction map of resource types to their base production values
     * @throws IllegalArgumentException if id is null or already registered
     */
    void register(@NotNull Identifier id, Map<IResourceType, Integer> baseProduction);

}
