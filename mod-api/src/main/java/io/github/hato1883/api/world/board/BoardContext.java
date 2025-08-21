package io.github.hato1883.api.world.board;

import java.util.*;

/**
 * BoardContext holds canonical registries for edges and vertices, and provides
 * API for edge/vertex lookup and edge splitting.
 */
public class BoardContext {
    private final TileEdgeRegistry edgeRegistry = new TileEdgeRegistry();
    private final TileVertexRegistry vertexRegistry = new TileVertexRegistry();
    // Map from edge to the tiles sharing it (for edge splitting)
    private final Map<TileEdge, List<ITilePosition>> edgeToTiles = new HashMap<>();
    // Map from vertex to the tiles sharing it
    private final Map<TileVertex, List<ITilePosition>> vertexToTiles = new HashMap<>();

    /**
     * Get or create a canonical edge between two positions.
     */
    public TileEdge getEdge(ITilePosition a, ITilePosition b) {
        TileEdge edge = edgeRegistry.getOrCreateEdge(a, b);
        edgeToTiles.computeIfAbsent(edge, k -> new ArrayList<>());
        if (!edgeToTiles.get(edge).contains(a)) edgeToTiles.get(edge).add(a);
        if (!edgeToTiles.get(edge).contains(b)) edgeToTiles.get(edge).add(b);
        return edge;
    }

    /**
     * Get or create a canonical vertex for a set of positions.
     */
    public TileVertex getVertex(Collection<ITilePosition> tiles) {
        TileVertex vertex = vertexRegistry.getOrCreateVertex(tiles);
        vertexToTiles.computeIfAbsent(vertex, k -> new ArrayList<>());
        for (ITilePosition pos : tiles) {
            if (!vertexToTiles.get(vertex).contains(pos)) vertexToTiles.get(vertex).add(pos);
        }
        return vertex;
    }

    /**
     * Get all edges for a tile using grid logic and canonicalization.
     */
    public List<TileEdge> getEdgesForTile(ITilePosition pos, ITileGrid grid) {
        List<TileEdge> edges = new ArrayList<>();
        for (List<ITilePosition> edgePair : grid.getEdges(pos)) {
            if (edgePair.size() == 2) {
                edges.add(getEdge(edgePair.get(0), edgePair.get(1)));
            }
        }
        return edges;
    }

    /**
     * Get all vertices for a tile using grid logic and canonicalization.
     */
    public List<TileVertex> getVerticesForTile(ITilePosition pos, ITileGrid grid) {
        List<TileVertex> vertices = new ArrayList<>();
        for (List<ITilePosition> vertexTiles : grid.getVertices(pos)) {
            vertices.add(getVertex(vertexTiles));
        }
        return vertices;
    }

    /**
     * Get all tiles sharing an edge.
     */
    public List<ITilePosition> getTilesAtEdge(TileEdge edge) {
        return edgeToTiles.getOrDefault(edge, Collections.emptyList());
    }

    /**
     * Get all tiles sharing a vertex.
     */
    public List<ITilePosition> getTilesAtVertex(TileVertex vertex) {
        return vertexToTiles.getOrDefault(vertex, Collections.emptyList());
    }

    /**
     * For edge splitting: get all segments for a "long" edge (if needed).
     * This is a stub; real logic should be provided by the board/grid implementation.
     */
    public List<TileEdge> getEdgeSegments(TileEdge edge) {
        // Placeholder: implement splitting logic as needed
        return Collections.singletonList(edge);
    }
}
