package io.github.hato1883.game.board.elements;

import com.badlogic.gdx.math.Vector2;
import io.github.hato1883.api.game.board.IBoard;
import io.github.hato1883.api.game.board.ICubeCoord;
import io.github.hato1883.game.player.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.hato1883.api.game.board.CubeCordUtils.cubeToWorld;
import static io.github.hato1883.game.board.elements.Vertex.average;

/**
 * Represents a connection between two vertices on the Catan game board that can hold roads or ships.
 * <p>
 * Edges form the pathways between vertices and are fundamental for:
 * <ul>
 *     <li>Player road/ship networks</li>
 *     <li>Expansion and connectivity</li>
 *     <li>Longest road calculations</li>
 * </ul>
 *
 * <h3>Ownership Rules:</h3>
 * Only one player can own a given edge, and roads/ships must connect to existing player structures.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * Edge edge = new Edge(vertex1, vertex2);
 * if (edge.canBuildRoad(player)) {
 *     edge.buildRoad(RoadType.ROAD, player);
 * }
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link Vertex} - The endpoints of each edge</li>
 *     <li>{@link IBoard} - Manages all edges in the game</li>
 *     <li>{@link Player} - Owners of edge structures</li>
 * </ul>
 */
public class Edge {
    /** The first vertex endpoint of this edge */
    private final Vertex vertex1;

    /** The second vertex endpoint of this edge */
    private final Vertex vertex2;

    /** The player who owns this edge (null if unclaimed) */
    private Player owner;

    /** The type of road/ship on this edge (null if empty) */
    private Road road;

    /**
     * Creates a new edge connecting two vertices.
     *
     * @param vertex1 the first vertex endpoint
     * @param vertex2 the second vertex endpoint
     * @throws IllegalArgumentException if either vertex is null
     *
     * <h3>Initial State:</h3>
     * New edges start with:
     * <ul>
     *     <li>No owner (null)</li>
     *     <li>No road type (null)</li>
     * </ul>
     */
    public Edge(Vertex vertex1, Vertex vertex2) {
        if (vertex1 == null || vertex2 == null) {
            throw new IllegalArgumentException("Edge vertices cannot be null");
        }
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        vertex1.addAdjacentEdge(this);
        vertex2.addAdjacentEdge(this);
    }

    /**
     * Gets the road currently built on this edge, if any.
     * @return the Road object, or null if no road exists
     */
    public Road getRoad() {
        return road;
    }

    /**
     * Gets the opposite vertex of this edge given one endpoint.
     *
     * @param origin one vertex of this edge
     * @return the other vertex of this edge
     * @throws IllegalArgumentException if origin is not part of this edge
     *
     * <h3>Example:</h3>
     * <pre>{@code
     * Edge road = board.getEdge(vertexA, vertexB);
     * Vertex opposite = road.getOther(vertexA); // Returns vertexB
     * }</pre>
     */
    public Vertex getOther(Vertex origin) {
        if (vertex1.equals(origin)) return vertex2;
        else if (vertex2.equals(origin)) return vertex1;
        else throw new IllegalArgumentException("Given vertex is not part of this edge");
    }

    /**
     * Checks if this edge has a road built on it.
     * @return true if a road exists, false otherwise
     */
    public boolean hasRoad() {
        return road != null;
    }

    public Vertex getVertex1() {
        return vertex1;
    }
    public Vertex getVertex2() {
        return vertex2;
    }


    public static Vector2 computeEdgePosition(Edge edge, float radius, float tileGap) {
        List<ICubeCoord> cords = new ArrayList<>();
        cords.addAll(edge.getVertex1().getVertexCoord().getAdjacentTiles());
        cords.addAll(edge.getVertex2().getVertexCoord().getAdjacentTiles());

        if (cords.size() == 6) {
            return average(
                cubeToWorld(cords.get(0), radius, tileGap),
                cubeToWorld(cords.get(1), radius, tileGap),
                cubeToWorld(cords.get(2), radius, tileGap),
                cubeToWorld(cords.get(3), radius, tileGap),
                cubeToWorld(cords.get(4), radius, tileGap),
                cubeToWorld(cords.get(5), radius, tileGap)
            );
        } else {
            throw new IllegalStateException("Not enough cubeCoords to determine vertex position");
        }
    }
}
