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
