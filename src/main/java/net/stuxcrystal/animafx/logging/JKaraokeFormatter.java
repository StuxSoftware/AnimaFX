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

package net.stuxcrystal.animafx.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Returns a configurable simple formatter.
 */
public class JKaraokeFormatter extends Formatter {

    /**
     * The maximal length of the level translation.
     */
    private final int maximalLevelSize;

    /**
     * Creates a new formatter for jKaraoke.
     */
    public JKaraokeFormatter() {
        int size = 0;
        for (Level level : new Level[] {
                Level.ALL,  Level.CONFIG, Level.OFF,
                Level.FINE, Level.FINER,  Level.FINEST,
                Level.INFO, Level.SEVERE, Level.WARNING
        }) {
            size = Math.max(level.getLocalizedName().length(), size);
        }
        maximalLevelSize = size;
    }

    @Override
    public String format(LogRecord record) {
        Date date = new Date(record.getMillis());
        StringBuilder sb = new StringBuilder();

        String level = record.getLevel().getLocalizedName();

        sb.append("[").append(new SimpleDateFormat("HH:mm:ss").format(date)).append("]");
        sb.append("[").append(record.getLoggerName()).append("]");
        sb.append("[").append(level).append("] ");
        for (int i = level.length(); i<maximalLevelSize; i++)
            sb.append(" ");

        sb.append(record.getMessage());
        if (record.getThrown() != null) {
            sb.append("\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            sb.append(sw.toString());
        } else {
            sb.append("\n");
        }
        return sb.toString();
    }
}
