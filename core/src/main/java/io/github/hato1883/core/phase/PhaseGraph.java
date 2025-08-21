package io.github.hato1883.core.phase;

import io.github.hato1883.api.world.phase.IGamePhase;
import io.github.hato1883.api.world.phase.IPhaseGraph;

import java.util.*;

/**
 * PhaseGraph models a directed acyclic graph (DAG) of game phases and their dependencies.
 * Mods and other modules can define the flow by adding phases and dependencies.
 * The graph is validated for cycles on each modification.
 */
public class PhaseGraph implements IPhaseGraph {
    private final Map<IGamePhase, Set<IGamePhase>> adjacency = new HashMap<>();

    /**
     * Adds a phase to the graph.
     */
    public void addPhase(IGamePhase phase) {
        adjacency.computeIfAbsent(phase, k -> new HashSet<>());
    }

    /**
     * Adds a dependency: phase depends on dependency (dependency must complete before phase).
     * Throws IllegalArgumentException if this introduces a cycle.
     */
    public void addDependency(IGamePhase phase, IGamePhase dependency) {
        addPhase(phase);
        addPhase(dependency);
        adjacency.get(phase).add(dependency);
        if (hasCycle()) {
            adjacency.get(phase).remove(dependency);
            throw new IllegalArgumentException("Adding this dependency would create a cycle");
        }
    }

    /**
     * Removes a phase and all its dependencies.
     */
    public void removePhase(IGamePhase phase) {
        adjacency.remove(phase);
        for (Set<IGamePhase> deps : adjacency.values()) {
            deps.remove(phase);
        }
    }

    /**
     * Returns a topological sort of the phases (execution order).
     * Throws IllegalStateException if the graph contains a cycle.
     */
    public List<IGamePhase> getExecutionOrder() {
        List<IGamePhase> result = new ArrayList<>();
        Set<IGamePhase> visited = new HashSet<>();
        Set<IGamePhase> stack = new HashSet<>();
        for (IGamePhase phase : adjacency.keySet()) {
            if (!visited.contains(phase)) {
                dfs(phase, visited, stack, result);
            }
        }
        return result;
    }

    private void dfs(IGamePhase phase, Set<IGamePhase> visited, Set<IGamePhase> stack, List<IGamePhase> result) {
        if (stack.contains(phase)) {
            throw new IllegalStateException("Cycle detected in phase graph");
        }
        if (visited.contains(phase)) return;
        stack.add(phase);
        for (IGamePhase dep : adjacency.getOrDefault(phase, Collections.emptySet())) {
            dfs(dep, visited, stack, result);
        }
        stack.remove(phase);
        visited.add(phase);
        result.add(phase);
    }

    /**
     * Checks if the graph contains a cycle.
     */
    public boolean hasCycle() {
        Set<IGamePhase> visited = new HashSet<>();
        Set<IGamePhase> stack = new HashSet<>();
        for (IGamePhase phase : adjacency.keySet()) {
            if (hasCycleDfs(phase, visited, stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycleDfs(IGamePhase phase, Set<IGamePhase> visited, Set<IGamePhase> stack) {
        if (stack.contains(phase)) return true;
        if (visited.contains(phase)) return false;
        visited.add(phase);
        stack.add(phase);
        for (IGamePhase dep : adjacency.getOrDefault(phase, Collections.emptySet())) {
            if (hasCycleDfs(dep, visited, stack)) return true;
        }
        stack.remove(phase);
        return false;
    }
}
