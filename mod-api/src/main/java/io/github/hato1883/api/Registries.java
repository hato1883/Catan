package io.github.hato1883.api;

import io.github.hato1883.api.world.phase.IGamePhase;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.*;
import io.github.hato1883.api.registries.*;
import io.github.hato1883.api.services.IServiceLocator;
import org.jetbrains.annotations.NotNull;

public final class Registries {

    private static IServiceLocator serviceProvider;
    private static volatile ITileTypeRegistry tileRegistry;
    private static volatile IBoardTypeRegistry boardRegistry;
    private static volatile IResourceTypeRegistry resourceRegistry;
    private static volatile IBuildingTypeRegistry buildingRegistry;
    private static volatile IRoadTypeRegistry roadRegistry;
    private static volatile IPortTypeRegistry portRegistry;
    private static volatile IGamePhaseRegistry phaseRegistry;

    // Prevent instantiation
    private Registries() {
        throw new UnsupportedOperationException("Registries is a utility class");
    }

    public static ITileTypeRegistry tiles() {
        if (tileRegistry == null) tileRegistry = getProvider().require(ITileTypeRegistry.class);
        return tileRegistry;
    }

    public static IBoardTypeRegistry boards() {
        if (boardRegistry == null) boardRegistry = getProvider().require(IBoardTypeRegistry.class);
        return boardRegistry;
    }

    public static IResourceTypeRegistry resources() {
        if (resourceRegistry == null) resourceRegistry = getProvider().require(IResourceTypeRegistry.class);
        return resourceRegistry;
    }

    public static IBuildingTypeRegistry buildings() {
        if (buildingRegistry == null) buildingRegistry = getProvider().require(IBuildingTypeRegistry.class);
        return buildingRegistry;
    }

    public static IRoadTypeRegistry roads() {
        if (roadRegistry == null) roadRegistry = getProvider().require(IRoadTypeRegistry.class);
        return roadRegistry;
    }

    public static IPortTypeRegistry ports() {
        if (portRegistry == null) portRegistry = getProvider().require(IPortTypeRegistry.class);
        return portRegistry;
    }

    public static IGamePhaseRegistry phases() {
        if (phaseRegistry == null) phaseRegistry = getProvider().require(IGamePhaseRegistry.class);
        return phaseRegistry;
    }

    // Convenience registration methods
    public static ITileType register(@NotNull ITileType tile) {
        return tiles().register(tile.getId(), tile);
    }

    public static IBoardType register(@NotNull IBoardType board) {
        return boards().register(board.getIdentifier(), board);
    }

    public static IResourceType register(@NotNull IResourceType resource) {
        return resources().register(resource.getId(), resource);
    }

    public static IBuildingType register(@NotNull IBuildingType building) {
        return buildings().register(building.getId(), building);
    }

    public static IRoadType register(@NotNull IRoadType road) {
        return roads().register(road.getId(), road);
    }

    public static IPortType register(@NotNull IPortType port) {
        return ports().register(port.getId(), port);
    }

    public static IGamePhase register(@NotNull IGamePhase building) {
        return phases().register(building.getId(), building);
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
