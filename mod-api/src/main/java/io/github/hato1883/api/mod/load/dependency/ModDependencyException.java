package io.github.hato1883.api.mod.load.dependency;

import io.github.hato1883.api.LogManager;

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

    public static void logDependencyException(ModDependencyException ex) {
        StringBuilder msg = new StringBuilder();

        msg.append("Mod loading failed due to dependency issues.\n");

        if (ex.getCause() != null) {
            msg.append("Detailed technical info: ").append(ex.getCause().getMessage()).append("\n");
        }

        msg.append("--- Mod Dependency Error ---\n")
            .append("One or more installed mods cannot be loaded because:\n");

        String exceptionMessage = ex.getMessage();

        if (exceptionMessage.contains("Missing required dependency")) {
            String missingMod = exceptionMessage.replace("Missing required dependency: ", "").trim();
            msg.append("  * A required mod is missing: ").append(missingMod).append("\n")
                .append("    Please download and install the missing mod.\n");
        } else if (exceptionMessage.contains("requires") && exceptionMessage.contains("but found")) {
            // Example: "Mod 'A' requires B >=1.2.0 but found 1.0.0"
            String[] parts = exceptionMessage.split("requires", 2);
            String mod = parts[0].replace("Mod '", "").replace("'", "").trim();
            String depMsg = parts[1].trim();
            msg.append("  * Mod '").append(mod).append("' cannot be loaded because of version mismatch:\n")
                .append("    ").append(depMsg).append("\n")
                .append("    Please update or downgrade the dependency mod to the correct version.\n");
        } else {
            // fallback for unexpected messages
            msg.append("  * ").append(exceptionMessage).append("\n");
        }

        msg.append("You can find the full technical log in the game log files.");

        LogManager.getLogger("Test").error(msg.toString());
    }
}
