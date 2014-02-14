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
