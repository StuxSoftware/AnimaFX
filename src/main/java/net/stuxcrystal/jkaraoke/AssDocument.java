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

package net.stuxcrystal.jkaraoke;

import net.stuxcrystal.jass.AssFile;
import net.stuxcrystal.jass.AssInfoEntry;

import java.awt.geom.Rectangle2D;

/**
 * The document
 */
public class AssDocument {

    /**
     * Contains the ASS-File.
     */
    private final AssFile file;

    /**
     * Instance to JKaraoke.
     */
    private final JKaraoke karaoke;

    /**
     * The resolution of the document.
     */
    private Rectangle2D size = null;

    /**
     * This is the file.
     * @param file    The file containing the data
     * @param karaoke The reference to JKaraoke.
     */
    public AssDocument(JKaraoke karaoke, AssFile file) {
        this.karaoke = karaoke;
        this.file = file;
    }

    /**
     * Returns the entry with the given name.
     * @param name The name of the entry.
     * @return The value of the entry or {@code null} if the entry was not found.
     */
    private String getEntry(String name) {
        if (this.file.getInfoEntries() == null) {
            return null;
        }

        for (AssInfoEntry entry : this.file.getInfoEntries()) {
            if (entry.getKey().equalsIgnoreCase(name))
                return entry.getValue();
        }
        return null;
    }

    /**
     * Returns the entry with the given name.
     * @param name The name of the entry.
     * @param def  The default value.
     * @return The value of the entry or the default value if the entry was not found.
     */
    private String getEntry(String name, String def) {
        String result = this.getEntry(name);
        if (result == null)
            return def;
        return result;
    }

    /**
     * Returns the size of the document.
     * @return The size of the document.
     */
    public Rectangle2D getSize() {
        if (this.size != null) {
            return this.size;
        }

        int width = Integer.valueOf(this.getEntry("PlayResX", "0"));
        int height = Integer.valueOf(this.getEntry("PlayResY", "0"));

        if (width == 0 && height == 0) {
            this.karaoke.getLogger().warning("No Resolution defined. Assuming 384x288");
            width = 384;
            height = 288;
        } else if (height == 0 && width == 1280) {
            height = 1024;
            this.karaoke.getLogger().warning("Height undefined. Assuming 1280x1024");
        } else if (height == 0) {
            height = width * 3 / 4;
            this.karaoke.getLogger().warning("Height undefined. Assuming " + width + "x" + height);
        } else if (width == 0 && height == 1024) {
            width = 1280;
            this.karaoke.getLogger().warning("Width undefined. Assuming 1280x1024");
        } else if (width == 0) {
            width = height * 4 / 3;
            this.karaoke.getLogger().warning("Width undefined. Assuming "+width+"x"+height);
        } else {
            this.karaoke.getLogger().info("Current script resolution: "+width+"x"+height);
        }

        this.size = new Rectangle2D.Float(0, 0, width, height);
        return this.size;
    }
}
