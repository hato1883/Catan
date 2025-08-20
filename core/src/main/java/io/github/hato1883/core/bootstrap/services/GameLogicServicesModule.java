package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.world.board.IBoardGenerator;
import io.github.hato1883.api.services.IServiceModule;
import io.github.hato1883.api.services.IServiceRegistrar;
import io.github.hato1883.core.game.world.board.DefaultBoardGenerator;

import java.util.function.Supplier;

public class GameLogicServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceContainer registrar) {
        registrar.registerIfAbsent(IBoardGenerator.class, (Supplier<? extends IBoardGenerator>) DefaultBoardGenerator::new);
        // Add other game logic services as they're created
        // registrar.registerIfAbsent(IGameEngine.class, GameEngine::new);
        // registrar.registerIfAbsent(IPlayerManager.class, PlayerManager::new);
    }

    @Override
    public String getModuleName() {
        return "GameLogicServices";
    }
}
