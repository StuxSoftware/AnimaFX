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

package net.stuxcrystal.jass.overrides;

/**
 * A single tag with the given value.
 */
public class AssOverrideTag<T> {

    /**
     * The type of the tag.
     */
    private AssOverrideTagType type;

    /**
     * The value of the tag.
     */
    private T value = null;

    /**
     * Constructs a new override tag with the given value.
     * @param type   The type of the tag.
     * @param value  The value of the tag.
     */
    public AssOverrideTag(AssOverrideTagType type, T value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Constructs a new override tag with null as the value.
     * @param type   The type of the tag.
     */
    public AssOverrideTag(AssOverrideTagType type) {
        this(type, null);
    }

    /**
     * Returns the type of the override tag.
     * @return The type of the override tag.
     */
    public AssOverrideTagType getType() {
        return this.type;
    }

    /**
     * Sets the type of the override tag.
     * @param type The new type of the override tag.
     */
    public void setType(AssOverrideTagType type) {
        this.type = type;
        this.value = null;
    }

    /**
     * Returns the value.
     * @return The value.
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Sets the value of the tag.
     * @param value The new value of the tag.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Returns the string value of the given override tag.
     * @param parser The parser to write.
     * @return The new value of the tag.
     */
    public String toStringValue(AssOverrideParser parser) {
        return this.getType().getDumpContents(parser, this);
    }

    public String toString() {
        return "AssOverrideTag[" + this.getType().getName() + ": " + this.getValue() + "]";
    }

}
