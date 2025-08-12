package io.github.hato1883.api.events.player;

import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;

/**
 * Base class for all combat-related events.
 * <p>
 * This event contains the shared data between attacker and defender during a combat sequence.
 * Specific combat stages (attack, defend, resolution) extend this class.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(CombatEvent.class, event -> {
 *     IPlayer attacker = event.getAttacker();
 *     IPlayer defender = event.getDefender();
 *     System.out.println(attacker.getName() + " is fighting " + defender.getName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PlayerAttackEvent}</li>
 *     <li>{@link PlayerDefendEvent}</li>
 *     <li>{@link PlayerCombatResolveEvent}</li>
 * </ul>
 */
public abstract class PlayerCombatEvent extends PlayerEvent {

    private final IPlayer defender;

    /**
     * Creates a new combat event.
     *
     * @param gameState The current game state
     * @param attacker  The attacking player
     * @param defender  The defending player
     */
    public PlayerCombatEvent(IGameState gameState, IPlayer attacker, IPlayer defender) {
        super(gameState, attacker);
        this.defender = defender;
    }

    /**
     * Gets the attacking player.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer attacker = event.getAttacker();
     * }</pre>
     *
     * @return the attacker
     */
    public IPlayer getAttacker() {
        return super.getPlayer();
    }

    /**
     * Gets the defending player.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer defender = event.getDefender();
     * }</pre>
     *
     * @return the defender
     */
    public IPlayer getDefender() {
        return defender;
    }

    /**
     * @deprecated This method is ambiguous in the context of Combat.
     * Use {@link #getAttacker()} ()} or {@link #getDefender()} ()} instead.
     * Calling this method will throw an exception.
     * @return Nothing, throws exception.
     * @throws UnsupportedOperationException use {@link #getAttacker()} ()} or {@link #getDefender()} ()} instead.
     */
    @Override
    @Deprecated
    public IPlayer getPlayer(){
        throw new UnsupportedOperationException(
            "PlayerCombatEvent does not support getPlayer(). Use getSender() or getReceiver() instead."
        );
    }
}

