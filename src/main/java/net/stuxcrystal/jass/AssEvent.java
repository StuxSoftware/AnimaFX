package net.stuxcrystal.jass;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.types.AssTime;
import net.stuxcrystal.jass.types.text.AssLinePart;

/**
 * Represents a Event
 */
public class AssEvent implements AssFormattedEntry, Cloneable {

    /**
     * A list of default values.
     */
    private static final Map<String, Object> DEFAULTS = new LinkedHashMap<String, Object>();

    /**
     * Sets the defaults.
     */
    static {
        DEFAULTS.put("Layer", 0);
        DEFAULTS.put("Start", new AssTime(0));
        DEFAULTS.put("End", new AssTime(0));
        DEFAULTS.put("Style", "Default");
        DEFAULTS.put("Name", "");
        DEFAULTS.put("MarginL", 0);
        DEFAULTS.put("MarginR", 0);
        DEFAULTS.put("MarginV", 0);
        DEFAULTS.put("Effect", "");
        DEFAULTS.put("Text", "");
    }

    /**
     * Lists the types of the values.
     */
    private static final Map<String, Class<?>> TYPES = new HashMap<String, Class<?>>();

    /**
     * The types.
     */
    static {
        TYPES.put("Layer", int.class);
        TYPES.put("Start", AssTime.class);
        TYPES.put("End", AssTime.class);
        TYPES.put("Style", String.class);
        TYPES.put("Name", String.class);
        TYPES.put("MarginL", int.class);
        TYPES.put("MarginR", int.class);
        TYPES.put("MarginV", int.class);
        TYPES.put("Effect", String.class);
        TYPES.put("Text", String.class);
    }

    /**
     * Stores all values of the class
     */
    private Map<String, Object> values = new HashMap<String, Object>();

    /**
     * Represents the type of the event.
     */
    private AssEventType type = AssEventType.DIALOGUE;

    /**
     * Default constructor.
     */
    public AssEvent() {
        this.values = new HashMap<String, Object>(DEFAULTS);
    }

    /**
     * Constructor using maps.
     * @param values The values as a map.
     */
    public AssEvent(Map<String, Object> values) {
        this();
        this.values.putAll(values);
    }

    /**
     * All Args Constructor
     */
    public AssEvent(AssEventType type, int layer, AssTime start, AssTime end, String style, String name, int marginl, int marginr, int marginv, String effect, String text) {
        this();
        this.type = type;
        this.values.put("Layer", layer);
        this.values.put("Start", start);
        this.values.put("End", end);
        this.values.put("Style", style);
        this.values.put("Name", name);
        this.values.put("MarginL", marginl);
        this.values.put("MarginR", marginr);
        this.values.put("MarginV", marginv);
        this.values.put("Effect", effect);
        this.values.put("Text", text);
    }

    /**
     * Returns the Layer
     */
    public int getLayer() {
        return (int) this.values.get("Layer");
    }

    /**
     * Sets the Layer
     */
    public void setLayer(int value) {
        this.values.put("Layer", value);
    }

    /**
     * Returns the Start
     */
    public AssTime getStart() {
        return (AssTime) this.values.get("Start");
    }

    /**
     * Sets the Start
     */
    public void setStart(AssTime value) {
        this.values.put("Start", value);
    }

    /**
     * Returns the End
     */
    public AssTime getEnd() {
        return (AssTime) this.values.get("End");
    }

    /**
     * Sets the End
     */
    public void setEnd(AssTime value) {
        this.values.put("End", value);
    }

    /**
     * Returns the Style
     */
    public String getStyle() {
        return (String) this.values.get("Style");
    }

    /**
     * Sets the Style
     */
    public void setStyle(String value) {
        this.values.put("Style", value);
    }

    /**
     * Returns the Name
     */
    public String getName() {
        return (String) this.values.get("Name");
    }

    /**
     * Sets the Name
     */
    public void setName(String value) {
        this.values.put("Name", value);
    }

    /**
     * Returns the MarginL
     */
    public int getMarginL() {
        return (int) this.values.get("MarginL");
    }

    /**
     * Sets the MarginL
     */
    public void setMarginL(int value) {
        this.values.put("MarginL", value);
    }

    /**
     * Returns the MarginR
     */
    public int getMarginR() {
        return (int) this.values.get("MarginR");
    }

    /**
     * Sets the MarginR
     */
    public void setMarginR(int value) {
        this.values.put("MarginR", value);
    }

    /**
     * Returns the MarginV
     */
    public int getMarginV() {
        return (int) this.values.get("MarginV");
    }

    /**
     * Sets the MarginV
     */
    public void setMarginV(int value) {
        this.values.put("MarginV", value);
    }

    /**
     * Returns the Effect
     */
    public String getEffect() {
        return (String) this.values.get("Effect");
    }

    /**
     * Sets the Effect
     */
    public void setEffect(String value) {
        this.values.put("Effect", value);
    }

    /**
     * Returns the Text
     */
    public String getText() {
        return (String) this.values.get("Text");
    }

    /**
     * Sets the Text
     */
    public void setText(String value) {
        this.values.put("Text", value);
    }

    /**
     * Returns the type of the event.
     * @return The type of the event.
     */
    public AssEventType getEventType() {
        return this.type;
    }

    /**
     * Returns the type of the event.
     * @param type The type of the event.
     */
    public void setEventType(AssEventType type) {
        this.type = type;
    }

    /**
     * Returns all raw values of the generator.
     */
    @Override
    public Map<String, Object> getValues() {
        return new HashMap<String, Object>(this.values);
    }

    @Override
    public String getLineType() {
        return this.type.getLineStart();
    }

    /**
     * Returns the type of the given value.
     */
    @Override
    public Class<?> getTypeOf(String name) {
        return getTypeOfValue(name);
    }

    /**
     * Returns the type of the given value.
     */
    public static Class<?> getTypeOfValue(String name) {
        return TYPES.get(name);
    }

    /**
     * Parses the line.
     * @param parser The parser.
     * @return The parsed line.
     */
    public List<AssLinePart> parseLine(AssOverrideParser parser) {
        return parser.parseLine(this.getText());
    }

    /**
     * Sets the text using the {@link net.stuxcrystal.jass.types.text.AssLinePart}s.
     * @param parser The parser to dump the line parts.
     * @param parts  The parts to set as text.
     */
    public void setText(AssOverrideParser parser, List<AssLinePart> parts) {
        this.setText(parser.dumpParts(parts));
    }

    /**
     * An equals function
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AssEvent))
            return false;
        if (this.type == ((AssEvent) other).type)
            return true;
        if (this.type == null)
            return false;
        if (this.values == null && ((AssEvent) other).values == null)
            return true;
        if (this.values == null)
            return false;
        return this.values.equals(((AssEvent) other).values);
    }

    /**
     * A hash code function
     */
    @Override
    public int hashCode() {
        return this.values != null?this.values.hashCode():0;
    }

    @Override
    public String toString() {
        return "AssEvent[" + this.type + ":" + this.values + "]";
    }

}