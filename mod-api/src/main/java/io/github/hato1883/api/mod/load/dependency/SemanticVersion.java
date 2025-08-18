package io.github.hato1883.api.mod.load.dependency;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a semantic version (major.minor.patch) with optional pre-release and build metadata.
 * Implements Comparable for version ordering.
 */
public record SemanticVersion(int major, int minor, int patch, String preRelease, String buildMetadata)
    implements Comparable<SemanticVersion> {

    private static final Pattern SEMVER_PATTERN = Pattern.compile(
        "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)" +
            "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))" +
            "?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$"
    );

    public SemanticVersion {
        if (major < 0) throw new IllegalArgumentException("Major version cannot be negative");
        if (minor < 0) throw new IllegalArgumentException("Minor version cannot be negative");
        if (patch < 0) throw new IllegalArgumentException("Patch version cannot be negative");
    }

    /**
     * Parses a semantic version string
     */
    public static SemanticVersion parse(String versionString) {
        if (versionString == null || versionString.isBlank()) {
            throw new IllegalArgumentException("Version string cannot be null or blank");
        }

        Matcher matcher = SEMVER_PATTERN.matcher(versionString.trim());
        if (!matcher.matches()) {
            // Fallback: try to parse as simple major.minor.patch
            return parseSimple(versionString.trim());
        }

        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = Integer.parseInt(matcher.group(3));
        String preRelease = matcher.group(4);
        String buildMetadata = matcher.group(5);

        return new SemanticVersion(major, minor, patch, preRelease, buildMetadata);
    }

    /**
     * Simple fallback parser for versions like "1.2.3" or "1.2"
     */
    private static SemanticVersion parseSimple(String versionString) {
        String[] parts = versionString.split("\\.");
        if (parts.length < 1 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid version format: " + versionString);
        }

        try {
            int major = Integer.parseInt(parts[0]);
            int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
            int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

            return new SemanticVersion(major, minor, patch, null, null);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version format: " + versionString, e);
        }
    }

    /**
     * Compares versions according to semantic versioning rules
     */
    @Override
    public int compareTo(SemanticVersion other) {
        // Compare major, minor, patch
        int result = Integer.compare(this.major, other.major);
        if (result != 0) return result;

        result = Integer.compare(this.minor, other.minor);
        if (result != 0) return result;

        result = Integer.compare(this.patch, other.patch);
        if (result != 0) return result;

        // Version numbering is the same, Now we need to determine if
        // 1.0.0-alpha vs 1.0.0-beta
        //

        // Handle pre-release versions (1.0.0-alpha < 1.0.0)
        if (this.preRelease == null && other.preRelease == null) return 0;
        if (this.preRelease == null && other.preRelease != null) return 1;  // 1.0.0 > 1.0.0-alpha
        if (this.preRelease != null && other.preRelease == null) return -1; // 1.0.0-alpha < 1.0.0

        // Both have pre-release, compare lexically
        return this.preRelease.compareTo(other.preRelease);
    }

    @Override
    public @NotNull String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(major).append('.').append(minor).append('.').append(patch);

        if (preRelease != null && !preRelease.isBlank()) {
            sb.append('-').append(preRelease);
        }

        if (buildMetadata != null && !buildMetadata.isBlank()) {
            sb.append('+').append(buildMetadata);
        }

        return sb.toString();
    }

    /**
     * Creates a version with only major.minor.patch (strips pre-release and build metadata)
     */
    public SemanticVersion normalize() {
        return new SemanticVersion(major, minor, patch, null, null);
    }
}
