package io.github.hato1883.api.world.phase;

import java.util.List;

/**
 * Interface for a directed acyclic graph (DAG) of game phases and their dependencies.
 */
public interface IPhaseGraph {
    /**
     * Adds a phase to the graph.
     */
    void addPhase(IGamePhase phase);

    /**
     * Adds a dependency: phase depends on dependency (dependency must complete before phase).
     * Throws IllegalArgumentException if this introduces a cycle.
     */
    void addDependency(IGamePhase phase, IGamePhase dependency);

    /**
     * Removes a phase and all its dependencies.
     */
    void removePhase(IGamePhase phase);

    /**
     * Returns a topological sort of the phases (execution order).
     * Throws IllegalStateException if the graph contains a cycle.
     */
    List<IGamePhase> getExecutionOrder();

    /**
     * Checks if the graph contains a cycle.
     */
    boolean hasCycle();
}

