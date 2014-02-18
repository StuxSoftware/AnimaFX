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

package net.stuxcrystal.animafx.python;

import net.stuxcrystal.jass.AssEvent;
import net.stuxcrystal.jass.AssEventType;
import net.stuxcrystal.jass.AssFile;
import net.stuxcrystal.jass.AssStyle;
import net.stuxcrystal.jass.types.AssTime;
import net.stuxcrystal.animafx.AnimaFX;
import net.stuxcrystal.animafx.KaraokeToolkit;
import net.stuxcrystal.animafx.structures.Syllable;
import net.stuxcrystal.animafx.structures.TextExtents;
import org.python.core.*;
import org.python.expose.ExposedMethod;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

/**
 * The environment for pykaraoke.
 */
public class KaraBuilderEnvironment implements ClassDictInit {

    /**
     * Creates a new __dict__ object-
     * @param dict The dictionary.
     */
    public static void classDictInit(PyObject dict) {
        dict.__setitem__("__doc__", new PyString("The environment for KaraBuilder."));
        dict.__setitem__("name", new PyString("AnimaFX"));
        dict.__setitem__("support", asTuple(new PyString("styles"), new PyString("output"), new PyString("images")));
        dict.__setitem__("attributes", asTuple(/*new PyString("no_edge_spaces")*/));
    }

    @ExposedMethod
    public static PyObject text_extents(PyObject[] args, String[] kwargs) throws PyException {
        ArgParser parser = new ArgParser("set_name", args, kwargs, new String[]{"style", "text"});

        PyObject style = parser.getPyObject(0);
        String text = parser.getString(1);

        if (style instanceof PyBaseString)
            return _text_extents(Py.tojava(style, String.class), text);
        else if (style instanceof PyDictionary)
            return _text_extents((PyDictionary) style, text);
        else
            throw Py.ValueError("The value has to be either a string or a dictionary.");
    }

    /**
     * Returns the text-extents with the given name.
     * @param style The name of the style.
     * @param text  The text to calculate.
     * @return The calculated values.
     */
    public static PyObject _text_extents(String style, String text) {
        return _text_extents(KaraokeToolkit.getToolkit().getStyle(style), text);
    }

    /**
     * Returns the text-extents with the given data.
     * @param dictionary The dictionary with the style data.
     * @param text       The text to calculate.
     * @return The caluclated values.
     */
    public static PyObject _text_extents(PyDictionary dictionary, String text) {
        // dict[font=None, size=0, bold=False, italic=False]
        AssStyle style = new AssStyle();
        style.setFontname((dictionary.get(new PyString("font"))).asString());
        style.setFontsize(Py.py2int(dictionary.get(new PyString("size"))));
        style.setBold(Py.py2boolean(dictionary.get(new PyString("bold"))));
        style.setItalic(Py.py2boolean(dictionary.get(new PyString("italic"))));
        return _text_extents(style, text);
    }

    /**
     * Returns the text-extents using a file.
     * @param style The style.
     * @param text  The text.
     * @return The text-extents.
     */
    private static PyObject _text_extents(AssStyle style, String text) {
        if (style == null)
            return Py.None;

        TextExtents extents = KaraokeToolkit.getToolkit().getTextExtents(style, text);

        if (extents == null)
            return Py.None;

        PyDictionary dictionary = new PyDictionary();
        dictionary.put(new PyString("width"),   Py.java2py(extents.getWidth()));
        dictionary.put(new PyString("height"),  Py.java2py(extents.getHeight()));
        dictionary.put(new PyString("ascent"),  Py.java2py(extents.getAscend()));
        dictionary.put(new PyString("descent"), Py.java2py(extents.getDescent()));
        dictionary.put(new PyString("extlead"), Py.java2py(extents.getExtlead()));
        return dictionary;
    }

