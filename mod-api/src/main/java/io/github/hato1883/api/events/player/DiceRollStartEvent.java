package io.github.hato1883.api.events.player;


import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a player is rolling the dice.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(DiceRollStartEvent.class, event -> {
 *     System.out.println("Dice are being rolled");
 *     IGameState state = event.getGameState();
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link GameEvent}</li>
 *   <li>{@link DiceRollResultEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class DiceRollStartEvent extends PlayerRollDiceEvent implements Cancelable {

    private boolean canceled = false;

    public DiceRollStartEvent(IGameState gameState, IPlayer player) {
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
