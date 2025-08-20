package io.github.hato1883.api;

import io.github.hato1883.api.factories.IResourceTypeFactory;
import io.github.hato1883.api.factories.ITileTypeFactory;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.*;
import io.github.hato1883.api.services.IServiceLocator;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Factories {

    private static IServiceLocator serviceProvider;

    // Prevent instantiation
    private Factories() {
        throw new UnsupportedOperationException("Factories is a utility class");
    }

    public static ITileTypeFactory tile() { return getProvider().require(ITileTypeFactory.class); }
    public static IResourceTypeFactory resource() { return getProvider().require(IResourceTypeFactory.class); }
    /*
     * Future factories that we need to expose for our modders.
    public static IBuildingTypeFactory building() { return getProvider().requireService(IBuildingTypeFactory.class); }
    public static IRoadTypeFactory road() { return getProvider().requireService(IRoadTypeFactory.class); }
    public static IPortTypeFactory port() { return getProvider().requireService(IPortTypeFactory.class); }
    public static IGamePhaseFactory phase() { return getProvider().requireService(IGamePhaseFactory.class); }
    */
    public static ITileType create(@NotNull Identifier id, Map<IResourceType, Integer> baseProduction) {
        return tile().create(id, baseProduction);
    }

    public static IResourceType create(@NotNull Identifier id, String name, String description) {
        return resource().create(id, name, description);
    }

    public static IResourceType create(@NotNull Identifier id, String name) {
        return resource().create(id, name);
    }

    public static void initialize(@NotNull IServiceLocator provider) {
        if (serviceProvider != null) {
            throw new IllegalStateException("Factories already initialized");
        }
        serviceProvider = provider;
    }

    private static IServiceLocator getProvider() {
        if (serviceProvider == null) {
            throw new IllegalStateException("Factories has not been initialized!");
        }
        return serviceProvider;
    }
}
