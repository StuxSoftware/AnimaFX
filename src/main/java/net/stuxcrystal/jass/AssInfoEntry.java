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

import java.util.Objects;

/**
 * Represents an info entry.
 */
public class AssInfoEntry implements AssEntry {

    /**
     * The name of the entry.
     */
    private String key;

    /**
     * Returns the value of the entry.
     */
    private String value;

    /**
     * Creates a new info entry.
     */
    public AssInfoEntry(String key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        this.key = key;
        this.value = value;
    }

    @Override
    public String getLineType() {
        return this.getKey();
    }

    /**
     * Returns the key of the option.
     * @return The key.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Sets the key of the info.
     * @param key The new key.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Returns the value of the entry.
     * @return The value of the entry.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value of the entry.
     * @param value The new value.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
