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

package net.stuxcrystal.animafx.structures;

/**
 * Contains the text extents for the given string.
 */
public class TextExtents {

    /**
     * Contains the width of the string.
     */
    private final double width;

    /**
     * Contains the height of the string.
     */
    private final double height;

    /**
     * Contains the ascent of the string
     */
    private final double ascend;

    /**
     * Contains the descent of the string.
     */
    private final double descent;

    /**
     * The extlead of the string.
     */
    private final double extlead;

    /**
     * Creates a new text-extents object.
     * @param width    The width of the string.
     * @param height   The height of the string.
     * @param ascend   The ascent of the string.
     * @param descent  The descent of the string.
     * @param extlead  The ext-lead of the string.
     */
    public TextExtents(double width, double height, double ascend, double descent, double extlead) {
        this.width = width;
        this.height = height;
        this.ascend = ascend;
        this.descent = descent;
        this.extlead = extlead;
    }

    /**
     * Returns the width.
     * @return The width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height.
     * @return The height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the ascend of the string.
     * @return The ascend.
     */
    public double getAscend() {
        return ascend;
    }

    /**
     * Returns the descent of the text.
     * @return The descent.
     */
    public double getDescent() {
        return descent;
    }

    /**
     * Returns the ext-lead of the text.
     * @return The ext-lead.
     */
    public double getExtlead() {
        return extlead;
    }

}
