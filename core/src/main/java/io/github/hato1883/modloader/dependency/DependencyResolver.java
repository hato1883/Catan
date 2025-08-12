package io.github.hato1883.modloader.dependency;

import io.github.hato1883.modloader.ModMetadata;
import java.util.*;

/**
 * Resolves mod load order based on dependencies and load priority.
 * Ensures:
 * - Mods load after their dependencies.
 * - High-priority mods load before normal/low within same dependency level.
 * - BaseGame mod is loaded first unless a high-priority mod without BaseGame dependency exists.
 */
public class DependencyResolver {

    public static List<ModMetadata> resolveLoadOrder(List<ModMetadata> mods) throws ModDependencyException {
        Map<String, ModMetadata> modMap = new HashMap<>();
        for (ModMetadata mod : mods) {
            modMap.put(mod.id(), mod);
        }

        // Build dependency graph and track in-degree for topological sorting
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (ModMetadata mod : mods) {
            graph.putIfAbsent(mod.id(), new HashSet<>());
            inDegree.putIfAbsent(mod.id(), 0);

            for (ModDependency dep : mod.dependencies()) {
                String depId = dep.modId();
                ModMetadata target = modMap.get(depId);
                if (target == null) {
                    if (!dep.optional()) {
                        throw new ModDependencyException("Missing required dependency: " + depId);
                    }
                    continue; // optional and missing is OK
                }

                if (!dep.constraint().matches(target.version())) {
                    throw new ModDependencyException(
                        "Mod '" + mod.id() + "' requires " + depId + " " + dep.constraint() +
                            " but found " + target.version()
                    );
                }

                // Build graph edge: depId -> mod.id() (mod depends on depId)
                graph.putIfAbsent(depId, new HashSet<>());
                graph.get(depId).add(mod.id());

                inDegree.put(mod.id(), inDegree.getOrDefault(mod.id(), 0) + 1);
                inDegree.putIfAbsent(depId, inDegree.getOrDefault(depId, 0));
            }
        }

        // Topological sort with priority (lowest priority value first)
        PriorityQueue<ModMetadata> queue = new PriorityQueue<>(Comparator
            .<ModMetadata>comparingInt(m -> priorityValue(m.loadPriority()))
            .thenComparing(ModMetadata::id)
        );

        for (String id : inDegree.keySet()) {
            if (inDegree.get(id) == 0) {
                queue.add(modMap.get(id));
            }
        }

        List<ModMetadata> loadOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            ModMetadata mod = queue.poll();
            loadOrder.add(mod);

            for (String childId : graph.getOrDefault(mod.id(), Collections.emptySet())) {
                inDegree.put(childId, inDegree.get(childId) - 1);
                if (inDegree.get(childId) == 0) {
                    queue.add(modMap.get(childId));
                }
            }
        }

        if (loadOrder.size() != mods.size()) {
            throw new ModDependencyException("Cyclic dependency detected or load ordering failed.");
        }

        return loadOrder;
    }


    private static int priorityValue(LoadPriority priority) {
        return switch (priority) {
            case HIGH -> 0;
            case NORMAL -> 1;
            case LOW -> 2;
            default -> throw new IllegalArgumentException("Unknown priority: " + priority);
        };
    }
}

