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

package net.stuxcrystal.jass.paint;

import net.stuxcrystal.jass.types.AssPosition;

import java.util.List;

/**
 * Stores the points of an ass paint type.
 */
public class AssPaintValue {

    /**
     * The type of the paint value.
     */
    private String type;

    /**
     * The points of this values
     */
    private List<AssPosition> points;

    /**
     * Creates a new paint tag.
     * @param type    The type of the paint
     * @param points  The points for the value.
     */
    public AssPaintValue(String type, List<AssPosition> points) {
        this.type = type;
        this.points = points;
    }

    /**
     * Returns the type of the line.
     * @return The type of the line.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the line.
     * @param type the type of the line.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns all points of the position.
     * @return A list of all points defining the line.
     */
    public List<AssPosition> getPoints() {
        return points;
    }

    /**
     * Sets all points of the position.
     * @param points A list of all points defining the line.
     */
    public void setPoints(List<AssPosition> points) {
        this.points = points;
    }
}
