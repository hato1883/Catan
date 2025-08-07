package io.github.hato1883.game.board;

import io.github.hato1883.game.board.elements.BoardElementFactory;
import io.github.hato1883.game.resource.ResourceType;

import java.util.*;

/**
 * Abstract base class representing a grid of hexagonal tiles using cube coordinates.
 * Provides structure for managing board layout and accessing individual tiles.
 *
 * <h3>Key Responsibilities:</h3>
 * <ul>
 *     <li>Maintains board dimensions and tile storage</li>
 *     <li>Provides coordinate validation</li>
 *     <li>Offers tile access and grouping functionality</li>
 * </ul>
 */
public abstract class Board {
    protected final int xExtent, yExtent, zExtent;
    protected final int tileCount;
    protected final Map<CubeCoord, HexTile> tiles;
    protected final BoardElementFactory elementFactory;
    private Map<ResourceType, List<HexTile>> groupedCache = null;

    /**
     * Constructs a new board with given dimensions in cube coordinate space.
     * <p>
     * Constructor does NOT call {@link #generateTiles()}. Subclasses must call this themselves.
     *
     * @param xExtent maximum number of x-axis units (must be > 0)
     * @param yExtent maximum number of y-axis units (must be > 0)
     * @param zExtent maximum number of z-axis units (must be > 0)
     * @param tileCount total number of tiles on the board (must be > 0)
     * @throws IllegalArgumentException if any extent or tileCount is less than or equal to 0
     *
     * <h3>Initialization Flow:</h3>
     * <ol>
     *     <li>Validates parameters</li>
     *     <li>Initializes storage structures</li>
     *     <li>Calls {@link #initializeBoard()}</li>
     * </ol>
     */
    public Board(int xExtent, int yExtent, int zExtent, int tileCount) {
        if (xExtent <= 0) throw new IllegalArgumentException("Invalid xExtent: xExtent must be greater than 0");
        if (yExtent <= 0) throw new IllegalArgumentException("Invalid yExtent: yExtent must be greater than 0");
        if (zExtent <= 0) throw new IllegalArgumentException("Invalid zExtent: zExtent must be greater than 0");
        if (tileCount <= 0) throw new IllegalArgumentException("Invalid tileCount: tileCount must be greater than 0");

        this.tiles = new HashMap<>();
        this.xExtent = xExtent;
        this.yExtent = yExtent;
        this.zExtent = zExtent;
        this.tileCount = tileCount;
        this.elementFactory = new BoardElementFactory();

        initializeBoard();
    }

    /**
     * Called during construction to populate the board with tiles.
     * Subclasses must define how tiles are arranged.
     *
     * <h3>Implementation Notes:</h3>
     * <ul>
     *     <li>Must populate the {@code tiles} map</li>
     *     <li>Should only create valid coordinates (as defined by {@link #isValidCoordinate(CubeCoord)})</li>
     *     <li>Typically creates exactly {@code tileCount} tiles</li>
     * </ul>
     */
    protected abstract void generateTiles();

    /**
     * Initializes the board by generating tiles and building the board graph.
     *
     * <h3>Initialization Sequence:</h3>
     * <ol>
     *     <li>Calls {@link #generateTiles()} to create tiles</li>
     *     <li>Builds the board graph via {@link BoardUtils#buildBoardGraph(Board, BoardElementFactory)}</li>
     * </ol>
     */
    protected final void initializeBoard() {
        generateTiles();
        BoardUtils.buildBoardGraph(this, elementFactory);
    }

    /**
     * Checks whether the given coordinate is valid for the board shape.
     *
     * @param coord the cube coordinate to validate
     * @return true if the coordinate is part of the board shape
     *
     * <h3>Contract:</h3>
     * <ul>
     *     <li>Must be consistent with the board's geometric rules</li>
     *     <li>Should return false for coordinates outside the board's extents</li>
     * </ul>
     */
    protected abstract boolean isValidCoordinate(CubeCoord coord);

    /**
     * Returns the tile at the given coordinate, or null if no tile exists.
     *
     * @param coord cube coordinate to look up
     * @return the {@link HexTile} at that position, or null if none exists
     *
     * <h3>Performance:</h3>
     * O(1) time complexity (uses HashMap lookup)
     */
    public HexTile getTile(CubeCoord coord) {
        return tiles.get(coord);
    }

