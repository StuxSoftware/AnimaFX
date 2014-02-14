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

/**
 * Represents a fade.
 */
public class AssFade {

    /**
     * Opaque objects have this alpha level.
     */
    private static final int ALPHA_OPAQUE = 0x00;

    /**
     * Transparent object have this alpha level.
     */
    private static final int ALPHA_TRANSPARENT = 0xFF;

    /**
     * Contains data about a fade.
     */
    public static class Fade {

        /**
         * The start time of the fade.<p />
         *
         * If -1, the start time is not defined yet.
         */
        private int start;

        /**
         * The duration of the fade.
         */
        private int duration;

        /**
         *
         */
        private int alpha;

        /**
         * Creates a new fade.
         * @param alpha    The alpha-level after the fade.
         * @param start      The start time of the fade.
         * @param duration   The duration of the fade.
         */
        public Fade(int alpha, int start, int duration) {
            this.alpha = alpha;
            this.start = start;
            this.duration = duration;
        }

        /**
         * Creates a new fade.
         * @param alpha    The alpha-level after the fade.
         * @param duration The duration of the fade.
         */
        public Fade(int alpha, int duration) {
            this(alpha, -1, duration);
        }

        /**
         * Returns the start time of the fade.<p />
         *
         * If the start time is not defined yet, {@code -1} is used.
         * @return The start time or {@code -1}
         */
        public int getStart() {
            return this.start;
        }

        /**
         * Sets the start time of the fade.<p />
         * @param start The new start time of the fade.
         */
        public void setStart(int start) {
            this.start = start;
        }

        /**
         * Returns the duration of the fade.
         * @return The duration of the fade.
         */
        public int getDuration() {
            return this.duration;
        }

        /**
         * Sets the duration of the fade.
         * @param duration The new duration of the fade.
         */
        public void setDuration(int duration) {
            this.duration = duration;
        }

        /**
         * Returns the alpha-level after the fade.
         * @return The alpha-level after the fade.
         */
        public int getAlpha() {
            return this.alpha;
        }

        /**
         * Sets the alpha level after the fade.
         * @param alpha The alpha-level after the fade.
         */
        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }
    }

    /**
     * The alpha before the fade.
     */
    private int alphaPre = 0xFF;

    /**
     * The fade in data.
     */
    private final Fade fadeIn;

    /**
     * The fade out data.
     */
    private final Fade fadeOut;

    /**
     * Creates a new fade object.
     * @param fadeInStart      The start of the fade in.
     * @param fadeInDuration   The duration of the fade in.
     * @param fadeOutStart     The start of the fade out.
     * @param fadeOutDuration  The duration of the fade out.
     * @param alphaPre         The alpha level before the fade in.
     * @param alphaMid         The alpha level after the fade in.
     * @param alphaEnd         The alpha level after the fade out.
     */
    private AssFade(int fadeInStart, int fadeInDuration, int fadeOutStart, int fadeOutDuration, int alphaPre, int alphaMid, int alphaEnd) {
        this.alphaPre = alphaPre;
        this.fadeIn = new Fade(alphaMid, fadeInStart, fadeInDuration);
        this.fadeOut = new Fade(alphaEnd, fadeOutStart, fadeOutDuration);
    }


    /**
     * Returns the container for fade-in data.
     * @return The container for fade-in data.
     */
    public Fade getFadeIn() {
        return this.fadeIn;
    }

    /**
     * Returns the container for fade-out data.
     * @return The container for fade-out data.
     */
    public Fade getFadeOut() {
        return this.fadeOut;
    }

    /**
     * Returns the alpha-level before the fade-in.
     * @return The alpha-level before the fade-in.
     */
    public int getPreFadeInAlpha() {
        return this.alphaPre;
    }

    /**
     * Sets the alpha-level before the fade-in.
     * @param alpha The alpha-level before the fade-in.
     */
    public void setPreFadeInAlpha(int alpha) {
        this.alphaPre = alpha;
    }

    @Override
    public String toString() {
        return "AssFade(" +
                this.alphaPre + "," + this.getFadeIn().getAlpha() + "," + this.getFadeOut().getAlpha() + "," +
                "Fade-In:<" + this.getFadeIn().getStart() + "," + this.getFadeIn().getDuration() + ">," +
                "Fade-Out:<" + this.getFadeOut().getStart() + "," + this.getFadeOut().getDuration() + ">," +
                ")";
    }

    /**
     * Creates a new fade object out of these values.
     * @param durationIn   The duration for the fade in.
     * @param durationOut  The duration for the fade out.
     * @return A new AssFade object.
     */
    public static AssFade newFade(int durationIn, int durationOut) {
        return new AssFade(-1, durationIn, -1, durationOut, ALPHA_TRANSPARENT, ALPHA_OPAQUE, ALPHA_TRANSPARENT);
    }

    /**
     * Creates a new fade object out of these values.
     * @param alphaPre     The alpha level before the fade in.
     * @param alphaMid     The alpha level after the fade in.
     * @param alphaEnd     The alpha level after the fade out.
     * @param fadeInStart  The time when the fade-in starts.
     * @param fadeInEnd    The time when the fade-in ends.
     * @param fadeOutStart The time when the fade-out starts.
     * @param fadeOutEnd   The time when the fade-out ends.
     * @return The new AssFade-Object.
     */
    public static AssFade newFade(int alphaPre, int alphaMid, int alphaEnd, int fadeInStart, int fadeInEnd, int fadeOutStart, int fadeOutEnd) {
        return new AssFade(fadeInStart, fadeInEnd-fadeInStart, fadeOutStart, fadeOutEnd-fadeInStart, alphaPre, alphaMid, alphaEnd);
    }
}
