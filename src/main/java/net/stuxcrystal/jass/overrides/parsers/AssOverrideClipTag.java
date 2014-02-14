package net.stuxcrystal.jass.overrides.parsers;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;
import net.stuxcrystal.jass.paint.AssPaintParser;
import net.stuxcrystal.jass.types.AssClip;
import net.stuxcrystal.jass.types.AssPosition;
import net.stuxcrystal.jass.types.AssTransition;

import java.util.List;

/**
 * Parses a clip tag.
 */
public class AssOverrideClipTag extends AssOverrideTagType {

    private final boolean inversed;

    /**
     * Constructs a new override tag.
     *
     * @param name The name of the override tag.
     */
    public AssOverrideClipTag(String name, boolean inversed) {
        super(name);
        this.inversed = inversed;
    }

    @Override
    public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

        if (values.size() == 3) {
            return new AssOverrideTag<>(this, new AssClip(AssPaintParser.parse(values.get(1))));
        } else if (values.size() == 6) {
            return new AssOverrideTag<>(this, new AssClip(
                    new AssPosition(Double.valueOf(values.get(1)), Double.valueOf(values.get(2))),
                    new AssPosition(Double.valueOf(values.get(3)), Double.valueOf(values.get(4)))
            ));
        } else {
            return new AssOverrideTag<AssClip>(this, null);
        }
    }

    @Override
    public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
        Object _value = values.getValue();

        if (!(_value instanceof AssClip))
            return null;

        AssClip value = (AssClip) _value;

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (value.getType() == AssClip.Type.RECTANGLE) {
            List<AssPosition> positions = value.getPoints().get(0).getPoints();

            sb.append(positions.get(0).getX()).append(",").append(positions.get(0).getY()).append(",");
            sb.append(positions.get(1).getX()).append(",").append(positions.get(1).getY());
        } else {
            sb.append(AssPaintParser.dump(value.getPoints()));
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Checks if the tag is inversed.
     * @return true if this tag is a inversed clip.
     */
    public boolean isInversed() {
        return inversed;
    }
}