    /**
     * Gets all tiles currently on the board.
     *
     * @return an unmodifiable collection of all {@link HexTile} objects
     *
     * <h3>Note:</h3>
     * The collection is a view and will reflect changes to the underlying board.
     */
    public Collection<HexTile> getTiles() {
        return Collections.unmodifiableCollection(tiles.values());
    }

    /**
     * Returns all coordinates of tiles that have been placed on the board.
     * <p>
     * This method reflects the current state of the board after tile generation, and is limited to
     * the coordinates that have actually been initialized via {@link #generateTiles()}.
     * <p>
     * Use this method when you are interested in the *current, populated* tiles on the board —
     * for rendering, gameplay logic, or interaction with already-existing tiles.
     * <p>
     * <strong>Note:</strong> This method does <em>not</em> compute all theoretically valid coordinates
     * for the board shape. If you need that (e.g., for procedural generation, validation, or AI planning),
     * use {@link #getAllValidCoordinates()} instead.
     *
     * @return set of all currently placed {@link CubeCoord}s on the board
     *
     * @see #getAllValidCoordinates()
     */
    public Set<CubeCoord> getAllCoords() {
        return tiles.keySet();
    }

    /**
     * Computes all cube coordinates within the board's bounding cube
     * that are considered valid by this board's shape rules.
     *
     * <p>
     * Each subclass defines what constitutes a "valid" coordinate by implementing
     * {@link #isValidCoordinate(CubeCoord)}. This method systematically iterates over
     * the full cube defined by the board's {@code xExtent}, {@code yExtent}, and {@code zExtent},
     * and returns only those that are valid.
     *
     * <p>
     * Note: The cube includes negative values depending on the board type (e.g. island-shaped boards).
     *
     * <h3>Example usage:</h3>
     * <pre>{@code
     *     Board board = new HexIslandBoard(2);
     *     Set<CubeCoord> validCoords = board.getAllValidCoordinates();
     *     validCoords.forEach(System.out::println);
     * }</pre>
     *
     * @see Board#getAllCoords()
     *
     * @return an unmodifiable set of all valid {@link CubeCoord}s on the board
     */
    public Set<CubeCoord> getAllValidCoordinates() {
        Set<CubeCoord> validCoords = new HashSet<>();

        for (int x = -xExtent; x <= xExtent; x++) {
            for (int y = -yExtent; y <= yExtent; y++) {
                int z = -x - y;
                if (Math.abs(z) > zExtent) {
                    continue;
                }
                CubeCoord coord = new CubeCoord(x, y, z);
                if (this.isValidCoordinate(coord)) {
                    validCoords.add(coord);
                }
            }
        }

        return Collections.unmodifiableSet(validCoords);
    }

    /**
     * Groups all tiles on the board by their resource type, preserving their CubeCoord.
     * <p>
     * Results are cached until {@link #invalidateGroupedCache()} is called.
     *
     * @return an unmodifiable map where each key is a ResourceType and the value is a List of corresponding HexTiles
     *
     * <h3>Performance:</h3>
     * <ul>
     *     <li>First call: O(n) where n is number of tiles</li>
     *     <li>Subsequent calls: O(1) (returns cached result)</li>
     * </ul>
     */
    public Map<ResourceType, List<HexTile>> groupTilesByResource() {
        if (groupedCache != null) return groupedCache;
        Map<ResourceType, List<HexTile>> grouped = new EnumMap<>(ResourceType.class);

        for (HexTile tile : tiles.values()) {
            CubeCoord coord = tile.getCoord();
            ResourceType type = tile.getResourceType();

            grouped
                .computeIfAbsent(type, k -> new ArrayList<>())
                .add(tile);
        }

        groupedCache = Collections.unmodifiableMap(grouped);
        return groupedCache;
    }

    /**
     * Invalidates the cached tile grouping, forcing a recomputation on next call to {@link #groupTilesByResource()}.
     *
     * <h3>When to Call:</h3>
     * <ul>
     *     <li>After modifying the board's tile composition</li>
     *     <li>After changing tile resource types</li>
     * </ul>
     */
    public void invalidateGroupedCache() {
        groupedCache = null;
    }

    /**
     * @return the maximum x extent of the board
     */
    public int getXExtent() { return xExtent; }

    /**
     * @return the maximum y extent of the board
     */
    public int getYExtent() { return yExtent; }

    /**
     * @return the maximum z extent of the board
     */
    public int getZExtent() { return zExtent; }

    /**
     * @return the total number of tiles on the board
     */
    public int getTileCount() { return tileCount; }

    public BoardElementFactory getElementFactory() {
        return elementFactory;
    }
}
