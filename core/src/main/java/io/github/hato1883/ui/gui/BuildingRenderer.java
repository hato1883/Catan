package io.github.hato1883.ui.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.hato1883.game.board.elements.BoardElementFactory;
import io.github.hato1883.game.board.elements.Edge;
import io.github.hato1883.game.board.elements.Structure;
import io.github.hato1883.game.board.elements.Vertex;
import io.github.hato1883.game.player.Player;

import java.util.function.Function;

/**
 * Handles rendering of building previews and placed structures.
 * <p>
 * Renders:
 * <ul>
 *     <li>Towns as circles</li>
 *     <li>Cities as squares</li>
 *     <li>Roads as rectangles along edges</li>
 * </ul>
 */
public class BuildingRenderer {
    private final BoardElementFactory elementFactory;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private static final float TOWN_RADIUS = 5f;
    private static final float CITY_SIZE = 5f;
    private static final float ROAD_WIDTH = 8f;

    private final Function<Vertex, Vector2> positionProvider;

    public BuildingRenderer(BoardElementFactory elementFactory,
                            Function<Vertex, Vector2> positionProvider,
                            OrthographicCamera camera) {
        this.elementFactory = elementFactory;
        this.positionProvider = positionProvider;
        this.shapeRenderer = new ShapeRenderer();
        this.camera = camera;
    }

    /**
     * Renders a building preview at screen coordinates.
     *
     * @param screenX X coordinate
     * @param screenY Y coordinate
     * @param structure Class of building to preview
     * @param player Player color to use
     */
    public void renderPreview(float screenX, float screenY, Class<? extends Structure> structure, Player player) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(player.getColor());

        // Render building preview
        // TODO: Find building model from mods

        shapeRenderer.end();
    }

    /**
     * Renders a road preview along an edge.
     *
     * @param start Start position of edge
     * @param end End position of edge
     * @param player Player color to use
     */
    public void renderRoadPreview(Vector2 start, Vector2 end, Player player) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(player.getColor());

        Vector2 direction = new Vector2(end).sub(start);
        Vector2 perpendicular = direction.rotate90(0).nor().scl(ROAD_WIDTH/2);

        shapeRenderer.rectLine(start, end, ROAD_WIDTH);

        shapeRenderer.end();
    }

    /**
     * Renders all placed buildings on the board.
     */
    public void renderBuildings() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Render roads
        for (Edge edge : elementFactory.getAllEdges()) {
            if (edge.hasRoad()) {
                Vector2 v1Pos = positionProvider.apply(edge.getVertex1());
                Vector2 v2Pos = positionProvider.apply(edge.getVertex2());
                shapeRenderer.setColor(edge.getRoad().getOwner().getColor());
                shapeRenderer.rectLine(v1Pos, v2Pos, ROAD_WIDTH);
            }
        }

        // Render buildings
        // TODO: Find building model from mods

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
