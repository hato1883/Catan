package io.github.hato1883.api.world.board;

import io.github.hato1883.api.Identifier;

import java.util.*;

public interface IBoardType {
    /**
     * The unique identification of this IBoardType
     */
    Identifier getIdentifier();

    /**
     * The user-friendly name of this board type (e.g., “Classic Hex Island”)
     */
    String getName();

    /**
     * Returns a shape generator for this board type
     */
    IShapeGenerator getShapeGenerator();

    /**
     * Returns default configuration for this board type
     */
    BoardGenerationConfig getDefaultConfig();

    default List<ICubeCoord> getCubeOrder(Set<ICubeCoord> coords, BoardGenerationConfig config, Random rng) {
        return List.copyOf(coords); // Ordering does not matter just take all cords and put them in a list.
    }

    // Optional hooks for tile selection
    default Optional<ITileType> chooseTile(ICubeCoord coord, BoardGenerationConfig config, Random rng) {
        return Optional.empty();
    }

    default Optional<Collection<Integer>> assignNumbers(ICubeCoord coord, BoardGenerationConfig config, Random rng) {
        return Optional.empty();
    }
}
