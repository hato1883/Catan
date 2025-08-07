package io.github.hato1883.game.board.elements.vertex;

import io.github.hato1883.game.board.elements.Structure;
import io.github.hato1883.game.board.elements.Vertex;
import io.github.hato1883.game.player.Player;
import io.github.hato1883.game.resource.ResourceType;

/**
 * Abstract base class representing a building structure on a vertex in Settlers of Catan.
 * <p>
 * Buildings are player-owned structures that can produce resources when adjacent hexes are activated.
 *
 * <h3>Inheritance Hierarchy:</h3>
 * <ul>
 *     <li>{@link Town} - Produces 1 resource per activation</li>
 *     <li>{@link City} - Produces 2 resources per activation</li>
 * </ul>
 *
 * <h3>Core Responsibilities:</h3>
 * <ul>
 *     <li>Maintains ownership relationship</li>
 *     <li>Defines resource production interface</li>
 *     <li>Enforces valid construction</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * Player player = new Player(Color.RED);
 * Building town = new Town(player);
 * town.produce(ResourceType.WOOL);
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link Vertex} - Location where buildings are placed</li>
 *     <li>{@link Player} - Owner of buildings</li>
 * </ul>
 */
public abstract class Building extends Structure {

    /**
     * Constructs a new building owned by the specified player.
     *
     * @param owner the player who owns this building
     * @throws IllegalStateException if owner is null
     *
     * <h3>Invariants:</h3>
     * <ul>
     *     <li>Owner reference must remain valid for building's lifetime</li>
     *     <li>Subclasses must call this constructor</li>
     * </ul>
     *
     * <h3>Side Effects:</h3>
     * // TODO: Consider automatically adding building to player's structure list
     */
    protected Building(Player owner) {
        super(owner);
    }

    /**
     * Produces resources when an adjacent hex is activated.
     *
     * @param resource the type of resource to produce
     * @throws IllegalStateException if building has no owner
     * @throws IllegalArgumentException if resource is null or invalid
     *
     * <h3>Contract:</h3>
     * Implementations must:
     * <ul>
     *     <li>Add appropriate quantity of resource to owner's inventory</li>
     *     <li>Handle null/invalid resource cases</li>
     *     <li>Verify building is still owned</li>
     * </ul>
     *
     * <h3>Edge Cases:</h3>
     * // FIXME: Should validate resource is not DESERT before production
     */
    public abstract void produce(ResourceType resource);

    public Player getPlayer() {
        return owner;
    }

    // TODO: Add getOwner() method for public access
    /**
     * Gets the owner of this building.
     * @return the player who owns this building
     *
     * <h3>Design Note:</h3>
     * Needed for:
     * <ul>
     *     <li>Victory point calculation</li>
     *     <li>Turn validation</li>
     *     <li>UI rendering</li>
     * </ul>
     */
    // public Player getOwner() { return owner; }
}
