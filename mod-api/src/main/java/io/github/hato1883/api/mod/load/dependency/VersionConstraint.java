package io.github.hato1883.api.mod.load.dependency;

import io.github.hato1883.api.LogManager;
import org.jetbrains.annotations.NotNull;
/**
 * Represents version constraints for mod dependencies supporting semantic versioning
 */
public interface VersionConstraint {
    boolean matches(String version);

    static VersionConstraint any() {
        return new AnyVersion();
    }

    static VersionConstraint exact(String version) {
        return new ExactVersion(version);
    }

    static VersionConstraint tilde(String version) {
        return new TildeVersion(version);
    }

    static VersionConstraint caret(String version) {
        return new CaretVersion(version);
    }

    static VersionConstraint range(String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        return new RangeVersion(minVersion, maxVersion, minInclusive, maxInclusive);
    }

    /**
     * Parses version constraint strings supporting:
     * - "*", "any" or empty: any version
     * - "1.2.3": exact version
     * - "~1.2.3": tilde range (patch-level changes)
     * - "^1.2.3": caret range (compatible changes)
     * - "[1.2.3,2.0.0)": range notation
     * - "(1.0.0,2.0.0]": range notation with exclusive/inclusive bounds
     */
    static VersionConstraint parse(String constraint) {
        if (constraint == null || constraint.equals("null")) {
            LogManager.getLogger("VersionConstraint").error("Version constraint is missing. To allow any version, use '*' or 'any'.");
            throw new IllegalArgumentException("Version constraint is missing. To allow any version, use '*' or 'any'.");
        }

        constraint = constraint.trim();
        if (constraint.equals("*") || constraint.equalsIgnoreCase("any")) {
            return any();
        }

        if (constraint.isEmpty()) {
            LogManager.getLogger("VersionConstraint").error("Version constraint is empty or blank. To allow any version, use '*' or 'any'.");
            throw new IllegalArgumentException("Version constraint is empty or blank. To allow any version, use '*' or 'any'.");
        }

        // Support for >, >=, <, <= operators with proper open-ended range support
        if (constraint.startsWith(">=")) {
            return range(constraint.substring(2).trim(), null, true, false); // [min,)
        }
        if (constraint.startsWith(">")) {
            return range(constraint.substring(1).trim(), null, false, false); // (min,)
        }
        if (constraint.startsWith("<=")) {
            return range(null, constraint.substring(2).trim(), false, true); // (,max]
        }
        if (constraint.startsWith("<")) {
            return range(null, constraint.substring(1).trim(), false, false); // (,max)
        }

        // Tilde version: ~1.2.3 (allows patch-level changes)
        if (constraint.startsWith("~")) {
            return tilde(constraint.substring(1));
        }

        // Caret version: ^1.2.3 (allows compatible changes)
        if (constraint.startsWith("^")) {
            return caret(constraint.substring(1));
        }

        // Range version: [1.0.0,2.0.0) or (1.0.0,2.0.0]
        if (isRangePattern(constraint)) {
            return parseRange(constraint);
        }

        // If the constraint is a valid semantic version, treat as exact
        try {
            SemanticVersion.parse(constraint);
            return exact(constraint);
        } catch (Exception e) {
            String msg = "Invalid version constraint '" + constraint + "'. To allow any version, use '*' or 'any'. Otherwise, use a valid version constraint such as '1.2.3', '~1.2.3', '^1.2.3', or a valid range like [1.0.0,2.0.0).";
            LogManager.getLogger("VersionConstraint").error(msg);
            throw new IllegalArgumentException(msg, e);
        }
    }

    private static boolean isRangePattern(String constraint) {
        return (constraint.startsWith("[") || constraint.startsWith("(")) &&
            (constraint.endsWith("]") || constraint.endsWith(")")) &&
            constraint.contains(",");
    }

