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

import net.stuxcrystal.jass.AssFileEntry;
import net.stuxcrystal.jass.AssFileEntryUtils;
import net.stuxcrystal.jass.AssUtils;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;
import net.stuxcrystal.jass.types.AssPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses the paint tags.
 */
public class AssPaintParser {

    /**
     * This is a utility class.
     */
    private AssPaintParser() {}

    /**
     * Parses a paint tag.
     * @param text The text to parse.
     * @return The parsed paint values.
     */
    public static List<AssPaintValue> parse(String text) {
        List<AssPaintValue> values = new ArrayList<>();
        String[] arguments = text.split(" ");

        List<AssPosition> points = new ArrayList<>();
        String cType = null;
        Float x = null;

        for (String argument : arguments) {
            argument = argument.trim();

            if (AssPaintParser.isNumeric(argument)) {
                if (x == null) {
                    x = Float.valueOf(argument);
                } else {
                    points.add(new AssPosition(x, Float.valueOf(argument)));
                    x = null;
                }
            } else {
                values.add(new AssPaintValue(cType, points));
                cType = argument;
                x = null;
                points = new ArrayList<>();
            }
        }

        return values;
    }

    /**
     * Dumps all values.
     * @param points The points to dump.
     * @return All points.
     */
    public static String dump(List<AssPaintValue> points) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (AssPaintValue value : points) {
            if (first)
                first = false;
            else
                sb.append(" ");

            sb.append(value.getType());

            if (!(value.getPoints()==null || value.getPoints().size()==0)) {
                sb.append(" ");
            } else {
                continue;
            }

            boolean firstPoint = true;
            for (AssPosition position : value.getPoints()) {
                if (firstPoint)
                    firstPoint = false;
                else
                    sb.append(" ");

                sb.append(AssOverrideUtils.itoa(position.getX())).append(" ").append(AssOverrideUtils.itoa(position.getX()));
            }
        }

        return sb.toString();
    }

    /**
     * Checks if the value is numeric
     * @param value The value to check.
     * @return checks if the value is numeric.
     */
    private static boolean isNumeric(String value) {
        boolean first = true;
        boolean pastComma = false;
        for (char c : value.toCharArray()) {
            if (first) {
                if (!"+-0123456789.".contains(new String(new char[]{c})))
                    return false;
            } else {
                if (!"0123456789.".contains(new String(new char[]{c})))
                    return false;
            }

            if (c == '.') {
                if (pastComma)
                    return false;
                pastComma = true;
            }

            first = false;
        }
        return true;
    }

}
