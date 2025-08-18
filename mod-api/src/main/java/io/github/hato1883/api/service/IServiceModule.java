package io.github.hato1883.api.service;

public interface IServiceModule {
    void registerServices(IServiceRegistrar registrar);
    String getModuleName();
}
