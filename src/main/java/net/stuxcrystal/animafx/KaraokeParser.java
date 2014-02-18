/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 StuxCrystal
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.stuxcrystal.animafx;

import net.stuxcrystal.jass.AssEvent;
import net.stuxcrystal.jass.overrides.AssOverrideDefaultTagList;
import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.types.text.AssLinePart;
import net.stuxcrystal.animafx.structures.Syllable;

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
