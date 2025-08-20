package io.github.hato1883.core.modloading.dependency;

import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.mod.load.dependency.ModDependency;
import io.github.hato1883.api.mod.load.dependency.ModDependencyException;

import java.nio.file.Path;
import java.util.*;

public class DependencyResolver implements IDependencyResolver {

    public Map<ModMetadata, Path> resolveLoadOrder(Map<ModMetadata, Path> mods) throws ModDependencyException {
        Map<String, ModMetadata> modMap = new HashMap<>(mods.size());
        for (ModMetadata mod : mods.keySet()) modMap.put(mod.id(), mod);

        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (ModMetadata mod : mods.keySet()) {
            graph.putIfAbsent(mod.id(), new HashSet<>());
            inDegree.putIfAbsent(mod.id(), 0);

            for (ModDependency dep : mod.dependencies()) {
                ModMetadata target = modMap.get(dep.modId());
                if (target == null) {
                    if (!dep.optional())
                        throw new ModDependencyException("Missing required dependency: " + dep.modId());
                    continue;
                }
                if (!dep.versionConstraint().matches(target.version())) {
                    throw new ModDependencyException(
                        "Mod '" + mod.id() + "' requires " + dep.modId() + " " + dep.versionConstraint() +
                            " but found " + target.version()
                    );
                }

                graph.putIfAbsent(dep.modId(), new HashSet<>());
                graph.get(dep.modId()).add(mod.id());
                inDegree.put(mod.id(), inDegree.getOrDefault(mod.id(), 0) + 1);
                inDegree.putIfAbsent(dep.modId(), inDegree.getOrDefault(dep.modId(), 0));
            }
        }

        PriorityQueue<ModMetadata> queue = new PriorityQueue<>(Comparator
            // Invert LoadPriority weight by negating, Sorting is made from lowest -> highest
            .<ModMetadata>comparingInt(m -> -m.loadPriority().getWeight())
            .thenComparing(ModMetadata::id)
        );

        for (String id : inDegree.keySet()) if (inDegree.get(id) == 0) queue.add(modMap.get(id));

        // TODO: Revaluate usage of map, maybe create a record to pack Path into ModMetadata.
        Map<ModMetadata, Path> loadOrder = new LinkedHashMap<>(mods.size());
        while (!queue.isEmpty()) {
            ModMetadata modMetadata = queue.poll();
            loadOrder.put(modMetadata, mods.get(modMetadata));
            for (String childId : graph.getOrDefault(modMetadata.id(), Collections.emptySet())) {
                inDegree.put(childId, inDegree.get(childId) - 1);
                if (inDegree.get(childId) == 0) queue.add(modMap.get(childId));
            }
        }

        if (loadOrder.size() != mods.size())
            throw new ModDependencyException("Cyclic dependency detected or load ordering failed.");

        return loadOrder;
    }
}

