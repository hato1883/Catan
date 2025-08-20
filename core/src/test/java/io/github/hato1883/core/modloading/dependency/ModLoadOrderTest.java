package io.github.hato1883.core.modloading.dependency;

import io.github.hato1883.api.mod.load.LoadPriority;
import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.dependency.ModDependency;
import io.github.hato1883.api.mod.load.dependency.ModDependencyException;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ModLoadOrderResolver}.
 * <p>
 * These tests verify that the mod load order is correctly resolved based on mod dependencies and load priorities.
 * The scenarios include single mods, dependency chains, multiple mods with different priorities, and more.
 */
class ModLoadOrderTest {

    /**
     * The resolver instance used for each test.
     */
    private ModLoadOrderResolver resolver;

    /**
     * Sets up a new resolver before each test.
     */
    @BeforeEach
    void setUp() {
        resolver = new ModLoadOrderResolver();
    }

    /**
     * Tests for basic mod load order functionality, such as single mods, dependency chains, and multiple mods with no dependencies.
     */
    @Nested
    @DisplayName("Basic Functionality")
    class BasicFunctionality {

        /**
         * Verifies that a single mod with no dependencies is loaded correctly.
         */
        @Test
        @DisplayName("Should handle single mod with no dependencies")
        void singleModNoDependencies() throws ModDependencyException {
            ModMetadata mod = createMod("single", "1.0", LoadPriority.NORMAL);
            Map<ModMetadata, Path> input = Map.of(mod, path("single.jar"));

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);

            assertEquals(1, result.size());
            assertTrue(result.containsKey(mod));
            assertEquals(List.of("single"), getLoadOrderIds(result));
        }

        /**
         * Verifies that a basic dependency chain is respected in the load order.
         */
        @Test
        @DisplayName("Should respect basic dependency chain")
        void basicDependencyChain() throws ModDependencyException {
            ModMetadata base = createMod("base", "1.0", LoadPriority.LOW);
            ModMetadata high = createMod("high", "1.0", LoadPriority.HIGH,
                dependency("base", "1.0", false));

            Map<ModMetadata, Path> input = Map.of(
                base, path("base.jar"),
                high, path("high.jar")
            );

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);
            List<String> loadOrder = getLoadOrderIds(result);

