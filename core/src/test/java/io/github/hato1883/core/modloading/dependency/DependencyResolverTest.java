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
 * These tests verify that the dependency resolution logic for mods works as expected, including handling of:
 * <ul>
 *   <li>Mods with no dependencies</li>
 *   <li>Simple and multiple dependencies</li>
 *   <li>Optional and required dependencies</li>
 *   <li>Version constraints</li>
 *   <li>Circular dependencies</li>
 *   <li>Complex dependency graphs</li>
 * </ul>
 */
class DependencyResolverTest {
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

    /**
     * Verifies that mods with no dependencies are loaded without errors.
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
}
