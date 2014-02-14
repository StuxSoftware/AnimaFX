package net.stuxcrystal.jkaraoke;

import net.stuxcrystal.jass.AssEvent;
import net.stuxcrystal.jass.overrides.AssOverrideDefaultTagList;
import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.types.text.AssLinePart;
import net.stuxcrystal.jkaraoke.structures.Syllable;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses all karaoke lines.
 */
public class KaraokeParser {

    /**
     * Parses the override tags of the ass-file.
     */
    private AssOverrideParser parser;

    /**
     * Initializes the karaoke parser.
     */
    public KaraokeParser() {
        this.parser = new AssOverrideParser();
        AssOverrideDefaultTagList.addDefaultTags(this.parser.getTagList());
    }

    /**
     * Parses the syllables of the line.
     * @param event The event.
     * @return The syllables of the line.
     */
    @SuppressWarnings("unchecked")
    public List<Syllable> getSyllablesOfLine(AssEvent event) {
        List<Syllable> current = new ArrayList<>();

        int time = event.getStart().getRawTime();


        StringBuilder sb = new StringBuilder();          // The text.
        int start = time, duration = 0;
        boolean rawTiming = false;
        for (AssLinePart name : event.parseLine(this.parser)) {
            if (name.getType() == AssLinePart.Type.TEXT) {
                sb.append(name.getValue());
                continue;
            }

            List<AssOverrideTag> tags = (List<AssOverrideTag>) name.getValue();
            for (AssOverrideTag tag : tags) {
                switch (tag.getType().getName()) {

                    // Parse the ASS2 - Karaoke timing tag.
                    case "kt":
                        time = ((Integer) tag.getValue()) + event.getStart().getRawTime()*10;
                        rawTiming = true;
                        break;

                    // Parse the ASS - Karaoke tag.
                    case "K":
                    case "k":
                    case "ko":
                    case "kf":
                        current.add(new Syllable(start, duration, sb.toString()));

                        // Update values.
                        if (!rawTiming) {
                            time += duration * 10;
                        } else {
                            rawTiming = false;
                        }

                        start = time;
                        sb = new StringBuilder();
                        duration = ((Integer) tag.getValue());

                        break;
                }
            }
        }
        current.add(new Syllable(start, duration, sb.toString()));
        return current;
    }

}
