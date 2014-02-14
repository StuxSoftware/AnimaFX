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

package net.stuxcrystal.jass;

/**
 * The sections of an ASS-File.
 */
public enum AssSections {

    /**
     * Represents the script-info section.
     */
    SCRIPT_INFO(new AssInfoSection()),

    /**
     * Represents the style section.
     */
    STYLES(new AssStyleSection()),

    /**
     * Lists all events
     */
    EVENTS(new AssEventSection()),

    /**
     * Lists all stored graphics.
     */
    GRAPHICS(new AssFileSection("Graphics", "filename")),

    /**
     * Lists all fonts.
     */
    FONTS(new AssFileSection("Fonts", "fontname")),
    ;

    /**
     * The parser for the section.
     */
    private final AssSection section;

    /**
     * Creates a new section.
     * @param section The parser for the section.
     */
    AssSections(AssSection section) {
        this.section = section;
    }

    /**
     * Returns the parser for the section.
     * @return The parser for the section.
     */
    public AssSection getSection() {
        return section;
    }
}
