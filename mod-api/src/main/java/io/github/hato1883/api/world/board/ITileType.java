package io.github.hato1883.api.world.board;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.entities.resource.IResourceType;

import java.util.Map;

public interface ITileType {
    Identifier getId(); // "catan:forest"

    /**
     * Base resource production this tile provides when activated.
     * Example: { LUMBER: 1 } for normal forest,
     * or { LUMBER: 2 } for rich forest.
     */
    Map<IResourceType, Integer> getBaseProduction();
}
