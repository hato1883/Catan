package io.github.hato1883.api;

import io.github.hato1883.api.ui.model.IBoardView;
import io.github.hato1883.api.Services;

/**
 * Facade for accessing the current board view (IBoardView).
 */
public final class Boards {
    private Boards() {}

    /**
     * Returns the current IBoardView instance using the service locator.
     */
    public static IBoardView current() {
        return Services.require(IBoardView.class);
    }
}

