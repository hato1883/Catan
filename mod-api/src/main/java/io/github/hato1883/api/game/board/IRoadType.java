package io.github.hato1883.api.game.board;

public interface IRoadType extends IStructureType {

    /**
     * Defines how "strong" or "connected" the road is.
     * Can be used in pathfinding, trade benefits, etc.
     */
    default int getConnectivityScore() {
        return 1;
    }

    /**
     * Whether this road type grants port connection when adjacent
     */
    default boolean grantsPortAccess() {
        return false;
    }

    /**
     * Optional: Whether this road type can only be placed on specific terrain
     * (e.g., only on sand, grass, or modded types)
     *
     */
    default boolean isTerrainAllowed(ITileType tileType) {
        /*
        Example:
            tileType.getId().equals("vanilla:sand")
        OR:
            Set<String> ALLOWED = Set.of("vanilla:sand", "vanilla:grass");
            ALLOWED.contains(tileType.getId());
         */
        return true;
    }
}
