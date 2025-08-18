package io.github.hato1883.api;

import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.*;
import io.github.hato1883.api.registries.*;
import io.github.hato1883.api.service.IServiceLocator;
import org.jetbrains.annotations.NotNull;

public final class Registries {

    private static IServiceLocator serviceProvider;

    public static ITileTypeRegistry tiles() { return getProvider().requireService(ITileTypeRegistry.class); }
    public static IBoardTypeRegistry boards() { return getProvider().requireService(IBoardTypeRegistry.class); }
    public static IResourceTypeRegistry resources() { return getProvider().requireService(IResourceTypeRegistry.class); }
    public static IBuildingTypeRegistry buildings() { return getProvider().requireService(IBuildingTypeRegistry.class); }
    public static IRoadTypeRegistry roads() { return getProvider().requireService(IRoadTypeRegistry.class); }
    public static IPortTypeRegistry ports() { return getProvider().requireService(IPortTypeRegistry.class); }
    public static IGamePhaseRegistry phases() { return getProvider().requireService(IGamePhaseRegistry.class); }

    // Convenience registration methods
    public static void register(@NotNull ITileType tile) {
        tiles().register(tile.getId(), tile);
    }

    public static void register(@NotNull IBoardType board) {
        boards().register(board.getIdentifier(), board);
    }

    public static void register(@NotNull IResourceType resource) {
        resources().register(resource.getId(), resource);
    }

    public static void register(@NotNull IBuildingType building) {
        buildings().register(building.getId(), building);
    }

    public static void register(@NotNull IRoadType road) {
        roads().register(road.getId(), road);
    }

    public static void register(@NotNull IPortType port) {
        ports().register(port.getId(), port);
    }

    public static void register(@NotNull IGamePhase building) {
        phases().register(building.getId(), building);
    }

    // Generic registration
    public static <T> void register(@NotNull T item) {
        switch (item) {
            case ITileType tileType -> register(tileType);
            case IBoardType boardType -> register(boardType);
            case IResourceType resourceType -> register(resourceType);
            case IBuildingType buildingType -> register(buildingType);
            case IRoadType roadType -> register(roadType);
            case IPortType portType -> register(portType);
            case IGamePhase gamePhase -> register(gamePhase);
            case null, default -> {
                throw new IllegalArgumentException("Unsupported registration type: " + item.getClass());
            }
        }
    }

    public static void initialize(@NotNull IServiceLocator provider) {
        if (serviceProvider != null) {
            throw new IllegalStateException("Registry already initialized");
        }
        serviceProvider = provider;
    }

    private static IServiceLocator getProvider() {
        if (serviceProvider == null) {
            throw new IllegalStateException("Registry has not been initialized!");
        }
        return serviceProvider;
    }
}
