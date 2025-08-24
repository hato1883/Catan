package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.LoadPriority;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Marker;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DefaultModInitializer}.
 * <p>
 * This class tests the behavior of DefaultModInitializer when initializing mods, including:
 * <ul>
 *     <li>Normal initialization of mods</li>
 *     <li>Exception handling during mod initialization</li>
 *     <li>Proper logging of initialization and errors</li>
 *     <li>Ensuring the thread context class loader is always restored</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class DefaultModInitializerTest {

    private DefaultModInitializer initializer;
    private MockedStatic<LogManager> logManagerMockedStatic;
    private TestLogger testLogger;

    @BeforeEach
    void setUp() {
        initializer = new DefaultModInitializer();
        testLogger = spy(new TestLogger());
        logManagerMockedStatic = Mockito.mockStatic(LogManager.class);
        logManagerMockedStatic.when(() -> LogManager.getLogger(anyString())).thenReturn(testLogger);
    }

    @AfterEach
    void tearDown() {
        logManagerMockedStatic.close();
    }

    /**
     * Tests that all mods are initialized successfully and context class loader is set/restored.
     */
    @Test
    @DisplayName("Initializes all mods and restores context class loader")
    void testInitializeAllMods() {
        ILoadedMod mod1 = new TestLoadedMod("mod1", getClass().getClassLoader(), new NoOpMod());
        ILoadedMod mod2 = new TestLoadedMod("mod2", getClass().getClassLoader(), new NoOpMod());

        ClassLoader original = Thread.currentThread().getContextClassLoader();
        initializer.initializeAll(Arrays.asList(mod1, mod2));
        assertSame(original, Thread.currentThread().getContextClassLoader(), "Context class loader should be restored");
        assertTrue(((NoOpMod) mod1.instance()).initialized);
        assertTrue(((NoOpMod) mod2.instance()).initialized);
        verify(testLogger, times(2)).info(contains("Initializing mod"), any(Object.class));
    }

    /**
     * Tests that if a mod throws during initialization, it is logged and other mods are still initialized.
     */
    @Test
    @DisplayName("Continues initializing other mods if one throws an exception")
    void testExceptionInOneModDoesNotStopOthers() {
        ILoadedMod mod1 = new TestLoadedMod("mod1", getClass().getClassLoader(), new FailingMod());
        ILoadedMod mod2 = new TestLoadedMod("mod2", getClass().getClassLoader(), new NoOpMod());

        initializer.initializeAll(Arrays.asList(mod1, mod2));
        verify(testLogger).error(eq("Error initializing mod"), any(Throwable.class));
        verify(testLogger, times(2)).info(contains("Initializing mod"), any(Object.class));
        assertTrue(((NoOpMod) mod2.instance()).initialized);
    }

    /**
     * Tests that no mods are initialized if the list is empty.
     */
    @Test
    @DisplayName("Does nothing if mod list is empty")
    void testEmptyModList() {
        initializer.initializeAll(Collections.emptyList());
        verifyNoInteractions(testLogger);
    }

    /**
     * Tests that the context class loader is always restored, even if an exception is thrown.
     */
    @Test
    @DisplayName("Restores context class loader after exception")
    void testContextClassLoaderRestoredOnException() {
        ILoadedMod mod = new TestLoadedMod("mod1", getClass().getClassLoader(), new FailingMod());
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        initializer.initializeAll(List.of(mod));
        assertSame(original, Thread.currentThread().getContextClassLoader(), "Context class loader should be restored after exception");
    }

    // --- Mocks and private helpers below ---

    /**
     * Minimal test double for ILoadedMod.
     */
    private static class TestLoadedMod implements ILoadedMod {
        private final String id;
        private final String version;
        private final String mainClass;
        private final Path path;
        private final ModMetadata metadata;
        private final CatanMod instance;
        private final ClassLoader classLoader;

        TestLoadedMod(String id, ClassLoader classLoader, CatanMod instance) {
            this.id = id;
            this.version = "1.0.0";
            this.mainClass = "MainClass";
            this.path = Path.of("/dummy/path/" + id);
            this.metadata = new ModMetadata(id, id, version, mainClass, "desc", List.of(), LoadPriority.NORMAL);
            this.instance = instance;
            this.classLoader = classLoader;
        }
        @Override public String id() { return id; }
        @Override public String version() { return version; }
        @Override public String mainClass() { return mainClass; }
        @Override public Path path() { return path; }
        @Override public ModMetadata metadata() { return metadata; }
        @Override public CatanMod instance() { return instance; }
        @Override public ClassLoader classLoader() { return classLoader; }
        @Override public String toString() { return id; }
    }

    /**
     * A no-op mod implementation for testing.
     */
    private static class NoOpMod implements CatanMod {
        boolean initialized = false;
        @Override public void onInitialize() { initialized = true; }
    }

    /**
     * A mod implementation that always throws in onInitialize.
     */
    private static class FailingMod implements CatanMod {
        @Override public void onInitialize() { throw new RuntimeException("fail"); }
    }

    /**
     * A simple logger mock to capture log calls.
     * Implements only the varargs info/error methods to avoid ambiguity.
     */
    private static class TestLogger implements org.slf4j.Logger {
        @Override public String getName() { return "TestLogger"; }
        @Override public boolean isTraceEnabled() { return false; }
        @Override public void trace(String msg) {}
        @Override public void trace(String format, Object arg) {}
        @Override public void trace(String format, Object arg1, Object arg2) {}
        @Override public void trace(String format, Object... arguments) {}
        @Override public void trace(String msg, Throwable t) {}
        @Override public boolean isDebugEnabled() { return false; }
        @Override public void debug(String msg) {}
        @Override public void debug(String format, Object arg) {}
        @Override public void debug(String format, Object arg1, Object arg2) {}
        @Override public void debug(String format, Object... arguments) {}
        @Override public void debug(String msg, Throwable t) {}
        @Override public boolean isInfoEnabled() { return true;}
        @Override public void info(String format, Object... arguments) {}
        @Override public void info(String msg) {}
        @Override public void info(String format, Object arg) {}
        @Override public void info(String format, Object arg1, Object arg2) {}
        @Override public void info(String msg, Throwable t) {}
        @Override public boolean isWarnEnabled() { return false; }
        @Override public void warn(String msg) {}
        @Override public void warn(String format, Object arg) {}
        @Override public void warn(String format, Object arg1, Object arg2) {}
        @Override public void warn(String format, Object... arguments) {}
        @Override public void warn(String msg, Throwable t) {}
        @Override public boolean isErrorEnabled() { return true;}
        @Override public void error(String format, Object... arguments) {}
        @Override public void error(String msg) {}
        @Override public void error(String format, Object arg) {}
        @Override public void error(String format, Object arg1, Object arg2) {}
        @Override public void error(String msg, Throwable t) {}// <-- was throwing, now no-op

        // Marker-based methods required by Logger interface
        @Override public boolean isTraceEnabled(Marker marker) { return false; }
        @Override public void trace(Marker marker, String msg) {}
        @Override public void trace(Marker marker, String format, Object arg) {}
        @Override public void trace(Marker marker, String format, Object arg1, Object arg2) {}
        @Override public void trace(Marker marker, String format, Object... arguments) {}
        @Override public void trace(Marker marker, String msg, Throwable t) {}
        @Override public boolean isDebugEnabled(Marker marker) { return false; }
        @Override public void debug(Marker marker, String msg) {}
        @Override public void debug(Marker marker, String format, Object arg) {}
        @Override public void debug(Marker marker, String format, Object arg1, Object arg2) {}
        @Override public void debug(Marker marker, String format, Object... arguments) {}
        @Override public void debug(Marker marker, String msg, Throwable t) {}
        @Override public boolean isInfoEnabled(Marker marker) { return false; }
        @Override public void info(Marker marker, String msg) {}
        @Override public void info(Marker marker, String format, Object arg) {}
        @Override public void info(Marker marker, String format, Object arg1, Object arg2) {}
        @Override public void info(Marker marker, String format, Object... arguments) {}
        @Override public void info(Marker marker, String msg, Throwable t) {}
        @Override public boolean isWarnEnabled(Marker marker) { return false; }
        @Override public void warn(Marker marker, String msg) {}
        @Override public void warn(Marker marker, String format, Object arg) {}
        @Override public void warn(Marker marker, String format, Object arg1, Object arg2) {}
        @Override public void warn(Marker marker, String format, Object... arguments) {}
        @Override public void warn(Marker marker, String msg, Throwable t) {}
        @Override public boolean isErrorEnabled(Marker marker) { return false; }
        @Override public void error(Marker marker, String format, Object... arguments) {}
        @Override public void error(Marker marker, String msg) {}
        @Override public void error(Marker marker, String format, Object arg) {}
        @Override public void error(Marker marker, String format, Object arg1, Object arg2) {}
        @Override public void error(Marker marker, String msg, Throwable t) {}
    }
}
