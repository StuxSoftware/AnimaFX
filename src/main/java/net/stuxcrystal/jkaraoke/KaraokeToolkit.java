package net.stuxcrystal.jkaraoke;

import net.stuxcrystal.jass.AssStyle;
import net.stuxcrystal.jkaraoke.structures.TextExtents;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a new toolkit for karaoke objects.
 */
public class KaraokeToolkit {

    /**
     * The scaling on the font-size for TextExtents.
     */
    private static final int FONT_SIZE_SCALE = Integer.valueOf(System.getProperty("jkaraoke.font.scale", "1"));

    /**
     * The DPI of the screen.<p />
     *
     * Returns the DPI of the screen.
     */
    private static float SCREEN_DPI = -1;

    /**
     * The instance of the karaoke toolkit.
     */
    private static KaraokeToolkit INSTANCE;

    /**
     * The base-class of JKaraoke.
     */
    private final JKaraoke base;

    /**
     * The context for the font renderer.
     */
    private FontRenderContext ctx;

    /**
     * Creates a new instance of the Toolkit.
     * @param base The instance of JKaraoke.
     */
    KaraokeToolkit(JKaraoke base) {
        if (KaraokeToolkit.INSTANCE != null)
            throw new IllegalStateException("There is already a toolkit instance.");

        KaraokeToolkit.INSTANCE = this;
        this.base = base;
    }

    /**
     * Initializes the toolkit.
     */
    void initialize() {
        // Create a new Font-Render Context
        // without any transformations but using fractional metrics.
        this.ctx = new FontRenderContext(null, true, true);
    }

    /**
     * Returns the font with the given attributes.
     * @param name    The name of the font.
     * @param bold    Use a bold variant of the font.
     * @param italic  Use an italic variant of the font.
     * @return The desired font.
     */
    public Font getFont(String name, boolean bold, boolean italic) {
        return this.base.getCache().getFont(name, bold, italic);
    }

    /**
     * Returns the image with the given filename.
     * @param name The name of the file.
     * @return The image with the given name.
     */
    public BufferedImage getImage(String name) {
        return this.base.getCache().getImage(name);
    }

    /**
     * Returns the extents of the text.<p />
     *
     * Note that it doesn't seem that there is the correct metrics for the font.
     *
     * @param style The style of the text.
     * @param text  The text to measure.
     * @return The text-extents.
     */
    public TextExtents getTextExtents(AssStyle style, String text) {
        // Reads the font object from the cache.
        Font font = this.getFont(style.getFontname(), style.isBold(), style.isItalic());

        // If the font is unknown, return null.
        if (font == null)
            return null;

        // Convert pt-Font to lfHeight.
        float screen_factor = 72F / KaraokeToolkit.getScreenDPI();

        // Add the font size.
        font = font.deriveFont((float) style.getFontsize() * FONT_SIZE_SCALE * screen_factor);

        // Returns other values like ascend, descend and ext-lead.
        LineMetrics metrics = font.getLineMetrics(text, this.ctx);

        // Calculate String bounds.
        double width = font.getStringBounds(text, this.ctx).getWidth();

        // Returns the text-extents.
        return new TextExtents(
                width / FONT_SIZE_SCALE,
                metrics.getHeight() / FONT_SIZE_SCALE,
                metrics.getAscent() / FONT_SIZE_SCALE,
                metrics.getDescent() / FONT_SIZE_SCALE,
                metrics.getLeading() / FONT_SIZE_SCALE
        );
    }

    /**
     * Returns the style with the given name.
     * @param name The name of the style.
     * @return The style with the given name or {@code null} if the style was not found.
     */
    public AssStyle getStyle(String name) {
        for (AssStyle style : this.base.getInput().getStyles()) {
            if (style.getName().equals(name))
                return style;
        }
        return null;
    }

    /**
     * Returns the instance of JKaraoke.
     * @return The instance of JKaraoke.
     */
    public JKaraoke getJKaraoke() {
        return this.base;
    }

    /**
     * Returns the instance of the toolkit.
     * @return Instance of the toolkit.
     */
    public static KaraokeToolkit getToolkit() {
        return KaraokeToolkit.INSTANCE;
    }

    /**
     * Returns the DPI of the screen accordingly to the current operation system.
     * @return The dpi of the screen.
     */
    private static float getScreenDPI() {
        if (SCREEN_DPI == -1) {
            String defaultDPI = "72";
            if (OperatingSystem.CURRENT_OPERATING_SYSTEM == OperatingSystem.WINDOWS)
                defaultDPI = "96";

            SCREEN_DPI = Integer.valueOf(System.getProperty("jkaraoke.screen.dpi", defaultDPI));
        }
        return SCREEN_DPI;
    }
}
