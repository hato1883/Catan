package io.github.hato1883.core.common.util;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ConditionalLoggerConverter extends ClassicConverter {

    // Toggle verbose mode (can be set via config or programmatically)
    private static boolean verbose = false;

    // Setter for your app to enable/disable verbose dynamically
    public static void setVerbose(boolean isVerbose) {
        verbose = isVerbose;
    }

    @Override
    public String convert(ILoggingEvent event) {
        String loggerName = event.getLoggerName();

        if (!verbose && loggerName != null && loggerName.startsWith("BaseGame")) {
            // Hide logger info completely
            return "";
        }

        // Otherwise show logger normally (can truncate or format as needed)
        return loggerName == null ? "" : loggerName;
    }
}
