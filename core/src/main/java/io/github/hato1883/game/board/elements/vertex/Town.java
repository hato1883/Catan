package io.github.hato1883.game.board.elements.vertex;

import io.github.hato1883.game.player.Player;
import io.github.hato1883.game.resource.ResourceType;

/**
 * Represents a Town building in Settlers of Catan that produces 1 resource when activated.
 * <p>
 * Extends the basic {@link Building} class to implement town-specific resource production behavior.
 *
 * <h3>Game Rules:</h3>
 * <ul>
 *     <li>Produces 1 resource when adjacent hex is activated</li>
 *     <li>Can be upgraded to a {@link City} for increased production</li>
 *     <li>Must be connected to player's road network</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * Town town = new Town();
 * town.setOwner(player);
 * town.produce(ResourceType.WOOL); // Player gains 1 wool
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link City} - The upgraded version producing 2 resources</li>
 *     <li>{@link Building} - Base class for all vertex buildings</li>
 * </ul>
 */
public class Town extends Building {

    /**
     * Creates a new Town owned by the specified player.
     *
     * @param owner the player who owns this town
     * @throws IllegalArgumentException if owner is null
     *
     * <h3>Initialization:</h3>
     * The town will be immediately associated with the owner's building network.
     */
    public Town(Player owner) {
        super(owner);
    }

    /**
     * Produces resources for the town's owner when an adjacent hex is activated.
     *
     * @param resource the type of resource to produce
     * @throws IllegalArgumentException if resource is null or invalid
     *
     * <h3>Production Rules:</h3>
     * <ul>
     *     <li>Adds exactly 1 resource to owner's inventory</li>
     *     <li>Resource type matches the activated hex</li>
     * </ul>
     *
     * <h3>Edge Cases:</h3>
     * if tile has been created with no owner, this method will throw an exception
     */
    @Override
    public void produce(ResourceType resource) {
        // Check resource is not null
        if (resource == null)
            throw new IllegalArgumentException("Null is not a valid resource");

        // Desert has no products
        if (resource == ResourceType.DESERT)
            return;

        // TODO: Consider adding validation for desert hexes (should not produce)
        owner.addResource(resource, 1);
    }

    // TODO: Implement upgrade mechanism to City
    /**
     * Upgrades this town to a city.
     * @return the new City instance
     * @throws IllegalStateException if town has no owner
     *
     * <h3>Game Rules:</h3>
     * Requires:
     * <ul>
     *     <li>Sufficient resources (typically 2 grain, 3 ore)</li>
     *     <li>Valid owner reference</li>
     * </ul>
     */
    // public City upgrade() { ... }
}
