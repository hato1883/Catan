package io.github.hato1883.api.world.board;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class for Catan-like boards, supporting structures and production logic.
 * Modders should extend this for Catan-style games.
 */
public abstract class AbstractCatanBoard extends AbstractBoard implements IStructurableBoard, IProductionBoard {
    private final List<IStructure> structures = new ArrayList<>();

    public AbstractCatanBoard(ITileGrid grid) {
        super(grid);
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
    public void triggerProductionForRoll(int rolledNumber) {
        for (ITile tile : getTiles()) {
            if (tile instanceof IProductionTile prodTile) {
                prodTile.triggerProduction(rolledNumber);
            }
        }
    }

    /**
     * Adds a structure to the board.
     */
    protected void addStructure(IStructure structure) {
        structures.add(structure);
    }
}