    private static VersionConstraint parseRange(String constraint) {
        boolean minInclusive = constraint.startsWith("[");
        boolean maxInclusive = constraint.endsWith("]");

        String inner = constraint.substring(1, constraint.length() - 1);
        String[] parts = inner.split(",", 2);

        if (parts.length != 2) {
            String msg;
            if (!inner.contains(",")) {
                msg = "Invalid range format: '" + constraint + "'. Did you forget a comma? Example: [1.0.0, 2.0.0)";
            } else if (inner.matches(".*\\d,\\d.*")) {
                msg = "Invalid version format in range: '" + constraint + "'. Use dots to separate version parts, e.g., [1.0.0, 2.0.0)";
            } else {
                msg = "Invalid range format: '" + constraint + "'. Use the format [minVersion, maxVersion) or (minVersion, maxVersion]";
            }
            LogManager.getLogger("VersionConstraint").error(msg);
            throw new IllegalArgumentException(msg);
        }

        String minVersion = parts[0].trim();
        String maxVersion = parts[1].trim();

        // If both min and max are present and min > max, swap them
        if (!minVersion.isEmpty() && !maxVersion.isEmpty()) {
            try {
                SemanticVersion min = SemanticVersion.parse(minVersion);
                SemanticVersion max = SemanticVersion.parse(maxVersion);
                if (min.compareTo(max) > 0) {
                    // Swap
                    String temp = minVersion;
                    minVersion = maxVersion;
                    maxVersion = temp;
                    boolean tempInc = minInclusive;
                    minInclusive = maxInclusive;
                    maxInclusive = tempInc;
                }
            } catch (Exception e) {
                String msg = "Invalid version format in range: '" + constraint + "'. Each version must be a valid semantic version, e.g., 1.2.3";
                LogManager.getLogger("VersionConstraint").error(msg);
                throw new IllegalArgumentException(msg, e);
            }
        }

        return range(minVersion, maxVersion, minInclusive, maxInclusive);
    }

    // ========================================
    // Implementation Classes
    // ========================================

    record AnyVersion() implements VersionConstraint {
        @Override
        public boolean matches(String version) {
            return true;
        }

        @Override
        public @NotNull String toString() {
            return "*";
        }
    }

    record ExactVersion(String requiredVersion) implements VersionConstraint {
        public ExactVersion {
            if (requiredVersion == null || requiredVersion.isBlank()) {
                throw new IllegalArgumentException("Required version cannot be null or blank");
            }
        }

        @Override
        public boolean matches(String version) {
            return requiredVersion.equals(version);
        }

        @Override
        public @NotNull String toString() {
            return requiredVersion;
        }
    }

    /**
     * Tilde version constraint: ~1.2.3 allows >=1.2.3 and <1.3.0
     * Reasonably close to the specified version (patch-level changes)
     */
    record TildeVersion(String baseVersion) implements VersionConstraint {
        public TildeVersion {
            if (baseVersion == null || baseVersion.isBlank()) {
                throw new IllegalArgumentException("Base version cannot be null or blank");
            }
        }

        @Override
        public boolean matches(String version) {
            SemanticVersion base = SemanticVersion.parse(baseVersion);
            SemanticVersion candidate = SemanticVersion.parse(version);

            // Must be >= base version
            if (candidate.compareTo(base) < 0) {
                return false;
            }

            // Major and minor must match exactly
            return candidate.major() == base.major() && candidate.minor() == base.minor();
        }

        @Override
        public @NotNull String toString() {
            return "~" + baseVersion;
        }
    }

    /**
     * Caret version constraint: ^1.2.3 allows >=1.2.3 and <2.0.0
     * Compatible with the specified version (no breaking changes)
     */
    record CaretVersion(String baseVersion) implements VersionConstraint {
        public CaretVersion {
            if (baseVersion == null || baseVersion.isBlank()) {
                throw new IllegalArgumentException("Base version cannot be null or blank");
            }
        }

        @Override
        public boolean matches(String version) {
            SemanticVersion base = SemanticVersion.parse(baseVersion);
            SemanticVersion candidate = SemanticVersion.parse(version);

            // Must be >= base version
            if (candidate.compareTo(base) < 0) {
                return false;
            }

            // Major version must match exactly (no breaking changes)
            return candidate.major() == base.major();
        }

        @Override
        public @NotNull String toString() {
            return "^" + baseVersion;
        }
    }

    /**
     * Range version constraint: [1.0.0, 2.0.0) or (1.0.0, 2.0.0]
     */
    record RangeVersion(String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive)
        implements VersionConstraint {

        public RangeVersion {
            // Allow null for open-ended bounds
        }

        @Override
        public boolean matches(String version) {
            SemanticVersion candidate = SemanticVersion.parse(version);
            boolean satisfiesMin = true;
            boolean satisfiesMax = true;
            if (minVersion != null && !minVersion.isBlank()) {
                SemanticVersion min = SemanticVersion.parse(minVersion);
                int minComparison = candidate.compareTo(min);
                satisfiesMin = minInclusive ? minComparison >= 0 : minComparison > 0;
            }
            if (maxVersion != null && !maxVersion.isBlank()) {
                SemanticVersion max = SemanticVersion.parse(maxVersion);
                int maxComparison = candidate.compareTo(max);
                satisfiesMax = maxInclusive ? maxComparison <= 0 : maxComparison < 0;
            }
            return satisfiesMin && satisfiesMax;
        }

        @Override
        public @NotNull String toString() {
            return (minInclusive ? "[" : "(") + (minVersion != null ? minVersion : "") + ", " + (maxVersion != null ? maxVersion : "") + (maxInclusive ? "]" : ")");
        }
    }
}
