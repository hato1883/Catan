package io.github.hato1883.core.services;

import io.github.hato1883.api.services.IServiceLocator;

import java.util.Map;
import java.util.Optional;

public class OverrideServiceLocator implements IServiceLocator {
    private final IServiceLocator delegate;
    private final Map<Class<?>, Object> overrides;

    public OverrideServiceLocator(IServiceLocator delegate, Map<Class<?>, Object> overrides) {
        this.delegate = delegate;
        this.overrides = overrides;
    }

    @Override
    public <T> T require(Class<T> type) {
        if (overrides.containsKey(type)) {
            return type.cast(overrides.get(type));
        }
        return delegate.require(type);
    }

    @Override
    public <T> boolean contains(Class<T> type) {
        if (overrides.containsKey(type)) {
            return true;
        }
        return delegate.contains(type);
    }

    @Override
    public <T> Optional<T> get(Class<T> type) {
        if (overrides.containsKey(type)) {
            return Optional.of(type.cast(overrides.get(type)));
        }
        return delegate.get(type);
    }
}
