package net.stuxcrystal.jass.overrides.parsers;

import net.stuxcrystal.jass.AssColorParser;
import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;

import java.util.List;

/**
 * Parses the alpha value of the tag.
 */
public class AssOverrideAlphaTag extends AssOverrideTagType {

    /**
     * Constructs a new override tag.
     *
     * @param name The name of the override tag.
     */
    public AssOverrideAlphaTag(String name) {
        super(name);
    }

    @Override
    public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

        if (values.size() == 0)
            return new AssOverrideTag<Integer>(this, null);

        return new AssOverrideTag<>(this, AssColorParser.parseAlpha(values.get(0)));
    }

    @Override
    public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
        Object value = values.getValue();
        if (!(value instanceof Integer))
            return null;

        return AssColorParser.dumpAlpha((int) value);
    }
}
