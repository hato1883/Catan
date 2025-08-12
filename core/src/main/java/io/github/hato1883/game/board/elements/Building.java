package io.github.hato1883.game.board.elements;

import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.game.board.IBuilding;
import io.github.hato1883.api.game.board.IBuildingType;
import io.github.hato1883.api.game.board.VertexCoord;
import io.github.hato1883.game.player.Player;

/**
 * Abstract base class representing a building structure on a vertex in Settlers of Catan.
 * <p>
 * Buildings are player-owned structures that can produce resources when adjacent hexes are activated.
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
public abstract class Building implements IBuilding {

    private final IPlayer owner;
    private final IBuildingType type;
    private final VertexCoord pos;

    public Building(IPlayer owner, IBuildingType type, VertexCoord pos) {
        this.owner = owner;
        this.type = type;
        this.pos = pos;
    }

    /**
     * @return the type of this building
     */
    @Override
    public IBuildingType getType() {
        return type;
    }

    /**
     * @return the board position of this building (e.g. vertex location)
     */
    @Override
    public VertexCoord getPosition() {
        return pos;
    }

    /**
     * @return the player who owns this building
     */
    @Override
    public IPlayer getOwner() {
        return owner;
    }
}
