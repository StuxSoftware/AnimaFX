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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parses a file section.<p />
 *
 * Please note that a file-entry in a ass-file only supports a little bit more than 2Gb of data. If you exceed
 * the limit of the size, an {@link java.lang.OutOfMemoryError} will be thrown. The exact number of bytes
 * supported varies from VM to VM. Generally it can be said that {@code 2^32 - 8} bytes are supported.
 */
public class AssFileSection extends AssSection<AssFileEntry> {

    /**
     * the title of the section.
     */
    private final String title;

    /**
     * The type of the line for this section.
     */
    private final String lineType;

    /**
     * Creates a new file section.
     */
    public AssFileSection(String title, String lineType) {
        this.title = title;
        this.lineType = lineType;
    }

    /**
     * Returns the title of the section.
     * @return The title of the section.
     */
    @Override
    public String getSectionTitle() {
        return this.title;
    }

    @Override
    public List<AssFileEntry> parse(List<String> lines) {
        List<AssFileEntry> entries = new ArrayList<>();

        String file = null;
        String data = "";
        for (String line : lines) {
            if (line.startsWith(this.lineType)) {
                if (file != null) {
                    entries.add(this.parseEntry(file, data));
                }

                file = line.substring(line.indexOf(":") + 1).trim();
                data = "";

                continue;
            }

            data += line;
        }

        if (file != null)
            entries.add(this.parseEntry(file, data));

        return entries;
    }

    private AssFileEntry parseEntry(String filename, String data) {
        AssFileEntry entry = new AssFileEntry(filename);
        entry.setContent(AssFileSection.uudecode(data));
        return entry;
    }

    @Override
    public List<String> dump(List<AssFileEntry> entries) {
        List<String> lines = new ArrayList<>();
        for (AssFileEntry entry : entries) {
            lines.add(this.lineType + ": " + entry.getFileName());
            lines.addAll(Arrays.<String>asList(AssFileSection.uuencode(entry.getContent()).split("\n")));
            lines.add("\n");
        }
        return lines;
    }

    /**
     * Encodes the data.<p />
     *
     * Ported from libaegisub.
     *
     * @param data The data to encdoe.
     * @return The encoded data.
     */
    public static String uuencode(byte[] data) {
        StringBuilder sb = new StringBuilder((data.length * 4 + 2) / 3 + data.length / 80 * 2);

        int written = 0;
        for (int pos = 0; pos < data.length; pos+=3) {
            int[] src = {0x00, 0x00, 0x00};

            // Copy the values. ("De-"sign the values.)
            for (int i = 0; i < Math.min(3, data.length - pos); i++)
                src[i] = data[pos + i] & 0xFF;

            // Begin encoding the atom.
            char[] dst = {
                    (char)                            (src[0]         >> 2),
                    (char) (((src[0] & 0x03) << 4) | ((src[1] & 0xF0) >> 4)),
                    (char) (((src[1] & 0x0F) << 2) | ((src[2] & 0xC0) >> 6)),
                    (char)   (src[2] & 0x3F)
            };

            for (int i = 0; i < Math.min(data.length - pos + 1, 4); i++) {
                sb.append((char) ((int) dst[i] + 33));

                if (++written == 80 && pos + 3 < data.length) {
                    written = 0;
                    sb.append("\n");
                }
            }
        }

        return sb.toString();
    }

    /**
     * Decodes the string.<p />
     *
     * Ported from libaegisub.
     *
     * @param data The data to decode.
     * @return The decoded byte-array.
     */
    public static byte[] uudecode(String data) {
        // Reserve at least that many bytes in the list.
        List<Byte> ret = new ArrayList<>(data.length() * 3/4);
        int length = data.length();

        for (int pos = 0; pos+1 < length;) {
            int bytes = 0;
            char[] src = new char[]{'\0', '\0', '\0', '\0'};

            for (int i = 0; i<4 && pos<length;) {
                char c = data.charAt(pos++);
                if (c != '\0' && c!= '\n' && c!='\r') {
                    src[i++] = (char) (c-33);
                    ++bytes;
                }
            }

            if (bytes > 1)
                ret.add((byte) ((src[0] << 2) | (src[1] >> 4)));
            if (bytes > 2)
                ret.add((byte) (((src[1] & 0xF) << 4) | (src[2] >> 2)));
            if (bytes > 3)
                ret.add((byte) (((src[2] & 0x3) << 6) | (src[3])));
        }

        return AssUtils.toByteArray(ret);
    }
}
