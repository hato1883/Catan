package io.github.hato1883.registries;

public final class RegistryManager {

    private static final GamePhaseRegistry GAME_PHASE_REGISTRY = new GamePhaseRegistry();

    private static final BoardTypeRegistry BOARD_TYPE_REGISTRY = new BoardTypeRegistry();
    private static final TileTypeRegistry TILE_TYPE_REGISTRY = new TileTypeRegistry();
    private static final ResourceTypeRegistry RESOURCE_TYPE_REGISTRY = new ResourceTypeRegistry();

    private static final BuildingTypeRegistry BUILDING_TYPE_REGISTRY = new BuildingTypeRegistry();
    private static final PortTypeRegistry PORT_TYPE_REGISTRY = new PortTypeRegistry();
    private static final RoadTypeRegistry ROAD_TYPE_REGISTRY = new RoadTypeRegistry();

    // Private constructor to prevent instantiation
    private RegistryManager() {}

    public static GamePhaseRegistry getGamePhaseRegistry() {return GAME_PHASE_REGISTRY;}

    public static BoardTypeRegistry getBoardTypeRegistry() {return BOARD_TYPE_REGISTRY;}
    public static TileTypeRegistry getTileTypeRegistry() {return TILE_TYPE_REGISTRY;}
    public static ResourceTypeRegistry getResourceTypeRegistry() {return RESOURCE_TYPE_REGISTRY;}

    public static BuildingTypeRegistry getBuildingTypeRegistry() {return BUILDING_TYPE_REGISTRY;}
    public static PortTypeRegistry getPortTypeRegistry() {return PORT_TYPE_REGISTRY;}
    public static RoadTypeRegistry getRoadTypeRegistry() {return ROAD_TYPE_REGISTRY;}
}

