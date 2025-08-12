package io.github.hato1883.api.game.board;

import io.github.hato1883.api.game.IResourceType;

import java.util.Collection;
import java.util.Map;

public interface IHexTile {

    /**
     *
     * @return the {@link ICubeCoord} coordinates for this Hexagon Tile
     */
    ICubeCoord getCoord();

    /**
     * Unique ID for tile type. Used in serialization, rules, and registry.
     */
    ITileType getTileType();

    /**
     * Number token that triggers production (e.g., 5, 6, 5 AND 6)
     * May be -1 if disabled or blocked.
     */
    Collection<Integer> getProductionNumbers();

    /**
     * Resource types this tile can produce.
     * May be empty (e.g., desert), or contain multiple resources.
     */
    Map<IResourceType, Integer> getBaseProduction();


    Collection<IStructure> getConnectedStructures();

    /**
     * Triggers production of the tile,
     * Called on every dice roll,
     * but uses roll as parameter to allow Structures to adhere or ignore dice result
     * @param rolledNumber number rolled by the dice
     */
    void triggerProduction(int rolledNumber);

    /**
     * Whether the tile is currently blocked (e.g., by the robber)
     */
    boolean isBlocked();
}
