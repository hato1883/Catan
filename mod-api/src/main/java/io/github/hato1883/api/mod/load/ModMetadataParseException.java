package io.github.hato1883.api.mod.load;

public class ModMetadataParseException extends RuntimeException {
    public ModMetadataParseException(String message) {
        super(message);
    }

    public ModMetadataParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
