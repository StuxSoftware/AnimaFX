package net.stuxcrystal.jass.paint;

import net.stuxcrystal.jass.types.AssPosition;

import java.util.List;

/**
 * Stores the points of an ass paint type.
 */
public class AssPaintValue {

    /**
     * The type of the paint value.
     */
    private String type;

    /**
     * The points of this values
     */
    private List<AssPosition> points;

    /**
     * Creates a new paint tag.
     * @param type    The type of the paint
     * @param points  The points for the value.
     */
    public AssPaintValue(String type, List<AssPosition> points) {
        this.type = type;
        this.points = points;
    }

    /**
     * Returns the type of the line.
     * @return The type of the line.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the line.
     * @param type the type of the line.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns all points of the position.
     * @return A list of all points defining the line.
     */
    public List<AssPosition> getPoints() {
        return points;
    }

    /**
     * Sets all points of the position.
     * @param points A list of all points defining the line.
     */
    public void setPoints(List<AssPosition> points) {
        this.points = points;
    }
}
