package net.stuxcrystal.jass;

import java.util.List;

/**
 * Represents a simple section.
 */
public abstract class AssSection<T extends AssEntry> {

    /**
     * Returns the title of the section.
     * @return The title of the section.
     */
    public abstract String getSectionTitle();

    /**
     * Parses the section.
     */
    public abstract List<T> parse(List<String> lines);

    /**
     * Dumps the files for the section.
     * @param entries The entries.
     * @return A list of strings.
     */
    public abstract List<String> dump(List<T> entries);
}
