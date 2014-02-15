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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Utility class for files (inside ass files).
 */
public final class AssFileEntryUtils {

    /**
     * All numeric values.
     */
    private static final String NUMERIC = "0123456789";

    /**
     * This is a utility class.
     */
    private AssFileEntryUtils() {}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Reads the data.

    /**
     * Returns the data part of the file.
     * @param filename The name to parse.
     * @return The data part of the file.
     */
    private static String getDataPart(String filename) {
        if (filename.contains("."))
            filename = filename.substring(0, filename.lastIndexOf("."));

        return filename.lastIndexOf("_")==-1?"":filename.substring(filename.lastIndexOf("_")+1);
    }

    /**
     * Returns the encoding of the font.
     * @param filename The name of the font.
     * @return The encoding of the font. (Defaults to zero.)
     */
    public static int getEncodingOfFont(String filename) {
        if (filename.contains("."))
            filename = filename.substring(0, filename.lastIndexOf("."));

        int cPart = 1;
        int encoding = 0;

        for (int i = filename.length() - 1 ; i>=0; i--) {
            // The character of the file.
            char character = filename.charAt(i);
            if (!NUMERIC.contains(new String(new char[]{character})))
                break;

            // Add the character to the encoding.
            encoding += (character - '0') * cPart;
            cPart *= 10;
        }

        return encoding;
    }

    /**
     * Checks if the font-file-name denotes a bold font.
     * @param filename The file name.
     * @return {@code true} if the font is a bold font.
     */
    public static boolean isBoldFont(String filename) {
        String data = AssFileEntryUtils.getDataPart(filename);
        return data.contains("B");
    }

    /**
     * Checks if the font-file-name denotes an italic font.
     * @param filename The name of the file.
     * @return {@code true} if the font is an italic font.
     */
    public static boolean isItalicFont(String filename) {
        String data = AssFileEntryUtils.getDataPart(filename);
        return data.contains("I");
    }

    /**
     * Returns the file name of the font according to the
     * specifications of the ass-files.
     * @param filename The original filename.
     * @param bold     Is the font a bold font?
     * @param italic   Is the font for italic texts?
     * @param encoding What is the encoding of the font.
     * @return The filename of the text according to the specifications.
     */
    public static String getFileName(String filename, boolean bold, boolean italic, int encoding) {
        String name = filename.contains(".")?filename.substring(0, filename.lastIndexOf(".")):filename;
        String ext = filename.contains(".")?filename.substring(filename.lastIndexOf(".")):"";

        return name + "_" + (bold?"B":"") + (italic?"I":"") + encoding + ext;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Reads the entry.

    /**
     * Reads the font from the file.
     * @param entry the entry to read.
     * @return The created font.
     * @throws java.awt.FontFormatException If an invalid font format
     * @throws IOException If an I/O-Exception occurs.
     */
    public static Font getFont(AssFileEntry entry) throws FontFormatException, IOException {
        AssFileEntryInputStream afeis = new AssFileEntryInputStream(entry);
        Font font = Font.createFont(Font.TRUETYPE_FONT, afeis);
        afeis.close();
        return font;
    }

    /**
     * Reads an image from the given file entry.
     * @param entry The entry to read.
     * @return The new image.
     * @throws IOException If an I/O-Exception occurs.
     */
    public static BufferedImage getImage(AssFileEntry entry) throws IOException {
        AssFileEntryInputStream afeis = new AssFileEntryInputStream(entry);
        BufferedImage image = ImageIO.read(afeis);
        afeis.close();
        return image;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // File operations.

    /**
     * Moves the content from the input stream to the output stream.
     * @param in   The input stream.
     * @param out  The output stream.
     * @throws IOException If an I/O-Operation fails.
     */
    private static void move(InputStream in, OutputStream out) throws IOException {
        int length;
        byte[] buffer = new byte[4096];
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
    }

    /**
     * Extracts the entry to the specified file.
     * @param entry The entry to extract.
     * @param file  The file to write.
     * @throws IOException If an I/O-Operation fails.
     */
    public static void extract(AssFileEntry entry, File file) throws IOException {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }

        AssFileEntryInputStream afeis = null;
        FileOutputStream fos = null;
        try {
            afeis = new AssFileEntryInputStream(entry);
            fos = new FileOutputStream(file);
            move(afeis, fos);
        } finally {
            if (afeis != null) try { afeis.close(); } catch (IOException ignored) {}
            if (fos != null) try { fos.close(); } catch (IOException ignored) {}
        }
    }

    /**
     * Moves the contents of the file to the specified entry.
     * @param entry   The entry to copy.
     * @param file    The file to copy to.
     * @param append  Appends to the given file.
     * @throws IOException If an I/O-Operation fails.
     */
    public static void copy(AssFileEntry entry, File file, boolean append) throws IOException {
        AssFileEntryOutputStream afeos = null;
        FileInputStream fis = null;

        try {
            afeos = new AssFileEntryOutputStream(entry, append);
            fis = new FileInputStream(file);
            move(fis, afeos);
        } finally {
            if (afeos != null) try { afeos.close(); } catch (IOException ignored) {}
            if (fis != null) try { fis.close(); } catch (IOException ignored) {}
        }
    }

    /**
     * Moves the contents of the file to the specified entry.
     * @param entry   The entry to copy.
     * @param file    The file to move.
     * @throws IOException If an I/O-Operation fails.
     */
    public static void copy(AssFileEntry entry, File file) throws IOException {
        AssFileEntryUtils.copy(entry, file, false);
    }
}
