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

package net.stuxcrystal.jass;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses an Ass-File.
 */
public class AssParser {

    /**
     * The skeleton of a ASS-File.
     */
    private static final AssSections[] ASS_SKELETON = {
            // First dump the script info.
            AssSections.SCRIPT_INFO,

            // Then dump the styles.
            AssSections.STYLES,

            // After that dump the lines.
            AssSections.EVENTS,

            // Than dump the fonts.
            AssSections.FONTS,

            // And finally dump the graphics.
            AssSections.GRAPHICS
    };

    /**
     * Stores the result of a section parse.
     */
    private static class SectionParserResult {

        /**
         * The section of the data.
         */
        private AssSections section;

        /**
         * The parsed entries.
         */
        private List<AssEntry> entries;

    }

    public static Map<AssSections, List<AssEntry>> parse(Reader source) throws IOException {
        BufferedReader reader = new BufferedReader(source);


        Map<AssSections, List<AssEntry>> result = new HashMap<>();

        List<String> cSectionData = new ArrayList<>();
        String cSection = null;

        boolean first = true;
        String cLine;
        while ((cLine = reader.readLine()) != null) {

            if (first) {
                if (cLine.startsWith("\u00EF"))
                first = false;
            }

            if (cLine.trim().startsWith("[") && cLine.trim().endsWith("]")) {
                if (cSection != null) {
                    SectionParserResult parserResult = AssParser.parseSection(cSection, cSectionData);
                    result.put(parserResult.section, parserResult.entries);
                }

                cSection = cLine.substring(1, cLine.length()-1);
                cSectionData = new ArrayList<>();
                continue;
            } else if (cSection == null) {
                throw new IOException("The file has to start with a section header.");
            }

            cSectionData.add(cLine);
        }

        if (cSection != null) {
            SectionParserResult parserResult = AssParser.parseSection(cSection, cSectionData);
            result.put(parserResult.section, parserResult.entries);
        }

        reader.close();

        return result;
    }

    @SuppressWarnings("unchecked")
    private static SectionParserResult parseSection(String section, List<String> sectionData) {
        SectionParserResult result = new SectionParserResult();

        for (AssSections sectionParser : AssSections.values()) {
            if (sectionParser.getSection().getSectionTitle().equalsIgnoreCase(section)) {
                result.section = sectionParser;
            }
        }

        result.entries = result.section.getSection().parse(sectionData);

        return result;
    }

    public static void dump(Writer sink, Map<AssSections, List<AssEntry>> entries) throws IOException {
        BufferedWriter writer = new BufferedWriter(sink);

        for (AssSections section : AssParser.ASS_SKELETON) {
            if (entries.containsKey(section)) {
                writer.write("[" + section.getSection().getSectionTitle() + "]");
                writer.newLine();
                for (Object line : section.getSection().dump(entries.get(section))) {
                    writer.write((String) line);
                    writer.newLine();
                }
                writer.newLine();
            }
        }
        writer.flush();
    }

}
