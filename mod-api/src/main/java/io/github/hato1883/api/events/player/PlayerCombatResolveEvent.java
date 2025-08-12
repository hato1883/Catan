package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired after a combat encounter has been resolved and results are finalized.
 * <p>
 * This event is <b>not cancellable</b>.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(CombatResolveEvent.class, event -> {
 *     System.out.println("Combat resolved between "
 *         + event.getAttacker().getName() + " and " + event.getDefender().getName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PlayerCombatEvent}</li>
 *     <li>{@link PlayerAttackEvent}</li>
 *     <li>{@link PlayerDefendEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerCombatResolveEvent extends PlayerCombatEvent {

    /**
     * Creates a new combat resolution event.
     *
     * @param gameState The current game state
     * @param attacker  The attacking player
     * @param defender  The defending player
     */
    public PlayerCombatResolveEvent(IGameState gameState, IPlayer attacker, IPlayer defender) {
        super(gameState, attacker, defender);
    }
}

