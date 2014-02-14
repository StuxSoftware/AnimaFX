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
import net.stuxcrystal.jass.types.AssMovement;
import net.stuxcrystal.jass.types.AssPosition;

import java.util.List;

/**
 * Parses the movement tag.
 */
public class AssOverrideMovementTag extends AssOverrideTagType {
    /**
     * Constructs a new override tag.
     *
     * @param name The name of the override tag.
     */
    public AssOverrideMovementTag(String name) {
        super(name);
    }

    @Override
    public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

        if (values.size() != 8 && values.size() != 6)
            return new AssOverrideTag<AssMovement>(this, null);

        int start = -1, end = -1;
        AssPosition from, to;

        if (values.size() == 8) {
            start = Integer.valueOf(values.get(5));
            end   = Integer.valueOf(values.get(6));
        }

        from = new AssPosition(Double.valueOf(values.get(1)), Double.valueOf(values.get(2)));
        to   = new AssPosition(Double.valueOf(values.get(3)), Double.valueOf(values.get(4)));

        return new AssOverrideTag<>(this, new AssMovement(from, to, start, end));

    }

    @Override
    public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
        Object value = values.getValue();
        if (!(value instanceof AssMovement))
            return null;

        AssMovement movement = (AssMovement) value;

        StringBuilder sb = new StringBuilder();

        sb.append("(");

        sb.append(AssOverrideUtils.itoa(movement.getOrigin().getX())).append(",").append(AssOverrideUtils.itoa(movement.getOrigin().getY())).append(",");
        sb.append(AssOverrideUtils.itoa(movement.getDestination().getX())).append(",").append(AssOverrideUtils.itoa(movement.getDestination().getY()));

        if (movement.getStart() != -1 && movement.getEnd() != -1) {
            sb.append(",").append(movement.getStart()).append(",").append(movement.getEnd());
        }

        sb.append(")");

        return sb.toString();
    }
}
