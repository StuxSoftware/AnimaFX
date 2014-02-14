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

/**
 * An entry for a file.
 */
public class AssFileEntry implements AssEntry {

    /**
     * The name of the file.
     */
    private final String name;

    /**
     * Represents the contents of the file.
     */
    private byte[] content = new byte[0];

    /**
     * Creates a new file entry.
     * @param name The name of the file.
     */
    public AssFileEntry(String name) {
        this.name = name;
    }

    /**
     * Returns the filename of the entry.
     * @return The filename of the entry.
     */
    public String getFileName() {
        return this.name;
    }

    /**
     * Returns the content of the entry.
     * @return The entry.
     */
    byte[] getContent() {
        return this.content;
    }

    /**
     * Sets the content of the entry.
     * @param content The content of the entry.
     */
    void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * This type does not support special line types.
     * @return {@code null}
     */
    @Override
    public String getLineType() {
        return null;
    }
}
