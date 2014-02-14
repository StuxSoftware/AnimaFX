package net.stuxcrystal.jass.overrides.parsers;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;
import net.stuxcrystal.jass.types.AssMovement;
import net.stuxcrystal.jass.types.AssPosition;

import java.util.List;

/**
 * Parses the movement tag.
 */
public class AssOverrideMovementTag extends AssOverrideTagType {
    /**
     * Constructs a new override tag.
     *
     * @param name The name of the override tag.
     */
    public AssOverrideMovementTag(String name) {
        super(name);
    }

    @Override
    public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

        if (values.size() != 8 && values.size() != 6)
            return new AssOverrideTag<AssMovement>(this, null);

        int start = -1, end = -1;
        AssPosition from, to;

        if (values.size() == 8) {
            start = Integer.valueOf(values.get(5));
            end   = Integer.valueOf(values.get(6));
        }

        from = new AssPosition(Double.valueOf(values.get(1)), Double.valueOf(values.get(2)));
        to   = new AssPosition(Double.valueOf(values.get(3)), Double.valueOf(values.get(4)));

        return new AssOverrideTag<>(this, new AssMovement(from, to, start, end));

    }

    @Override
    public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
        Object value = values.getValue();
        if (!(value instanceof AssMovement))
            return null;

        AssMovement movement = (AssMovement) value;

        StringBuilder sb = new StringBuilder();

        sb.append("(");

        sb.append(AssOverrideUtils.itoa(movement.getOrigin().getX())).append(",").append(AssOverrideUtils.itoa(movement.getOrigin().getY())).append(",");
        sb.append(AssOverrideUtils.itoa(movement.getDestination().getX())).append(",").append(AssOverrideUtils.itoa(movement.getDestination().getY()));

        if (movement.getStart() != -1 && movement.getEnd() != -1) {
            sb.append(",").append(movement.getStart()).append(",").append(movement.getEnd());
        }

        sb.append(")");

        return sb.toString();
    }
}
