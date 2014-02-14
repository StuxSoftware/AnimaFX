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

/**
 * Represents an event type.
 */
public enum AssEventType {

    /**
     * Represents a dialogue line.
     */
    DIALOGUE("Dialogue"),

    /**
     * Represents a comment.
     */
    COMMENT("Comment"),

    /**
     * Used on lines that should show a picture.<p />
     *
     * While this event type is described in the "unofficial"
     * specification of ASS-Files, this feature is not implemented
     * in any subtitle-editor.
     */
    PICTURE("Picture"),

    /**
     * Used on lines that should show a movie.<p />
     *
     * While this event type is described in the "unofficial"
     * specification of ASS-Files, this feature is not implemented
     * in any subtitle-editor.
     */
    MOVIE("Movie"),

    /**
     * Used on lines that should play a sound.<p />
     *
     * While this event type is described in the "unofficial"
     * specification of ASS-Files, this feature is not implemented
     * in any subtitle-editor.
     */
    SOUND("Sound"),

    /**
     * Represents a command line.<p />
     *
     * While this event type is described in the "unoffical"
     * documentation of ASS-Files, this feature is not implemented
     * in any subtitle-editor.<p />
     *
     * Do not execute these command as this may result in mayor
     * security issues while playback.
     */
    COMMAND("Command"),
    ;

    /**
     * The name of the line type.
     */
    private final String lineStart;

    /**
     * Constructs a new event type.
     * @param lineStart The start of the line.
     */
    AssEventType(String lineStart) {
        this.lineStart = lineStart;
    }

    /**
     * Returns the start of the line.
     * @return The string containing the line-type.
     */
    public String getLineStart() {
        return this.lineStart;
    }
}
