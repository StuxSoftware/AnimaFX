package net.stuxcrystal.jass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Section for events.
 */
public class AssEventSection extends AssFormattedSection<AssEvent> {


    @Override
    protected List<String> getDefaultFormat() {
        return Arrays.asList(
                "Layer",
                "Start",
                "End",
                "Style",
                "Name",
                "MarginL",
                "MarginR",
                "MarginV",
                "Effect",
                "Text"
        );
    }

    @Override
    protected AssEvent getDefaultEntry() {
        return new AssEvent();
    }

    @Override
    protected AssEvent getEntry(String linetype, Map<String, Object> values) {
        AssEventType type = null;
        for (AssEventType cType : AssEventType.values()) {
            if (cType.getLineStart().equalsIgnoreCase(linetype)) {
                type = cType;
                break;
            }
        }

        if (type == null)
            throw new NumberFormatException("Invalid line type.");

        AssEvent result = new AssEvent(values);
        result.setEventType(type);
        return result;
    }

    @Override
    public String getSectionTitle() {
        return "Events";
    }
}
