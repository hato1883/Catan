package io.github.hato1883.modloader.dependency;

import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents version constraints for mod dependencies.
 * Supports:
 * - Exact version: 1.0.0
 * - Range: >=1.1.14, <2.0.0
 * - Tilde (~): ~1.2.3  => >=1.2.3 <1.3.0
 * - Caret (^): ^1.2.0  => >=1.2.0 <2.0.0 (rules vary for 0.x.y)
 */
public class VersionConstraint {
    private final List<Range> ranges = new ArrayList<>();

    public static VersionConstraint parse(String input) {
        VersionConstraint constraint = new VersionConstraint();
        String[] parts = input.split(",");
        for (String part : parts) {
            part = part.trim();

            if (part.isEmpty()) continue;

            if (part.startsWith("~")) {
                parseTilde(part.substring(1).trim(), constraint);
            } else if (part.startsWith("^")) {
                parseCaret(part.substring(1).trim(), constraint);
            } else if (part.matches("\\d+\\.\\d+\\.\\d+")) {
                // Exact version
                constraint.ranges.add(new Range(part, part, true, true));
            } else if (part.startsWith(">=") || part.startsWith("<=") || part.startsWith(">") || part.startsWith("<")) {
                parseRange(part, constraint);
            } else {
                throw new JsonParseException("Invalid version constraint: " + part);
            }
        }
        return constraint;
    }

    private static void parseTilde(String version, VersionConstraint constraint) {
        String[] v = version.split("\\.");
        if (v.length != 3) throw new IllegalArgumentException("Tilde requires full version (x.y.z)");
        String max = v[0] + "." + (Integer.parseInt(v[1]) + 1) + ".0";
        constraint.ranges.add(new Range(version, null, true, false));
        constraint.ranges.add(new Range(null, max, false, false));
    }

    private static void parseCaret(String version, VersionConstraint constraint) {
        String[] v = version.split("\\.");
        if (v.length != 3) throw new IllegalArgumentException("Caret requires full version (x.y.z)");
        String max;

        int major = Integer.parseInt(v[0]);
        int minor = Integer.parseInt(v[1]);
        int patch = Integer.parseInt(v[2]);

        if (major > 0) {
            max = (major + 1) + ".0.0";
        } else if (minor > 0) {
            max = "0." + (minor + 1) + ".0";
        } else {
            max = "0.0." + (patch + 1);
        }

        constraint.ranges.add(new Range(version, null, true, false));
        constraint.ranges.add(new Range(null, max, false, false));
    }

    private static void parseRange(String part, VersionConstraint constraint) {
        String op = part.startsWith(">=") || part.startsWith("<=") ? part.substring(0, 2) : part.substring(0, 1);
        String ver = part.substring(op.length()).trim();
        switch (op) {
            case ">=" -> constraint.ranges.add(new Range(ver, null, true, false));
            case ">"  -> constraint.ranges.add(new Range(ver, null, false, false));
            case "<=" -> constraint.ranges.add(new Range(null, ver, false, true));
            case "<"  -> constraint.ranges.add(new Range(null, ver, false, false));
        }
    }

    public boolean matches(String version) {
        for (Range range : ranges) {
            if (!range.matches(version)) {
                return false;
            }
        }
        return true;
    }

    private static class Range {
        String min;
        String max;
        boolean minInclusive;
        boolean maxInclusive;

        Range(String min, String max, boolean minInclusive, boolean maxInclusive) {
            this.min = min;
            this.max = max;
            this.minInclusive = minInclusive;
            this.maxInclusive = maxInclusive;
        }

        boolean matches(String version) {
            if (min != null) {
                int cmp = compareVersions(version, min);
                if (cmp < 0 || (cmp == 0 && !minInclusive)) return false;
            }
            if (max != null) {
                int cmp = compareVersions(version, max);
                return cmp <= 0 && (cmp != 0 || maxInclusive);
            }
            return true;
        }
    }

    public static int compareVersions(String v1, String v2) {
        String[] a1 = v1.split("\\.");
        String[] a2 = v2.split("\\.");
        for (int i = 0; i < Math.max(a1.length, a2.length); i++) {
            int n1 = i < a1.length ? Integer.parseInt(a1[i]) : 0;
            int n2 = i < a2.length ? Integer.parseInt(a2[i]) : 0;
            if (n1 != n2) return Integer.compare(n1, n2);
        }
        return 0;
    }

    @Override
    public String toString() {
        return ranges.toString();
    }
}
