package net.stuxcrystal.jkaraoke.structures;

/**
 * Represents a syllable.
 */
public class Syllable {

    /**
     * The start time of the syllable.<p />
     *
     * This is the absolute timing!
     */
    private int start;

    /**
     * The duration of the syllable.
     */
    private int duration;

    /**
     * The text of the syllable.
     */
    private String text;

    /**
     * Creates a new syllable.
     * @param start     The start of the syllable.
     * @param duration  The duration of the syllable.
     * @param text      The text of the syllable.
     */
    public Syllable(int start, int duration, String text) {
        this.start = start;
        this.duration = duration;
        this.text = text;
    }

    /**
     * Returns the absolute start time of the syllable
     * @return The absolute start time of the syllable.
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the start time of the syllable.
     * @param start The start time of the syllable.
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Returns the duration of the syllable.
     * @return The start time of the syllable.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the syllable.
     * @param duration The new duration of the syllable.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Returns the text of the syllable.
     * @return The text of the syllable.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the syllable.
     * @param text The new text of the syllable.
     */
    public void setText(String text) {
        this.text = text;
    }
}
