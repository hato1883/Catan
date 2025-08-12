package io.github.hato1883.modloader.dependency;

/**
 * Exception thrown when there is an issue with mod dependencies,
 * such as missing required mods or cyclic dependencies detected.
 */
public class ModDependencyException extends Exception {

    /**
     * Constructs a new ModDependencyException with the specified detail message.
     *
     * @param message the detail message explaining the dependency error.
     */
    public ModDependencyException(String message) {
        super(message);
    }

    /**
     * Constructs a new ModDependencyException with the specified detail message and cause.
     *
     * @param message the detail message explaining the dependency error.
     * @param cause the cause of the exception.
     */
    public ModDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
