package io.github.hato1883.ui.gui;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import io.github.hato1883.game.board.BuildingManager;
import io.github.hato1883.game.board.elements.Edge;
import io.github.hato1883.game.board.elements.Structure;
import io.github.hato1883.game.board.elements.Vertex;
import io.github.hato1883.game.board.elements.edge.SimpleRoad;
import io.github.hato1883.game.board.elements.vertex.City;
import io.github.hato1883.game.board.elements.vertex.Town;
import io.github.hato1883.game.player.Player;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Handles player input for building placement and upgrades.
 * <p>
 * Works with BuildingManager to enforce game rules while providing visual feedback.
 */
public class InteractionHandler extends InputAdapter {
    private static final float VERTEX_THRESHOLD = 25f; // pixels
    private static final float EDGE_THRESHOLD = 20f;   // pixels
    private final BuildingManager buildingManager;
    private final BuildingRenderer renderer;
    private Player currentPlayer;
    private Class<? extends Structure> currentBuildingType;
    private Vertex hoveredVertex;
    private Edge hoveredEdge;


    private final BiFunction<Integer, Integer, Vector2> screenToWorldConverter;
    private final Function<Vertex, Vector2> vertexPositionProvider;
    private final BiFunction<Vector2, Float, Vertex> findClosestVertex;
    private final BiFunction<Vector2, Float, Edge> findClosestEdge;

    public InteractionHandler(
        BuildingManager manager,
        BuildingRenderer renderer,
        BiFunction<Integer, Integer, Vector2> screenToWorldConverter,
        Function<Vertex, Vector2> vertexPositionProvider,
        BiFunction<Vector2, Float, Vertex> findClosestVertex,
        BiFunction<Vector2, Float, Edge> findClosestEdge
    ) {
        this.buildingManager = manager;
        this.renderer = renderer;
        this.screenToWorldConverter = screenToWorldConverter;
        this.vertexPositionProvider = vertexPositionProvider;
        this.findClosestVertex = findClosestVertex;
        this.findClosestEdge = findClosestEdge;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public void setBuildingMode(Class<? extends Structure> buildingType) {
        this.currentBuildingType = buildingType;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 worldPos = screenToWorldConverter.apply(screenX, screenY);
//        Vector2 worldPos = new Vector2(screenX, screenY);

        // Find closest vertex/edge
        hoveredVertex = findClosestVertex(worldPos);
        hoveredEdge = findClosestEdge(worldPos);

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("Clicked at (%d, %d) \n", screenX, screenY);
        if (currentPlayer == null || currentBuildingType == null) return false;

        Vector2 worldPos = screenToWorldConverter.apply(screenX, screenY);
//        Vector2 worldPos = new Vector2(screenX, screenY);
        System.out.printf("World position at (%f.2, %f.2) \n", worldPos.x, worldPos.y);
        Vertex clickedVertex = findClosestVertex(worldPos);
        Edge clickedEdge = findClosestEdge(worldPos);

        System.out.println("Closet Vertex: " + clickedVertex);
        System.out.println("Closet Edge: " + clickedEdge);

        if (currentBuildingType == Town.class && clickedVertex != null) {
            System.out.println("Trying to build a Town");
            // Success - proceed to next turn
            return buildingManager.placeSettlement(clickedVertex, currentPlayer);
        }
        else if (currentBuildingType == City.class && clickedVertex != null) {
            System.out.println("Trying to build a City");
            // Success
            return buildingManager.upgradeToCity(clickedVertex, currentPlayer);
        }
        else if (currentBuildingType == SimpleRoad.class && clickedEdge != null) {
            System.out.println("Trying to build a Road");
            // Success
            return buildingManager.placeRoad(clickedEdge, currentPlayer);
        }

        return false;
    }

    public void render() {
        // Render preview
        if (hoveredVertex != null && currentBuildingType != null) {
            Vector2 pos = vertexPositionProvider.apply(hoveredVertex);
            renderer.renderPreview(pos.x, pos.y, currentBuildingType, currentPlayer);
        }

        if ((hoveredEdge != null) && (currentBuildingType == SimpleRoad.class)) {
            Vector2 v1Pos = vertexPositionProvider.apply(hoveredEdge.getVertex1());
            Vector2 v2Pos = vertexPositionProvider.apply(hoveredEdge.getVertex2());
            renderer.renderRoadPreview(v1Pos, v2Pos, currentPlayer);
        }
    }

    private Vertex findClosestVertex(Vector2 worldPos) {
        // Implementation to find nearest vertex within threshold
        // TODO: Implement vertex finding logic
        return findClosestVertex.apply(worldPos, VERTEX_THRESHOLD);
    }

    private Edge findClosestEdge(Vector2 worldPos) {
        // Implementation to find nearest edge within threshold
        // TODO: Implement edge finding logic
        return findClosestEdge.apply(worldPos, EDGE_THRESHOLD);
    }
}
