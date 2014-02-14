package net.stuxcrystal.jass.overrides.parsers;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;

import java.util.List;

/**
 * Parses a string argument.
 * @author StuxCrystal
 *
 */
public class AssOverrideStringTag extends AssOverrideTagType {

	public AssOverrideStringTag(String name) {
		super(name);
	}

	@Override
	public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
		if (values.size() == 0)
			return new AssOverrideTag<String>(this, null);
		
		return new AssOverrideTag<>(this, values.get(0));
	}

	@Override
	public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
		return values.getValue().toString();
	}

}
