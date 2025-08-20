package io.github.hato1883.basemod.board;

import io.github.hato1883.api.Factories;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Registries;
import io.github.hato1883.api.entities.resource.IResourceType;

import static io.github.hato1883.api.LogManager.getLogger;
import static io.github.hato1883.basemod.BaseModMain.MOD_ID;

public class BaseCatanResources {

    public static final IResourceType LUMBER = register("lumber", "Lumber", "Finest lumber from the local forest.");
    public static final IResourceType GRAIN = register("grain", "Grain", "Grain from local farmer.");
    public static final IResourceType BRICK = register("brick", "Brick", "Sturdy sun dried clay brick.");
    public static final IResourceType ORE = register("ore", "Ore", "Pure ore from the mountain.");
    public static final IResourceType WOOL = register("wool", "Wool", "Nicest wool in town!");

    public static void initialize() {
        getLogger(MOD_ID).info("Loaded base game resources into register");
    }

    /**
     * Helper method that creates the IResourceType instance, and registers it to the game.
     * @param id unique identifier within this mod for this resource
     * @param name Display of the resource in the game
     * @param description Resource description
     * @return newly created and registered IResourceType
     */
    public static IResourceType register(String id, String name, String description) {
        // Create the tile instance.
        IResourceType tile = Factories.resource().create(Identifier.of(MOD_ID, id), name, description);;

        // Register the tile.
        Registries.register(tile);

        return tile;
    }
}
