package io.github.hato1883.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class LogManager {

    private static final boolean SHOW_CLASSNAME = true; // toggle to include/exclude class name in logger name
    private static final String PATH_SEPERATOR = "->";

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
        String callerClassName = getCallingClassName().orElse("UnknownClass");
        String simpleClassName = simpleName(callerClassName);
        return getLogger(modId, simpleClassName);
    }

    /**
     * Get a logger for the given modId and explicit class.
     *
     * @param modId The unique mod ID.
     * @param clazz The class to associate the logger with.
     * @return Logger instance tagged with modId and the class name.
     */
    public static Logger getLogger(String modId, Class<?> clazz) {
        return getLogger(modId, clazz.getSimpleName());
    }

    /**
     * Get a logger for the given modId and explicit class.
     *
     * @param modId The unique mod ID.
     * @param className The class to associate the logger with.
     * @return Logger instance tagged with modId and the class name.
     */
    public static Logger getLogger(String modId, String className) {
        String loggerName = SHOW_CLASSNAME ? modId + PATH_SEPERATOR + className : modId;
        return LoggerFactory.getLogger(loggerName);
    }

    /**
     * Uses Java 9+ StackWalker API to find the calling class name.
     * Skips frames related to this LogManager class.
     *
     * @return Optional containing the caller class name or empty if not found.
     */
    private static Optional<String> getCallingClassName() {
        return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(frames -> frames
                .filter(frame -> !frame.getClassName().equals(LogManager.class.getName()))
//                .skip(1) // skip the direct caller of this method
                .findFirst()
                .map(StackWalker.StackFrame::getClassName));
    }

    /**
     * Extracts the simple class name from a fully qualified class name.
     *
     * @param fqcn The fully qualified class name.
     * @return The simple class name.
     */
    private static String simpleName(String fqcn) {
        int lastDot = fqcn.lastIndexOf('.');
        return (lastDot < 0) ? fqcn : fqcn.substring(lastDot + 1);
    }
}
