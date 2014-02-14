package net.stuxcrystal.jass.types;

import net.stuxcrystal.jass.AssValue;

/**
 * Internal value.
 */
public enum AssBorderStyle implements AssValue {

    /**
     * Represents an opaque box.
     */
    OPQAUE_BOX("2"),

    /**
     * Use an outline.
     */
    OUTLINED("1"),

    /**
     * On unknown values.
     */
    UNKNOWN("0");

    private final String name;

    private AssBorderStyle(String name) {
        this.name = name;
    }

    @Override
    public String getAssFormatted() {
        return this.name;
    }
}
