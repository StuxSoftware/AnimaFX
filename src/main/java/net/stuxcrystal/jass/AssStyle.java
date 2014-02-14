package net.stuxcrystal.jass;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.stuxcrystal.jass.types.AssAlignment;
import net.stuxcrystal.jass.types.AssBorderStyle;
/**
 * Represents a Style
 */
public class AssStyle implements Cloneable, AssFormattedEntry {

    /**
     * A list of default values.
     */
    private static final Map<String, Object> DEFAULTS = new LinkedHashMap<String, Object>();

    /**
     * Sets the defaults.
     */
    static {
        DEFAULTS.put("Name", "Default");
        DEFAULTS.put("Fontname", "Areal");
        DEFAULTS.put("Fontsize", 20);
        DEFAULTS.put("PrimaryColour", Color.WHITE);
        DEFAULTS.put("SecondaryColour", Color.RED);
        DEFAULTS.put("OutlineColour", Color.BLACK);
        DEFAULTS.put("BackColour", Color.BLACK);
        DEFAULTS.put("Bold", false);
        DEFAULTS.put("Italic", false);
        DEFAULTS.put("StrikeOut", false);
        DEFAULTS.put("Underline", false);
        DEFAULTS.put("ScaleX", 100);
        DEFAULTS.put("ScaleY", 100);
        DEFAULTS.put("Spacing", 0.00);
        DEFAULTS.put("Angle", 0.00);
        DEFAULTS.put("BorderStyle", AssBorderStyle.OUTLINED);
        DEFAULTS.put("Outline", 2);
        DEFAULTS.put("Shadow", 2);
        DEFAULTS.put("Alignment", AssAlignment.BOTTOM_CENTER);
        DEFAULTS.put("MarginL", 10);
        DEFAULTS.put("MarginR", 10);
        DEFAULTS.put("MarginV", 10);
        DEFAULTS.put("Encoding", 0);
    }

    /**
     * Lists the types of the values.
     */
    private static final Map<String, Class<?>> TYPES = new HashMap<String, Class<?>>();

    /**
     * The types.
     */
    static {
        TYPES.put("Name", String.class);
        TYPES.put("Fontname", String.class);
        TYPES.put("Fontsize", int.class);
        TYPES.put("PrimaryColour", Color.class);
        TYPES.put("SecondaryColour", Color.class);
        TYPES.put("OutlineColour", Color.class);
        TYPES.put("BackColour", Color.class);
        TYPES.put("Bold", boolean.class);
        TYPES.put("Italic", boolean.class);
        TYPES.put("StrikeOut", boolean.class);
        TYPES.put("Underline", boolean.class);
        TYPES.put("ScaleX", int.class);
        TYPES.put("ScaleY", int.class);
        TYPES.put("Spacing", float.class);
        TYPES.put("Angle", float.class);
        TYPES.put("BorderStyle", AssBorderStyle.class);
        TYPES.put("Outline", float.class);
        TYPES.put("Shadow", float.class);
        TYPES.put("Alignment", AssAlignment.class);
        TYPES.put("MarginL", int.class);
        TYPES.put("MarginR", int.class);
        TYPES.put("MarginV", int.class);
        TYPES.put("Encoding", int.class);
    }

    /**
     * Stores all values of the class
     */
    private Map<String, Object> values = new HashMap<String, Object>();

    /**
     * Default constructor.
     */
    public AssStyle() {
        this.values = new HashMap<String, Object>(DEFAULTS);
    }

    /**
     * Constructor using maps.
     * @param values The values as a map.
     */
    public AssStyle(Map<String, Object> values) {
        this();
        this.values.putAll(values);
    }

