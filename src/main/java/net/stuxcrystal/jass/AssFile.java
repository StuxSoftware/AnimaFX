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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads an Ass-Script.
 */
public class AssFile {

    /**
     * A list of all entries.
     */
    private Map<AssSections, List<AssEntry>> entries = new HashMap<>();

    /**
     * Default constructor.
     */
    public AssFile() {}

    /**
     * Constructs the file and parses the data.
     * @param data The data to parse.
     * @throws IOException If an I/O-Operation fails.
     */
    public AssFile(Reader data) throws IOException {
        this();
        this.load(data);
    }

    /**
     * Constructs the file and parses the data.
     * @param data If an I/O-Operation fails.
     * @throws IOException If an I/O-Operation fails.
     */
    public AssFile(InputStream data) throws IOException {
        this();
        this.load(data);
    }

    /**
     * Constructs the file from the disk.
     * @param file If an I/O-Operation fails.
     * @throws IOException If an I/O-Operation fails.
     */
    public AssFile(File file) throws IOException {
        this();
        this.load(file);
    }

    /**
     * Sets the given entry.
     * @param section The section to replace.
     * @param entries The entries.
     */
    @SuppressWarnings("unchecked")
    private void setEntry(AssSections section, List entries) {
        this.entries.put(section, (List<AssEntry>) entries);
    }

    /**
     * Returns all info entries.
     * @return All entries of the script info section.
     */
    @SuppressWarnings("unchecked")
    public List<AssInfoEntry> getInfoEntries() {
        return (List<AssInfoEntry>) (List) this.entries.get(AssSections.SCRIPT_INFO);
    }

    /**
     * Sets the entries for the script info section.
     * @param entries The entries of the file.
     */
    public void setInfoEntries(List<AssInfoEntry> entries) {
        this.setEntry(AssSections.SCRIPT_INFO, entries);
    }

    /**
     * Returns a list of all supported styles.
     * @return All supported styles.
     */
    @SuppressWarnings("unchecked")
    public List<AssStyle> getStyles() {
        return (List<AssStyle>) (List) this.entries.get(AssSections.STYLES);
    }

    /**
     * Sets the new style list.
     * @param styles All existing styles.
     */
    public void setStyles(List<AssStyle> styles) {
        this.setEntry(AssSections.STYLES, styles);
    }

    /**
     * Returns a list of all events.
     * @return All events inside the file.
     */
    @SuppressWarnings("unchecked")
    public List<AssEvent> getEvents() {
        return (List<AssEvent>) (List) this.entries.get(AssSections.EVENTS);
    }

    /**
     * Sets the new event list.
     * @param events The new events.
     */
    public void setEvents(List<AssEvent> events) {
        this.setEntry(AssSections.EVENTS, events);
    }

    /**
     * Returns a list of all fonts.
     * @return All packaged fonts.
     */
    @SuppressWarnings("unchecked")
    public List<AssFileEntry> getFonts() {
        return (List<AssFileEntry>) (List) this.entries.get(AssSections.FONTS);
    }

    /**
     * Sets the new font list.
     * @param fonts The new fonts.
     */
    public void setFonts(List<AssFileEntry> fonts) {
        this.setEntry(AssSections.FONTS, fonts);
    }

    /**
     * Returns a list of all graphics.
     * @return All graphics inside the subtitle.
     */
    @SuppressWarnings("unchecked")
    public List<AssFileEntry> getGraphics() {
        return (List<AssFileEntry>) (List) entries.get(AssSections.GRAPHICS);
    }

    /**
     * Sets all new graphics.
     * @param graphics The new graphics.
     */
    public void setGraphics(List<AssFileEntry> graphics) {
        this.setEntry(AssSections.GRAPHICS, graphics);
    }

    /**
     * Returns all supported entries.
     * @return All entries.
     */
    public Map<AssSections, List<AssEntry>> getEntries() {
        return this.entries;
    }

    public void load(Reader reader) throws IOException {
        this.entries = AssParser.parse(reader);
    }

    /**
     * Loads the file from the input stream.
     * @param stream the stream.
     * @throws IOException If an I/O-Operation fails.
     */
    public void load(InputStream stream) throws IOException {
        this.load(AssUtils.getBOMLessReader(stream));
    }

    /**
     * Loads the data from a file.
     * @param file
     * @throws IOException
     */
    public void load(File file) throws IOException {
        this.load(new FileInputStream(file));
    }

    /**
     * Dumps the file into the writer.
     * @param writer The writer.
     * @throws IOException If an I/O-Operation fails.
     */
    public void dump(Writer writer) throws IOException {
        AssParser.dump(writer, this.entries);
    }

    /**
     * Dumps the file into the stream.
     * @param stream The stream to read.
     * @throws IOException If an I/O-Operation fails.
     */
    public void dump(OutputStream stream) throws IOException {
        this.dump(new OutputStreamWriter(stream, "UTF-8"));
    }

    /**
     * Dumps the file.
     * @param file The file to write to.
     * @throws IOException If an I/O-Operation fails.
     */
    public void dump(File file) throws IOException {
        FileWriter fw = new FileWriter(file);
        this.dump(fw);
        fw.close();
    }
}
