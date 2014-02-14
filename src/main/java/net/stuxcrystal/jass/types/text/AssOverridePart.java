package net.stuxcrystal.jass.types.text;

import net.stuxcrystal.jass.overrides.AssOverrideTag;

import java.util.List;

/**
 * Stores the override values inside the brackets
 */
public class AssOverridePart extends AssLinePart<List<AssOverrideTag>>{

    @Override
    public Type getType() {
        return Type.OVERRIDES;
    }
}
