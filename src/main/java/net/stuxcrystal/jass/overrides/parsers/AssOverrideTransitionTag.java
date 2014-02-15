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

package net.stuxcrystal.jass.overrides.parsers;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;
import net.stuxcrystal.jass.types.AssTransition;

import java.util.List;

/**
 * Parses a transition.
 */
public class AssOverrideTransitionTag extends AssOverrideTagType {
    /**
     * Constructs a new override tag.
     *
     * @param name The name of the override tag.
     */
    public AssOverrideTransitionTag(String name) {
        super(name);
    }

    @Override
    public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

        int offset = 0;

        int start = 0, end = -1;
        if (values.size() > 5) {
            start = Integer.valueOf(values.get(1));
            end = Integer.valueOf(values.get(2));

            offset = 2;
        }

        double acceleration = 1D;
        if (values.size() == 4 || values.size() == 6) {
            acceleration = Double.valueOf(values.get(1 + offset));
            offset += 1;
        }

        List<AssOverrideTag> tags = parser.parseTags(values.get(1 + offset));

        return new AssOverrideTag<>(this, new AssTransition(start, end, acceleration, tags));
    }

    @Override
    public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
        Object _value = values.getValue();

        if (!(_value instanceof AssTransition))
            return null;

        AssTransition value = (AssTransition) _value;

        StringBuilder sb = new StringBuilder();
        sb.append("(");

        if (!(value.getStart() == 0 && value.getEnd() == -1))
            sb.append(value.getStart()).append(",").append(value.getEnd()).append(",");

        if (value.getAcceleration() != 1)
            sb.append(AssOverrideUtils.itoa(value.getAcceleration())).append(",");

        sb.append(parser.dumpBracketContents(value.getTags())).append(")");
        return sb.toString();
    }
}
