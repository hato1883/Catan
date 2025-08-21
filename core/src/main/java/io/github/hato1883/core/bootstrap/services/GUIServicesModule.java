package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.assets.IAssetProvider;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;
import io.github.hato1883.api.ui.screen.ScreenRegistry;
import io.github.hato1883.api.ui.screen.IScreenManager;
import io.github.hato1883.api.world.board.BoardProvider;
import io.github.hato1883.api.ui.model.BoardViewAdapter;
import io.github.hato1883.api.ui.model.IBoardView;
import io.github.hato1883.api.world.board.IBoard;
import io.github.hato1883.core.assets.AssetProvider;
import io.github.hato1883.core.assets.management.loaders.DefaultRenderAssetLoader;
import io.github.hato1883.core.assets.management.loaders.RenderAssetLoader;
import io.github.hato1883.core.ui.gui.screen.ScreenManager;
import io.github.hato1883.core.ui.gui.screen.ScreenRegistryImpl;

import java.util.function.Supplier;

/**
 * Registers GUI-specific services such as ScreenManager and ScreenRegistry.
 * Allows for easy swapping of GUI/TUI implementations.
 */
public class GUIServicesModule implements IServiceModule {
    @Override
    public void registerServices(IServiceContainer registrar) {
        registrar.register(ScreenRegistry.class, new ScreenRegistryImpl());
        registrar.register(IScreenManager.class, new ScreenManager(registrar.require(ScreenRegistry.class)));
        // Register our asset loader
        registrar.register(RenderAssetLoader.class, (Supplier<? extends RenderAssetLoader>) () ->
            new DefaultRenderAssetLoader("fonts/Roboto-Regular.ttf"));
        // Register AssetProvider using the RenderAssetLoader
        registrar.register(IAssetProvider.class, (Supplier<? extends IAssetProvider>) () ->
            new AssetProvider(registrar.require(RenderAssetLoader.class)));
        // Register BoardProvider and dynamic IBoardView
        registrar.register(IBoardView.class, (Supplier<? extends IBoardView>) () -> {
            IBoard board = registrar.require(BoardProvider.class).getBoard();
            return new BoardViewAdapter(board);
        });
    }
    @Override
    public String getModuleName() {
        return "GUIServicesModule";
    }
}
