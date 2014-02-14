package net.stuxcrystal.jass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The style section.
 */
public class AssStyleSection extends AssFormattedSection<AssStyle> {

    @Override
    protected List<String> getDefaultFormat() {
        return Arrays.asList(
                "Name",
                "Fontname",
                "Fontsize",
                "PrimaryColour",
                "SecondaryColour",
                "OutlineColour",
                "BackColour",
                "Bold",
                "Italic",
                "Underline",
                "StrikeOut",
                "ScaleX",
                "ScaleY",
                "Spacing",
                "Angle",
                "BorderStyle",
                "Outline",
                "Shadow",
                "Alignment",
                "MarginL",
                "MarginR",
                "MarginV",
                "Encoding"
        );
    }

    @Override
    protected AssStyle getDefaultEntry() {
        return new AssStyle();
    }

    @Override
    protected AssStyle getEntry(String linetype, Map<String, Object> values) {
        if (!linetype.equalsIgnoreCase("Style"))
            throw new NumberFormatException("Invalid line type");

        return new AssStyle(values);
    }

    @Override
    public String getSectionTitle() {
        return "v4+ Styles";
    }
}
