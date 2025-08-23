package io.github.hato1883.core.modloading.loading;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link VersionConstraintDeserializer}.
 * <p>
 * This class verifies the correct deserialization of version constraints from JSON strings
 * into {@link VersionConstraint} objects, ensuring both wanted and unwanted behaviors are covered.
 * It tests standard, edge, and error cases for version constraint parsing.
 */
class VersionConstraintDeserializerTest {
    private final ObjectMapper mapper;

    public VersionConstraintDeserializerTest() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(VersionConstraint.class, new VersionConstraintDeserializer());
        mapper.registerModule(module);
    }

    @Nested
    @DisplayName("Deserialization cases")
    class DeserializationCases {
        /**
         * Tests that the wildcard '*' constraint matches any version string.
         * Edge case: Should match all possible versions.
         */
        @Test
        @DisplayName("Wildcard '*' should match any version")
        void testAnyVersion() throws Exception {
            VersionConstraint constraint = mapper.readValue("\"*\"", VersionConstraint.class);
            assertTrue(constraint.matches("1.2.3"));
            assertTrue(constraint.matches("0.0.1"));
        }

        /**
         * Tests that an exact version constraint only matches the specified version.
         * Edge case: Should not match versions that differ by any component.
         */
        @Test
        @DisplayName("Exact version should only match itself")
        void testExactVersion() throws Exception {
            VersionConstraint constraint = mapper.readValue("\"1.2.3\"", VersionConstraint.class);
            assertFalse(constraint.matches("1.2.2"));
            assertTrue(constraint.matches("1.2.3"));
            assertFalse(constraint.matches("1.2.4"));
        }

        /**
         * Tests that a tilde constraint matches the specified version and higher patch versions within the same minor version.
         * Edge case: Should not match lower or next minor versions.
         */
        @Test
        @DisplayName("Tilde constraint should match patch versions in same minor")
        void testTildeVersion() throws Exception {
            VersionConstraint constraint = mapper.readValue("\"~1.2.3\"", VersionConstraint.class);
            assertFalse(constraint.matches("1.2.2"));
            assertTrue(constraint.matches("1.2.3"));
            assertTrue(constraint.matches("1.2.9"));
            assertFalse(constraint.matches("1.3.0"));
        }

        /**
         * Tests that a caret constraint matches compatible versions according to semver rules.
         * Edge case: Should not match next major version.
         */
        @Test
        @DisplayName("Caret constraint should match compatible versions")
        void testCaretVersion() throws Exception {
            VersionConstraint constraint = mapper.readValue("\"^1.2.3\"", VersionConstraint.class);
            assertFalse(constraint.matches("1.2.2"));
            assertTrue(constraint.matches("1.2.3"));
            assertTrue(constraint.matches("1.9.9"));
            assertFalse(constraint.matches("2.0.0"));
        }

        /**
         * Tests that a range constraint with inclusive lower and exclusive upper bounds works as expected.
         * Edge case: Should not match the upper bound.
         */
        @Test
        @DisplayName("Range constraint [inclusive, exclusive)")
        void testRangeVersionInclusiveExclusive() throws Exception {
            VersionConstraint constraint = mapper.readValue("\"[1.2.3,2.0.0)\"", VersionConstraint.class);
            assertFalse(constraint.matches("1.2.2"));
            assertTrue(constraint.matches("1.2.3"));
            assertTrue(constraint.matches("1.9.9"));
            assertFalse(constraint.matches("2.0.0"));
        }

        /**
         * Tests that a greater-than-or-equal constraint matches the specified version and above.
         * Edge case: Should not match below the lower bound.
         */
        @Test
        @DisplayName(">= constraint should match version and above")
        void testGreaterThanOrEqual() throws Exception {
            VersionConstraint constraint = mapper.readValue("\">=1.2.3\"", VersionConstraint.class);
            assertTrue(constraint.matches("1.2.3"));
            assertTrue(constraint.matches("2.0.0"));
            assertFalse(constraint.matches("1.2.2"));
        }

        /**
         * Tests that a less-than constraint matches versions below the specified version.
         * Edge case: Should not match the upper bound.
         */
        @Test
        @DisplayName("< constraint should match below upper bound")
        void testLessThan() throws Exception {
            VersionConstraint constraint = mapper.readValue("\"<2.0.0\"", VersionConstraint.class);
            assertTrue(constraint.matches("1.9.9"));
            assertFalse(constraint.matches("2.0.0"));
        }

        /**
         * Tests that invalid input defaults to the 'any' constraint, matching all versions.
         * Edge case: Should not throw, but match any version.
         */
        @Test
        @DisplayName("Invalid input should default to any version constraint")
        void testInvalidInputDefaultsToAny() throws Exception {
            VersionConstraint constraint = mapper.readValue("\"any\"", VersionConstraint.class);
            assertTrue(constraint.matches("0.0.0"));
            assertTrue(constraint.matches("999.999.999"));
        }

        /**
         * Tests that null input throws an exception with actionable message.
         * Edge case: Should throw, not match any version.
         */
        @Test
        @DisplayName("Null input should throw exception with actionable message")
        void testNullInputThrows() {
            JsonMappingException ex = assertThrows(JsonMappingException.class, () -> mapper.readValue("null", VersionConstraint.class));
            assertInstanceOf(IllegalArgumentException.class, ex.getCause());
            assertTrue(ex.getCause().getMessage().contains("missing") && ex.getCause().getMessage().contains("'*' or 'any'"));
        }

        /**
         * Tests that an empty string input throws an exception with actionable message.
         * Edge case: Should throw, not match any version.
         */
        @Test
        @DisplayName("Empty string input should throw exception with actionable message")
        void testEmptyStringThrows() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> mapper.readValue("\"\"", VersionConstraint.class));
            assertTrue(ex.getMessage().contains("empty or blank") && ex.getMessage().contains("'*' or 'any'"));
        }

        /**
         * Tests that malformed version strings throw an exception with actionable message.
         * Edge case: Should throw, not match any version.
         */
        @Test
        @DisplayName("Malformed version string should throw exception with actionable message")
        void testMalformedVersionStringThrows() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> mapper.readValue("\"not_a_version\"", VersionConstraint.class));
            assertTrue(ex.getMessage().contains("Invalid version constraint") && ex.getMessage().contains("valid version constraint"));
        }

        /**
         * Tests that non-string JSON input (e.g., numbers) throws an exception with actionable message.
         * Edge case: Should throw, not match any version.
         */
        @Test
        @DisplayName("Non-string JSON input should throw exception with actionable message")
        void testNonStringJsonThrows() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> mapper.readValue("123", VersionConstraint.class));
            assertTrue(ex.getMessage().toLowerCase().contains("string") && ex.getMessage().contains("'*' or 'any'"));
        }

        /**
         * Tests that leading/trailing whitespace in the version constraint string is trimmed and parsed correctly.
         * Edge case: Should parse as valid constraint if possible.
         */
        @Test
        @DisplayName("Whitespace in version string should be trimmed and parsed")
        void testWhitespaceTrimmed() throws Exception {
            VersionConstraint constraint = mapper.readValue("\" 1.2.3 \"", VersionConstraint.class);
            assertTrue(constraint.matches("1.2.3"));
            assertFalse(constraint.matches("1.2.4"));
        }
    }
    // No mocking classes needed for this test class.
}