    /**
     * Returns all syllables as well as important metadata of the respective lines.
     * @param args   The arguments.
     * @param kwargs The kw-arguments.
     * @return All syllables.
     */
    @ExposedMethod
    public static PyObject get_syllables(PyObject[] args, String[] kwargs) {
        KaraokeToolkit toolkit = KaraokeToolkit.getToolkit();
        AnimaFX karaoke = toolkit.getJKaraoke();
        PyList list = new PyList();
        for (AssEvent event : karaoke.getInput().getEvents()) {

            PyList line = new PyList();

            //      {"start":int, "end":int,
            //       "style":dict[font=None, size=0, bold=False, italic=False] or Style-Name (if styles are supported),
            //       "anchor": ASS-Anchor-Integer value.
            //       "margin": (l,r,v)
            // }

            PyDictionary metadata = new PyDictionary();
            metadata.put(new PyString("start"), Py.java2py(event.getStart().getRawTime()));
            metadata.put(new PyString("end"), Py.java2py(event.getEnd().getRawTime()));
            metadata.put(new PyString("style"), new PyString(event.getStyle()));
            metadata.put(new PyString("anchor"), Py.java2py(Integer.valueOf(toolkit.getStyle(event.getStyle()).getAlignment().getAssFormatted())));
            metadata.put(new PyString("margin"), KaraBuilderEnvironment.getMargins(event, toolkit.getStyle(event.getStyle())));
            metadata.put(new PyString("layer"), Py.java2py(event.getLayer()));

            line.append(metadata);

            for (Syllable syllable : karaoke.getKaraokeParser().getSyllablesOfLine(event)) {
                // {"start":int (relative to line start), "duration":int, "text":str}
                PyDictionary syl = new PyDictionary();
                syl.put(new PyString("start"), Py.java2py(syllable.getStart()));
                syl.put(new PyString("duration"), Py.java2py(syllable.getDuration()));
                syl.put(new PyString("text"), new PyString(syllable.getText()));

                line.append(syl);
            }

            list.append(line);
        }

        return list;
    }

    /**
     * Returns the event.
     * @param event The event
     * @param style The style.
     * @return The margins.
     */
    private static PyObject getMargins(AssEvent event, AssStyle style) {
        return asTuple(
                Py.java2py(event.getMarginL()==0?style.getMarginL():event.getMarginL()),
                Py.java2py(event.getMarginR()==0?style.getMarginR():event.getMarginR()),
                Py.java2py(event.getMarginV()==0?style.getMarginV():event.getMarginV())
        );
    }

    /**
     * Writes the output into the file.
     * @param args    *args
     * @param kwargs  **kwargs
     * @return {@code None}
     */
    @ExposedMethod
    public static PyObject write_line(PyObject[] args, String[] kwargs) {
        ArgParser parser = new ArgParser("output", args, kwargs, new String[]{"style", "text", "start", "end", "layer"});

        KaraokeToolkit toolkit = KaraokeToolkit.getToolkit();
        AssFile file = toolkit.getJKaraoke().getOutput();
        if (file.getEvents() == null)
            file.setEvents(new ArrayList<AssEvent>());

        List<AssEvent> events = file.getEvents();
        String stylename;

        PyObject style = parser.getPyObject(0);
        if (style instanceof PyBaseString) {
            stylename = Py.tojava(style, String.class);
        } else if (style instanceof PyDictionary) {
            AssStyle fakeStyle = new AssStyle();
            fakeStyle.setFontname((((PyDictionary) style).get(new PyString("font"))).asString());
            fakeStyle.setFontsize(Py.py2int(((PyDictionary) style).get(new PyString("size"))));
            fakeStyle.setBold(Py.py2boolean(((PyDictionary) style).get(new PyString("bold"))));
            fakeStyle.setItalic(Py.py2boolean(((PyDictionary) style).get(new PyString("italic"))));

            stylename = fakeStyle.getFontname() + fakeStyle.getFontsize() + fakeStyle.isBold() + fakeStyle.isItalic();

            if (toolkit.getStyle(stylename) == null) {
                fakeStyle.setName(stylename);
                file.getStyles().add(fakeStyle);
            }
        } else {
            throw Py.ValueError("The style has to be either a string or a dictionary");
        }

        events.add(
                new AssEvent(
                        AssEventType.DIALOGUE,
                        parser.getInt(4),
                        new AssTime(parser.getInt(2)),
                        new AssTime(parser.getInt(3)),
                        stylename,
                        "",
                        0,
                        0,
                        0,
                        "",
                        parser.getString(1)
                )
        );

        return Py.None;
    }

