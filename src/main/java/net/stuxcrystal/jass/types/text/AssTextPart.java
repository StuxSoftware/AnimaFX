package net.stuxcrystal.jass.types.text;

/**
 * Represents a text in the line.
 */
public class AssTextPart extends AssLinePart<String> {
    @Override
    public Type getType() {
        return Type.TEXT;
    }
}
