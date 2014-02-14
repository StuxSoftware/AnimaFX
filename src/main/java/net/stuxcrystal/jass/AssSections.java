package net.stuxcrystal.jass;

/**
 * The sections of an ASS-File.
 */
public enum AssSections {

    /**
     * Represents the script-info section.
     */
    SCRIPT_INFO(new AssInfoSection()),

    /**
     * Represents the style section.
     */
    STYLES(new AssStyleSection()),

    /**
     * Lists all events
     */
    EVENTS(new AssEventSection()),

    /**
     * Lists all stored graphics.
     */
    GRAPHICS(new AssFileSection("Graphics", "filename")),

    /**
     * Lists all fonts.
     */
    FONTS(new AssFileSection("Fonts", "fontname")),
    ;

    /**
     * The parser for the section.
     */
    private final AssSection section;

    /**
     * Creates a new section.
     * @param section The parser for the section.
     */
    AssSections(AssSection section) {
        this.section = section;
    }

    /**
     * Returns the parser for the section.
     * @return The parser for the section.
     */
    public AssSection getSection() {
        return section;
    }
}
