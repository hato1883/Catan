package io.github.hato1883.game.board.types;

import io.github.hato1883.api.game.board.Dimension;
import io.github.hato1883.api.game.board.IHexTile;
import io.github.hato1883.game.board.*;
import io.github.hato1883.game.board.CatanBoardGenerator.TileAssignment;

import java.util.Map;

/**
 * A hexagonal island board implementation using cube coordinates.
 * <p>
 * The board generates a hex-shaped map centered at (0,0,0) with the specified radius.
 * Tiles are generated either procedurally or using the standard Catan distribution (for radius 3).
 *
 * <h3>Coordinate System:</h3>
 * Uses cube coordinates (x, y, z) where x + y + z = 0.
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link AbstractBoard} - Base class for all board implementations</li>
 *     <li>{@link CubeCoord} - Cube coordinate implementation</li>
 *     <li>{@link HexTile} - Hexagonal tile implementation</li>
 * </ul>
 */
public class StandardBoard extends AbstractBoard {

    /* Default board radius */
    private final static int radius = 3;

    public StandardBoard() {
        Map<CubeCoord, TileAssignment> layout = CatanBoardGenerator.generateDefault(null);

        for (var entry : layout.entrySet()) {
            CubeCoord coord = entry.getKey();
            TileAssignment data = entry.getValue();

            IHexTile tile = HexTileFactory.createTile(
                data.tile(),
                coord,
                data.productionNumbers()
            );
            addTile(tile);
        }
    }

    @Override
    public String getName() {
        return "Settlers of Catan Classic Board";
    }

    @Override
    public Dimension getDimensions() {
        // Provide bounding box or shape info
        int width = 2 * radius - 1;
        int height = 2 * radius - 1;
        return new Dimension(width, height);
    }
}

