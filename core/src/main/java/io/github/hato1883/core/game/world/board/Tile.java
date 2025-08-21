package io.github.hato1883.core.game.world.board;

import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.*;
import java.util.*;

public class Tile implements ITile {
    private final ITileType tileType;
    private final ITilePosition position;
    private final Collection<Integer> productionNumbers;
    private boolean isBlocked;

    public Tile(
        ITileType tileType,
        ITilePosition position,
        Collection<Integer> productionNumbers
    ) {
        this.tileType = tileType;
        this.position = position;
        this.productionNumbers = Collections.unmodifiableCollection(productionNumbers);
        this.isBlocked = false;
    }
    @Override
    public ITilePosition getPosition() {
        return position;
    }
    @Override
    public ITileType getType() {
        return null;
    }

    @Override
    public Collection<Integer> getProductionNumbers() {
        return productionNumbers;
    }
//    @Override
//    public Map<IResourceType, Integer> getBaseProduction() {
//        return tileType.getBaseProduction();
//    }
//    @Override
//    public Collection<IStructure> getConnectedStructures() {
//        return List.of();
//    }
//    @Override
//    public void triggerProduction(int rolledNumber) {
//        // ...existing code...
//    }
//    @Override
//    public boolean isBlocked() {
//        return isBlocked;
//    }
    public void block() {
        isBlocked = true;
    }
    public void unblock() {
        isBlocked = false;
    }
}


