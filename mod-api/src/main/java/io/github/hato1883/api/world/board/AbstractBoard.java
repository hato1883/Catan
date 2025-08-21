package io.github.hato1883.api.world.board;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class representing a board of tiles.
 * Provides structure for managing board layout and accessing individual tiles.
 *
 * <h3>Key Responsibilities:</h3>
 * <ul>
 *     <li>Maintains board dimensions and tile storage</li>
 *     <li>Provides coordinate validation</li>
 *     <li>Offers tile access and grouping functionality</li>
 * </ul>
 */
public abstract class AbstractBoard implements IBoard {
    private String name;
    private Dimension dimensions;
    private final Map<ITilePosition, ITile> tiles = new HashMap<>();
    private final ITileGrid grid;
    private final Map<ITilePosition, ITileGrid> positionToGrid = new HashMap<>();
    private boolean isHeterogeneous = false;

    protected AbstractBoard(ITileGrid grid) {
        this.name = "Uninitialized Board";
        this.dimensions = new Dimension(0, 0, 0);
        this.grid = grid;
    }

    /**
     * Use this to register a custom grid for a specific tile position (for heterogeneous boards).
     * Call before adding the tile.
     */
    public void registerGridForPosition(ITilePosition pos, ITileGrid customGrid) {
        positionToGrid.put(pos, customGrid);
        isHeterogeneous = true;
    }

    /**
     * Returns the grid for a given tile. For homogeneous boards, returns the main grid.
     * For heterogeneous boards, returns the registered grid for the tile's position, or the main grid as fallback.
     */
    public ITileGrid getGridForTile(ITile tile) {
        if (isHeterogeneous) {
            return positionToGrid.getOrDefault(tile.getPosition(), grid);
        }
        return grid;
    }

    /**
     * Returns the grid for a given position. For homogeneous boards, returns the main grid.
     * For heterogeneous boards, returns the registered grid for the position, or the main grid as fallback.
     */
    public ITileGrid getGridForPosition(ITilePosition pos) {
        if (isHeterogeneous) {
            return positionToGrid.getOrDefault(pos, grid);
        }
        return grid;
    }

    /**
     * Returns the main grid if the board is homogeneous, or Optional.empty() if heterogeneous.
     */
    public Optional<ITileGrid> getGrid() {
        return isHeterogeneous ? Optional.empty() : Optional.of(grid);
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Dimension getDimensions() {
        return dimensions;
    }
    @Override
    public void setDimensions(Dimension dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public Collection<ITile> getTiles() {
        return Collections.unmodifiableCollection(tiles.values());
    }

    @Override
    public Map<ITileType, List<ITile>> getTilesGroupedByTileType() {
        return tiles.values().stream()
            .collect(Collectors.groupingBy(ITile::getType));
    }

    @Override
    public Optional<ITile> getTile(ITilePosition coord) {
        return Optional.ofNullable(tiles.get(coord));
    }

    @Override
    public int getTileCount() {
        return tiles.size();
    }

    @Override
    public Collection<ITile> getNeighbors(ITile tile) {
        ITileGrid tileGrid = getGrid().orElseGet(() -> getGridForTile(tile));
        List<ITile> neighbors = new ArrayList<>();
        for (ITilePosition neighborCoord : tileGrid.getNeighbors(tile.getPosition())) {
            ITile neighbor = tiles.get(neighborCoord);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    @Override
    public void addTile(ITile tile) {
        tiles.put(tile.getPosition(), tile);
    }
}
