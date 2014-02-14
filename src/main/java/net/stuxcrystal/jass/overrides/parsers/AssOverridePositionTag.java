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

import java.util.List;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;
import net.stuxcrystal.jass.types.AssPosition;

/**
 * Parses an override tag consisting of a position.
 * @author StuxCrystal
 *
 */
public class AssOverridePositionTag extends AssOverrideTagType {

	public AssOverridePositionTag(String name) {
		super(name);
	}

	@Override
	public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

		if (values.size() < 2)
			return new AssOverrideTag<AssPosition>(this, null);
		
		int offset = (values.size() == 2)?0:1;
		
		double x = Double.valueOf(values.get(0 + offset));
		double y = Double.valueOf(values.get(1 + offset));
		
		return new AssOverrideTag<>(this, new AssPosition(x, y));
	}

	@Override
	public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
		Object value = values.getValue();
		if (!(value instanceof AssPosition)) {
			return null;
		}
		
		AssPosition position = (AssPosition) value;
		
		return "(" + AssOverrideUtils.itoa(position.getX()) + "," + AssOverrideUtils.itoa(position.getY()) + ")";
	}

}
