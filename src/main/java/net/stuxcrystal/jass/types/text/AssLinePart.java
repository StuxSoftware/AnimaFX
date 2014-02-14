package net.stuxcrystal.jass.types.text;

/**
 * Represents a part in the line.
 */
public abstract class AssLinePart<T> {

    /**
     * The type of the line part.
     */
    public static enum Type {

        /**
         * If the part represents a text inside the line.
         */
        TEXT,

        /**
         * If the current part is a text.
         */
        OVERRIDES

    }

    /**
     * Contains the value.
     */
    private T value;

    /**
     * Returns the type of the line.
     * @return The type of the line.
     */
    public abstract Type getType();

    /**
     * Returns the value of the part.
     * @return The contents of the part.
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Sets the contents of the part.
     * @param value The new contents of the part.
     */
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AssLinePart<" + this.getType().toString() + ">[" + this.getValue().toString() + "]";
    }
}
