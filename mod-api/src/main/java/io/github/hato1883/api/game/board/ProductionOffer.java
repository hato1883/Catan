package io.github.hato1883.api.game.board;

import io.github.hato1883.api.game.IResourceType;

import java.util.Map;

public record ProductionOffer(IHexTile sourceTile, ITileType tileType, int rolledNumber,
                              Map<IResourceType, Integer> baseProduction, boolean isBlocked) {
    public ProductionOffer(IHexTile sourceTile, ITileType tileType, int rolledNumber,
                           Map<IResourceType, Integer> baseProduction, boolean isBlocked) {
        this.sourceTile = sourceTile;
        this.tileType = tileType;
        this.rolledNumber = rolledNumber;
        this.baseProduction = Map.copyOf(baseProduction);
        this.isBlocked = isBlocked;
    }
}
