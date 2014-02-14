package net.stuxcrystal.jass;

import java.util.Map;

/**
 * Represents an entry that can be parsed using
 * a format-parser.
 */
public interface AssFormattedEntry extends AssEntry {

    /**
     * Returns the type of the given format value.
     * @param name The name of the format-value.
     * @return The type of the format value.
     */
    public Class<?> getTypeOf(String name);

    /**
     * Returns a map containing the values of the entry.
     * @return The values.
     */
    public Map<String, Object> getValues();

}
