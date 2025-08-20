package io.github.hato1883.core.common.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

public class AlignedMessageConverter extends CompositeConverter<ILoggingEvent> {

    private static String VERTICAL_MARKER1 = "|  ";
    private static String HORIZONTAL_MARKER1 = "└─ ";
    private static String VERTICAL_MARKER = "║  ";
    private static String HORIZONTAL_MARKER = "╚═ ";

    @Override
    protected String transform(ILoggingEvent event, String in) {
        if (in == null || in.isEmpty()) return "";

        int sepIndex = in.indexOf(": ");
        if (sepIndex < 0) return in;  // no header found, return as-is

        String header = in.substring(0, sepIndex + 2);
        String message = in.substring(sepIndex + 2);

        // Count number of newlines
        int newlineCount = message.split("\\r?\\n", -1).length - 1;
        if (newlineCount <= 1) {
            // Single-line message, return as-is
            return header + message;
        }

        // Multiline message -> apply markers
        String[] lines = message.split("\\r?\\n", -1);
        StringBuilder sb = new StringBuilder();
        String padding = header.substring(0, header.length()-VERTICAL_MARKER.length()).replaceAll(".", " ");

        for (int i = 0; i < lines.length - 1; i++) {
            if (i == lines.length - 2) {
                sb.append(HORIZONTAL_MARKER).append(lines[i]).append('\n');
            } else if (i == 0) {
                sb.append(lines[i]).append("\n").append(padding);
            } else {
                sb.append(VERTICAL_MARKER).append(lines[i]).append("\n").append(padding);
            }
        }

        return header + sb.toString();
    }
}
