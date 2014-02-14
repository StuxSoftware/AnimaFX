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

package net.stuxcrystal.jass.overrides.parsers;

import net.stuxcrystal.jass.overrides.AssOverrideParser;
import net.stuxcrystal.jass.overrides.AssOverrideTag;
import net.stuxcrystal.jass.overrides.AssOverrideTagType;
import net.stuxcrystal.jass.overrides.AssOverrideUtils;
import net.stuxcrystal.jass.types.AssFade;

import java.util.List;

/**
 * Parses a fade tag.
 */
public class AssOverrideFadeTag extends AssOverrideTagType {

    /**
     *
     */
    private final boolean isSimple;

    /**
     * Constructs a new override tag.
     *
     * @param name The name of the override tag.
     */
    public AssOverrideFadeTag(String name, boolean simple) {
        super(name);
        this.isSimple = simple;
    }

    @Override
    public AssOverrideTag getOverrideTag(AssOverrideParser parser, List<String> values) {
        AssOverrideUtils.trimStringList(values);

        if (!(this.isSimple && values.size() == 4 || !this.isSimple && values.size() == 9))
            return null;

        return new AssOverrideTag<>(this, this.isSimple?this.parseSimple(values):this.parseComplex(values));
    }

    private AssFade parseSimple(List<String> values) {
        return AssFade.newFade(
                Integer.valueOf(values.get(1)),
                Integer.valueOf(values.get(2))
        );
    }

    private AssFade parseComplex(List<String> values) {
        return AssFade.newFade(
                Integer.valueOf(values.get(1)),
                Integer.valueOf(values.get(2)),
                Integer.valueOf(values.get(3)),
                Integer.valueOf(values.get(4)),
                Integer.valueOf(values.get(5)),
                Integer.valueOf(values.get(6)),
                Integer.valueOf(values.get(7))
        );
    }

    @Override
    public String getDumpContents(AssOverrideParser parser, AssOverrideTag values) {
        Object value = values.getValue();
        if (!(value instanceof AssFade))
            return null;

        return this.isSimple?this.dumpSimple((AssFade) value):this.dumpComplex((AssFade) value);
    }

    private String dumpSimple(AssFade value) {
        return "(" + value.getFadeIn().getDuration() + "," + value.getFadeOut().getDuration() + ")";
    }

    private String dumpComplex(AssFade value) {
        return "(" +
                value.getPreFadeInAlpha() + "," + value.getFadeIn().getAlpha() + "," + value.getFadeOut().getAlpha() + "," +
                value.getFadeIn().getStart() + "," + (value.getFadeIn().getStart() + value.getFadeIn().getDuration()) + "," +
                value.getFadeOut().getStart() + "," + (value.getFadeOut().getStart() + value.getFadeOut().getDuration()) +
                ")";


    }
}
