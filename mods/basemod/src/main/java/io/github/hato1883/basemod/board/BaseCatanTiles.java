package io.github.hato1883.basemod.board;

import io.github.hato1883.api.Factories;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Registries;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;

import java.util.Map;

import static io.github.hato1883.api.LogManager.getLogger;
import static io.github.hato1883.basemod.Main.MOD_ID;

public class BaseCatanTiles {

    // Example of getting a resource from another mod
    public static final ITileType FOREST = register("forest",
        Map.of(
            Registries.resources().require(Identifier.of(MOD_ID, "lumber")), 1
            // Add more lines to allow tile to produce more than 1 type
        )
    );

    // Using resources provided by this mod, where we have a instance of it.
    public static final ITileType FIELD = register("field", Map.of(BaseCatanResources.GRAIN, 1));
    public static final ITileType HILL = register("hill", Map.of(BaseCatanResources.BRICK, 1));
    public static final ITileType MOUNTAIN = register("mountain", Map.of(BaseCatanResources.ORE, 1));
    public static final ITileType PASTURE = register("pasture", Map.of(BaseCatanResources.WOOL, 1));
    public static final ITileType DESERT = register("desert", Map.of());

    public static void initialize() {
        getLogger(MOD_ID).info("Loaded base game tiles into register");
    }

    public static ITileType register(String name, Map<IResourceType, Integer> production) {
        // Create the tile instance.
        ITileType tile = Factories.tile().create(Identifier.of(MOD_ID, name), Map.of());;

        // Register the tile.
        Registries.register(tile);

        return tile;
    }
}
