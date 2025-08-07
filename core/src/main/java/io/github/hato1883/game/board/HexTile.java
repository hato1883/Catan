package io.github.hato1883.game.board;

import io.github.hato1883.game.board.elements.Vertex;
import io.github.hato1883.game.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single hex tile on the board.
 * Each tile has a resource type and an optional number token (0 if not assigned).
 */
public class HexTile {
    private final CubeCoord coord;  // Make final since coordinate shouldn't change
    private final ResourceType resourceType;
    private final int numberToken;
    private final List<Vertex> vertices;
    private boolean hasRobber;

    public HexTile(CubeCoord coord, ResourceType resourceType, int numberToken) {
        this.coord = coord;
        this.resourceType = resourceType;
        this.numberToken = numberToken;
        this.vertices = new ArrayList<>();
    }

    public void produceResources() {
        if (resourceType == null || hasRobber) return; // Desert

        for (Vertex vertex : vertices) {
            if (vertex.hasBuilding()) {
                vertex.getBuilding().produce(resourceType);
            }
        }
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getNumberToken() {
        return numberToken;
    }

    public CubeCoord getCoord() {
        return coord;
    }

    public void setVertices(List<Vertex> tileVertices) {
        if (!vertices.isEmpty())
            vertices.clear();
        vertices.addAll(tileVertices);
    }
}
