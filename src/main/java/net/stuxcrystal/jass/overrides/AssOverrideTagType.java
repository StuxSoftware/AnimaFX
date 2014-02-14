package net.stuxcrystal.jass.overrides;

import java.util.List;

/**
 * Represents a single override tag.
 */
public abstract class AssOverrideTagType {

    /**
     * The name of the tag.
     */
    private final String name;

    /**
     * Constructs a new override tag.
     * @param name The name of the override tag.
     */
    public AssOverrideTagType(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the tag.
     * @return The name of the tag.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Parses the arguments of an override tag.
     * @param parser The parser. Use this to parse block arguments.
     * @param values The values to parse.
     * @return The new override tag.
     */
    public abstract AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values);

    /**
     * Dumps the contents of the override tag into a string.
     * @param parser The parser. Use this to parse block arguments.
     * @param values The values to parse.
     * @return A string containing the values of the tag.
     */
    public abstract String getDumpContents(AssOverrideParser parser, AssOverrideTag values);
}
