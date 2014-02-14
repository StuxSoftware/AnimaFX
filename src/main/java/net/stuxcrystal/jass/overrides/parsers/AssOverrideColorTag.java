package net.stuxcrystal.jass.overrides.parsers;

import net.stuxcrystal.jass.AssColorParser;
import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;

import java.awt.*;
import java.util.List;

/**
 * Parses the ass color.
 */
public class AssOverrideColorTag extends AssOverrideTagType {

    /**
     * Constructs a new override tag.
     *
     * @param name The name of the override tag.
     */
    public AssOverrideColorTag(String name) {
        super(name);
    }

    @Override
    public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

        if (values.size() == 0)
            return new AssOverrideTag<Color>(this, null);

        return new AssOverrideTag<>(this, AssColorParser.parse(values.get(0)));
    }

    @Override
    public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
        Object value = values.getValue();
        if (!(value instanceof Color))
            return null;

        return AssColorParser.dumpAlphaless((Color) value);
    }
}
