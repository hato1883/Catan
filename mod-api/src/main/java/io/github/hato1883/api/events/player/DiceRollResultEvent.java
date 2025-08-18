package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;

/**
 * Fired when a player rolls the dice and the result is determined.
 * This event contains both the rolled values and the total sum.
 * It is triggered after the dice roll calculation but before any resource distribution occurs.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(DiceRollResultEvent.class, event -> {
 *     System.out.println("Player rolled: " + event.getDie1() + " + " + event.getDie2() + " = " + event.getTotal());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PlayerEvent}</li>
 *   <li>{@link DiceRollStartEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
 * </ul>
 */
public class DiceRollResultEvent extends PlayerRollDiceEvent {

    private final int die1;
    private final int die2;

    /**
     * Creates a new dice roll result event.
     *
     * @param gameState the current game state
     * @param die1 the value of the first die
     * @param die2 the value of the second die
     */
    public DiceRollResultEvent(IGameState gameState, IPlayer player, int die1, int die2) {
        super(gameState, player);
        this.die1 = die1;
        this.die2 = die2;
    }

    /**
     * Gets the value of the first die.
     *
     * @return the first die value
     *
     * <h3>Defaults:</h3>
     * Returns the exact value rolled for die 1, no modification applied.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * int first = event.getDie1();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getDie2()}</li>
     * </ul>
     */
    public int getDie1() {
        return die1;
    }

    /**
     * Gets the value of the second die.
     *
     * @return the second die value
     *
     * <h3>Defaults:</h3>
     * Returns the exact value rolled for die 2, no modification applied.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * int second = event.getDie2();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getDie1()}</li>
     * </ul>
     */
    public int getDie2() {
        return die2;
    }

    /**
     * Gets the total of both dice.
     *
     * @return the sum of die1 and die2
     *
     * <h3>Defaults:</h3>
     * Computed as {@code die1 + die2}.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * int total = event.getTotal();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getDie1()}</li>
     *   <li>{@link #getDie2()}</li>
     * </ul>
     */
    public int getTotal() {
        return die1 + die2;
    }
}

