package io.github.hato1883.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LogManager {
    private static final Map<String, Logger> modLoggers = new ConcurrentHashMap<>();

    private LogManager() {
        // Utility class, prevent instantiation
    }

    /**
     * Get a logger for the given modId with automatic detection of calling class.
     * Uses StackWalker to find the caller class.
     *
     * @param modId The unique mod ID.
     * @return Logger instance tagged with modId and calling class name (if enabled).
     */
    public static Logger getLogger(String modId) {
        return modLoggers.computeIfAbsent(modId,
            id -> LoggerFactory.getLogger(modId));
    }
}
