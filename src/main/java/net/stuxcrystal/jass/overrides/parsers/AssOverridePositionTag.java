package net.stuxcrystal.jass.overrides.parsers;

import java.util.List;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;
import net.stuxcrystal.jass.types.AssPosition;

/**
 * Parses an override tag consisting of a position.
 * @author StuxCrystal
 *
 */
public class AssOverridePositionTag extends AssOverrideTagType {

	public AssOverridePositionTag(String name) {
		super(name);
	}

	@Override
	public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

		if (values.size() < 2)
			return new AssOverrideTag<AssPosition>(this, null);
		
		int offset = (values.size() == 2)?0:1;
		
		double x = Double.valueOf(values.get(0 + offset));
		double y = Double.valueOf(values.get(1 + offset));
		
		return new AssOverrideTag<>(this, new AssPosition(x, y));
	}

	@Override
	public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
		Object value = values.getValue();
		if (!(value instanceof AssPosition)) {
			return null;
		}
		
		AssPosition position = (AssPosition) value;
		
		return "(" + AssOverrideUtils.itoa(position.getX()) + "," + AssOverrideUtils.itoa(position.getY()) + ")";
	}

}
