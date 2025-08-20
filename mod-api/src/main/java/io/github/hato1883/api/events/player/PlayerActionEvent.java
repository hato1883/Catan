package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.entities.player.IPlayer;

public class PlayerActionEvent extends PlayerEvent implements Cancelable {

    private boolean canceled = false;

    public PlayerActionEvent(IGameState gameState, IPlayer player) {
        super(gameState, player);
    }

    /**
     * Checks if this event has been canceled.
     *
     * @return {@code true} if the event has been canceled, {@code false} otherwise.
     */
    @Override
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Cancels this event.
     * <p>
     * Once an event is canceled, it should not be uncanceled.
     * Multiple calls to this method have no additional effect.
     * </p>
     */
    @Override
    public void cancel() {
        canceled = true;
    }
}
