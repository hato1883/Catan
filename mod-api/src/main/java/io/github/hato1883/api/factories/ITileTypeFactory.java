package io.github.hato1883.api.factories;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.ITileType;

import java.util.Map;

public interface ITileTypeFactory {
    ITileType create(Identifier id, Map<IResourceType, Integer> baseProduction);
}
