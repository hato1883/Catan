package io.github.hato1883.core.game.board;

import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.*;

import java.util.*;

/**
 * Represents a single hex tile on the board.
 * Each tile has a resource type and an optional number token (0 if not assigned).
 */
public class HexTileImpl implements IHexTile {
    private final ITileType tileType;
    private final ICubeCoord coord;
    private final Collection<Integer> productionNumbers;
    private boolean isBlocked;

    public HexTileImpl(
        ITileType tileType,
        ICubeCoord coord,
        Collection<Integer> productionNumbers
    ) {
        this.tileType = tileType;
        this.coord = coord;
        this.productionNumbers = Collections.unmodifiableCollection(productionNumbers);
        this.isBlocked = false;
    }

    @Override
    public ITileType getTileType() {
        return tileType;
    }

    @Override
    public ICubeCoord getCoord() {
        return coord;
    }

    @Override
    public Collection<Integer> getProductionNumbers() {
        return productionNumbers;
    }

    @Override
    public Map<IResourceType, Integer> getBaseProduction() {
        return tileType.getBaseProduction();
    }

    @Override
    public Collection<IStructure> getConnectedStructures() {
        return List.of();
    }


    /**
     * Triggers production of the tile,
     * Called on every dice roll,
     * but uses roll as parameter to allow Structures to adhere or ignore dice result
     *
     * @param rolledNumber number rolled by the dice
     */
    @Override
    public void triggerProduction(int rolledNumber) {
        // Always produce an offer, even if the rolled number doesn't match
        ProductionOffer offer = new ProductionOffer(
            this,
            getTileType(),
            rolledNumber,
            getBaseProduction(),
            isBlocked
        );
        for (IStructure structure : getConnectedStructures()) {
            structure.receiveProductionOffer(offer);
        }
    }

    /**
     * Whether the tile is currently blocked (e.g., by the robber)
     */
    @Override
    public boolean isBlocked() {
        return isBlocked;
    }

    public void block() {
        isBlocked = true;
    }

    public void unblock() {
        isBlocked = true;
    }
}
