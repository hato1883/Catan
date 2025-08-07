package io.github.hato1883.game.board.types;

import io.github.hato1883.game.board.*;

import java.util.List;
import java.util.Set;

import static io.github.hato1883.game.board.BoardUtils.generateProceduralTilePool;
import static io.github.hato1883.game.board.BoardUtils.getStandardCatanTilePool;

/**
 * A rectangular board layout using cube coordinates.
 * Typically used for debugging or testing non-island tile layouts.
 *
 * <p>Note: This board supports a rectangular view of cube coordinates
 * by constraining x and y, and computing z as -x - y.</p>
 *
 * <h3>Coordinate System:</h3>
 * <ul>
 *     <li>q (x-axis) ranges from 0 to width-1</li>
 *     <li>r (y-axis) ranges from 0 to height-1</li>
 *     <li>s (z-axis) is computed as -q - r to maintain cube coordinate constraints</li>
 * </ul>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link Board} - Base class for all board implementations</li>
 *     <li>{@link CubeCoord} - Coordinate system used by this board</li>
 * </ul>
 */
public class RectangularBoard extends Board {

    /**
     * Constructs a rectangular board of the given width and height.
     *
     * @param width number of tiles along x-axis (must be positive)
     * @param height number of tiles along y-axis (must be positive)
     * @throws IllegalArgumentException if width or height are non-positive
     */
    public RectangularBoard(int width, int height) {
        super(width, height, width + height - 1, width*height);
        // TODO: Add validation for positive width/height
    }

    /**
     * Generates and populates all tiles for this board.
     *
     * <p>This implementation:
     * <ul>
     *     <li>Clears any cached tile groupings</li>
     *     <li>Gets all valid coordinates for this board</li>
     *     <li>Uses standard Catan tile distribution for 19-tile boards</li>
     *     <li>Generates procedural distribution for all other sizes</li>
     *     <li>Populates the tile map</li>
     * </ul>
     *
     * <h3>Tile Generation Rules:</h3>
     * <ul>
     *     <li>For exactly 19 coordinates: uses standard Catan distribution</li>
     *     <li>For all other cases: generates procedural distribution</li>
     * </ul>
     *
     * @see BoardUtils#getStandardCatanTilePool(Set, HexTileFactory)
     * @see BoardUtils#generateProceduralTilePool(Set, HexTileFactory)
     */
    @Override
    protected void generateTiles() {
        // Clear grouped tile cache
        invalidateGroupedCache();

        // Get All valid coords that need to be populated
        Set<CubeCoord> coords = this.getAllValidCoordinates();

        // Create Tiles
        List<HexTile> tilePool = coords.size() == 19
            ? getStandardCatanTilePool(coords, (r, t, c) -> new HexTile(c, r, t))
            : generateProceduralTilePool(coords, (r, t, c) -> new HexTile(c, r, t));

        // Directly put into map
        for (HexTile tile : tilePool) {
            tiles.put(tile.getCoord(), tile);
        }
    }

    /**
     * Validates whether a coordinate is within this board's bounds.
     *
     * @param coord the cube coordinate to validate
     * @return true if:
     * <ul>
     *     <li>q (x) is within [0, xExtent)</li>
     *     <li>r (y) is within [0, yExtent)</li>
     *     <li>s (z) equals -q - r (maintaining cube coordinate constraint)</li>
     * </ul>
     *
     * <h3>Example:</h3>
     * <pre>{@code
     * RectangularBoard board = new RectangularBoard(3, 3);
     * boolean valid = board.isValidCoordinate(new CubeCoord(1, 1, -2)); // returns true
     * }</pre>
     */
    @Override
    protected boolean isValidCoordinate(CubeCoord coord) {
        // Accept everything inside x/y bounds with matching z
        return coord.q >= 0 && coord.q < xExtent &&
            coord.r >= 0 && coord.r < yExtent &&
            coord.s == -coord.q - coord.r;
    }
}
