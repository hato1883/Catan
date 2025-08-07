package io.github.hato1883.ui.gui;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Renders number tokens on hex tiles with either textured sprites or dynamic text.
 * <p>
 * Handles visual representation of dice roll numbers (2-12) with:
 * <ul>
 *     <li>Color-coded text (red for 6/8)</li>
 *     <li>Size scaling based on probability</li>
 *     <li>Optional texture-based rendering</li>
 *     <li>Text outline effects</li>
 * </ul>
 *
 * <h3>Rendering Modes:</h3>
 * <ol>
 *     <li>Textured - Uses pre-made token images</li>
 *     <li>Dynamic Text - Renders numbers with styled fonts</li>
 * </ol>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link HexagonSprite} - The tiles these tokens decorate</li>
 *     <li>{@link GameBoard} - Manages token placement</li>
 * </ul>
 */
public class NumberTokenSprite {
    // FIXME: Consider making colors configurable
    private static final Color RED = Color.RED;
    private static final Color BLACK = Color.BLACK;

    // TODO: Make scaling factors configurable
    private static final float BASE_SCALE = 0.25f;
    private static final float LARGE_SCALE = 1.2f*BASE_SCALE;
    private static final float NORMAL_SCALE = BASE_SCALE;
    private static final float SMALL_SCALE = 0.8f*BASE_SCALE;
    private static final float SMALLEST_SCALE = 0.6f*BASE_SCALE;

    private String numberText;
    private BitmapFont font;
    private TextureRegion texture;
    private float x, y;
    private float size = 32f;
    private boolean useTexture;

    /**
     * Creates a text-based number token.
     *
     * @param numberText the token value (2-12)
     * @param font the font to use for rendering
     * @throws IllegalArgumentException if numberText is null/empty
     *
     * <h3>Rendering Features:</h3>
     * <ul>
     *     <li>Automatically applies color coding</li>
     *     <li>Includes white outline effect</li>
     *     <li>Scales based on roll probability</li>
     * </ul>
     */
    public NumberTokenSprite(String numberText, BitmapFont font) {
        // FIXME: Validate numberText format
        this.numberText = numberText;
        this.font = font;
        this.useTexture = false;
    }

    /**
     * Creates a texture-based number token.
     *
     * @param numberText the token value (for reference)
     * @param texture the pre-made token texture
     * @throws IllegalArgumentException if texture is null
     */
    public NumberTokenSprite(String numberText, TextureRegion texture) {
        if (texture == null) throw new IllegalArgumentException("Texture cannot be null");
        this.numberText = numberText;
        this.texture = texture;
        this.useTexture = true;
    }

    /**
     * Positions the token's center point.
     *
     * @param centerX world X coordinate
     * @param centerY world Y coordinate
     *
     * <h3>Positioning:</h3>
     * Typically centered on a hex tile's midpoint.
     */
    public void setPosition(float centerX, float centerY) {
        this.x = centerX;
        this.y = centerY;
    }

    /**
     * Sets the rendered size for texture-based tokens.
     *
     * @param size width/height in world units
     * @throws IllegalArgumentException if size ≤ 0
     */
    public void setSize(float size) {
        if (size <= 0) throw new IllegalArgumentException("Size must be positive");
        this.size = size;
    }

    /**
     * Renders the token using the provided SpriteBatch.
     *
     * @param batch the active SpriteBatch
     * @throws IllegalStateException if batch is null or not in drawing state
     *
     * <h3>Rendering Logic:</h3>
     * <ul>
     *     <li>Texture mode: Simple centered sprite</li>
     *     <li>Text mode: Styled text with outline effect</li>
     * </ul>
     *
     * <h3>Performance Note:</h3>
     * // TODO: Cache GlyphLayout calculations for static tokens
     */
    public void draw(SpriteBatch batch) {
        if (useTexture && texture != null) {
            batch.draw(texture,
                x - size / 2f, y - size / 2f,
                size, size);
        } else if (font != null) {
            // Determine styling based on token number
            Color color;
            float baseScale;

            if (numberText.equals("6") || numberText.equals("8")) {
                color = RED;
                baseScale = LARGE_SCALE;
            } else if (numberText.equals("5") || numberText.equals("9")) {
                color = BLACK;
                baseScale = NORMAL_SCALE;
            } else if (numberText.equals("4") || numberText.equals("10")) {
                color = Color.BLACK;
                baseScale = SMALL_SCALE;
            } else {
                color = Color.BLACK;
                baseScale = SMALLEST_SCALE;
            }

            // Render text outline
            font.getData().setScale(baseScale);
            font.setColor(Color.WHITE);
            GlyphLayout layout = new GlyphLayout(font, numberText);

            float offset = 1f;
            for (float dx = -offset; dx <= offset; dx += offset) {
                for (float dy = -offset; dy <= offset; dy += offset) {
                    if (dx == 0 && dy == 0) continue;
                    font.draw(batch, numberText,
                        x - layout.width / 2f + dx,
                        y + layout.height / 2f + dy);
                }
            }

            // Render main text
            font.setColor(color);
            font.draw(batch, numberText,
                x - layout.width / 2f,
                y + layout.height / 2f);
        }
    }

    /**
     * Gets the token's numeric value.
     * @return the number as a string (2-12)
     */
    public String getNumberText() {
        return numberText;
    }

    // TODO: Add contains() method for hit testing
    /**
     * Checks if screen coordinates intersect with this token.
     * @param screenX X coordinate
     * @param screenY Y coordinate
     * @return true if coordinates hit the token
     *
     * <h3>Implementation Needed:</h3>
     * Should account for both texture and text rendering modes.
     */
    // public boolean contains(float screenX, float screenY) { ... }
}
