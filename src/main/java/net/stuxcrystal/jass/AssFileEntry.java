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
