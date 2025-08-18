package io.github.hato1883.core.service;

import io.github.hato1883.api.game.board.IBoardGenerator;
import io.github.hato1883.api.service.IServiceModule;
import io.github.hato1883.api.service.IServiceRegistrar;
import io.github.hato1883.core.game.board.DefaultBoardGenerator;

import java.util.function.Supplier;

public class GameLogicServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceRegistrar registrar) {
        registrar.register(IBoardGenerator.class, (Supplier<? extends IBoardGenerator>) DefaultBoardGenerator::new);
        // Add other game logic services as they're created
        // registrar.register(IGameEngine.class, GameEngine::new);
        // registrar.register(IPlayerManager.class, PlayerManager::new);
    }

    @Override
    public String getModuleName() {
        return "GameLogicServices";
    }
}
