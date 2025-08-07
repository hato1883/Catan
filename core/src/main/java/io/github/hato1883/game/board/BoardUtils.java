package io.github.hato1883.game.board;

import io.github.hato1883.Main;
import io.github.hato1883.game.board.elements.BoardElementFactory;
import io.github.hato1883.game.board.elements.Edge;
import io.github.hato1883.game.board.elements.Vertex;
import io.github.hato1883.game.resource.ResourceType;

import java.util.*;
/**
 * Utility class for board construction and tile generation in Settlers of Catan.
 * <p>
 * Provides static methods for building the game board graph and generating tile distributions.
 * This class cannot be instantiated.
 *
 * <h3>Key Responsibilities:</h3>
 * <ul>
 *     <li>Constructing the board's vertex-edge graph structure</li>
 *     <li>Generating standard and procedural tile distributions</li>
 *     <li>Managing number token allocation</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * // Building a board graph
 * BoardUtils.buildBoardGraph(board, elementFactory);
 *
 * // Generating tiles
 * List<HexTile> tiles = BoardUtils.getStandardCatanTilePool(coords, tileFactory);
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link Board} - The game board this utility operates on</li>
 *     <li>{@link BoardElementFactory} - Creates vertices and edges</li>
 *     <li>{@link HexTileFactory} - Creates hex tiles with resources</li>
 * </ul>
 */
public class BoardUtils {
    /**
     * Private constructor to prevent instantiation.
     * All methods are static and should be accessed directly.
     */
    private BoardUtils() {}

    /**
     * Constructs the complete board graph by creating vertices and edges for all tiles.
     * <p>
     * Performs the operation in two passes:
     * <ol>
     *     <li>Creates all vertices for each tile</li>
     *     <li>Connects vertices with edges</li>
     * </ol>
     *
     * @param board The game board to build the graph for
     * @param factory The factory for creating board elements
     *
     * <h3>Implementation Details:</h3>
     * Each hex tile generates 6 vertices (shared with adjacent tiles) and 6 edges.
     * The factory ensures canonical instances are reused.
     */
    public static void buildBoardGraph(Board board, BoardElementFactory factory) {
        // First pass: create all vertices
        for (HexTile tile : board.getTiles()) {
            createVerticesForTile(tile, board, factory);
        }

        // Second pass: create edges
        for (HexTile tile : board.getTiles()) {
            createEdgesForTile(tile, factory);
        }
    }

    /**
     * Creates all vertices for a single hex tile.
     * <p>
     * Each vertex is shared by 3 tiles (except at board edges).
     * The factory ensures each vertex is only created once.
     *
     * @param tile The tile to create vertices for
     * @param board The game board (for vertex registration)
     * @param factory The factory for creating vertices
     *
     * <h3>Vertex Creation:</h3>
     * Vertices are identified by the three tiles that meet at that point:
     * <ul>
     *     <li>The current tile</li>
     *     <li>The neighbor in direction i</li>
     *     <li>The neighbor in direction (i+1)%6</li>
     * </ul>
     */
    private static void createVerticesForTile(HexTile tile, Board board, BoardElementFactory factory) {
        CubeCoord tileCoord = tile.getCoord();
        CubeCoord[] neighbors = tileCoord.getAllNeighbors();

        for (int i = 0; i < 6; i++) {
            Vertex vertex = factory.getVertex(
                tileCoord,
                neighbors[i],
                neighbors[(i + 1) % 6]
            );

            vertex.addAdjacentTile(tile);
        }
    }

    /**
     * Creates all edges connecting vertices of a single hex tile.
     * <p>
     * Each edge is shared by 2 tiles. The factory ensures each edge is only created once.
     *
     * @param tile The tile to create edges for
     * @param factory The factory for creating edges
     *
     * <h3>Edge Creation:</h3>
     * Connects each vertex to its clockwise neighbor, forming the hexagon's edges.
     * Each edge is automatically linked to both vertices it connects.
     */
    private static void createEdgesForTile(HexTile tile, BoardElementFactory factory) {
        List<Vertex> vertices = new ArrayList<>();

        CubeCoord tileCoord = tile.getCoord();
        CubeCoord[] neighbors = tileCoord.getAllNeighbors();

        for (int i = 0; i < 6; i++) {
            vertices.add(factory.getVertex(
                tileCoord,
                neighbors[i],
                neighbors[(i + 1) % 6]
            ));
        }

        for (int i = 0; i < 6; i++) {
            Vertex v1 = vertices.get(i);
            Vertex v2 = vertices.get((i + 1) % 6);

            factory.getEdge(v1.getVertexCoord(), v2.getVertexCoord());
        }
    }

