package io.github.hato1883.core.modloading.loading;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.hato1883.api.mod.load.dependency.ModDependency;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ModDependencyDeserializer}.
 * <p>
 * This class verifies correct and incorrect deserialization of ModDependency objects from JSON,
 * including edge cases such as missing or invalid fields, and ensures that unwanted behaviors are prevented.
 */
@Nested
class ModDependencyDeserializerTest {
    private final ObjectMapper mapper = createObjectMapper();

    /**
     * Tests deserialization of a valid dependency with all fields present.
     */
    @Test
    @DisplayName("Deserializes valid dependency with id, version, and optional fields")
    void testDeserializeValidDependency() throws Exception {
        String json = "{\"id\":\"foo.mod\",\"version\":\"^1.2.3\",\"optional\":true}";
        ModDependency dep = mapper.readValue(json, ModDependency.class);
        assertEquals("foo.mod", dep.modId());
        assertTrue(dep.optional());
        assertTrue(dep.versionConstraint().matches("1.2.3"));
    }

    /**
     * Tests deserialization when the 'optional' field is missing (should default to false).
     */
    @Test
    @DisplayName("Deserializes dependency with missing optional field (defaults to false)")
    void testDeserializeMissingOptional() throws Exception {
        String json = "{\"id\":\"bar.mod\",\"version\":\"1.0.0\"}";
        ModDependency dep = mapper.readValue(json, ModDependency.class);
        assertEquals("bar.mod", dep.modId());
        assertFalse(dep.optional());
        assertTrue(dep.versionConstraint().matches("1.0.0"));
    }

    /**
     * Tests deserialization when the 'version' field is missing (should use VersionConstraint.any()).
     */
    @Test
    @DisplayName("Deserializes dependency with missing version field (defaults to any version)")
    void testDeserializeMissingVersion() throws Exception {
        String json = "{\"id\":\"baz.mod\"}";
        ModDependency dep = mapper.readValue(json, ModDependency.class);
        assertEquals("baz.mod", dep.modId());
        assertFalse(dep.optional());
        assertTrue(dep.versionConstraint().matches("0.0.1")); // any version matches
    }

    /**
     * Tests deserialization when the 'id' field is missing (should throw IllegalArgumentException).
     */
    @Test
    @DisplayName("Throws when deserializing dependency with missing id field")
    void testDeserializeMissingId() {
        String json = "{\"version\":\"1.0.0\"}";
        assertThrows(IllegalArgumentException.class, () -> mapper.readValue(json, ModDependency.class));
    }

    /**
     * Tests deserialization when the 'id' field is blank (should throw IllegalArgumentException).
     */
    @Test
    @DisplayName("Throws when deserializing dependency with blank id field")
    void testDeserializeBlankId() {
        String json = "{\"id\":\"   \",\"version\":\"1.0.0\"}";
        assertThrows(IllegalArgumentException.class, () -> mapper.readValue(json, ModDependency.class));
    }

    /**
     * Tests deserialization when the 'version' field is invalid (should throw IllegalArgumentException).
     */
    @Test
    @DisplayName("Throws when deserializing dependency with invalid version field")
    void testDeserializeInvalidVersion() {
        String json = "{\"id\":\"foo.mod\",\"version\":null}";
        assertThrows(IllegalArgumentException.class, () -> mapper.readValue(json, ModDependency.class));
    }

    /**
     * Tests deserialization with 'optional' set to false explicitly.
     */
    @Test
    @DisplayName("Deserializes dependency with optional set to false")
    void testDeserializeOptionalFalse() throws Exception {
        String json = "{\"id\":\"foo.mod\",\"version\":\"1.0.0\",\"optional\":false}";
        ModDependency dep = mapper.readValue(json, ModDependency.class);
        assertEquals("foo.mod", dep.modId());
        assertFalse(dep.optional());
    }

    /**
     * Tests that extra fields in the JSON are ignored and do not affect deserialization.
     */
    @Test
    @DisplayName("Ignores extra fields in dependency JSON")
    void testDeserializeWithExtraFields() throws Exception {
        String json = "{\"id\":\"foo.mod\",\"version\":\"1.0.0\",\"optional\":true,\"extra\":\"ignored\"}";
        ModDependency dep = mapper.readValue(json, ModDependency.class);
        assertEquals("foo.mod", dep.modId());
        assertTrue(dep.optional());
    }

    // --- Mocks and private helpers below ---

    /**
     * Creates an ObjectMapper with the ModDependencyDeserializer registered.
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ModDependency.class, new ModDependencyDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}

