package io.github.hato1883.core.game.board;

import io.github.hato1883.api.game.board.*;

import java.util.*;
import java.util.stream.Collectors;

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
public abstract class AbstractIBoardImpl implements IBoard {

    private String name;
    private Dimension dimensions;
    private final Map<ICubeCoord, IHexTile> tiles = new HashMap<>();
    private final List<IStructure> structures = new ArrayList<>();

    protected AbstractIBoardImpl() {
        this.name = "Uninitialized Board";
        this.dimensions = new Dimension(0, 0, 0);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Dimension getDimensions() {
        return dimensions;
    }
    public void setDimensions(Dimension dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public Collection<IHexTile> getTiles() {
        return Collections.unmodifiableCollection(tiles.values());
    }

    @Override
    public Map<ITileType, List<IHexTile>> getTilesGroupedByTileType() {
        return tiles.values().stream()
            .collect(Collectors.groupingBy(IHexTile::getTileType));
    }

    @Override
    public Optional<IHexTile> getTile(ICubeCoord coord) {
        return Optional.ofNullable(tiles.get(coord));
    }

    @Override
    public int getTileCount() {
        return tiles.size();
    }

    @Override
    public Collection<IStructure> getStructures() {
        return Collections.unmodifiableList(structures);
    }

    @Override
    public Collection<IBuilding> getBuildings() {
        return structures.stream()
            .filter(s -> s instanceof IBuilding)
            .map(s -> (IBuilding) s)
            .toList();
    }

    @Override
    public Collection<IRoad> getRoads() {
        return structures.stream()
            .filter(s -> s instanceof IRoad)
            .map(s -> (IRoad) s)
            .toList();
    }

    @Override
    public Collection<IPort> getPorts() {
        return structures.stream()
            .filter(s -> s instanceof IPort)
            .map(s -> (IPort) s)
            .toList();
    }

    @Override
    public Map<IBuildingType, List<IBuilding>> getBuildingsGroupedByType() {
        return structures.stream()
            .filter(s -> s instanceof IBuilding)
            .map(s -> (IBuilding) s)
            .collect(Collectors.groupingBy(IBuilding::getType));
    }

    @Override
    public Map<IRoadType, List<IRoad>> getRoadsGroupedByType() {
        return structures.stream()
            .filter(s -> s instanceof IRoad)
            .map(s -> (IRoad) s)
            .collect(Collectors.groupingBy(IRoad::getType));
    }

    @Override
    public Map<IPortType, List<IPort>> getPortsGroupedByType() {
        return structures.stream()
            .filter(s -> s instanceof IPort)
            .map(s -> (IPort) s)
            .collect(Collectors.groupingBy(IPort::getType));
    }

    @Override
    public Collection<IHexTile> getNeighbors(IHexTile tile) {
        // default hex neighbor calculation, can be overridden
        List<IHexTile> neighbors = new ArrayList<>();
        ICubeCoord c = tile.getCoord();
        for (ICubeCoord neighborCoord : c.getNeighbors()) {
            IHexTile neighbor = tiles.get(neighborCoord);
            if (neighbor != null) neighbors.add(neighbor);
        }
        return neighbors;
    }

    /**
     * Called by the game engine when a roll occurs.
     */
    public void triggerProductionForRoll(int rolledNumber) {
        for (IHexTile tile : tiles.values()) {
            tile.triggerProduction(rolledNumber);
        }
    }

    /**
     * Adds a tile to the board.
     */
    public void addTile(IHexTile tile) {
        tiles.put(tile.getCoord(), tile);
    }

    /**
     * Adds a structure to the board.
     */
    protected void addStructure(IStructure structure) {
        structures.add(structure);
    }

    // Subclasses must implement methods like getName(), getDimensions()
}
