package io.github.hato1883.api.mod.load;

public class ModLoadingException extends RuntimeException {
    public ModLoadingException(String message) {
        super(message);
    }
    public ModLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
    public ModLoadingException(Throwable cause) {
        super(cause);
    }
}

