package io.github.hato1883.api;

import io.github.hato1883.api.assets.IAssetProvider;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.events.ui.IBatchingJob;
import io.github.hato1883.api.events.ui.UIRenderEvent;
import io.github.hato1883.api.ui.screen.ICameraScreen;
import io.github.hato1883.api.ui.screen.ScreenRegistry;
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
    private static volatile IUIHandlerRegistry uiHandlerRegistry;
    private static volatile IUIBatchingJobRegistry uiBatchingJobRegistry;
    private static volatile ScreenRegistry screenRegistry;

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

    public static IUIHandlerRegistry uiHandlers() {
        if (uiHandlerRegistry == null) uiHandlerRegistry = getProvider().require(IUIHandlerRegistry.class);
        return uiHandlerRegistry;
    }

    public static IUIBatchingJobRegistry uiBatchingJobs() {
        if (uiBatchingJobRegistry == null) uiBatchingJobRegistry = getProvider().require(IUIBatchingJobRegistry.class);
        return uiBatchingJobRegistry;
    }

    public static IAssetProvider assets() {
        return getProvider().require(IAssetProvider.class);
    }

    public static ScreenRegistry screenRegistry() {
        if (screenRegistry == null) screenRegistry = getProvider().require(ScreenRegistry.class);
        return screenRegistry;
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

    public static IEventListener<UIRenderEvent> registerUIHandler(@NotNull Identifier id, @NotNull IEventListener<UIRenderEvent> handler) {
        return uiHandlers().register(id, handler);
    }

    public static IBatchingJob registerUIBatchingJob(@NotNull Identifier id, @NotNull IBatchingJob job) {
        uiBatchingJobs().register(id, job);
        return job;
    }

    public static ICameraScreen register(@NotNull Identifier id, @NotNull ICameraScreen screen) {
        screenRegistry().registerScreen(id, screen);
        return screen;
    }

    static void initialize(@NotNull IServiceLocator provider) {
        if (serviceProvider != null) {
            throw new IllegalStateException("Registry already initialized");
        }
        serviceProvider = provider;
    }

    static void reset() {
        serviceProvider = null;
        tileRegistry = null;
        boardRegistry = null;
        resourceRegistry = null;
        buildingRegistry = null;
        roadRegistry = null;
        portRegistry = null;
        phaseRegistry = null;
        uiHandlerRegistry = null;
        uiBatchingJobRegistry = null;
        screenRegistry = null;
    }

    static IServiceLocator getProvider() {
        if (serviceProvider == null) {
            throw new IllegalStateException("Registry has not been initialized!");
        }
        return serviceProvider;
    }
}
