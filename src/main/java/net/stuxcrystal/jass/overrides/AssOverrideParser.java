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

package net.stuxcrystal.jass.overrides;

import net.stuxcrystal.jass.types.text.AssLinePart;
import net.stuxcrystal.jass.types.text.AssOverridePart;
import net.stuxcrystal.jass.types.text.AssTextPart;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A parser for Override-Tags.
 */
public class AssOverrideParser {

    /**
     * A list of tags.
     */
    private AssOverrideTagList tagList;

    /**
     * Creates a new parser for ass overrides.
     */
    public AssOverrideParser() {
        this.tagList = new AssOverrideTagList();
    }

    /**
     * Creates a new tag-list for the parser.
     * @return The new parser of the tag list.
     */
    public AssOverrideTagList getTagList() {
        return this.tagList;
    }

    /**
     * Parses the tags from the string.
     * @param tags The tags to parse.
     * @return A list of tags contained inside the parser.
     */
    public List<AssOverrideTag> parseTags(String tags) {
        int index = this.findFirstSlash(tags, 0);
        if (index == -1)
            return Collections.emptyList();


        tags = tags.substring(index);

        // The reader for the tokenizer and parsing functions.
        PushbackReader reader = new PushbackReader(new StringReader(tags), tags.length());

        List<AssOverrideTag> result = new ArrayList<>();

        // Parse the tags.
        while (true) {

            // Read the next char.
            int cChar;
            try {
                cChar = reader.read();
            } catch (IOException e) {
                break;
            }

            // There is no char anymore.
            if (cChar == -1)
                break;

            // Skip a slash.
            if ((char) cChar != '\\') {
                try {
                    reader.unread((char) cChar);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to push character back to stream.", e);
                }
            }

            // Read the next tag.
            AssOverrideTagType type;
            try {
                type = this.getTagList().get(reader);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read from a string reader.", e);
            }

            // If there is no valid tag:
            // skip tag.
            if (type == null) {
                try {
                    this.skipTag(reader);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to skip the tag.", e);
                }

                continue;
            }

            List<String> arguments;
            try {
                arguments = AssOverrideTokenizer.next(reader);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read from a string reader.", e);
            }

            result.add(type.getOverrideTag(this, arguments));
        }

        return result;
    }

    /**
     * Finds the first active slash for the tags.<p />
     *
     * Always uses the parsing algorithm since the indexOf algorithm iterate
     * over the entire string.
     *
     * @param tags The tags to parse.
     * @return The index of the first slash.
     */
    private int findFirstSlash(String tags, int from) {

        // Begin parsing.
        int level = 0;
        for (int i = from; i<tags.length(); i++) {
            char c = tags.charAt(i);

            switch (c) {

                // Add a level if a opening bracket appears.
                case '(':
                    level++;
                    break;

                // Remove a level if a closing bracket appears.
                case ')':
                    if (level > 0)
                        level--;
                    break;

                // Return the index if there is a slash on
                // the base level.
                case '\\':
                    if (level == 0)
                        return i;
            }
        }

        return -1;
    }

    /**
     * Skips the current tag.
     * @param reader The reader to skip.
     */
    private void skipTag(PushbackReader reader) throws IOException {

        int level = 0;

        while (true) {
            int cChar = reader.read();
            if (cChar == -1)
                return;

            switch ((char) cChar) {

                case '(':
                    level++;
                    break;

                case ')':
                    if (level > 0)
                        level--;
                    break;

                case '\\':
                    // Continue if the parser is currently at the base level.
                    if (level > 0)
                        continue;

                    reader.unread(cChar);
                    return;
            }
        }

    }

    /**
     * Dumps the tags into a string. Omits all invalid override tags.
     * @param tags The tags to dump.
     * @return A string containing all tags.
     */
    public String dumpBracketContents(List<AssOverrideTag> tags) {
        StringBuilder sb = new StringBuilder();

        for (AssOverrideTag tag : tags) {
            String stringValue = tag.toStringValue(this);
            if (stringValue != null)
                sb.append("\\").append(tag.getType().getName()).append(stringValue);
        }

        return sb.toString();
    }

    /**
     * Parses the entire line.
     * @param line The line to parse.
     * @return The parsed line.
     */
    public List<AssLinePart> parseLine(String line) {
        List<AssLinePart> parts = new ArrayList<>();
        PushbackReader reader = new PushbackReader(new StringReader(line));

        String part;
        while ((part = this.nextPart(reader)) != null) {
            if (part.startsWith("{") && part.endsWith("}")) {
                AssOverridePart overrides = new AssOverridePart();
                overrides.setValue(this.parseTags(part.substring(1, part.length() - 1)));
                parts.add(overrides);
            } else {
                AssTextPart text = new AssTextPart();
                text.setValue(part);
                parts.add(text);
            }
        }
        return parts;
    }

    /**
     * Reads the next part from the reader.
     * @param reader The reader to read.
     * @return The next part of the line.
     */
    private String nextPart(PushbackReader reader) {
        try {
            return AssOverrideTokenizer.nextPart(reader);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Dumps the parts of the line into a string.
     * @param parts The parts to dump.
     * @return The dumped parts.
     */
    @SuppressWarnings("unchecked")
    public String dumpParts(List<AssLinePart> parts) {
        StringBuilder sb = new StringBuilder();
        for (AssLinePart part : parts) {
            switch (part.getType()) {
                case TEXT:
                    sb.append(part.getValue());
                    break;

                case OVERRIDES:
                    sb.append("{").append(this.dumpBracketContents((List<AssOverrideTag>) part.getValue())).append("}");
                    break;
            }
        }
        return sb.toString();
    }
}
