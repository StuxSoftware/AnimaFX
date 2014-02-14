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
 * Override utilities.
 */
public class AssOverrideUtils {

    /**
     * This is a utility class.
     */
    private AssOverrideUtils() {}

    /**
     * Trims all strings inside a list of strings.
     * @param values The list of strings.
     */
    public static void trimStringList(List<String> values) {
        for (int i = 0; i<values.size(); i++)
            values.set(i, values.get(i).trim());
    }

    /**
     * Integer to alphanumeric.
     * @param f The float to convert.
     * @return The converted float
     */
    public static String itoa(float f) {
        if ((int) f == f)
            return Integer.toString((int) f);
        return Float.toString(f);
    }

    /**
     * Integer to alphanumeric.
     * @param d The double to convert.
     * @return The converted double
     */
    public static String itoa(double d) {
        if ((int) d == d)
            return Integer.toString((int) d);
        return Double.toString(d);
    }

}
