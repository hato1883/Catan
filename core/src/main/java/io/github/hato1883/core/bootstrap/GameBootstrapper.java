package io.github.hato1883.core.bootstrap;

import io.github.hato1883.api.ui.IUI;

public class GameBootstrapper {

    private final IUI ui;

    public GameBootstrapper(IUI ui) {
        this.ui = ui;
    }

    public void start() {
        // Show the main menu first
        ui.create();
    }
}