    /**
     * Returns the style of the object.
     * @param args   *args
     * @param kwargs **kwargs
     * @return The data of the style.
     */
    @ExposedMethod
    public static PyObject get_style(PyObject[] args, String[] kwargs) {
        ArgParser argparser = new ArgParser("get_style", args, kwargs, new String[]{"name"});

        AssStyle style = KaraokeToolkit.getToolkit().getStyle(argparser.getString(0));
        if (style == null)
            return Py.None;

        return KaraBuilderEnvironment.to_style(style);
    }

    /**
     * Returns all style of the object.
     * @param args   *args
     * @param kwargs **kwargs
     * @return A dictionary with all styles.
     */
    @ExposedMethod
    public static PyObject get_styles(PyObject[] args, String[] kwargs) {
        PyDictionary list = new PyDictionary();

        if (KaraokeToolkit.getToolkit().getJKaraoke().getInput().getStyles() != null) {
            for (AssStyle style : KaraokeToolkit.getToolkit().getJKaraoke().getInput().getStyles()) {
                list.put(new PyString(style.getName()), KaraBuilderEnvironment.to_style(style));
            }
        }

        return list;
    }

    /**
     * Returns the pixels of the image.
     * @param args    *args
     * @param kwargs  **kwargs
     * @return A two-dimensional array of colors.
     */
    @ExposedMethod
    public static PyObject get_image(PyObject[] args, String[] kwargs) {
        ArgParser argParser = new ArgParser("get_image", args, kwargs, "filename");

        BufferedImage image = KaraokeToolkit.getToolkit().getImage(argParser.getString(0));
        if (image == null)
            return Py.None;
        PyList img = new PyList();
        for (int x = 0; x<image.getWidth(); x++) {
            PyList row = new PyList();
            for (int y = 0; y<image.getHeight(); x++) {
                row.append(Py.java2py(image.getRGB(x, y)));
            }
            img.append(row);
        }
        return img;
    }

    /**
     * Returns the size of the viewport.
     * @param args   *args
     * @param kwargs **kwargs
     * @return The bounds of the viewport.
     */
    @ExposedMethod
    public static PyObject get_viewport_size(PyObject[] args, String[] kwargs) {
        Rectangle2D rect = KaraokeToolkit.getToolkit().getJKaraoke().getInputMetaData().getSize();
        return asTuple(Py.java2py((int) rect.getWidth()), Py.java2py((int) rect.getHeight()));
    }

    /**
     * Makes a KaraBuilder-Style-Data-Dictionary.
     * @param style The style.
     * @return The data of the style.
     */
    private static PyObject to_style(AssStyle style) {
        PyDictionary dictionary = new PyDictionary();
        dictionary.put(new PyString("font"), new PyString(style.getFontname()));
        dictionary.put(new PyString("size"), Py.java2py(style.getFontsize()));
        dictionary.put(new PyString("bold"), Py.java2py(style.isBold()));
        dictionary.put(new PyString("italic"), Py.java2py(style.isItalic()));
        dictionary.put(new PyString("spacing"), Py.java2py(style.getSpacing()));
        return dictionary;
    }

    /**
     * Makes a python tuple.
     * @param objects The objects to make a tuple of.
     * @param <T>     The type.
     * @return A python tuple.
     */
    @SafeVarargs
    private static <T> PyTuple asTuple(T... objects) {
        PyObject[] result = new PyObject[objects.length];

        for (int i = 0; i<objects.length; i++) {
            result[i] = Py.java2py(objects[i]);
        }

        return new PyTuple(result);
    }
}
