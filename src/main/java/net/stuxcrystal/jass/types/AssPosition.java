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

/**
 * Represents a point in jAss.
 * @author StuxCrystal
 *
 */
public class AssPosition {
	
	/**
	 * The x-coordinate.
	 */
	private double x;
	
	/**
	 * The y-coordinate.
	 */
	private double y;
	
	/**
	 * Creates a new point.
	 * @param x The x-coordinates.
	 * @param y The y-coordinates.
	 */
	public AssPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x-coordinate.
	 * @return The x-coordinate.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns the y-coordinate
	 * @return The y-coordinate
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Sets the x-coordinate.
	 * @param x The new x-coordinate.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Sets the y-coordinate.
	 * @param y The new y-coordinate.
	 */
	public void setY(double y) {
		this.y = y;
	}

    @Override
    public String toString() {
        return "AssPosition(" + this.getX() + "," + this.getY() + ")";
    }
}
