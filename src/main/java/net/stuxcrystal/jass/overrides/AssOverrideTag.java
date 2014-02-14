package net.stuxcrystal.jass.overrides;

/**
 * A single tag with the given value.
 */
public class AssOverrideTag<T> {

    /**
     * The type of the tag.
     */
    private AssOverrideTagType type;

    /**
     * The value of the tag.
     */
    private T value = null;

    /**
     * Constructs a new override tag with the given value.
     * @param type   The type of the tag.
     * @param value  The value of the tag.
     */
    public AssOverrideTag(AssOverrideTagType type, T value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Constructs a new override tag with null as the value.
     * @param type   The type of the tag.
     */
    public AssOverrideTag(AssOverrideTagType type) {
        this(type, null);
    }

    /**
     * Returns the type of the override tag.
     * @return The type of the override tag.
     */
    public AssOverrideTagType getType() {
        return this.type;
    }

    /**
     * Sets the type of the override tag.
     * @param type The new type of the override tag.
     */
    public void setType(AssOverrideTagType type) {
        this.type = type;
        this.value = null;
    }

    /**
     * Returns the value.
     * @return The value.
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Sets the value of the tag.
     * @param value The new value of the tag.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Returns the string value of the given override tag.
     * @param parser The parser to write.
     * @return The new value of the tag.
     */
    public String toStringValue(AssOverrideParser parser) {
        return this.getType().getDumpContents(parser, this);
    }

    public String toString() {
        return "AssOverrideTag[" + this.getType().getName() + ": " + this.getValue() + "]";
    }

}
