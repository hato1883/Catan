package io.github.hato1883.game.board.types;

import io.github.hato1883.game.board.Board;
import io.github.hato1883.game.board.BoardUtils;
import io.github.hato1883.game.board.CubeCoord;
import io.github.hato1883.game.board.HexTile;
import io.github.hato1883.game.resource.ResourceType;

import java.util.List;
import java.util.Set;

import static io.github.hato1883.game.board.BoardUtils.generateProceduralTilePool;
import static io.github.hato1883.game.board.BoardUtils.getStandardCatanTilePool;

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
 *     <li>{@link Board} - Base class for all board implementations</li>
 *     <li>{@link CubeCoord} - Cube coordinate implementation</li>
 *     <li>{@link HexTile} - Hexagonal tile implementation</li>
 * </ul>
 */
public class HexIslandBoard extends Board {
    private final int radius;

    /**
     * Constructs a hexagonal island board with the specified radius.
     * <p>
     * The radius defines how many tiles out from the center the island reaches.
     * A radius of 1 yields 1 tile. A radius of 2 yields a 3x3x3 space with 7 tiles, etc.
     *
     * @param radius the radius of the island (must be > 0)
     * @throws IllegalArgumentException if radius is less than or equal to 0
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * Board board = new HexIslandBoard(2);
     * HexTile tile = board.getTile(new CubeCoord(1, 1, -2));
     * }</pre>
     */
    public HexIslandBoard(int radius) {
        super(radius*2 - 1, radius*2 - 1, radius*2 - 1, (1 + 3 * (radius - 1) * radius)); // 0 to 5 exclusive for x, y, z
        if (radius <= 0) throw new IllegalArgumentException("Invalid radius: radius must be greater than 0");
        this.radius = radius;
        this.initializeBoard();
    }
    /**
     * Generates all valid hex tiles in cube coordinate space
     * that fall within the defined radius from the center.
     * <p>
     * This populates the {@code tiles} map with valid {@link CubeCoord} keys and {@link HexTile} values.
     * Uses standard Catan tile distribution when radius is 3, otherwise generates tiles procedurally.
     *
     * <h3>Implementation Details:</h3>
     * <ul>
     *     <li>Invalidates any cached tile groupings</li>
     *     <li>For radius 3: Uses standard Catan resource distribution</li>
     *     <li>For other radii: generates a balanced procedural distribution</li>
     *     <li>Number tokens are randomized via {@link BoardUtils#generateNumberTokens(int)}</li>
     * </ul>
     *
     * <h3>TODO:</h3>
     * <ul>
     *     <li>Implement 6/8 adjacency rule - red number tokens (6 and 8) should not be placed on adjacent tiles</li>
     *     <li>Add support for custom tile distributions</li>
     * </ul>
     *
     * <h3>FIXME:</h3>
     * <ul>
     *     <li>Validate that tilePool size matches coords size before population</li>
     * </ul>
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link BoardUtils#generateNumberTokens(int)} - Number token generation</li>
     *     <li>{@link BoardUtils#getStandardCatanTilePool} - Standard Catan setup</li>
     * </ul>
     */
    @Override
    protected void generateTiles() {
        invalidateGroupedCache();
        Set<CubeCoord> coords = this.getAllValidCoordinates();

        List<HexTile> tilePool = radius == 3
            ? getStandardCatanTilePool(coords, (r, t, c) -> new HexTile(c, r, t))
            : generateProceduralTilePool(coords, (r, t, c) -> new HexTile(c, r, t));

        // FIXME: Should validate that tilePool size matches coords size
        // Directly put into map
        for (HexTile tile : tilePool) {
            tiles.put(tile.getCoord(), tile);
        }
    }

    /**
     * Validates whether the provided coordinate is part of the island shape.
     * It uses the cube coordinate distance formula.
     *
     * @param coord the cube coordinate to check
     * @return true if the coordinate is within {@code radius} of the center tile
     *
     * <h3>Mathematical Basis:</h3>
     * Uses the cube coordinate distance formula: max(|x|, |y|, |z|)
     *
     * <h3>Example:</h3>
     * <pre>{@code
     * HexIslandBoard board = new HexIslandBoard(2);
     * boolean valid = board.isValidCoordinate(new CubeCoord(1, 1, -2)); // true
     * }</pre>
     */
    @Override
    protected boolean isValidCoordinate(CubeCoord coord) {
        return coord.distance() < radius; // Radius check
    }
}
