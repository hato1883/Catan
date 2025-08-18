package io.github.hato1883.api;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Immutable identifier consisting of a namespace (usually modId) and a path (usually resourceId).
 * Examples:
 *   Identifier.of("basemod", "hill")
 *   Identifier.of("basemod:hill")
 *   Identifier.fromMod("basemod", "hill") // convenience for mod authors
 *   InvalidIdentifierException: "Invalid namespace 'base123'. Namespaces must contain only lowercase letters and underscores, no digits allowed."
 *
 * Examples:
 * {@code
 * Identifier.of("basemod:brick");
 * // Ok
 * }
 * {@code
 * Identifier.of("basemod:brick1");
 * // Ok
 * }
 * {@code
 * Identifier.of("basemod1:brick");
 * // InvalidIdentifierException: "Invalid namespace 'base123'. Namespaces must contain only lowercase letters and underscores, no digits allowed."
 * }
 * {@code
 * Identifier.of("basemodbrick");
 * // InvalidIdentifierException: "Invalid identifier format. Missing ':' character. Expected namespace:path but got 'basemodbrick'"
 * }
 * {@code
 * Identifier.of("basemod:Brick!");
 * // InvalidIdentifierException: "Invalid path 'Brick!'. Paths may only contain lowercase letters, digits or underscores."
 * }
 */
public final class Identifier {
    static final Pattern VALID_NAMESPACE = Pattern.compile("^[a-z_-]+$"); // shared
    static final Pattern VALID_PATH = Pattern.compile("^[a-z0-9_-]+$"); // shared

    private final String namespace;
    private final String path;

    private Identifier(String namespace, String path) {
        validate(namespace, path);
        this.namespace = namespace;
        this.path = path;
    }

    public static Identifier of(String namespace, String path) {
        return new Identifier(namespace, path);
    }

    public static Identifier of(String fqn) {
        String[] split = fqn.split(":", 2);
        if (split.length != 2) {
            throw new InvalidIdentifierException(
                "Invalid identifier format. Missing ':' character. Expected namespace:path but got '" + fqn + "'"
            );
        }
        return new Identifier(split[0], split[1]);
    }

    static void validateNamespace(String namespace) {
        if (!VALID_NAMESPACE.matcher(namespace).matches()) {
            throw new InvalidIdentifierException(
                "Invalid namespace '" + namespace + "'. "
                    + "Namespaces must contain only lowercase letters, dashes and underscores, no digits allowed."
            );
        }
    }

    static void validatePath(String path) {
        if (!VALID_PATH.matcher(path).matches()) {
            throw new InvalidIdentifierException(
                "Invalid path '" + path + "'. "
                    + "Paths may only contain lowercase letters, digits, dashes, or underscores."
            );
        }
    }

    static void validate(String namespace, String path) {
        validateNamespace(namespace);
        validatePath(path);
    }

    public String getNamespace() { return namespace; }
    public String getPath() { return path; }

    @Override
    public String toString() { return namespace + ":" + path; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Identifier)) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(namespace, that.namespace) &&
            Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() { return Objects.hash(namespace, path); }
}
