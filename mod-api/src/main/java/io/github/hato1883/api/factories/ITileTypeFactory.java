package io.github.hato1883.api.factories;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;

import java.util.Map;

public interface ITileTypeFactory {
    ITileType create(Identifier id, Map<IResourceType, Integer> baseProduction);
}
