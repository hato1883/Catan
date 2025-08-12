package io.github.hato1883.game.board;

import io.github.hato1883.api.game.board.ITileType;
import io.github.hato1883.registries.RegistryManager;
import io.github.hato1883.registries.TileTypeRegistry;

import java.util.*;

public class CatanBoardGenerator {

    private static final List<String> DEFAULT_TILE_IDS = List.of(
        "catan:mountain",    // top-left corner
        "catan:wheat_field",
        "catan:grassland",
        "catan:clay_patch",
        "catan:wheat_field",
        "catan:mountain",
        "catan:forest",
        "catan:forest",
        "catan:wheat_field",
        "catan:grassland",
        "catan:clay_patch",
        "catan:forest",
        "catan:mountain",
        "catan:forest",
        "catan:grassland",
        "catan:grassland",
        "catan:wheat_field",
        "catan:clay_patch",
        "catan:desert"
    );

    private static final List<Integer> DEFAULT_NUMBERS = List.of(
        5, 2, 6, 3, 8, 10, 9, 12, 11,
        4, 8, 10, 9, 4, 5, 6, 3, 11
    );

    public static Map<CubeCoord, TileAssignment> generateDefault(Random rng) {
        List<String> tiles = new ArrayList<>(DEFAULT_TILE_IDS);
        if (rng != null)
            Collections.shuffle(tiles, rng);

        // Find desert index and remove it from number token assignment
        int desertIndex = tiles.indexOf("catan:desert");

        // Generate all CubeCords that need to be filled
        Set<CubeCoord> coords = HexBoardUtils.generateHexCoords(3);

        // Generate the spiral order in which to add NumberToken in.
        List<CubeCoord> spiral = SpiralUtils.spiralOrder(coords, rng);

        return getTileAssignmentMap(spiral, tiles, desertIndex);
    }

    private static Map<CubeCoord, TileAssignment> getTileAssignmentMap(List<CubeCoord> spiral, List<String> tiles, int desertIndex) {
        // A copy of default tokens order.
        List<Integer> numbers = new ArrayList<>(DEFAULT_NUMBERS);

        Map<CubeCoord, TileAssignment> result = new LinkedHashMap<>();

        int numIndex = 0;
        for (int i = 0; i < spiral.size(); i++) {
            CubeCoord coord = spiral.get(i);
            ITileType tile = RegistryManager.getTileTypeRegistry().get(tiles.get(i)).orElseThrow();

            Collection<Integer> prodNumbers;
            if (i == desertIndex) {
                prodNumbers = Collections.emptyList();
            } else {
                prodNumbers = List.of(numbers.get(numIndex++));
            }

            result.put(coord, new TileAssignment(tile, prodNumbers));
        }
        return result;
    }

    public record TileAssignment(ITileType tile, Collection<Integer> productionNumbers) {}
}

