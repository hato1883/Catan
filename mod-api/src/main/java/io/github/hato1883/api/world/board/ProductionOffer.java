package io.github.hato1883.api.world.board;

import io.github.hato1883.api.entities.resource.IResourceType;

import java.util.Map;

public record ProductionOffer(ITile sourceTile, ITileType tileType, int rolledNumber,
                              Map<IResourceType, Integer> baseProduction, boolean isBlocked) {
    public ProductionOffer(ITile sourceTile, ITileType tileType, int rolledNumber,
                           Map<IResourceType, Integer> baseProduction, boolean isBlocked) {
        this.sourceTile = sourceTile;
        this.tileType = tileType;
        this.rolledNumber = rolledNumber;
        this.baseProduction = Map.copyOf(baseProduction);
        this.isBlocked = isBlocked;
    }
}
