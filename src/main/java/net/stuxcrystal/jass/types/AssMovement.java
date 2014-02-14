package net.stuxcrystal.jass.types;

/**
 * Represents a movement.
 * @author StuxCrystal
 *
 */
public class AssMovement {
	
	/**
	 * The origin point
	 */
	private AssPosition from;
	
	/**
	 * The destination point.
	 */
	private AssPosition to;

	/**
	 * The start time.
	 */
	private int start = -1;
	
	/**
	 * The end time.
	 */
	private int end = -1;
	
	/**
	 * Creates a new movement
	 * @param from   The origin point.
	 * @param to     The destination point.
	 * @param start  When should the movement start. (-1 denotes the start)
	 * @param end    when should the movement end.   (-1 denotes the end)
	 */
	public AssMovement(AssPosition from, AssPosition to, int start, int end) {
		this.from  = from;
		this.to    = to;
		this.start = start;
		this.end   = end;
	}
	
	/**
	 * Creates a new movement.
	 * @param from   The origin point.
	 * @param to     The destination point.
	 */
	public AssMovement(AssPosition from, AssPosition to) {
		this(from, to, -1, -1);
	}

	/**
	 * Returns the origin point
	 * @return The origin point
	 */
	public AssPosition getOrigin() {
		return from;
	}

	/**
	 * Sets a new origin point.
	 * @param from The new origin point.
	 */
	public void setOrigin(AssPosition from) {
		this.from = from;
	}

	/**
	 * Sets the destination point.
	 * @return The destination point.
	 */
	public AssPosition getDestination() {
		return to;
	}

	/**
	 * Sets the destination point.
	 * @param to The new destination point.
	 */
	public void setDestination(AssPosition to) {
		this.to = to;
	}

    /**
     * Returns the start time of the movement.<p />
     *
     * If the start time is {@code -1}, it is the start time of the line itself.
     *
     * @return The start time.
     */
	public int getStart() {
		return start;
	}

    /**
     * Sets the start time.
     * @param start The new start time.
     */
	public void setStart(int start) {
		this.start = start;
	}

    /**
     * Returns the end time of the movement.<p />
     *
     * If the end time is {@code -1}, it is the end time of the line itself.
     *
     * @return The end time.
     */
	public int getEnd() {
		return end;
	}

    /**
     * Sets the end time.
     * @param end The new end time.
     */
	public void setEnd(int end) {
		this.end = end;
	}

    @Override
    public String toString() {
        return "AssMovement(" + this.getOrigin() + "," + this.getDestination() + "," + this.getStart() + "," + this.getEnd() + ")";
    }
}
