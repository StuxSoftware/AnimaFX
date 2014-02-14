package net.stuxcrystal.jass.types;

/**
 * Represents a point in jAss.
 * @author StuxCrystal
 *
 */
public class AssPosition {
	
	/**
	 * The x-coordinate.
	 */
	private double x;
	
	/**
	 * The y-coordinate.
	 */
	private double y;
	
	/**
	 * Creates a new point.
	 * @param x The x-coordinates.
	 * @param y The y-coordinates.
	 */
	public AssPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x-coordinate.
	 * @return The x-coordinate.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns the y-coordinate
	 * @return The y-coordinate
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Sets the x-coordinate.
	 * @param x The new x-coordinate.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Sets the y-coordinate.
	 * @param y The new y-coordinate.
	 */
	public void setY(double y) {
		this.y = y;
	}

    @Override
    public String toString() {
        return "AssPosition(" + this.getX() + "," + this.getY() + ")";
    }
}
