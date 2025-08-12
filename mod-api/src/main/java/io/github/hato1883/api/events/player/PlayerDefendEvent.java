package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a defending player prepares for or reacts to an attack.
 * <p>
 * This event is cancellable — cancelling will prevent the defense action from proceeding.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerDefendEvent.class, event -> {
 *     if (event.getDefender().hasSpecialShield()) {
 *         System.out.println("Defender has a shield — no damage will be taken.");
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PlayerCombatEvent}</li>
 *     <li>{@link PlayerAttackEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerDefendEvent extends PlayerCombatEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Creates a new player attack event.
     *
     * @param gameState The current game state
     * @param attacker  The attacking player
     * @param defender  The defending player
     */
    public PlayerDefendEvent(IGameState gameState, IPlayer attacker, IPlayer defender) {
        super(gameState, attacker, defender);
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

