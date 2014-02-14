package net.stuxcrystal.jass;

/**
 * Represents an event type.
 */
public enum AssEventType {

    /**
     * Represents a dialogue line.
     */
    DIALOGUE("Dialogue"),

    /**
     * Represents a comment.
     */
    COMMENT("Comment"),

    /**
     * Used on lines that should show a picture.<p />
     *
     * While this event type is described in the "unofficial"
     * specification of ASS-Files, this feature is not implemented
     * in any subtitle-editor.
     */
    PICTURE("Picture"),

    /**
     * Used on lines that should show a movie.<p />
     *
     * While this event type is described in the "unofficial"
     * specification of ASS-Files, this feature is not implemented
     * in any subtitle-editor.
     */
    MOVIE("Movie"),

    /**
     * Used on lines that should play a sound.<p />
     *
     * While this event type is described in the "unofficial"
     * specification of ASS-Files, this feature is not implemented
     * in any subtitle-editor.
     */
    SOUND("Sound"),

    /**
     * Represents a command line.<p />
     *
     * While this event type is described in the "unoffical"
     * documentation of ASS-Files, this feature is not implemented
     * in any subtitle-editor.<p />
     *
     * Do not execute these command as this may result in mayor
     * security issues while playback.
     */
    COMMAND("Command"),
    ;

    /**
     * The name of the line type.
     */
    private final String lineStart;

    /**
     * Constructs a new event type.
     * @param lineStart The start of the line.
     */
    AssEventType(String lineStart) {
        this.lineStart = lineStart;
    }

    /**
     * Returns the start of the line.
     * @return The string containing the line-type.
     */
    public String getLineStart() {
        return this.lineStart;
    }
}
