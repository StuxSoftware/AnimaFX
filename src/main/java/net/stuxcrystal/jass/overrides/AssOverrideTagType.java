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

import java.util.List;

/**
 * Represents a single override tag.
 */
public abstract class AssOverrideTagType {

    /**
     * The name of the tag.
     */
    private final String name;

    /**
     * Constructs a new override tag.
     * @param name The name of the override tag.
     */
    public AssOverrideTagType(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the tag.
     * @return The name of the tag.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Parses the arguments of an override tag.
     * @param parser The parser. Use this to parse block arguments.
     * @param values The values to parse.
     * @return The new override tag.
     */
    public abstract AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values);

    /**
     * Dumps the contents of the override tag into a string.
     * @param parser The parser. Use this to parse block arguments.
     * @param values The values to parse.
     * @return A string containing the values of the tag.
     */
    public abstract String getDumpContents(AssOverrideParser parser, AssOverrideTag values);
}
