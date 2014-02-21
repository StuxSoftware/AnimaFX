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

import net.stuxcrystal.jass.AssFile;
import net.stuxcrystal.jass.AssFileEntry;
import net.stuxcrystal.jass.AssFileEntryUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores multiple values.
 */
public class Cache {

    /**
     * Stores all fonts.
     */
    private Map<String, Font> fonts = new HashMap<>();

    /**
     * Stores all images.
     */
    private Map<String, BufferedImage> images = new HashMap<>();

    /**
     * Reference to AnimaFX.
     */
    private final AnimaFX karaoke;

    /**
     * Creates a new cache for objects inside an ASS-File.
     */
    public Cache(AnimaFX karaoke) {
        this.karaoke = karaoke;
    }

    /**
     * Adds all fonts and images of the file into the cache.
     * @param file The file to add.
     */
    public void addFile(AssFile file) throws IOException {
        // Parse Fonts
        if (file.getFonts() != null) {
            for (AssFileEntry entry : file.getFonts()) {
                this.addFont(entry);
            }
        }

        // Parse Images
        if (file.getGraphics() != null) {
            for (AssFileEntry entry : file.getGraphics()) {
                this.addImage(entry);
            }
        }
    }

    /**
     * Adds a font from the file entry.
     * @param file The file to read.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    public void addFont(AssFileEntry file) throws IOException {
        String name = file.getFileName();
        boolean italic = AssFileEntryUtils.isItalicFont(name);
        boolean bold = AssFileEntryUtils.isBoldFont(name);

        this.karaoke.getLogger().fine("Registering Font: " + name);

        Font font;
        try {
            font = AssFileEntryUtils.getFont(file);
        } catch (FontFormatException e) {
            throw new IOException("Failed to read font", e);
        }

        this.fonts.put(AssFileEntryUtils.getFileName(font.getFontName(), bold, italic, 0), font);
    }

    /**
     * Adds an image to the cache.
     * @param file The file to add.
     * @throws IOException If an I/O-Operation fails.
     */
    public void addImage(AssFileEntry file) throws IOException {
        this.images.put(file.getFileName(), AssFileEntryUtils.getImage(file));
        this.karaoke.getLogger().fine("Registering Image: " + file.getFileName());
    }

    /**
     * Returns the font with the given name.<p />
     *
     * The font will be searched in the cache first and on systems supporting display devices (aka. not headless
     * environments) the system-fonts will be searched too.
     *
     *
     * @param name    The name of the font.
     * @param bold    If the font is bold.
     * @param italic  If the font is italic.
     * @return The given font or null if the font is unknown. Please note that on headless environments
     *         only the fonts inside the cache are supported.
     */
    public Font getFont(String name, boolean bold, boolean italic) {
        Font f = this.getFontFromCache(name, bold, italic);
        if (f == null && !GraphicsEnvironment.isHeadless()) {
            return new Font(name, (bold?Font.BOLD:0) | (italic?Font.ITALIC:0), 20);
        }
        return f;
    }

    /**
     * Returns the font with the given data.<p />
     *
     * Only allows fonts from the cache.
     *
     * @param name    The name of the font.
     * @param bold    Return the bold version of the font.
     * @param italic  Return the italic version of the font.
     * @return The desired font.
     */
    private Font getFontFromCache(String name, boolean bold, boolean italic) {
        Font f = this.fonts.get(AssFileEntryUtils.getFileName(name, bold, italic, 0));
        if (f == null) {
            // Derive from non-bold fonts.
            if (bold) {
                f = this.getFontFromCache(name, false, italic);

                // Font not found.
                if (f == null)
                    return null;

                return f.deriveFont(Font.BOLD);
            }

            // Derive from non-italic fonts.
            if (italic) {
                // Bold is always false when this method is called.
                f = this.getFontFromCache(name, false, false);

                // Font not found
                if (f == null)
                    return null;

                // Makes an italic font.
                return f.deriveFont(Font.ITALIC);
            }

            return null;
        }
        return f;
    }

    /**
     * Returns the image with the specified file.
     * @param filename The name of the file containing the image.
     * @return The image or {@code null} if not found.
     */
    public BufferedImage getImage(String filename) {
        return this.images.get(filename);
    }
}