    /**
     * Generates the standard Catan tile distribution for a 19-tile board.
     *
     * @param coords The set of coordinates to assign tiles to
     * @param factory The factory for creating hex tiles
     * @return A shuffled list of tiles with standard Catan distribution
     *
     * <h3>Standard Distribution:</h3>
     * <ul>
     *     <li>4 Grain tiles</li>
     *     <li>4 Lumber tiles</li>
     *     <li>4 Wool tiles</li>
     *     <li>3 Ore tiles</li>
     *     <li>3 Brick tiles</li>
     *     <li>1 Desert tile</li>
     * </ul>
     *
     * <h3>Number Tokens:</h3>
     * Uses the standard Catan number token distribution (2-12 excluding 7).
     */
    public static List<HexTile> getStandardCatanTilePool(Set<CubeCoord> coords, HexTileFactory factory) {
        List<ResourceType> resources = new ArrayList<>();

        // Classic resource counts
        resources.addAll(Collections.nCopies(4, ResourceType.GRAIN));
        resources.addAll(Collections.nCopies(4, ResourceType.LUMBER));
        resources.addAll(Collections.nCopies(4, ResourceType.WOOL));
        resources.addAll(Collections.nCopies(3, ResourceType.ORE));
        resources.addAll(Collections.nCopies(3, ResourceType.BRICK));
        resources.add(ResourceType.DESERT);

        // Shuffle resources
        Collections.shuffle(resources, Main.PRNG);

        // Assign number tokens (excluding desert)
        List<Integer> tokens = new ArrayList<>(List.of(
            5, 2, 6, 3, 8, 10, 9, 12, 11, 4,
            8, 10, 9, 4, 5, 6, 3, 11 // 18 tokens total
        ));

        // Shuffle tokens
        Collections.shuffle(tokens, Main.PRNG);

        List<HexTile> tiles = new ArrayList<>();
        Iterator<CubeCoord> coordIter = coords.iterator();
        int tokenIndex = 0;

        for (ResourceType resource : resources) {
            int token = (resource == ResourceType.DESERT) ? 0 : tokens.get(tokenIndex++);
            tiles.add(factory.createTile(resource, token, coordIter.next()));
        }

        return tiles;
    }

    /**
     * Generates a procedurally balanced tile distribution for non-standard board sizes.
     *
     * @param coords The set of coordinates to assign tiles to
     * @param factory The factory for creating hex tiles
     * @return A shuffled list of tiles with proportional distribution
     *
     * <h3>Distribution Rules:</h3>
     * <ul>
     *     <li>Maintains proportional resource counts relative to standard Catan</li>
     *     <li>Ensures at least one of each resource type (for boards ≥5 tiles)</li>
     *     <li>Includes exactly one desert for boards ≥6 tiles</li>
     *     <li>Fills remaining tiles with random resources</li>
     * </ul>
     */
    public static List<HexTile> generateProceduralTilePool(Set<CubeCoord> coords, HexTileFactory factory) {
        int tileCount = coords.size();
        if (tileCount < 1) return new ArrayList<>();

        // Use standard distribution if tile count is exactly 19
        if (tileCount == 19) {
            return getStandardCatanTilePool(coords, factory);
        }

        List<ResourceType> resources = new ArrayList<>();

        // Proportional distribution (classic has: 4 grain, 4 lumber, 4 wool, 3 brick, 3 ore, 1 desert)
        double p = tileCount / 19.0;
        int numGrain  = Math.max(1, (int) Math.round(4 * p));
        int numLumber = Math.max(1, (int) Math.round(4 * p));
        int numWool   = Math.max(1, (int) Math.round(4 * p));
        int numOre    = Math.max(1, (int) Math.round(3 * p));
        int numBrick  = Math.max(1, (int) Math.round(3 * p));
        int numDesert = (int) Math.round(tileCount * (1.0 / 19));

        // Add base resources
        resources.addAll(Collections.nCopies(numGrain, ResourceType.GRAIN));
        resources.addAll(Collections.nCopies(numLumber, ResourceType.LUMBER));
        resources.addAll(Collections.nCopies(numWool, ResourceType.WOOL));
        resources.addAll(Collections.nCopies(numOre, ResourceType.ORE));
        resources.addAll(Collections.nCopies(numBrick, ResourceType.BRICK));
        resources.addAll(Collections.nCopies(numDesert, ResourceType.DESERT));

        // Trim or pad list to exact tileCount
        Collections.shuffle(resources, Main.PRNG);
        if (resources.size() > tileCount) {
            resources = resources.subList(0, tileCount);
        } else {
            while (resources.size() < tileCount) {
                resources.add(ResourceType.random());
            }
        }

        // Shuffle again to prevent ordering bias
        Collections.shuffle(resources, Main.PRNG);

        // Generate number tokens (excluding desert)
        List<Integer> tokens = generateNumberTokens(tileCount - numDesert);

        List<HexTile> tiles = new ArrayList<>();
        Iterator<CubeCoord> coordIter = coords.iterator();
        int tokenIndex = 0;

        for (ResourceType resource : resources) {
            int token = (resource == ResourceType.DESERT) ? 0 : tokens.get(tokenIndex++);
            tiles.add(factory.createTile(resource, token, coordIter.next()));
        }

        return tiles;
    }

    /**
     * Generates a fair distribution of number tokens for non-desert tiles.
     *
     * @param count The number of tokens needed
     * @return A shuffled list of number tokens
     *
     * <h3>Token Distribution:</h3>
     * Uses the standard Catan number frequencies:
     * <ul>
     *     <li>2 and 12: 1 each</li>
     *     <li>3 and 11: 2 each</li>
     *     <li>4 and 10: 2 each</li>
     *     <li>5 and 9: 2 each</li>
     *     <li>6 and 8: 2 each</li>
     * </ul>
     * The list is replicated and shuffled to meet the required count.
     */
    public static List<Integer> generateNumberTokens(int count) {
        List<Integer> baseTokens = List.of(
            2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12
        );

        List<Integer> tokens = new ArrayList<>();

        while (tokens.size() < count) {
            List<Integer> shuffled = new ArrayList<>(baseTokens);
            Collections.shuffle(shuffled, Main.PRNG);
            tokens.addAll(shuffled);
        }

        tokens = tokens.subList(0, count);
        Collections.shuffle(tokens, Main.PRNG);
        return tokens;
    }
}
