package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.ModMetadataParseException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DefaultModMetadataReader}.
 * <p>
 * These tests verify that mod metadata is correctly read from disk, including:
 * <ul>
 *   <li>Successful parsing of valid metadata files.</li>
 *   <li>Proper error handling for missing or invalid metadata files (edge cases).</li>
 *   <li>Correct parsing of dependencies and default values for optional fields.</li>
 * </ul>
 * The tests ensure robustness and correctness of the metadata reader in various scenarios.
 */
class DefaultModMetadataReaderTest {
    private DefaultModMetadataReader reader;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        reader = new DefaultModMetadataReader();
        tempDir = Files.createTempDirectory("modtest");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (tempDir != null) {
            try (var walk = Files.walk(tempDir)) {
                walk.sorted(java.util.Comparator.reverseOrder())
                    .forEach(path -> {
                        try { Files.delete(path); } catch (IOException ignored) {}
                    });
            }
        }
    }

    /**
     * Tests that a valid catan.mod.json file in a directory is parsed correctly and all fields are read as expected.
     * <p>
     * Edge case: Ensures that the reader works with a minimal valid file and all required fields are present.
     */
    @Test
    @DisplayName("Should parse valid catan.mod.json and read all required fields")
    void testReadMetadataFromDirectory() throws IOException {
        String json = """
            {
              "id": "testmod",
              "version": "1.0.0",
              "entrypoint": "com.example.Main"
            }
            """;
        Path metadataFile = tempDir.resolve("catan.mod.json");
        Files.write(metadataFile, json.getBytes());
        ModMetadata metadata = reader.readMetadata(tempDir);
        assertEquals("testmod", metadata.id());
        assertEquals("1.0.0", metadata.version());
        assertEquals("com.example.Main", metadata.entrypoint());
    }

    /**
     * Tests that attempting to read metadata from a directory with no catan.mod.json file throws an IOException.
     * <p>
     * Edge case: Ensures the reader fails gracefully and provides a helpful error message when the file is missing.
     */
    @Test
    @DisplayName("Should throw IOException if catan.mod.json is missing")
    void testMissingMetadataFileThrows() {
        Exception ex = assertThrows(IOException.class, () -> reader.readMetadata(tempDir));
        assertTrue(ex.getMessage().contains("Metadata file not found"));
    }

    /**
     * Tests that an invalid (non-JSON) catan.mod.json file throws a ModMetadataParseException.
     * <p>
     * Edge case: Ensures the reader detects and reports malformed JSON input.
     */
    @Test
    @DisplayName("Should throw ModMetadataParseException for invalid JSON")
    void testInvalidJsonThrows() throws IOException {
        Path metadataFile = tempDir.resolve("catan.mod.json");
        Files.write(metadataFile, "not a json".getBytes());
        Exception ex = assertThrows(ModMetadataParseException.class, () -> reader.readMetadata(tempDir));
        assertTrue(ex.getMessage().contains("Failed to parse metadata"));
    }

    /**
     * Tests that dependencies are parsed correctly, including default values for optional fields.
     * <p>
     * Edge case: Ensures that missing 'optional' fields default to false and that all dependency fields are read.
     */
    @Test
    @DisplayName("Should parse dependencies and default optional fields correctly")
    void testReadDependenciesStructureAndDefaults() throws IOException {
        String json = """
            {
              "id": "moda",
              "version": "1.0.0",
              "entrypoint": "com.example.Main",
              "dependencies": [
                { "id": "modb", "version": ">=1.2.0" },
                { "id": "modc", "optional": true },
                { "id": "modd" }
              ]
            }
            """;
        Path metadataFile = tempDir.resolve("catan.mod.json");
        Files.write(metadataFile, json.getBytes());
        ModMetadata metadata = reader.readMetadata(tempDir);
        assertEquals("moda", metadata.id());
        assertEquals("1.0.0", metadata.version());
        assertEquals("com.example.Main", metadata.entrypoint());
        assertNotNull(metadata.dependencies());
        assertEquals(3, metadata.dependencies().size());
        var dep1 = metadata.dependencies().getFirst();
        assertEquals("modb", dep1.modId());
        assertFalse(dep1.optional()); // default is false
        var dep2 = metadata.dependencies().get(1);
        assertEquals("modc", dep2.modId());
        assertTrue(dep2.optional());
        var dep3 = metadata.dependencies().get(2);
        assertEquals("modd", dep3.modId());
        assertFalse(dep3.optional()); // default is false
    }
}
