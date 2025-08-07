package io.github.hato1883.game.board.elements.vertex;

import io.github.hato1883.game.player.Player;
import io.github.hato1883.game.resource.ResourceType;

/**
 * Represents a City building in Settlers of Catan that produces 2 resources when activated.
 * <p>
 * Cities are upgraded versions of {@link Town} structures that provide increased resource production.
 *
 * <h3>Game Rules:</h3>
 * <ul>
 *     <li>Produces 2 resources per adjacent activated hex</li>
 *     <li>Counts as 2 victory points (vs Town's 1 point)</li>
 *     <li>Requires 2 Grain and 3 Ore to upgrade from a Town</li>
 *     <li>Must follow same placement rules as Towns</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * Player player = new Player(Color.BLUE);
 * City city = new City(player);
 * city.produce(ResourceType.GRAIN); // Player gains 2 grain
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link Town} - The basic structure that upgrades to City</li>
 *     <li>{@link Building} - Base class for all vertex structures</li>
 *     <li>{@link Player#addResource} - How resources are added to player inventory</li>
 * </ul>
 */
public class City extends Building {

    /**
     * Creates a new City owned by the specified player.
     *
     * @param owner the player who owns this city
     * @throws IllegalStateException if owner is null
     *
     * <h3>Construction Rules:</h3>
     * Cities are typically created by:
     * <ul>
     *     <li>Upgrading an existing Town</li>
     *     <li>Meeting resource requirements (2 Grain + 3 Ore)</li>
     * </ul>
     *
     * <h3>Side Effects:</h3>
     * // TODO: Should automatically increment player's city count
     */
    public City(Player owner) {
        super(owner);
        // TODO: Verify player has met upgrade requirements
    }

    /**
     * Produces double resources when an adjacent hex's number is rolled.
     *
     * @param resource the type of resource to produce
     * @throws IllegalArgumentException if resource is null or invalid
     *
     * <h3>Production Rules:</h3>
     * <ul>
     *     <li>Adds exactly 2 resources to owner's inventory</li>
     *     <li>Only produces if the hex is not a desert</li>
     *     <li>Does not produce if robber is on the hex</li>
     * </ul>
     */
    @Override
    public void produce(ResourceType resource) {
        // Check resource is not null
        if (resource == null)
            throw new IllegalArgumentException("Null is not a valid resource");

        // Desert has no products
        if (resource == ResourceType.DESERT)
            return;

        owner.addResource(resource, 2);
    }

    // TODO: Add getVictoryPoints() method
    /**
     * Gets the victory points awarded for this city.
     * @return always returns 2 victory points
     *
     * <h3>Victory Points:</h3>
     * Cities are worth:
     * <ul>
     *     <li>2 victory points each</li>
     *     <li>Count toward longest road/largest army bonuses</li>
     * </ul>
     */
    // public int getVictoryPoints() { return 2; }
}
