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

import net.stuxcrystal.jass.overrides.AssOverrideTag;

import java.util.List;

/**
 * Represents a transition.
 */
public class AssTransition {

    /**
     * Tags defined in the transition.
     */
    private List<AssOverrideTag> tags;

    /**
     * The start time of the transition.<p />
     *
     * If the start time is {@code 0} the transition starts at the start of the line.
     */
    private int start;

    /**
     * The end time of the transition.<p />
     *
     * If the start time is {@code -1} the transition starts at the end of the line.
     */
    private int end;

    /**
     * The acceleration of the transition.<p />
     *
     * A value of {@code 1} denotes a linear transition.
     */
    private double acceleration;

    /**
     * Creates a new transition.
     * @param start           The start time.
     * @param end             The end time.
     * @param acceleration    The acceleration.
     * @param tags            The tags.
     */
    public AssTransition(int start, int end, double acceleration, List<AssOverrideTag> tags) {
        this.start = start;
        this.end = end;
        this.acceleration = acceleration;
        this.tags = tags;
    }

    /**
     * Creates a new transition.
     * @param start           The start time.
     * @param end             The end time.
     * @param tags
     */
    public AssTransition(int start, int end, List<AssOverrideTag> tags) {
        this(start, end, 1D, tags);
    }

    /**
     * Creates a new transition.
     * @param acceleration    The acceleration.
     * @param tags            The tags.
     */
    public AssTransition(double acceleration, List<AssOverrideTag> tags) {
        this(0, -1, acceleration, tags);
    }

    /**
     * Creates a new transition.
     * @param tags            The tags.
     */
    public AssTransition(List<AssOverrideTag> tags) {
        this(0, -1, 1D, tags);
    }

    /**
     * Returns the transitioning tags.
     * @return The transitioning tags.
     */
    public List<AssOverrideTag> getTags() {
        return tags;
    }

    /**
     * Sets the transitioning tags.
     * @param tags The new tags.
     */
    public void setTags(List<AssOverrideTag> tags) {
        this.tags = tags;
    }

    /**
     * Returns the start time.
     * @return The start time.
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the start time.
     * @param start The new start time.
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Returns the end time.<p />
     *
     * An end time of {@code -1} denotes the end time of the line.
     *
     * @return The end time.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets the end time.
     * @param end The new end time.
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Returns the acceleration.
     * @return The acceleration.
     */
    public double getAcceleration() {
        return acceleration;
    }

    /**
     * Sets the new acceleration.
     * @param acceleration The new acceleration.
     */
    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public String toString() {
        return "AssTransition(" +  this.getStart() + "," + this.getEnd() + "," + this.getAcceleration() + "," + this.getTags().toString() + ")";
    }
}
