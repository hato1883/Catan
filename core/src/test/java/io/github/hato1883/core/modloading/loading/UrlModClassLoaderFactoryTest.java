package io.github.hato1883.core.modloading.loading;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UrlModClassLoaderFactory}.
 * <p>
 * This test class verifies that the UrlModClassLoaderFactory correctly creates a URLClassLoader
 * with the expected parent and URLs when provided with a directory path. It ensures that the
 * factory's core functionality works as intended and that the resulting class loader is properly
 * configured to load classes and resources from the specified path.
 */
class UrlModClassLoaderFactoryTest {
    /**
     * Tests that {@link UrlModClassLoaderFactory#createClassLoader(Path)} returns a URLClassLoader
     * with the correct parent and that the class loader's URLs include only the provided directory.
     * <p>
     * This ensures that the factory produces a class loader that is properly set up to load classes
     * and resources from the given path, and that it uses the specified parent class loader.
     */
    @Test
    @DisplayName("Should create URLClassLoader with correct parent and URLs for directory path")
    void testCreateClassLoaderReturnsURLClassLoader() throws Exception {
        ClassLoader parent = getClass().getClassLoader();
        UrlModClassLoaderFactory factory = new UrlModClassLoaderFactory(parent);
        Path tempDir = Files.createTempDirectory("modtest");
        try {
            try (URLClassLoader loader = (URLClassLoader) factory.createClassLoader(tempDir)) {
                assertInstanceOf(URLClassLoader.class, loader);
                assertEquals(parent, loader.getParent());
                URL[] urls = loader.getURLs();
                assertEquals(1, urls.length);
                assertEquals(tempDir.toUri().toURL(), urls[0]);
            }
        } finally {
            Files.deleteIfExists(tempDir);
        }
    }
}
