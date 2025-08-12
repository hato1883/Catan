package io.github.hato1883.game.board.elements;

import io.github.hato1883.game.player.Player;

public class Structure {
    /** The player who owns this structure. Never null after construction. */
    protected final Player owner;

    /**
     * Constructs a new structure owned by the specified player.
     *
     * @param owner the player who owns this structure
     * @throws IllegalStateException if owner is null
     *
     * <h3>Invariants:</h3>
     * <ul>
     *     <li>Owner reference must remain valid for structure's lifetime</li>
     *     <li>Subclasses must call this constructor</li>
     * </ul>
     *
     * <h3>Side Effects:</h3>
     * // TODO: Consider automatically adding structure to player's structure list
     */
    protected Structure(Player owner) {
        if (owner == null)
            throw new IllegalStateException("Structure must have an owner");
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
