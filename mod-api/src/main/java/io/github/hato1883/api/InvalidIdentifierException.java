package io.github.hato1883.api;

/**
 * Exception thrown when an Identifier is malformed or fails validation.
 */
public class InvalidIdentifierException extends IllegalArgumentException {
    public InvalidIdentifierException(String message) {
        super(message);
    }

    public InvalidIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
