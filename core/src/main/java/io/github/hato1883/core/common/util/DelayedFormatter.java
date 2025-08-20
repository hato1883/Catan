package io.github.hato1883.core.common.util;

import java.util.Locale;

public class DelayedFormatter {
    public static Object format(String format, Object... args) {
        return new Object() {
            @Override
            public String toString() {
                return String.format(format, args);
            }
        };
    }

    public static Object format(Locale locale, String format, Object... args) {
        return new Object() {
            @Override
            public String toString() {
                return String.format(locale, format, args);
            }
        };
    }
}