            assertEquals(2, result.size());
            assertEquals("base", loadOrder.get(0));
            assertEquals("high", loadOrder.get(1));
        }

        /**
         * Verifies that multiple mods with no dependencies are loaded in order of their priorities.
         */
        @Test
        @DisplayName("Should handle multiple mods with no dependencies")
        void multipleMods() throws ModDependencyException {
            ModMetadata high = createMod("high", "1.0", LoadPriority.HIGH);
            ModMetadata normal = createMod("normal", "1.0", LoadPriority.NORMAL);
            ModMetadata low = createMod("low", "1.0", LoadPriority.LOW);

            Map<ModMetadata, Path> input = Map.of(
                high, path("high.jar"),
                normal, path("normal.jar"),
                low, path("low.jar")
            );

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);
            List<String> loadOrder = getLoadOrderIds(result);

            assertEquals(3, result.size());
            assertEquals("high", loadOrder.get(0));
            assertEquals("normal", loadOrder.get(1));
            assertEquals("low", loadOrder.get(2));
        }
    }

    /**
     * Tests for correct ordering based on mod load priorities.
     */
    @Nested
    @DisplayName("Priority Ordering")
    class PriorityOrdering {

        /**
         * Verifies that mods are loaded in the order HIGH > NORMAL > LOW when there are no dependencies.
         */
        @Test
        @DisplayName("Should prioritize HIGH over NORMAL over LOW")
        void priorityOrdering() throws ModDependencyException {
            ModMetadata base = createMod("base", "1.0", LoadPriority.LOW);
            ModMetadata high = createMod("high", "1.0", LoadPriority.HIGH,
                dependency("base", "1.0", false));
            ModMetadata normal = createMod("normal", "1.0", LoadPriority.NORMAL,
                dependency("base", "1.0", false));

            Map<ModMetadata, Path> input = Map.of(
                base, path("base.jar"),
                high, path("high.jar"),
                normal, path("normal.jar")
            );

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);
            List<String> loadOrder = getLoadOrderIds(result);

            assertEquals("base", loadOrder.get(0));
            assertEquals("high", loadOrder.get(1)); // HIGH priority
            assertEquals("normal", loadOrder.get(2)); // NORMAL priority
        }

        /**
         * Verifies that a complex scenario with mixed priorities is handled correctly.
         */
        @Test
        @DisplayName("Should handle complex priority scenario")
        void complexPriorityScenario() throws ModDependencyException {
            // Your exact scenario
            ModMetadata base = createMod("base", "1.0", LoadPriority.LOW);
            ModMetadata high = createMod("high", "1.0", LoadPriority.HIGH,
                dependency("base", "1.0", false));
            ModMetadata normal = createMod("normal", "1.0", LoadPriority.NORMAL,
                dependency("base", "1.0", false));
            ModMetadata other = createMod("other", "1.0", LoadPriority.LOW);

            Map<ModMetadata, Path> input = Map.of(
                base, path("base.jar"),
                high, path("high.jar"),
                normal, path("normal.jar"),
                other, path("other.jar")
            );

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);
            List<String> loadOrder = getLoadOrderIds(result);

            // Verify dependencies are respected
            int baseIndex = loadOrder.indexOf("base");
            int highIndex = loadOrder.indexOf("high");
            int normalIndex = loadOrder.indexOf("normal");
            int otherIndex = loadOrder.indexOf("other");

            assertTrue(baseIndex < highIndex, "Base must load before high");
            assertTrue(baseIndex < normalIndex, "Base must load before normal");
            assertTrue(highIndex < normalIndex, "High must load before normal (NORMAL)");

            // Other can be first or last, but let's check it's handled correctly
            assertTrue(otherIndex >= 0, "Other should be in the load order");
        }

        /**
         * Verifies that the dependency ordering is consistent across multiple runs of the resolver.
         */
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5})
        @DisplayName("Should maintain consistent dependency ordering across multiple runs")
        void consistentDependencyOrdering(int run) throws ModDependencyException {
            ModMetadata base = createMod("base", "1.0", LoadPriority.LOW);
            ModMetadata high = createMod("high", "1.0", LoadPriority.HIGH,
                dependency("base", "1.0", false));
            ModMetadata normal = createMod("normal", "1.0", LoadPriority.NORMAL,
                dependency("base", "1.0", false));

            Map<ModMetadata, Path> input = Map.of(
                base, path("base.jar"),
                high, path("high.jar"),
                normal, path("normal.jar")
            );

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);
            List<String> loadOrder = getLoadOrderIds(result);

            // These invariants must hold across all runs
            int baseIndex = loadOrder.indexOf("base");
            int highIndex = loadOrder.indexOf("high");
            int normalIndex = loadOrder.indexOf("normal");

            assertTrue(baseIndex < highIndex, "Run " + run + ": Base must load before high");
            assertTrue(baseIndex < normalIndex, "Run " + run + ": Base must load before normal");
            assertTrue(highIndex < normalIndex, "Run " + run + ": High must load before normal");
        }
    }

    /**
     * Tests for error handling in the resolver, such as missing dependencies, version mismatches, and cyclic dependencies.
     */
    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        /**
         * Verifies that an exception is thrown when a required dependency is missing.
         */
        @Test
        @DisplayName("Should throw exception for missing required dependency")
        void missingRequiredDependency() {
            ModMetadata high = createMod("high", "1.0", LoadPriority.HIGH,
                dependency("missing", "1.0", false));

            Map<ModMetadata, Path> input = Map.of(high, path("high.jar"));

            ModDependencyException exception = assertThrows(ModDependencyException.class,
                () -> resolver.resolveLoadOrder(input));

            assertTrue(exception.getMessage().contains("Missing required dependency: missing"));
        }

        /**
         * Verifies that an exception is thrown when there is a version mismatch between mods.
         */
        @Test
        @DisplayName("Should throw exception for version mismatch")
        void versionMismatch() {
            ModMetadata base = createMod("base", "1.0", LoadPriority.LOW);
            ModMetadata high = createMod("high", "1.0", LoadPriority.HIGH,
                dependency("base", "2.0", false)); // Requires version 2.0, but base is 1.0

            Map<ModMetadata, Path> input = Map.of(
                base, path("base.jar"),
                high, path("high.jar")
            );

            ModDependencyException exception = assertThrows(ModDependencyException.class,
                () -> resolver.resolveLoadOrder(input));

            assertTrue(exception.getMessage().contains("requires base 2.0 but found 1.0"));
        }

        /**
         * Verifies that an exception is thrown when there is a cyclic dependency between mods.
         */
        @Test
        @DisplayName("Should throw exception for cyclic dependency")
        void cyclicDependency() {
            ModMetadata modA = createMod("modA", "1.0", LoadPriority.LOW,
                dependency("modB", "1.0", false));
            ModMetadata modB = createMod("modB", "1.0", LoadPriority.LOW,
                dependency("modA", "1.0", false));

            Map<ModMetadata, Path> input = Map.of(
                modA, path("modA.jar"),
                modB, path("modB.jar")
            );

            ModDependencyException exception = assertThrows(ModDependencyException.class,
                () -> resolver.resolveLoadOrder(input));

            assertTrue(exception.getMessage().contains("Cyclic dependency detected"));
        }

        /**
         * Verifies that optional dependencies are handled gracefully, allowing the mod to load even if the optional
         * dependency is missing.
         */
        @Test
        @DisplayName("Should handle optional dependencies gracefully")
        void optionalDependencies() throws ModDependencyException {
            ModMetadata base = createMod("base", "1.0", LoadPriority.LOW);
            ModMetadata high = createMod("high", "1.0", LoadPriority.HIGH,
                dependency("base", "1.0", false),
                dependency("missing", "1.0", true) // Optional dependency
            );

            Map<ModMetadata, Path> input = Map.of(
                base, path("base.jar"),
                high, path("high.jar")
            );

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);
            List<String> loadOrder = getLoadOrderIds(result);

            assertEquals(2, result.size());
            assertEquals("base", loadOrder.get(0));
            assertEquals("high", loadOrder.get(1));
        }
    }

    /**
     * Tests for edge cases in the mod load order, such as empty mod lists and complex dependency trees.
     */
    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        /**
         * Verifies that an empty mod list is handled correctly, resulting in an empty load order.
         */
        @Test
        @DisplayName("Should handle empty mod list")
        void emptyModList() throws ModDependencyException {
            Map<ModMetadata, Path> input = Map.of();

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);

            assertTrue(result.isEmpty());
        }

        /**
         * Verifies that a complex dependency tree is flattened correctly, respecting all dependencies and priorities.
         */
        @Test
        @DisplayName("Should handle complex dependency tree")
        void complexDependencyTree() throws ModDependencyException {
            ModMetadata core = createMod("core", "1.0", LoadPriority.LOW);
            ModMetadata utils = createMod("utils", "1.0", LoadPriority.NORMAL,
                dependency("core", "1.0", false));
            ModMetadata api = createMod("api", "1.0", LoadPriority.NORMAL,
                dependency("core", "1.0", false));
            ModMetadata plugin = createMod("plugin", "1.0", LoadPriority.HIGH,
                dependency("utils", "1.0", false),
                dependency("api", "1.0", false));

            Map<ModMetadata, Path> input = Map.of(
                core, path("core.jar"),
                utils, path("utils.jar"),
                api, path("api.jar"),
                plugin, path("plugin.jar")
            );

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);
            List<String> loadOrder = getLoadOrderIds(result);

            // Verify all dependencies are respected
            int coreIndex = loadOrder.indexOf("core");
            int utilsIndex = loadOrder.indexOf("utils");
            int apiIndex = loadOrder.indexOf("api");
            int pluginIndex = loadOrder.indexOf("plugin");

            assertTrue(coreIndex < utilsIndex, "Core must load before utils");
            assertTrue(coreIndex < apiIndex, "Core must load before api");
            assertTrue(utilsIndex < pluginIndex, "Utils must load before plugin");
            assertTrue(apiIndex < pluginIndex, "API must load before plugin");
        }

        /**
         * Verifies that mods with the same priority are ordered deterministically, using their IDs as tiebreakers.
         */
        @Test
        @DisplayName("Should preserve order deterministically for same priority mods")
        void deterministicOrderingSamePriority() throws ModDependencyException {
            ModMetadata modA = createMod("aaa", "1.0", LoadPriority.NORMAL);
            ModMetadata modB = createMod("bbb", "1.0", LoadPriority.NORMAL);
            ModMetadata modC = createMod("ccc", "1.0", LoadPriority.NORMAL);

            Map<ModMetadata, Path> input = Map.of(
                modA, path("aaa.jar"),
                modB, path("bbb.jar"),
                modC, path("ccc.jar")
            );

            Map<ModMetadata, Path> result = resolver.resolveLoadOrder(input);
            List<String> loadOrder = getLoadOrderIds(result);

            // Should be ordered alphabetically by mod ID as tiebreaker
            assertEquals(List.of("aaa", "bbb", "ccc"), loadOrder);
        }
    }

    // Helper methods
    private ModMetadata createMod(String id, String version, LoadPriority priority, ModDependency... dependencies) {
        return new ModMetadata(id, "", version, "io.github.hato1883.test.TestEntryPoint", "", List.of(dependencies), priority);
    }

    private ModDependency dependency(String modId, String version, boolean optional) {
        return new ModDependency(modId, VersionConstraint.parse(version), optional);
    }

    private Path path(String filename) {
        return Paths.get(filename);
    }

    private List<String> getLoadOrderIds(Map<ModMetadata, Path> result) {
        return result.keySet().stream()
            .map(ModMetadata::id)
            .collect(Collectors.toList());
    }

    // Mock classes for testing (replace with your actual classes)
    public static class ModLoadOrderResolver {
        public Map<ModMetadata, Path> resolveLoadOrder(Map<ModMetadata, Path> mods) throws ModDependencyException {
            Map<String, ModMetadata> modMap = new HashMap<>();
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
                }
            }

            PriorityQueue<ModMetadata> queue = new PriorityQueue<>(Comparator
                .<ModMetadata>comparingInt(m -> -m.loadPriority().getWeight())
                .thenComparing(ModMetadata::id)
            );

            for (String id : inDegree.keySet()) {
                if (inDegree.get(id) == 0) {
                    queue.add(modMap.get(id));
                }
            }

            Map<ModMetadata, Path> loadOrder = new LinkedHashMap<>();
            while (!queue.isEmpty()) {
                ModMetadata modMetadata = queue.poll();
                loadOrder.put(modMetadata, mods.get(modMetadata));

                for (String childId : graph.getOrDefault(modMetadata.id(), Collections.emptySet())) {
                    inDegree.put(childId, inDegree.get(childId) - 1);
                    if (inDegree.get(childId) == 0) {
                        queue.add(modMap.get(childId));
                    }
                }
            }

            if (loadOrder.size() != mods.size())
                throw new ModDependencyException("Cyclic dependency detected or load ordering failed.");

            return loadOrder;
        }
    }
}
