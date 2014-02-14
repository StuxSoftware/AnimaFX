package net.stuxcrystal.jkaraoke.structures;

/**
 * Contains the text extents for the given string.
 */
public class TextExtents {

    /**
     * Contains the width of the string.
     */
    private final double width;

    /**
     * Contains the height of the string.
     */
    private final double height;

    /**
     * Contains the ascent of the string
     */
    private final double ascend;

    /**
     * Contains the descent of the string.
     */
    private final double descent;

    /**
     * The extlead of the string.
     */
    private final double extlead;

    /**
     * Creates a new text-extents object.
     * @param width    The width of the string.
     * @param height   The height of the string.
     * @param ascend   The ascent of the string.
     * @param descent  The descent of the string.
     * @param extlead  The ext-lead of the string.
     */
    public TextExtents(double width, double height, double ascend, double descent, double extlead) {
        this.width = width;
        this.height = height;
        this.ascend = ascend;
        this.descent = descent;
        this.extlead = extlead;
    }

    /**
     * Returns the width.
     * @return The width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height.
     * @return The height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the ascend of the string.
     * @return The ascend.
     */
    public double getAscend() {
        return ascend;
    }

    /**
     * Returns the descent of the text.
     * @return The descent.
     */
    public double getDescent() {
        return descent;
    }

    /**
     * Returns the ext-lead of the text.
     * @return The ext-lead.
     */
    public double getExtlead() {
        return extlead;
    }

}
