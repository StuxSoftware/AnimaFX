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

package net.stuxcrystal.jass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Section for events.
 */
public class AssEventSection extends AssFormattedSection<AssEvent> {


    @Override
    protected List<String> getDefaultFormat() {
        return Arrays.asList(
                "Layer",
                "Start",
                "End",
                "Style",
                "Name",
                "MarginL",
                "MarginR",
                "MarginV",
                "Effect",
                "Text"
        );
    }

    @Override
    protected AssEvent getDefaultEntry() {
        return new AssEvent();
    }

    @Override
    protected AssEvent getEntry(String linetype, Map<String, Object> values) {
        AssEventType type = null;
        for (AssEventType cType : AssEventType.values()) {
            if (cType.getLineStart().equalsIgnoreCase(linetype)) {
                type = cType;
                break;
            }
        }

        if (type == null)
            throw new NumberFormatException("Invalid line type.");

        AssEvent result = new AssEvent(values);
        result.setEventType(type);
        return result;
    }

    @Override
    public String getSectionTitle() {
        return "Events";
    }
}
