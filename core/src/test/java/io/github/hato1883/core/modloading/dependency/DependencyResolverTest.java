package io.github.hato1883.core.modloading.dependency;

import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.LoadPriority;
import io.github.hato1883.api.mod.load.dependency.ModDependency;
import io.github.hato1883.api.mod.load.dependency.ModDependencyException;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;
import io.github.hato1883.api.mod.load.dependency.ModWithPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DependencyResolver}.
 * <p>
 * This class tests the dependency resolution logic for mods, ensuring correct load order and error handling.
 * <p>
 * The following scenarios are covered:
 * <ul>
 *   <li>Mods with no dependencies</li>
 *   <li>Simple and multiple dependencies</li>
 *   <li>Optional and required dependencies</li>
 *   <li>Version constraints</li>
 *   <li>Circular dependencies</li>
 *   <li>Complex dependency graphs</li>
 *   <li>Duplicate mod IDs</li>
 *   <li>Self-dependencies</li>
 *   <li>Load priority ordering</li>
 * </ul>
 */
class DependencyResolverTest {
    /**
     * Verifies that mods with no dependencies are loaded without errors.
     * This is the simplest case and should always succeed.
     */
    @Test
    @DisplayName("Should resolve mods with no dependencies")
    void testNoDependencies() throws Exception {
        ModMetadata a = mod("a", "1.0.0");
        ModMetadata b = mod("b", "1.0.0");
        Map<ModMetadata, Path> mods = Map.of(a, path("a"), b, path("b"));
        List<ModWithPath> result = new DependencyResolver().resolveLoadOrder(mods);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(mwp -> mwp.metadata().equals(a)));
        assertTrue(result.stream().anyMatch(mwp -> mwp.metadata().equals(b)));
    }

    /**
     * Verifies that a mod depending on another mod is loaded after its dependency.
     * Ensures direct dependencies are respected in load order.
     */
    @Test
    @DisplayName("Should resolve simple direct dependency")
    void testSimpleDependency() throws Exception {
        ModMetadata b = mod("b", "1.0.0");
        ModMetadata a = mod("a", "1.0.0", dep("b", ">=1.0.0", false));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"), b, path("b"));
        List<ModWithPath> result = new DependencyResolver().resolveLoadOrder(mods);
        List<ModMetadata> order = result.stream().map(ModWithPath::metadata).toList();
        assertTrue(order.indexOf(b) < order.indexOf(a));
    }

    /**
     * Verifies that a mod with multiple dependencies is loaded after all its dependencies.
     * Ensures all dependencies are resolved before the dependent mod.
     */
    @Test
    @DisplayName("Should resolve multiple dependencies correctly")
    void testMultipleDependencies() throws Exception {
        ModMetadata c = mod("c", "1.0.0");
        ModMetadata b = mod("b", "1.0.0");
        ModMetadata a = mod("a", "1.0.0", dep("b", ">=1.0.0", false), dep("c", ">=1.0.0", false));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"), b, path("b"), c, path("c"));
        List<ModWithPath> result = new DependencyResolver().resolveLoadOrder(mods);
        List<ModMetadata> order = result.stream().map(ModWithPath::metadata).toList();
        assertTrue(order.indexOf(b) < order.indexOf(a));
        assertTrue(order.indexOf(c) < order.indexOf(a));
    }

    /**
     * Verifies that a missing optional dependency does not prevent the mod from loading.
     * Optional dependencies should not cause errors if missing.
     */
    @Test
    @DisplayName("Should allow missing optional dependency")
    void testOptionalDependencyMissing() throws Exception {
        ModMetadata a = mod("a", "1.0.0", dep("b", ">=1.0.0", true));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"));
        List<ModWithPath> result = new DependencyResolver().resolveLoadOrder(mods);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(mwp -> mwp.metadata().equals(a)));
    }

    /**
     * Verifies that a missing required dependency causes an exception.
     * Required dependencies must be present, otherwise an error is thrown.
     */
    @Test
    @DisplayName("Should throw if required dependency is missing")
    void testRequiredDependencyMissing() {
        ModMetadata a = mod("a", "1.0.0", dep("b", ">=1.0.0", false));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"));
        assertThrows(ModDependencyException.class, () -> new DependencyResolver().resolveLoadOrder(mods));
    }

    /**
     * Verifies that an unsatisfied version constraint causes an exception.
     * If a dependency is present but does not meet the version requirement, an error is thrown.
     */
    @Test
    @DisplayName("Should throw if version constraint is not satisfied")
    void testVersionConstraintNotSatisfied() {
        ModMetadata b = mod("b", "1.0.0");
        ModMetadata a = mod("a", "1.0.0", dep("b", ">=2.0.0", false));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"), b, path("b"));
        assertThrows(ModDependencyException.class, () -> new DependencyResolver().resolveLoadOrder(mods));
    }

    /**
     * Verifies that a circular dependency between mods causes an exception.
     * Cycles in the dependency graph should be detected and rejected.
     */
    @Test
    @DisplayName("Should throw on circular dependency")
    void testCircularDependency() {
        ModMetadata a = mod("a", "1.0.0", dep("b", ">=1.0.0", false));
        ModMetadata b = mod("b", "1.0.0", dep("a", ">=1.0.0", false));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"), b, path("b"));
        assertThrows(ModDependencyException.class, () -> new DependencyResolver().resolveLoadOrder(mods));
    }

    /**
     * Verifies that a complex dependency graph is resolved in the correct order.
     * Ensures the resolver can handle multiple layers of dependencies.
     */
    @Test
    @DisplayName("Should resolve complex dependency graph")
    void testComplexGraph() throws Exception {
        ModMetadata d = mod("d", "1.0.0");
        ModMetadata c = mod("c", "1.0.0", dep("d", ">=1.0.0", false));
        ModMetadata b = mod("b", "1.0.0", dep("d", ">=1.0.0", false));
        ModMetadata a = mod("a", "1.0.0", dep("b", ">=1.0.0", false), dep("c", ">=1.0.0", false));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"), b, path("b"), c, path("c"), d, path("d"));
        List<ModWithPath> result = new DependencyResolver().resolveLoadOrder(mods);
        List<ModMetadata> order = result.stream().map(ModWithPath::metadata).toList();
        assertTrue(order.indexOf(d) < order.indexOf(b));
        assertTrue(order.indexOf(d) < order.indexOf(c));
        assertTrue(order.indexOf(b) < order.indexOf(a));
        assertTrue(order.indexOf(c) < order.indexOf(a));
    }

    /**
     * Verifies that duplicate mod IDs are not allowed and cause an exception.
     * This is an unwanted behavior and should be rejected.
     */
    @Test
    @DisplayName("Should throw if duplicate mod IDs are present")
    void testDuplicateModIds() {
        ModMetadata a1 = mod("dup", "1.0.0");
        ModMetadata a2 = mod("dup", "2.0.0");
        Map<ModMetadata, Path> mods = new HashMap<>();
        mods.put(a1, path("a1"));
        mods.put(a2, path("a2"));
        assertThrows(Exception.class, () -> new DependencyResolver().resolveLoadOrder(mods));
    }

    /**
     * Verifies that a mod with a self-dependency is rejected.
     * Mods should not be able to depend on themselves.
     */
    @Test
    @DisplayName("Should throw if mod depends on itself")
    void testSelfDependency() {
        ModMetadata a = mod("a", "1.0.0", dep("a", ">=1.0.0", false));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"));
        assertThrows(ModDependencyException.class, () -> new DependencyResolver().resolveLoadOrder(mods));
    }

    /**
     * Verifies that mods with only optional dependencies are loaded successfully.
     * Optional dependencies should not block loading if missing.
     */
    @Test
    @DisplayName("Should resolve mods with only optional dependencies")
    void testOnlyOptionalDependencies() throws Exception {
        ModMetadata a = mod("a", "1.0.0", dep("b", ">=1.0.0", true));
        Map<ModMetadata, Path> mods = Map.of(a, path("a"));
        List<ModWithPath> result = new DependencyResolver().resolveLoadOrder(mods);
        assertEquals(1, result.size());
        assertEquals("a", result.get(0).metadata().id());
    }

    /**
     * Verifies that mods are loaded in order of load priority when possible.
     * Mods with higher load priority should be loaded before others if dependencies allow.
     */
    @Test
    @DisplayName("Should respect load priority when resolving order")
    void testLoadPriorityOrdering() throws Exception {
        ModMetadata a = new ModMetadata("a", "a", "1.0.0", "Main", "desc", List.of(), LoadPriority.HIGHEST);
        ModMetadata b = new ModMetadata("b", "b", "1.0.0", "Main", "desc", List.of(), LoadPriority.LOWEST);
        Map<ModMetadata, Path> mods = Map.of(a, path("a"), b, path("b"));
        List<ModWithPath> result = new DependencyResolver().resolveLoadOrder(mods);
        assertEquals("a", result.get(0).metadata().id());
        assertEquals("b", result.get(1).metadata().id());
    }

    /**
     * Verifies that a mod with higher load priority will still load after a lower-priority mod if it depends on it.
     * This checks for correct priority inversion: dependency order takes precedence over load priority.
     */
    @Test
    @DisplayName("Should load lower-priority mod before higher-priority mod if dependency requires (priority inversion)")
    void testPriorityInversionDueToDependency() throws Exception {
        ModMetadata low = new ModMetadata("low", "low", "1.0.0", "Main", "desc", List.of(), LoadPriority.LOWEST);
        ModMetadata high = new ModMetadata("high", "high", "1.0.0", "Main", "desc", List.of(dep("low", ">=1.0.0", false)), LoadPriority.HIGHEST);
        Map<ModMetadata, Path> mods = Map.of(low, path("low"), high, path("high"));
        List<ModWithPath> result = new DependencyResolver().resolveLoadOrder(mods);
        List<ModMetadata> order = result.stream().map(ModWithPath::metadata).toList();
        assertTrue(order.indexOf(low) < order.indexOf(high), "Low-priority mod should load before high-priority mod if high depends on low");
    }

    // ----------------------
    // Helper methods & mocks
    // ----------------------
    private static ModMetadata mod(String id, String version, ModDependency... deps) {
        return new ModMetadata(id, id, version, "Main", "desc", Arrays.asList(deps), LoadPriority.NORMAL);
    }
    private static ModDependency dep(String modId, String version, boolean optional) {
        // Replace ">=x.y.z" with "[x.y.z,)" for supported VersionConstraint syntax
        if (version.startsWith(">=")) {
            version = "[" + version.substring(2) + ",)";
        }
        return new ModDependency(modId, VersionConstraint.parse(version), optional);
    }
    private static Path path(String id) {
        return Path.of("/fake/" + id);
    }
}
