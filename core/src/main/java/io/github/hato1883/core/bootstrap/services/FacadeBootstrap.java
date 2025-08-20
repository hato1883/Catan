package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.*;
import io.github.hato1883.api.services.IServiceContainer;

public class FacadeBootstrap {

    private static boolean init = false;

    public static void initialize(IServiceContainer container) {
        if (init) {
            throw new IllegalStateException("ServiceBootstrap is already initialized");
        }
        init = true;

        Events.initialize(container);
        Factories.initialize(container);
        ModLoading.initialize(container);
        Registries.initialize(container);
        Services.initialize(container);
    }
}
