package net.stuxcrystal.jass.types;

import net.stuxcrystal.jass.AssValue;

/**
 * Represents an alignment.
 */
public enum AssAlignment implements AssValue {

    TOP_LEFT   ("7"), TOP_CENTER   ("8"),    TOP_RIGHT("9"),

    MIDDLE_LEFT("4"), MIDDLE_CENTER("5"), MIDDLE_RIGHT("6"),

    BOTTOM_LEFT("1"), BOTTOM_CENTER("2"), BOTTOM_RIGHT("3")

    ;

    private final String ass;

    private AssAlignment(String ass) {
        this.ass = ass;
    }

    @Override
    public String getAssFormatted() {
        return this.ass;
    }
}
