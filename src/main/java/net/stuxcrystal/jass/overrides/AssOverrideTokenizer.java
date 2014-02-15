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

import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A tokenizer for ASS-Tags.
 */
public class AssOverrideTokenizer {

    /**
     * This class cannot be instantiated.
     */
    private AssOverrideTokenizer() {}

    /**
     * Tokenizes the string.<p />
     *
     * If the string does not start with an opening parenthesis the only element of the list will be the text
     * itself.<p />
     *
     * If the string starts with an opening parentheses the first element will be everything outside the
     * parenthesis. The following elements will be the arguments inside the parentheses in the order they are
     * written followed by anything after the parenthesis.
     *
     * @return A list of the arguments
     * @see #next(java.io.PushbackReader)
     */
    public static List<String> tokenize(String text) {
        // The list has maximal 6 values.
        List<String> data = new ArrayList<>(6);

        // Return an empty list
        if (text.length() == 0)
            return data;

        StringBuilder sb = new StringBuilder();

        // -1 is the base level.
        int level = -1;

        for (char c : text.toCharArray()) {
            switch (c) {
                case '(':
                    if (level++ == -1) {
                        data.add(sb.toString());
                        sb = new StringBuilder();
                        continue;
                    }
                    break;

                case ',':
                    if (level == 0) {
                        data.add(sb.toString());
                        sb = new StringBuilder();
                        continue;
                    }
                    break;

                case ')':
                    if (level-- == 0) {
                        if (level < 0)
                            level = -1;

                        data.add(sb.toString());
                        sb = new StringBuilder();
                        continue;
                    }
                    break;
            }

            sb.append(c);
        }

        data.add(sb.toString());

        // If the text ends abruptly.
        if (level >= 0)
            data.add("");

        return data;
    }

    /**
     * Reads the next tag from the reader. Supports nested brackets.
     *
     * @param reader The reader to read from.
     * @return The list of arguments following the tag.
     * @throws IOException If an I/O-Operation fails.
     */
    public static List<String> next(PushbackReader reader) throws IOException {
        // Returns the result string.
        StringBuilder sb = new StringBuilder();

        int level = 0;

        charParser:
        while (true) {
            // Read the next character.
            int cRaw = reader.read();

            // Stop if the stream reaches the end.
            if (cRaw == -1)
                break;

            char c = (char) cRaw;

            // Begin parsing.
            switch (c) {

                // Parse opening parenthesis.
                case '(':
                    // Go to next level.
                    level++;
                    sb.append(c);
                    break;

                // Parse closing parenthesis.
                case ')':
                    // Go down a level.
                    if (level > 0)
                        level--;

                    sb.append(c);
                    break;

                // Parse slashes.
                case '\\':
                    // If the slash is on the base level, the next tag begins.
                    if (level <= 0) {
                        reader.unread(c);
                        break charParser;
                    }

                    /* Fall through */

                // Just add anything other.
                default:
                    sb.append(c);
            }
        }

        // Returns the string.
        return AssOverrideTokenizer.tokenize(sb.toString());
    }

    /**
     * Reads the next part of the string.<p />
     *
     * If the string starts with a "{" and ends with an "}" this part is a
     * comment part and could contain comments.
     *
     * @param reader The reader to read from.
     * @return The next part of the text.
     * @throws IOException If an I/O-Operation fails.
     */
    public static String nextPart(PushbackReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        boolean eos = false;
        boolean comment = false;
        for (;;) {
            int cChar = reader.read();
            if (cChar == -1) {
                eos = true;
                break;
            }

            char c = (char) cChar;

            if (c == '{') {
                if (sb.length() != 0 && !comment) {
                    reader.unread(c);
                    break;
                } else if (sb.length() == 0) {
                    comment = true;
                }
            } else if (c == '}') {
                if (comment) {
                    sb.append(c);
                    break;
                }
            }

            sb.append(c);
        }

        if (sb.length() == 0 && eos)
            return null;

        if (comment && !sb.toString().endsWith("}"))
            sb.append("}");

        return sb.toString();
    }
}
