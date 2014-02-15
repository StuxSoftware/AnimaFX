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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Writes the data into an output stream.
 */
public class AssFileEntryOutputStream extends ByteArrayOutputStream {

    /**
     * The entry of the file.
     */
    private final AssFileEntry entry;

    /**
     * Throws exceptions if the stream is already closed
     * and data is written into the stream.
     */
    private boolean closed = false;

    /**
     * Lets the user choose if the current content of the stream does
     * only append the given bytes to the content or if it does more.
     * @param entry  The entry.
     * @param append Should the given content only be appended.
     */
    public AssFileEntryOutputStream(AssFileEntry entry, boolean append) {
        super((append?entry.getContent().length:32));
        this.entry = entry;

        if (append) {
            try {
                this.write(entry.getContent());
                this.flush();
            } catch (IOException ignore) {}
        }
    }

    /**
     * Opens a stream to the content.
     * @param entry The entry to write to.
     */
    public AssFileEntryOutputStream(AssFileEntry entry) {
        this(entry, false);
    }

    /**
     * Flushes the content of the stream.
     */
    @Override
    public void flush() throws IOException {
        if (this.closed) {
            throw new IOException("Stream already closed");
        }
        this.entry.setContent(this.toByteArray());
    }

    /**
     * Closes the stream to the given file entry.
     * @throws IOException The stream.
     */
    @Override
    public void close() throws IOException {
        this.flush();
        this.closed = true;
    }
}
