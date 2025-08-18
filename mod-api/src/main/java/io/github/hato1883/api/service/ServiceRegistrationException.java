package io.github.hato1883.api.service;

public class ServiceRegistrationException extends RuntimeException {
    public ServiceRegistrationException(String message) {
        super(message);
    }

    public ServiceRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
