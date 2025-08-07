package io.github.hato1883.ui.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.hato1883.Main;
import io.github.hato1883.game.board.Board;
import io.github.hato1883.game.board.BuildingManager;
import io.github.hato1883.game.board.CubeCoord;
import io.github.hato1883.game.board.HexTile;
import io.github.hato1883.game.board.elements.Edge;
import io.github.hato1883.game.board.elements.Structure;
import io.github.hato1883.game.board.elements.Vertex;
import io.github.hato1883.game.board.elements.edge.SimpleRoad;
import io.github.hato1883.game.board.elements.vertex.Town;
import io.github.hato1883.game.board.elements.vertex.VertexCoord;
import io.github.hato1883.game.board.types.HexIslandBoard;
import io.github.hato1883.game.player.Player;
import io.github.hato1883.game.resource.ResourceType;

import java.util.*;

import static io.github.hato1883.game.board.elements.Edge.computeEdgePosition;
import static io.github.hato1883.game.board.elements.Vertex.computeVertexPosition;

/**
 * The primary game screen rendering the Catan board with hexagonal tiles and handling player interaction.
 * <p>
 * Manages:
 * <ul>
 *     <li>Asset loading (textures, fonts)</li>
 *     <li>Dynamic LOD (Level of Detail) rendering</li>
 *     <li>Camera controls</li>
 *     <li>Debug visualization</li>
 * </ul>
 *
 * <h3>Rendering Pipeline:</h3>
 * <ol>
 *     <li>{@link #show()} - Initial setup</li>
 *     <li>{@link #render(float)} - Main game loop</li>
 *     <li>{@link #renderScene()} - Core rendering</li>
 * </ol>
 */
public class GameBoard implements Screen {
    private final Main game;
    private PolygonSpriteBatch polyBatch;
    private SpriteBatch spriteBatch;

    // Asset Management
    private AssetManager assetManager;
    // FIXME: Make these paths configurable via properties file
    private final static String LOD_1024_PATH = "textures/packed/lod_1024/atlas.atlas";
    private final static String LOD_512_PATH = "textures/packed/lod_512/atlas.atlas";
    private final static String LOD_256_PATH = "textures/packed/lod_256/atlas.atlas";
    private final static String LOD_128_PATH = "textures/packed/lod_128/atlas.atlas";
    private Texture whiteTexture;
    private boolean assetsLoaded;

    private final Map<HexTile, HexagonSprite> tileToSpriteMap = new HashMap<>();
    private final Map<Vertex, Vector2> vertexPositionCache = new HashMap<>();
    private final Map<Edge, Vector2> edgePositionCache = new HashMap<>();

    // Rendering State
    private int currentLod = -1;
    private final Map<Integer, List<HexagonSprite>> lodToSprites = new HashMap<>();
    private List<HexagonSprite> currentSpriteList;
    private List<NumberTokenSprite> tokenSprites;

    // Camera
    private OrthographicCamera camera;

    // Game Model
    private Board board;

    // Constants
    // TODO: Make tile radius configurable
    private static final float TILE_RADIUS = 50f;
    private static final float TILE_GAP = 10;
    private final float ROTATION_SYMMETRY = 60f;
    private final float sqrt3 = (float) Math.sqrt(3);

    private List<Player> players;
    private BuildingManager buildingManager;
    private BuildingRenderer buildingRenderer;
    private InteractionHandler interactionHandler;

    public GameBoard(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Fix camera projection
        camera = new OrthographicCamera();
        camera.zoom = 1.0f;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 0, 0);  // center the camera at world origin
        camera.update();

        // Create a SpriteBatch instance to draw from
        polyBatch = new PolygonSpriteBatch();
        spriteBatch = new SpriteBatch();

        // Apply projection
        polyBatch.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        // Load textures Async
        assetManager = new AssetManager();
        assetManager.load(LOD_128_PATH, TextureAtlas.class);
        assetManager.load(LOD_256_PATH, TextureAtlas.class);
        assetManager.load(LOD_512_PATH, TextureAtlas.class);
        assetManager.load(LOD_1024_PATH, TextureAtlas.class);

