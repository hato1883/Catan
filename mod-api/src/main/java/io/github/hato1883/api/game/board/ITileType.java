package io.github.hato1883.api.game.board;

import io.github.hato1883.api.game.IResourceType;

import java.util.Map;

public interface ITileType {
    String getId(); // "catan:forest"

    /**
     * Base resource production this tile provides when activated.
     * Example: { LUMBER: 1 } for normal forest,
     * or { LUMBER: 2 } for rich forest.
     */
    Map<IResourceType, Integer> getBaseProduction();
}
