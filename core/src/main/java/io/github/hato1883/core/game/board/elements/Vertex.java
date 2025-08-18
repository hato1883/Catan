package io.github.hato1883.core.game.board.elements;

import com.badlogic.gdx.math.Vector2;
import io.github.hato1883.api.game.board.ICubeCoord;
import io.github.hato1883.core.game.board.HexTileImpl;
import io.github.hato1883.api.game.board.VertexCoord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.hato1883.api.game.board.CubeCordUtils.cubeToWorld;

/**
 * Represents a vertex (intersection point) on the Catan game board where buildings can be placed.
 * <p>
 * Each vertex connects to:
 * <ul>
 *     <li>3 {@link HexTileImpl}s (except at board edges)</li>
 *     <li>2-3 {@link Edge}s (depending on board position)</li>
 * </ul>
 *
 * <h3>Building Rules:</h3>
 * Vertices can contain a {@link Building}
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * Vertex vertex = new Vertex(vertexCoord);
 * vertex.addAdjacentTile(tile);
 * if (vertex.canBuild(player)) {
 *     vertex.build(Building.SETTLEMENT, player);
 * }
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link Edge} - Connections between vertices</li>
 *     <li>{@link io.github.hato1883.api.game.board.IBoard} - Manages all vertices in the game</li>
 * </ul>
 */
public class Vertex {

    /** The coordinate identifier for this vertex */
    private final VertexCoord coord;

    /** The building type placed on this vertex (null if empty) */
    private Building building; // Enum: SETTLEMENT, CITY

    /** List of connected edges (roads/ships) */
    private List<Edge> adjacentEdges;

    /** List of adjacent hex tiles that influence this vertex */
    private List<HexTileImpl> adjacentTiles;

    /**
     * Creates a new vertex at the specified coordinate.
     *
     * @param coord the vertex coordinate identifying this intersection
     * @throws IllegalArgumentException if coord is null
     *
     * <h3>Initial State:</h3>
     * New vertices start with:
     * <ul>
     *     <li>No building (null)</li>
     *     <li>Empty adjacent collections</li>
     * </ul>
     */
    public Vertex(VertexCoord coord) {
        if (coord == null) {
            throw new IllegalArgumentException("VertexCoord cannot be null");
        }
        this.coord = coord;
         this.adjacentEdges = new ArrayList<>();
         this.adjacentTiles = new ArrayList<>();
    }

    /**
     * Checks if this vertex has a building placed on it.
     *
     * @return true if a settlement or city is present, false otherwise
     */
    public boolean hasBuilding() {
        return building != null;
    }

    /**
     * Gets the building type placed on this vertex.
     *
     * @return the current building (SETTLEMENT/CITY) or null if empty
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Gets the coordinate identifier for this vertex.
     *
     * @return the immutable vertex coordinate
     */
    public VertexCoord getVertexCoord() {
        return coord;
    }

    /**
     * Adds an adjacent hex tile to this vertex's influence area.
     *
     * @param tile the hex tile to add
     * @throws IllegalArgumentException if tile is null
     *
     * <h3>Resource Production:</h3>
     * Adjacent tiles determine which resources this vertex's building can receive.
     */
    public void addAdjacentTile(HexTileImpl tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null");
        }
        adjacentTiles.add(tile);
    }

    /**
     * Adds a connecting edge (road/ship) to this vertex.
     *
     * @param edge the edge to connect
     * @throws IllegalArgumentException if edge is null
     *
     * <h3>Road Network:</h3>
     * Connected edges form the road/ship network for player expansion.
     */
    public void addAdjacentEdge(Edge edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge cannot be null");
        }
        adjacentEdges.add(edge);
    }

    /**
     * Gets all edges connected to this vertex.
     * @return unmodifiable list of adjacent edges
     */
    public List<Edge> getAdjacentEdges() {
        return Collections.unmodifiableList(adjacentEdges);
    }

    public static Vector2 computeVertexPosition(Vertex vertex, float radius, float tileGap) {
        List<ICubeCoord> cords = vertex.getVertexCoord().getAdjacentTiles();

        if (cords.size() == 3) {
            return average(
                cubeToWorld(cords.get(0), radius, tileGap),
                cubeToWorld(cords.get(1), radius, tileGap),
                cubeToWorld(cords.get(2), radius, tileGap)
            );
        } else {
            throw new IllegalStateException("Not enough cubeCoords to determine vertex position");
        }
    }

    public static Vector2 average(Vector2... vectors) {
        if (vectors == null || vectors.length == 0) {
            throw new IllegalArgumentException("At least one Vector2 is required to compute average.");
        }

        float sumX = 0f;
        float sumY = 0f;

        for (Vector2 vec : vectors) {
            if (vec != null) {
                sumX += vec.x;
                sumY += vec.y;
            }
        }

        return new Vector2(sumX / vectors.length, sumY / vectors.length);
    }

    // TODO: Implement resource collection logic
    // public List<ResourceType> getProducedResources(int diceRoll) { ... }
}