        // Create Sprites for each tile
        // Example: generate a radius-3 hex map (19 tiles)
        int boardRadius = 3;
        board = new HexIslandBoard(boardRadius);
        buildingManager = new BuildingManager(board);
        buildingRenderer = new BuildingRenderer(board.getElementFactory(), this::getScreenPosition, camera);
        interactionHandler = new InteractionHandler(
            buildingManager,
            buildingRenderer,
            this::convertToWorldCoordinates,
            this::getScreenPosition,
            this::findClosestVertex,
            this::findClosestEdge
        );
        players = new ArrayList<>();
        players.add(new Player(Color.BLUE));
        this.setCurrentPlayer(players.getFirst());
        this.setBuildingMode(SimpleRoad.class);

        Gdx.input.setInputProcessor(interactionHandler);
    }

    private float progress = -1f;

    @Override
    public void render(float delta) {
        if (!assetsLoaded && assetManager.update()) {
            initializeSprites();
            assetsLoaded = true;
        }

        if (assetsLoaded) {
            renderScene();
//            renderDebug();
        } else {
            renderLoading();
        }
    }

    public void setBuildingMode(Class<? extends Structure> buildingType) {
        interactionHandler.setBuildingMode(buildingType);
    }

    public void setCurrentPlayer(Player player) {
        interactionHandler.setCurrentPlayer(player);
    }

    /**
     * Initializes {@link HexagonSprite} objects for each tile on the game board.
     * <p>
     * This method is invoked once after all assets are successfully loaded (see {@link #render(float)}).
     * It uses the appropriate {@link TextureRegion} for each {@link ResourceType} to generate sprites,
     * placing them in the world based on their {@link CubeCoord} position.
     *
     * <h3>Hexagonal Grid Layout (pointy-top, axial coordinates)</h3>
     * The coordinate system uses (q, r, s) cube coordinates mapped to screen-space.
     *
     * <pre>{@code
     * Example using Flat-top hexagons due to ascii art limitation
     *  -R __  /-------\   -Q     /-------\   __ -S
     *    |\  /         \   ↑    /         \  /|
     *       ( -1, 0, 1 )-------( 1, -1, 0 )-------(
     *        \        /         \        /         \
     *        /-------\  0, 0, 0 /-------\ 2, -1, -1 /
     *       /         \        /         \        /
     *      ( -1, 1, 0 )-------( 1, 0, -1 )-------(
     *  \/_  \        /    ↓    \        / _\|
     * +S     --------    +Q     --------   +R
     * }</pre>
     * <p>
     * Each hex is offset/staggered vertically by row (r) with calculated spacing to avoid gaps.
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #getHexagonSprite(HexTile, TextureRegion)} – creates each sprite</li>
     *     <li>{@link #renderScene()} – renders them</li>
     * </ul>
     */
    private void initializeSprites() {
        // TODO: Make font parameters configurable
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto/static/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 144;  // Large font size to maintain quality
        parameter.magFilter = Texture.TextureFilter.Linear;  // Smooth scaling
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.borderColor = Color.WHITE;  // outline color
        parameter.borderWidth = 3;             // thickness of outline (adjust as needed)
        parameter.borderStraight = true;       // straight border, no smoothing
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        font.getData().setScale(0.25f);

        tokenSprites = new ArrayList<>();
        for (int lod : List.of(128, 256, 512, 1024)) {
            TextureAtlas atlas = switch (lod) {
                case 128 -> assetManager.get(LOD_128_PATH);
                case 256 -> assetManager.get(LOD_256_PATH);
                case 512 -> assetManager.get(LOD_512_PATH);
                default -> assetManager.get(LOD_1024_PATH);
            };

            List<HexagonSprite> sprites = new ArrayList<>(board.getTileCount());

            for (Map.Entry<ResourceType, List<HexTile>> entry : board.groupTilesByResource().entrySet()) {
                TextureRegion texture = atlas.findRegion(getRegionName(entry.getKey()));

                for (HexTile tile : entry.getValue()) {
                    HexagonSprite sprite = getHexagonSprite(tile, texture);
                    sprites.add(sprite);
                    tileToSpriteMap.put(tile, sprite);

                    int numberToken = tile.getNumberToken(); // getToken() == 0 if desert
                    if (numberToken > 0 && lod == 128) {
                        if (numberToken == 6 || numberToken == 8) {
                            font.getData().setScale(0.30f);
                            font.setColor(Color.RED);
                        } else if (numberToken == 5 || numberToken == 9) {
                            font.getData().setScale(0.25f);
                            font.setColor(Color.BLACK);
                        } else if (numberToken == 4 || numberToken == 10) {
                            font.getData().setScale(0.20f);
                            font.setColor(Color.BLACK);
                        } else if (numberToken == 3 || numberToken == 11) {
                            font.getData().setScale(0.15f);
                            font.setColor(Color.BLACK);
                        } else if (numberToken == 2 || numberToken == 12) {
                            font.getData().setScale(0.10f);
                            font.setColor(Color.BLACK);
                        }
                        // TODO: Textured Tokens
//                        TextureRegion tokenTex = atlas.findRegion("token_" + numberToken);
                        NumberTokenSprite token = new NumberTokenSprite(String.valueOf(numberToken), font);
                        token.setPosition(
                            sprite.getX() + sprite.getOriginX(),
                            sprite.getY() + sprite.getOriginY()
                        ); // helper method?
                        tokenSprites.add(token);
                    }
                }
            }
            lodToSprites.put(lod, sprites);
        }

        // Set initial current list
        currentLod = 1024;
        currentSpriteList = lodToSprites.get(1024);
    }

    /**
     * Renders the main game scene, including all visible {@link HexagonSprite} tiles.
     * <p>
     * This method is called every frame after assets have finished loading. It:
     * <ol>
     *     <li>Clears the screen</li>
     *     <li>Handles camera zoom input (via {@link #handleZoom()})</li>
     *     <li>Draws all hex tile sprites using the {@link PolygonSpriteBatch}</li>
     * </ol>
     *
     * <h3>Camera Controls</h3>
     * - <code>UP</code> key → zoom in
     * - <code>DOWN</code> key → zoom out
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #render(float)} – calls this method conditionally</li>
     *     <li>{@link #handleZoom()} – modifies camera zoom</li>
     * </ul>
     */
    private void renderScene() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleZoom();
        camera.update();
        polyBatch.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        int selectedLod = getLODFromZoom(camera.zoom);

        if (selectedLod != currentLod) {
            currentLod = selectedLod;
            currentSpriteList = lodToSprites.get(selectedLod);
        }

        polyBatch.begin();
        for (HexagonSprite sprite : currentSpriteList) {
            sprite.draw(polyBatch);
        }
        polyBatch.end();

        spriteBatch.begin();
        for (NumberTokenSprite token : tokenSprites) {
            token.draw(spriteBatch);
        }
        spriteBatch.end();

        buildingRenderer.renderBuildings();
        interactionHandler.render();
    }

    /**
     * Renders debug visualizations on top of the game scene using {@link ShapeRenderer}.
     * <p>
     * For each {@link HexagonSprite}:
     * <ul>
     *     <li>Draws a red bounding box</li>
     *     <li>Draws a blue circle at the sprite’s origin</li>
     * </ul>
     *
     * <h3>Developer Use:</h3>
     * This is helpful for verifying alignment, bounding errors, and origin rotation logic.
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #drawDebug(PolygonSprite, ShapeRenderer)}</li>
     * </ul>
     */
    private void renderDebug() {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        for (HexagonSprite sprite : currentSpriteList) {
            drawDebug(sprite, shapeRenderer);
        }

        shapeRenderer.dispose();  // Optional cleanup
    }

    /**
     * Displays the loading screen while assets are asynchronously loaded via {@link AssetManager}.
     * <p>
     * This method is called every frame during the loading phase of the game loop,
     * before the main scene is ready. The current implementation prints progress to the terminal.
     *
     * <h3>Console Output Format:</h3>
     * <pre>{@code
     * Loading: 43.00%
     * Loading: 84.50%
     * }</pre>
     *
     * <h3>Placeholder Note:</h3>
     * This is a text-based stand-in for a proper visual loading screen.
     * Consider replacing this with an actual UI loading screen.
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #render(float)} – dispatches to either this or {@link #renderScene()} based on loading state</li>
     *     <li>{@link #renderScene()} – displays the actual game after loading is complete</li>
     * </ul>
     */
    private void renderLoading() {
        float newProgress = assetManager.getProgress(); // 0.0 to 1.0
        if (newProgress > progress) {
            progress = newProgress;
            System.out.printf(Locale.US, "Loading: %.2f%%\n", (progress * 100f));
        }
    }

    /**
     * Creates a {@link HexagonSprite} representing a hex tile using the world coordinates and resource type.
     * <p>
     * The method takes a {@link Map.Entry} of {@link CubeCoord} and {@link HexTile}, which provides both
     * the tile's spatial position and its metadata (e.g. {@link ResourceType}). This is convenient
     * when iterating over a {@code Map<CubeCoord, HexTile>} structure.
     *
     * <h3>Why Map.Entry?</h3>
     * Using a map entry allows:
     * <ul>
     *     <li>Access to the tile's coordinate via {@code entry.getKey()}</li>
     *     <li>Access to the tile's data via {@code entry.getValue()}</li>
     * </ul>
     * This avoids requiring two separate parameters when iterating over your tile map.
     *
     * <h3>Hex Layout Formula:</h3>
     * <pre>{@code
     * worldX = xSpacing * (q + r / 2f)
     * worldY = ySpacing * -r
     * }</pre>
     *
     * @param tile a {@link HexTile} with number token and world position info
     * @param texture   the texture region representing the resource type
     * @return a fully configured {@link HexagonSprite}
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #initializeSprites()}</li>
     * </ul>
     */
    private static HexagonSprite getHexagonSprite(HexTile tile, TextureRegion texture) {
        CubeCoord coord = tile.getCoord();

        HexagonSprite sprite = new HexagonSprite(texture, texture.getRegionWidth() / 2f);
        sprite.setScale(TILE_RADIUS * 2 / texture.getRegionWidth());

        float width = (float) (Math.sqrt(3) * TILE_RADIUS);  // width of hex tile
        float height = 2f * TILE_RADIUS;

        // Spacing between hex centers
        float xSpacing = width + TILE_GAP;
        float ySpacing = height * 0.75f + TILE_GAP;

        // Convert cube coordinates to world (x, y)
        float worldX = xSpacing * (coord.q + coord.r / 2f);
        float worldY = ySpacing * -coord.r; // Negative Z -> downward

        // Set pos
        sprite.setPosition(worldX - sprite.getOriginX(), worldY - sprite.getOriginY());
        return sprite;
    }


    /**
     * Handles zoom level changes and LOD switching.
     * <p>
     * Automatically selects the appropriate texture LOD based on current zoom level.
     *
     * @param zoom the current camera zoom factor
     * @return the selected LOD level (128, 256, 512, or 1024)
     *
     * <h3>LOD Selection Logic:</h3>
     * Based on the on-screen pixel size of hex tiles:
     * <ul>
     *     <li>1024 - Zoomed in close</li>
     *     <li>512 - Medium zoom</li>
     *     <li>256 - Default view</li>
     *     <li>128 - Far zoomed out</li>
     * </ul>
     *
     * <h3>Performance Note:</h3>
     * // TODO: Add hysteresis to prevent rapid LOD switching
     */
    private int getLODFromZoom(float zoom) {
        float hexWorldHeight = (float) (2 * TILE_RADIUS);
        float screenScale = Gdx.graphics.getDensity() / zoom;
        float tilePixelHeight = screenScale * hexWorldHeight;

        int selectedLod;
        if (tilePixelHeight > 256) selectedLod = 1024;
        else if (tilePixelHeight > 128) selectedLod = 512;
        else if (tilePixelHeight > 64) selectedLod = 256;
        else selectedLod = 128;

        return selectedLod;
    }

    @Override
    public void resize(int width, int height) {
        // FIXME: Add aspect ratio handling for non-square screens
        camera.setToOrtho(false, width, height);
        camera.position.set(0, 0, 0);
        camera.update();
        polyBatch.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);
    }


    /**
     * Handles user input to zoom the {@link OrthographicCamera} in or out.
     * <p>
     * Controls:
     * <ul>
     *     <li>Press <code>UP</code> to zoom in (decrease zoom value)</li>
     *     <li>Press <code>DOWN</code> to zoom out (increase zoom value)</li>
     * </ul>
     *
     * <h3>Edge Case:</h3>
     * No upper or lower bounds are enforced, so extreme zooms may cause rendering issues.
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #renderScene()}</li>
     * </ul>
     */
    private void handleZoom() {
        float baseZoomSpeed = 1.5f; // 400% increase over a second
        float delta = Gdx.graphics.getDeltaTime();

        // Calculate exponential zoom factor
        float zoomFactor = (float) Math.pow(baseZoomSpeed, delta);
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            camera.zoom /= zoomFactor;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            camera.zoom *= zoomFactor;
        }
        camera.zoom = MathUtils.clamp(camera.zoom, 0.05f, 2f);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    // ... [Previous well-documented methods like initializeSprites() and renderScene() remain unchanged] ...

    /**
     * Creates a debug texture - a 1x1 white pixel.
     * <p>
     * Useful for:
     * <ul>
     *     <li>Placeholder graphics</li>
     *     <li>Debug rendering</li>
     *     <li>Shader effects</li>
     * </ul>
     *
     * <h3>Memory Management:</h3>
     * Caller must manually {@link Texture#dispose()} when finished.
     */
    private Texture createWhiteTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    // TODO: Implement proper scene transition handling
    @Override
    public void hide() {
        // FIXME: Save game state here when implemented
    }

    @Override
    public void dispose() {
        // FIXME: Add null checks
        polyBatch.dispose();
        spriteBatch.dispose();
        assetManager.dispose();
        // TODO: Dispose whiteTexture if created
    }

    /**
     * Converts a {@link ResourceType} into a string name used to fetch the corresponding region in a {@link TextureAtlas}.
     * <p>
     * This mapping is used to locate the correct texture region for rendering resource hexes.
     *
     * @param type the resource type (e.g., {@link ResourceType#LUMBER})
     * @return a region name like "lumber", "ore", "grain", etc.
     * @throws IllegalArgumentException if ResourceType is unrecognized
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * String regionName = getRegionName(ResourceType.ORE);
     * TextureRegion region = atlas.findRegion(regionName);
     * }</pre>
     *
     * <h3>Performance Note:</h3>
     * // TODO: Consider caching results if called frequently
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #initializeSprites()} - Where these mappings are typically used</li>
     *     <li>{@link ResourceType} - The enum containing all valid resource types</li>
     * </ul>
     */
    private String getRegionName(ResourceType type) {
        // TODO: Add support for expansion resources if needed
        return switch (type) {
            case ResourceType.LUMBER -> "lumber";
            case ResourceType.ORE -> "ore";
            case ResourceType.BRICK -> "brick";
            case ResourceType.WOOL -> "wool";
            case ResourceType.GRAIN -> "grain";
            case ResourceType.DESERT -> "desert";
            default -> throw new IllegalArgumentException("Unknown resource type");
        };
    }

    /**
     * Prints detailed debug information about a {@link TextureRegion}.
     *
     * <h3>Logged Info Includes:</h3>
     * <ul>
     *     <li>UV coordinates (U, U2, V, V2)</li>
     *     <li>Region width and height in pixels</li>
     * </ul>
     *
     * @param region the texture region to debug
     */
    private void debugAtlasRegion(TextureRegion region) {
        System.out.println("Region: " + region);
        System.out.println("U: " + region.getU() + ", U2: " + region.getU2());
        System.out.println("V: " + region.getV() + ", V2: " + region.getV2());
        System.out.println("Width: " + region.getRegionWidth() + ", Height: " + region.getRegionHeight());
    }

    /**
     * Logs a formatted debug summary of a {@link PolygonSprite}, including:
     * <ul>
     *     <li>World position (x, y)</li>
     *     <li>Width and height</li>
     *     <li>Origin point</li>
     *     <li>Bounding box coordinates</li>
     * </ul>
     *
     * @param sprite the polygon sprite to inspect
     *
     *               <h3>See Also:</h3>
     *               <ul>
     *                   <li>{@link #drawDebug(PolygonSprite, ShapeRenderer)}</li>
     *               </ul>
     */
    public static void printDebugInfo(PolygonSprite sprite) {
        float x = sprite.getX();
        float y = sprite.getY();
        float width = sprite.getWidth();
        float height = sprite.getHeight();
        float originX = sprite.getOriginX();
        float originY = sprite.getOriginY();

        System.out.println("== HexagonSprite Debug Info ==");
        System.out.printf("Position: (%.2f, %.2f)\n", x, y);
        System.out.printf("Size: %.2f x %.2f\n", width, height);
        System.out.printf("Origin: (%.2f, %.2f)\n", originX, originY);
        System.out.printf("Bounding Box: [%.2f, %.2f] to [%.2f, %.2f]\n",
            x, y, x + width, y + height);
        System.out.println("================================");
    }

    /**
     * Draws debug overlays for a {@link PolygonSprite}:
     * <ul>
     *     <li>A red rectangle for bounding box</li>
     *     <li>A blue filled circle marking the sprite's origin</li>
     * </ul>
     *
     * @param sprite        the target sprite
     * @param shapeRenderer the renderer to draw shapes with
     *
     *                      <h3>Visual Debugging Aid:</h3>
     *                      Helps verify correct scaling, positioning, and origin alignment.
     *
     *                      <h3>See Also:</h3>
     *                      <ul>
     *                          <li>{@link #printDebugInfo(PolygonSprite)}</li>
     *                      </ul>
     */
    public static void drawDebug(PolygonSprite sprite, ShapeRenderer shapeRenderer) {
        float x = sprite.getX();
        float y = sprite.getY();
        float width = sprite.getWidth();
        float height = sprite.getHeight();

        // Convert local origin to world coordinates
        float originWorldX = x + sprite.getOriginX();
        float originWorldY = y + sprite.getOriginY();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Draw bounding box in red
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);

        // Draw origin as blue circle
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.end(); // switch type to Filled for circle
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(originWorldX, originWorldY, 10f); // radius = 10

        shapeRenderer.end();
    }
    // Add these methods:

    /**
     * Converts screen coordinates to world coordinates using the game camera.
     */
    public Vector2 convertToWorldCoordinates(int screenX, int screenY) {
        Vector3 unprojected = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(unprojected.x, unprojected.y);
    }

    /**
     * Gets the screen position of a vertex by averaging adjacent tile positions.
     */
    public Vector2 getScreenPosition(Vertex vertex) {
        // Return cached position if available
        if (vertexPositionCache.containsKey(vertex)) {
            return vertexPositionCache.get(vertex).cpy();
        }

        VertexCoord coord = vertex.getVertexCoord();
        Vector2 position = computeVertexPosition(vertex, TILE_RADIUS, TILE_GAP);
        vertexPositionCache.put(vertex, position.cpy());
        return position;
    }

    /**
     * Gets the screen position of a vertex by averaging adjacent tile positions.
     */
    public Vector2 getScreenPosition(Edge edge) {
        // Return cached position if available
        if (edgePositionCache.containsKey(edge)) {
            return edgePositionCache.get(edge).cpy();
        }

        Vector2 position = computeEdgePosition(edge, TILE_RADIUS, TILE_GAP);
        edgePositionCache.put(edge, position.cpy());
        return position;
    }



    /**
     * Finds the closest vertex to a world position within a threshold
     * @param worldPos The position to check
     * @param threshold Max distance in world units (recommend ~20-30)
     */
    public Vertex findClosestVertex(Vector2 worldPos, float threshold) {
        Vertex closest = null;
        float minDistance = Float.MAX_VALUE;

        for (Vertex vertex : board.getElementFactory().getAllVertices()) {
            Vector2 vertexPos = getScreenPosition(vertex);

            float distance = vertexPos.dst(worldPos);

            if (distance < threshold && distance < minDistance) {
                minDistance = distance;
                closest = vertex;
            }
        }

        return closest;
    }

    /**
     * Finds the closest edge to a world position within a threshold
     * @param worldPos The position to check
     * @param threshold Max distance in world units (recommend ~15-25)
     */
    public Edge findClosestEdge(Vector2 worldPos, float threshold) {
        Edge closest = null;
        float minDistance = Float.MAX_VALUE;

        for (Edge edge : board.getElementFactory().getAllEdges()) {
            Vector2 v1 = getScreenPosition(edge.getVertex1());
            Vector2 v2 = getScreenPosition(edge.getVertex2());

            // Calculate distance from point to line segment
            float distance = distanceToSegment(worldPos, v1, v2);

            if (distance < threshold && distance < minDistance) {
                minDistance = distance;
                closest = edge;
            }
        }

        return closest;
    }

    /**
     * Calculates distance from point P to line segment AB
     */
    private float distanceToSegment(Vector2 P, Vector2 A, Vector2 B) {
        // Vector AP
        float ax = P.x - A.x;
        float ay = P.y - A.y;

        // Vector AB
        float bx = B.x - A.x;
        float by = B.y - A.y;

        // Project AP onto AB
        float dot = ax * bx + ay * by;
        float len_sq = bx * bx + by * by;
        float t = MathUtils.clamp(dot / len_sq, 0, 1);

        // Projection point
        float projx = A.x + t * bx;
        float projy = A.y + t * by;

        // Distance to projection point
        float dx = P.x - projx;
        float dy = P.y - projy;

        return (float)Math.sqrt(dx * dx + dy * dy);
    }
}
