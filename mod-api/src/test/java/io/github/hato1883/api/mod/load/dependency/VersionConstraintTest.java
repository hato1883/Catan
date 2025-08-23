package io.github.hato1883.api.mod.load.dependency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link VersionConstraint} covering pre-release handling, range inversion, and invalid constraints.
 */
@DisplayName("VersionConstraint core logic")
class VersionConstraintTest {

    @Nested
    @DisplayName("Pre-release and range handling")
    class PreReleaseAndRange {
        @Test
        @DisplayName("Pre-release versions are less than release version and compare lexically")
        void preReleaseComparison() {
            SemanticVersion alpha = SemanticVersion.parse("1.0.0-alpha");
            SemanticVersion beta = SemanticVersion.parse("1.0.0-beta");
            SemanticVersion release = SemanticVersion.parse("1.0.0");
            assertTrue(alpha.compareTo(beta) < 0);
            assertTrue(beta.compareTo(release) < 0);
            assertTrue(release.compareTo(alpha) > 0);
        }

        @Test
        @DisplayName("Range with pre-releases includes all between, regardless of order")
        void rangeWithPreReleases() {
            VersionConstraint range = VersionConstraint.parse("[1.0.0, 1.0.0-beta]");
            assertTrue(range.matches("1.0.0-alpha"));
            assertTrue(range.matches("1.0.0-beta"));
            assertTrue(range.matches("1.0.0"));
            assertFalse(range.matches("1.0.1"));
        }

        @Test
        @DisplayName("Range with pre-releases in normal order")
        void rangeWithPreReleasesNormalOrder() {
            VersionConstraint range = VersionConstraint.parse("[1.0.0-alpha, 1.0.0]");
            assertTrue(range.matches("1.0.0-alpha"));
            assertTrue(range.matches("1.0.0-beta"));
            assertTrue(range.matches("1.0.0"));
            assertFalse(range.matches("0.9.9"));
        }
    }

    @Nested
    @DisplayName("Wildcard and invalid constraint handling")
    class WildcardAndInvalidConstraint {
        @Test
        @DisplayName("'*' returns any version constraint")
        void starIsAny() {
            VersionConstraint constraint = VersionConstraint.parse("*");
            assertInstanceOf(VersionConstraint.AnyVersion.class, constraint);
        }

        @Test
        @DisplayName("'any' (case-insensitive) returns any version constraint")
        void anyIsAny() {
            VersionConstraint constraint = VersionConstraint.parse("any");
            assertInstanceOf(VersionConstraint.AnyVersion.class, constraint);
            constraint = VersionConstraint.parse("ANY");
            assertInstanceOf(VersionConstraint.AnyVersion.class, constraint);
        }

        @Test
        @DisplayName("Invalid constraint string throws exception")
        void invalidConstraintThrows() {
            assertThrows(IllegalArgumentException.class, () -> VersionConstraint.parse("not_a_valid_constraint"));
        }

        @Test
        @DisplayName("Empty string throws exception")
        void emptyStringThrows() {
            assertThrows(IllegalArgumentException.class, () -> VersionConstraint.parse("   "));
        }

        @Test
        @DisplayName("Null throws exception")
        void nullThrows() {
            assertThrows(IllegalArgumentException.class, () -> VersionConstraint.parse(null));
        }

        @Test
        @DisplayName("Valid version constraint parses without exception")
        void validConstraintAccepted() {
            assertDoesNotThrow(() -> VersionConstraint.parse("1.2.3"));
            assertDoesNotThrow(() -> VersionConstraint.parse("~1.2.3"));
            assertDoesNotThrow(() -> VersionConstraint.parse("^1.2.3"));
            assertDoesNotThrow(() -> VersionConstraint.parse("[1.0.0,2.0.0)"));
        }
    }
}
