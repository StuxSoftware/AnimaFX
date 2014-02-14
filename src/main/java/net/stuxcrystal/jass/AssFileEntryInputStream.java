package net.stuxcrystal.jass;

import java.io.ByteArrayInputStream;

/**
 * Reads the content of a file entry.
 */
public class AssFileEntryInputStream extends ByteArrayInputStream {

    /**
     * Reads the content of a file entry.
     * @param entry The entry to read.
     */
    public AssFileEntryInputStream(AssFileEntry entry) {
        super(entry.getContent());
    }
}
