package io.github.hato1883.api.world.board;

import java.util.List;

/**
 * Optional interface for tiles that can produce resources (e.g., Catan tiles).
 * Only implement this if your tile needs to support production logic.
 */
public interface IProductionTile extends ITile {
    /**
     * Called when a production event (e.g., dice roll) occurs.
     * @param rolledNumber the number/event that triggers production
     */
    void triggerProduction(int rolledNumber);

    /**
     * Returns the structures connected to this tile (e.g., settlements, cities).
     */
    List<IStructure> getConnectedStructures();

    /**
     * Adds a structure to this tile's connections.
     */
    void addConnectedStructure(IStructure structure);

    /**
     * Removes a structure from this tile's connections.
     */
    void removeConnectedStructure(IStructure structure);
}

