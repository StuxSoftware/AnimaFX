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

package net.stuxcrystal.jass.types;

import net.stuxcrystal.jass.paint.AssPaintParser;
import net.stuxcrystal.jass.paint.AssPaintValue;

import java.util.Arrays;
import java.util.List;

/**
 * Clips the values.
 */
public class AssClip {

    /**
     * The type of the clip.
     */
    public static enum Type {

        /**
         * Used on rectangles.
         */
        RECTANGLE,

        /**
         * Used on paint tags.<p />
         *
         * The ass-paint type is null in these cases.
         */
        POLYGON

    }

    /**
     * List of all paint values.<p />
     *
     * If {@code type} is {@link net.stuxcrystal.jass.types.AssClip.Type#RECTANGLE} there is only one
     * paint value containing the upper left as well as the lower right point of the rectangle.
     */
    private List<AssPaintValue> points;

    /**
     * Represents a type.
     */
    private Type type;

    /**
     * Creates a new clip using a rectangle.
     * @param point1   The upper left point of the rectangle
     * @param point2   The lower right point of the rectangle.
     */
    public AssClip(AssPosition point1, AssPosition point2) {
        this.points = Arrays.asList(new AssPaintValue(null, Arrays.asList(point1, point2)));
        this.type = Type.RECTANGLE;
    }

    /**
     * Creates a new clip using a polygon.
     * @param points   The points for the polygon.
     */
    public AssClip(List<AssPaintValue> points) {
        this.points = points;
        this.type = Type.POLYGON;
    }

    /**
     * Returns the points for the clipping polygon.
     * @return A list of AssPaint-Values.
     */
    public List<AssPaintValue> getPoints() {
        return points;
    }

    /**
     * Sets the points for the clipping polygon.
     * @param points The points for the polygon.
     */
    public void setPoints(List<AssPaintValue> points) {
        this.points = points;
    }

    /**
     * Returns the type of the polygon.
     * @return The type of the polygon.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the polygon.<p />
     *
     * This method will clear the points of the clipping.
     *
     * @param type The type of the polygon.
     */
    public void setType(Type type) {
        this.setPoints(null);
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AssClip(");

        sb.append(this.getType()).append(",");

        if (this.getType() == Type.RECTANGLE) {
            sb.append(this.getPoints().get(0).getPoints().get(0)).append(",").append(this.getPoints().get(0).getPoints().get(1));
        } else {
            sb.append(AssPaintParser.dump(this.getPoints()));
        }
        return sb.append(")").toString();
    }
}
