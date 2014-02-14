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

package net.stuxcrystal.jass.types;

import net.stuxcrystal.jass.AssUtils;
import net.stuxcrystal.jass.AssValue;

/**
 * Represents a time-code in Ass.<p />
 *
 * Ported from Aegisub ASS-File-Parser.
 */
public class AssTime implements AssValue {

    /**
     * Contains the time. (in milliseconds)
     */
    private final int time;

    /**
     * Default constructor.
     */
    public AssTime() {
        this(0);
    }

    /**
     * The time of the ass-file.
     * @param ms The time in milliseconds.
     */
    public AssTime(int ms) {
        this.time = AssUtils.clamp(0, ms, 10 * 60 * 60 * 1000 - 1);
    }

    /**
     * Parses the time from the time code.
     * @param ms The time code.
     */
    public AssTime(String ms) {
        this(AssTime.parseTime(ms));
    }

    /**
     * Parses the time.
     * @param ms The time as a string.
     * @return The parsed time.
     */
    private static int parseTime(String ms) {
        int after_decimal = -1;
        int current = 0;

        int currentMs = 0;

        for (char c : ms.toCharArray()) {
            if (!",.0123456789:".contains(new String(new char[]{c})))
                continue;

            char_parsing:
            switch (c) {
                case ':':
                    currentMs = currentMs * 60 + current;
                    current = 0;

                    // Clarifying labels.
                    break char_parsing;

                case '.':
                case ',':
                    currentMs = (currentMs * 60 + current) * 1000;
                    current = 0;
                    after_decimal = 100;

                    // Clarifying labels.
                    break char_parsing;

                default:
                    if (after_decimal < 0) {
                        current *= 10;
                        current += c - '0';
                    } else {
                        currentMs += (c - '0') * after_decimal;
                        after_decimal /= 10;
                    }

                    // Clarifying labels.
                    break char_parsing;
            }
        }

        if (after_decimal < 0)
            currentMs = (currentMs * 60 + current) * 1000;

        return currentMs;
    }

    /**
     * Returns the hour part of the time.
     * @return The hours for the time code.
     */
    public int getTimeHours() {
        return time / 3600000;
    }

    /**
     * Returns the minute part of the time.
     * @return The minutes of the time code.
     */
    public int getTimeMinutes() {
        return (time % 3600000) / 60000;
    }

    /**
     * Returns the second part of the time.
     * @return The minutes of the time code.
     */
    public int getTimeSeconds() {
        return (time % 60000) / 1000;
    }

    /**
     * Returns the milliseconds part of the time.
     * @return The milliseconds shown in the time code.
     */
    public int getTimeMilliseconds() {
        return (time % 1000);
    }

    /**
     * Returns the centiseconds part of the time.
     * @return The centiseconds shown in the time code.
     */
    public int getTimeCentiseconds() {
        return this.getTimeMilliseconds() / 10;
    }

    /**
     * Returns the raw amount of milliseconds.
     * @return The raw amount of milliseconds.
     */
    public int getRawTime() {
        return this.time;
    }

    /**
     * Returns the formatted time code.
     * @param msPrecision The precision in milliseconds for other subtitle formats.
     * @return The formatted value.
     */
    public String getAssFormatted(boolean msPrecision) {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getTimeHours()).append(':');
        sb.append((time % (60*60*1000)) / (60*1000*10)).append((time % (10 * 60 * 1000)) / (60 * 1000)).append(':');
        sb.append((time % (60*1000)) / (1000*10)).append((time % (10*1000)) / 1000).append('.');
        sb.append((time % 1000) / 100).append((time % 100) / 10);
        if (msPrecision)
            sb.append(time % 10);
        return sb.toString();
    }

    @Override
    public String getAssFormatted() {
        return this.getAssFormatted(false);
    }

    /**
     * Returns the time from the string.<p />
     *
     * Partly internal method.
     *
     * @param value The value.
     * @return The generated time.
     */
    public static AssTime fromString(String value) {
        return new AssTime(value);
    }

    @Override
    public String toString() {
        return "AssTime(" + this.getAssFormatted(true) + ")";
    }
}
