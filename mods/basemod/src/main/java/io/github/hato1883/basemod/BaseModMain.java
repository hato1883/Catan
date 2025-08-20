package io.github.hato1883.basemod;

import io.github.hato1883.api.Registries;
import io.github.hato1883.api.world.board.IBoardType;
import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.IModRegistrar;
import io.github.hato1883.basemod.board.*;

import static io.github.hato1883.api.LogManager.*;

public class BaseModMain implements CatanMod {

    public final static String MOD_ID = "basemod";

    @Override
    public void registerModContent(IModRegistrar registrar) {
        CatanMod.super.registerModContent(registrar);
        getLogger(MOD_ID).info("Boards Register Content: {}", Registries.boards().getAll());
        registerBoard(new DonutBoardType());
        registerBoard(new ClassicHexIslandBoard());
        registerBoard(new ShuffledHexIslandBoard());
        getLogger(MOD_ID).info("Boards Register Content: {}", Registries.boards().getAll());

    }

    private void registerBoard(IBoardType type) {
        Registries.register(type);
    }

    @Override
    public void onInitialize() {
        getLogger(MOD_ID).info("Mod is loaded!");
        getLogger(MOD_ID).info("Resources Register Content: {}", Registries.resources().getAll());
        BaseCatanResources.initialize();
        getLogger(MOD_ID).info("Resources Register Content: {}", Registries.resources().getAll());

        getLogger(MOD_ID).info("Tile Register Content: {}", Registries.tiles().getAll());
        BaseCatanTiles.initialize();
        getLogger(MOD_ID).info("Tile Register Content: {}", Registries.tiles().getAll());
    }

    @Override
    public void onLoad() {
        CatanMod.super.onLoad();
    }

    @Override
    public void onEnable() {
        CatanMod.super.onEnable();
    }

    @Override
    public void onDisable() {
        CatanMod.super.onDisable();
    }
}
