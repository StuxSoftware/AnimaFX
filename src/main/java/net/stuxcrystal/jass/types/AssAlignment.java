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

import net.stuxcrystal.jass.AssValue;

/**
 * Represents an alignment.
 */
public enum AssAlignment implements AssValue {

    TOP_LEFT   ("7"), TOP_CENTER   ("8"),    TOP_RIGHT("9"),

    MIDDLE_LEFT("4"), MIDDLE_CENTER("5"), MIDDLE_RIGHT("6"),

    BOTTOM_LEFT("1"), BOTTOM_CENTER("2"), BOTTOM_RIGHT("3")

    ;

    private final String ass;

    private AssAlignment(String ass) {
        this.ass = ass;
    }

    @Override
    public String getAssFormatted() {
        return this.ass;
    }
}
