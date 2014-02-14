package net.stuxcrystal.jass.overrides.parsers;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;

import java.util.List;

/**
 * Parses a float tag.
 */
public class AssOverrideFloatTag extends AssOverrideTagType {

    /**
     * Constructs a new override tag.
     *
     * @param name The name of the override tag.
     */
    public AssOverrideFloatTag(String name) {
        super(name);
    }

    @Override
    public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

        if (values.size() == 0)
            return new AssOverrideTag<Float>(this, null);

        return new AssOverrideTag<>(this, Float.valueOf(values.get(0)));
    }

    @Override
    public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
        Object value = values.getValue();
        if (!(value instanceof Float))
            return null;

        return AssOverrideUtils.itoa((Float) value);
    }
}
