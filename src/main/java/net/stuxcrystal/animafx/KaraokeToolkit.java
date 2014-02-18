/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 StuxCrystal
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.stuxcrystal.animafx;

import net.stuxcrystal.jass.AssStyle;
import net.stuxcrystal.animafx.structures.TextExtents;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;

/**
 * Creates a new toolkit for karaoke objects.
 */
public class KaraokeToolkit {

    /**
     * The scaling on the font-size for TextExtents.
     */
    private static final int FONT_SIZE_SCALE = Integer.valueOf(System.getProperty("animafx.font.scale", "1"));

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
     * The base-class of AnimaFX.
     */
    private final AnimaFX base;

    /**
     * The context for the font renderer.
     */
    private FontRenderContext ctx;

    /**
     * Creates a new instance of the Toolkit.
     * @param base The instance of AnimaFX.
     */
    KaraokeToolkit(AnimaFX base) {
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
     * Returns the instance of AnimaFX.
     * @return The instance of AnimaFX.
     */
    public AnimaFX getAnimaFX() {
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
        if (SCREEN_DPI != -1) {
            return SCREEN_DPI;
        }

        if (System.getProperties().containsKey("animafx.screen.dpi")) {
            SCREEN_DPI = Integer.valueOf("animagx.screen.dpi");
        } else {
            switch (OperatingSystem.CURRENT_OPERATING_SYSTEM) {
                case WINDOWS:
                case LINUX:
                    SCREEN_DPI = 96;
                    break;

                case MAC:
                    SCREEN_DPI = 72;
                    break;

                default:
                    SCREEN_DPI = 72;
                    break;
            }
        }
        return SCREEN_DPI;
    }
}
