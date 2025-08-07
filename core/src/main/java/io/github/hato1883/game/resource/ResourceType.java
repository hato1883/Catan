package io.github.hato1883.game.resource;

import io.github.hato1883.Main;

import java.util.List;

/**
 * Enum representing the resource types found on the board.
 */
public enum ResourceType {
    BRICK,
    LUMBER,
    ORE,
    GRAIN,
    WOOL,
    DESERT; // for desert tile, no production

    /**
     * List of {@link ResourceType} with the same amount of each
     * {@link ResourceType} as in a default Settlers of Catan match.
     * <p></p>
     * Pull a resource at random using {@link  ResourceType#random()} to maintain normal resource distribution
     */
    private static final List<ResourceType> resourcePool = List.of(
            GRAIN, GRAIN, GRAIN, GRAIN,
            LUMBER, LUMBER, LUMBER, LUMBER,
            WOOL, WOOL, WOOL, WOOL,
            BRICK, BRICK, BRICK,
            ORE, ORE, ORE,
            DESERT
    );


    /**
     * Selects a {@link ResourceType} at random using standard Settlers of Catan tile distribution weights.
     * <p>
     * This method is useful for boards of arbitrary size where predefined tile pools (such as radius 3)
     * are no longer used. It ensures the relative proportion of resources mimics the traditional board:
     * <ul>
     *     <li>{@link ResourceType#GRAIN}  ~21%</li>
     *     <li>{@link ResourceType#LUMBER} ~21%</li>
     *     <li>{@link ResourceType#WOOL}   ~21%</li>
     *     <li>{@link ResourceType#ORE}    ~16%</li>
     *     <li>{@link ResourceType#BRICK}  ~16%</li>
     *     <li>{@link ResourceType#DESERT} ~5%</li>
     * </ul>
     *
     * <p>These values are enforced probabilistically using weighted list sampling.
     *
     * @return a pseudo-random {@link ResourceType} with weights approximating the default Catan balance.
     */
    public static ResourceType random() {
        return resourcePool.get(Main.PRNG.nextInt(resourcePool.size()));
    }
}
