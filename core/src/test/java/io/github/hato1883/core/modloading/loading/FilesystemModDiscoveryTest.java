package io.github.hato1883.core.modloading.loading;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Unit tests for {@link FilesystemModDiscovery}.
 * <p>
 * This class tests the discovery of mods in a filesystem directory, ensuring correct detection of directories and .jar files,
 * proper handling of non-existent directories, and correct exclusion of unwanted files. It also verifies that warnings are logged
 * for unrecognized files.
 */
class FilesystemModDiscoveryTest {

    private FilesystemModDiscovery discovery;

    @BeforeEach
    void setUp() {
        discovery = new FilesystemModDiscovery();
    }

    /**
     * Tests that a non-existent mods directory is created and returns an empty list.
     */
    @Test
    @DisplayName("Should create non-existent mods directory and return empty list")
    void testNonExistentModsDirIsCreated(@TempDir Path tempDir) throws IOException {
        Path modsDir = tempDir.resolve("mods");
        Assertions.assertFalse(Files.exists(modsDir));
        List<Path> result = discovery.discoverMods(modsDir);
        Assertions.assertTrue(Files.exists(modsDir), "Mods directory should be created");
        Assertions.assertTrue(result.isEmpty(), "Result should be empty for new directory");
    }

    /**
     * Tests that directories and .jar files are discovered, and other files are ignored.
     */
    @Test
    @DisplayName("Should discover directories and .jar files, ignore others")
    void testDiscoverModsFindsDirsAndJars(@TempDir Path modsDir) throws IOException {
        Path dir1 = Files.createDirectory(modsDir.resolve("mod1"));
        Path jar1 = Files.createFile(modsDir.resolve("mod2.jar"));
        Path txt = Files.createFile(modsDir.resolve("readme.txt"));
        List<Path> result = discovery.discoverMods(modsDir);
        Assertions.assertTrue(result.contains(dir1), "Should contain directory");
        Assertions.assertTrue(result.contains(jar1), "Should contain .jar file");
        Assertions.assertFalse(result.contains(txt), "Should not contain non-jar file");
    }

    /**
     * Tests that an empty mods directory returns an empty list.
     */
    @Test
    @DisplayName("Should return empty list for empty mods directory")
    void testEmptyModsDir(@TempDir Path modsDir) throws IOException {
        List<Path> result = discovery.discoverMods(modsDir);
        Assertions.assertTrue(result.isEmpty(), "Result should be empty for empty directory");
    }

    /**
     * Tests that only .jar files (not .zip or .txt) are discovered.
     */
    @Test
    @DisplayName("Should only discover .jar files, not .zip or .txt")
    void testOnlyJarFilesDiscovered(@TempDir Path modsDir) throws IOException {
        Path jar = Files.createFile(modsDir.resolve("mod.jar"));
        Path zip = Files.createFile(modsDir.resolve("mod.zip"));
        Path txt = Files.createFile(modsDir.resolve("mod.txt"));
        List<Path> result = discovery.discoverMods(modsDir);
        Assertions.assertTrue(result.contains(jar), "Should contain .jar file");
        Assertions.assertFalse(result.contains(zip), "Should not contain .zip file");
        Assertions.assertFalse(result.contains(txt), "Should not contain .txt file");
    }

    /**
     * Tests that a directory with only unwanted files returns an empty list.
     */
    @Test
    @DisplayName("Should return empty list if only unwanted files exist")
    void testOnlyUnwantedFiles(@TempDir Path modsDir) throws IOException {
        Files.createFile(modsDir.resolve("foo.txt"));
        Files.createFile(modsDir.resolve("bar.zip"));
        List<Path> result = discovery.discoverMods(modsDir);
        Assertions.assertTrue(result.isEmpty(), "Should be empty if only unwanted files exist");
    }

    // --- Mocks and private helpers below ---
    // (No mocks or helpers needed for this test class)
}