    /**
     * All Args Constructor
     */
    public AssStyle(String name, String fontname, int fontsize, Color primarycolour, Color secondarycolour, Color outlinecolour, Color backcolour, boolean bold, boolean italic, boolean strikeout, boolean underline, int scalex, int scaley, float spacing, float angle, AssBorderStyle borderstyle, float outline, float shadow, AssAlignment alignment, int marginl, int marginr, int marginv, int encoding) {
        this();
        this.values.put("Name", name);
        this.values.put("Fontname", fontname);
        this.values.put("Fontsize", fontsize);
        this.values.put("PrimaryColour", primarycolour);
        this.values.put("SecondaryColour", secondarycolour);
        this.values.put("OutlineColour", outlinecolour);
        this.values.put("BackColour", backcolour);
        this.values.put("Bold", bold);
        this.values.put("Italic", italic);
        this.values.put("StrikeOut", strikeout);
        this.values.put("Underline", underline);
        this.values.put("ScaleX", scalex);
        this.values.put("ScaleY", scaley);
        this.values.put("Spacing", spacing);
        this.values.put("Angle", angle);
        this.values.put("BorderStyle", borderstyle);
        this.values.put("Outline", outline);
        this.values.put("Shadow", shadow);
        this.values.put("Alignment", alignment);
        this.values.put("MarginL", marginl);
        this.values.put("MarginR", marginr);
        this.values.put("MarginV", marginv);
        this.values.put("Encoding", encoding);
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
     * Returns the Fontname
     */
    public String getFontname() {
        return (String) this.values.get("Fontname");
    }

    /**
     * Sets the Fontname
     */
    public void setFontname(String value) {
        this.values.put("Fontname", value);
    }

    /**
     * Returns the Fontsize
     */
    public int getFontsize() {
        return (int) this.values.get("Fontsize");
    }

    /**
     * Sets the Fontsize
     */
    public void setFontsize(int value) {
        this.values.put("Fontsize", value);
    }

    /**
     * Returns the PrimaryColour
     */
    public Color getPrimaryColour() {
        return (Color) this.values.get("PrimaryColour");
    }

    /**
     * Sets the PrimaryColour
     */
    public void setPrimaryColour(Color value) {
        this.values.put("PrimaryColour", value);
    }

    /**
     * Returns the SecondaryColour
     */
    public Color getSecondaryColour() {
        return (Color) this.values.get("SecondaryColour");
    }

    /**
     * Sets the SecondaryColour
     */
    public void setSecondaryColour(Color value) {
        this.values.put("SecondaryColour", value);
    }

    /**
     * Returns the OutlineColour
     */
    public Color getOutlineColour() {
        return (Color) this.values.get("OutlineColour");
    }

    /**
     * Sets the OutlineColour
     */
    public void setOutlineColour(Color value) {
        this.values.put("OutlineColour", value);
    }

    /**
     * Returns the BackColour
     */
    public Color getBackColour() {
        return (Color) this.values.get("BackColour");
    }

    /**
     * Sets the BackColour
     */
    public void setBackColour(Color value) {
        this.values.put("BackColour", value);
    }

    /**
     * Returns the Bold
     */
    public boolean isBold() {
        return (boolean) this.values.get("Bold");
    }

    /**
     * Sets the Bold
     */
    public void setBold(boolean value) {
        this.values.put("Bold", value);
    }

    /**
     * Returns the Italic
     */
    public boolean isItalic() {
        return (boolean) this.values.get("Italic");
    }

    /**
     * Sets the Italic
     */
    public void setItalic(boolean value) {
        this.values.put("Italic", value);
    }

    /**
     * Returns the StrikeOut
     */
    public boolean getStrikeOut() {
        return (boolean) this.values.get("StrikeOut");
    }

    /**
     * Sets the StrikeOut
     */
    public void setStrikeOut(boolean value) {
        this.values.put("StrikeOut", value);
    }

    /**
     * Returns the Underline
     */
    public boolean getUnderline() {
        return (boolean) this.values.get("Underline");
    }

    /**
     * Sets the Underline
     */
    public void setUnderline(boolean value) {
        this.values.put("Underline", value);
    }

    /**
     * Returns the ScaleX
     */
    public int getScaleX() {
        return (int) this.values.get("ScaleX");
    }

    /**
     * Sets the ScaleX
     */
    public void setScaleX(int value) {
        this.values.put("ScaleX", value);
    }

    /**
     * Returns the ScaleY
     */
    public int getScaleY() {
        return (int) this.values.get("ScaleY");
    }

    /**
     * Sets the ScaleY
     */
    public void setScaleY(int value) {
        this.values.put("ScaleY", value);
    }

    /**
     * Returns the Spacing
     */
    public float getSpacing() {
        return (float) this.values.get("Spacing");
    }

    /**
     * Sets the Spacing
     */
    public void setSpacing(float value) {
        this.values.put("Spacing", value);
    }

    /**
     * Returns the Angle
     */
    public float getAngle() {
        return (float) this.values.get("Angle");
    }

    /**
     * Sets the Angle
     */
    public void setAngle(float value) {
        this.values.put("Angle", value);
    }

    /**
     * Returns the BorderStyle
     */
    public AssBorderStyle getBorderStyle() {
        return (AssBorderStyle) this.values.get("BorderStyle");
    }

    /**
     * Sets the BorderStyle
     */
    public void setBorderStyle(AssBorderStyle value) {
        this.values.put("BorderStyle", value);
    }

    /**
     * Returns the Outline
     */
    public float getOutline() {
        return (float) this.values.get("Outline");
    }

    /**
     * Sets the Outline
     */
    public void setOutline(float value) {
        this.values.put("Outline", value);
    }

    /**
     * Returns the Shadow
     */
    public float getShadow() {
        return (float) this.values.get("Shadow");
    }

    /**
     * Sets the Shadow
     */
    public void setShadow(float value) {
        this.values.put("Shadow", value);
    }

    /**
     * Returns the Alignment
     */
    public AssAlignment getAlignment() {
        return (AssAlignment) this.values.get("Alignment");
    }

    /**
     * Sets the Alignment
     */
    public void setAlignment(AssAlignment value) {
        this.values.put("Alignment", value);
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
     * Returns the Encoding
     */
    public int getEncoding() {
        return (int) this.values.get("Encoding");
    }

    /**
     * Sets the Encoding
     */
    public void setEncoding(int value) {
        this.values.put("Encoding", value);
    }


    /**
     * Returns all raw values of the generator.
     */
    public Map<String, Object> getValues() {
        return new HashMap<String, Object>(this.values);
    }

    @Override
    public String getLineType() {
        return "Style";
    }

    /**
     * Returns the type of the given value.
     */
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
     * An equals function
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AssStyle))
            return false;
        if (this.values == null && ((AssStyle) other).values == null)
            return true;
        if (this.values == null)
            return false;
        return this.values.equals(((AssStyle) other).values);
    }

    /**
     * A hash code function
     */
    @Override
    public int hashCode() {
        return this.values != null?this.values.hashCode():0;
    }

}