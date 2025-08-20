package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServiceModuleManager {
    private final List<IServiceModule> modules = new ArrayList<>();

    // Builder pattern for easy configuration
    public static class Builder {
        private final List<IServiceModule> modules = new ArrayList<>();

        public Builder withCoreModules() {
            modules.add(new AsyncServicesModule());
            modules.add(new ModLoadingServicesModule());
            modules.add(new FactoryServicesModule());
            modules.add(new RegistryServicesModule());
            modules.add(new GameLogicServicesModule());
            return this;
        }

        public Builder withModule(IServiceModule module) {
            modules.add(Objects.requireNonNull(module, "Module cannot be null"));
            return this;
        }

        public Builder withModules(IServiceModule... modules) {
            for (IServiceModule module : modules) {
                withModule(module);
            }
            return this;
        }

        public ServiceModuleManager build() {
            return new ServiceModuleManager(new ArrayList<>(modules));
        }
    }

    private ServiceModuleManager(List<IServiceModule> modules) {
        this.modules.addAll(modules);
    }

    public void registerAllServices(IServiceContainer registrar) {
        for (IServiceModule module : modules) {
            try {
                module.registerServices(registrar);
            } catch (ServiceLocatorException | IllegalArgumentException e) {
                throw new ServiceRegistrationException(
                    "Failed to register services from module: " + module.getModuleName(), e);
            }
        }
    }

    public List<String> getRegisteredModuleNames() {
        return modules.stream()
            .map(IServiceModule::getModuleName)
            .collect(Collectors.toList());
    }
}
