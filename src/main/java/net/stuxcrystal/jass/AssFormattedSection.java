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

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;

/**
 * Represents a formatted section of ASS-Files.<p />
 *
 * This class parses all formatted sections.
 *
 * @param <T>
 */
public abstract class AssFormattedSection<T extends AssFormattedEntry> extends AssSection<T> {

    /**
     * A map of all existing wrappers.
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_MAP = new HashMap<>();

    /**
     * Initializes the map.
     */
    static {
        WRAPPER_MAP.put(boolean.class, Boolean.class);
        WRAPPER_MAP.put(byte.class, Byte.class);
        WRAPPER_MAP.put(char.class, Character.class);
        WRAPPER_MAP.put(short.class, Short.class);
        WRAPPER_MAP.put(int.class, Integer.class);
        WRAPPER_MAP.put(long.class, Long.class);
        WRAPPER_MAP.put(float.class, Float.class);
        WRAPPER_MAP.put(double.class, Double.class);
    }

    /**
     * Returns the default format of the section.
     * @return The default format.
     */
    protected abstract List<String> getDefaultFormat();

    /**
     * Returns the default entry.
     * @return The default entry.
     */
    protected abstract T getDefaultEntry();

    /**
     * Packs the entry into the designated entry type.
     * @param linetype The type of the line.
     * @param values   the values
     * @return The generated entry.
     */
    protected abstract T getEntry(String linetype, Map<String, Object> values);

    /**
     * Parses the value for jAss.
     * @param type  The type
     * @param value
     * @return
     */
    private Object parseValue(Class<?> type, String value) {
        if (boolean.class.isAssignableFrom(type)) {
            return value.equals("-1");
        }

        if (type.isPrimitive())
            type = WRAPPER_MAP.get(type);

        if (WRAPPER_MAP.containsValue(type)) {
            try {
                Method m = type.getMethod("valueOf", String.class);
                int mods = m.getModifiers();
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods))
                    return m.invoke(null, value);
                throw new NumberFormatException("Failed to find formatter...");
            } catch (NoSuchMethodException e) {
                if (char.class.isAssignableFrom(type)) {
                    if (value.length() > 0)
                        return value.charAt(0);
                    else
                        throw new NumberFormatException("Unknown format value.");
                }
                throw new InternalError("Didn't found value-of method of a primitive class.");
            } catch (InvocationTargetException e) {
                throw (RuntimeException) e.getCause();
            } catch (IllegalAccessException e) {
                throw new InternalError("Failed to call public function -> Access Violation.");
            }
        } else if (Color.class.isAssignableFrom(type)) {
            return AssColorParser.parse(value);
        } else if (AssValue.class.isAssignableFrom(type)) {
            if (type.isEnum()) {
                for (Object cValue : type.getEnumConstants()) {
                    if (((AssValue) cValue).getAssFormatted().equalsIgnoreCase(value))
                        return cValue;
                }
                throw new NumberFormatException("Invalid format value.");
            } else {
                try {
                    Constructor<?> constructor = type.getConstructor(String.class);
                    return constructor.newInstance(value);
                } catch (NoSuchMethodException e) {
                    throw new NumberFormatException("The given value does not support strings as values.");
                } catch (InvocationTargetException | InstantiationException e) {
                    throw new RuntimeException("Failed to construct values", e);
                } catch (IllegalAccessException e) {
                    throw new NumberFormatException("The given value does not have a public constructor.");
                }
            }
        } else if (String.class.equals(type)) {
            return value;
        } else {
            throw new NumberFormatException("Invalid type given: " + type);
        }
    }

    @Override
    public List<T> parse(List<String> lines) {
        List<T> result = new ArrayList<>();
        T def = this.getDefaultEntry();

        List<String> format = this.getDefaultFormat();

        for (String line : lines) {

            if (!line.contains(":"))
                continue;

            String[] data = line.split(":[ ]?", 2);

            if (data[0].equalsIgnoreCase("Format")) {
                format = Arrays.asList(data[1].split(",[ ]?"));
                continue;
            }

            Map<String, Object> content = new HashMap<>(def.getValues());
            String[] values = data[1].split(",", format.size());

            for (int i = 0; i<values.length; i++) {
                content.put(format.get(i), this.parseValue(def.getTypeOf(format.get(i)), values[i]));
            }

            result.add(this.getEntry(data[0], content));
        }

        return result;
    }

    @Override
    public List<String> dump(List<T> entries) {
        List<String> lines = new ArrayList<>();
        List<String> format = this.getDefaultFormat();
        T def = this.getDefaultEntry();

        lines.add(this.getLine("Format", true, format));

        for (T entry : entries) {
            Map<String, Object> values = entry.getValues();
            List<String> result = new ArrayList<>();

            for (String value : format) {
                Object o = values.get(value);
                if (o == null)
                    o = def.getValues().get(value);

                if (o instanceof AssValue)
                    result.add(((AssValue) o).getAssFormatted());
                else if (o instanceof Color)
                    result.add("&H" + AssColorParser.dump((Color) o));
                else if (o instanceof Boolean)
                    result.add((boolean) o?"-1":"0");
                else
                    result.add(o.toString());
            }

            lines.add(getLine(entry.getLineType(), false, result));
        }

        return lines;
    }

    private String getLine(String type, boolean space, List<String> values) {
        StringBuilder sb = new StringBuilder();

        sb.append(type).append(": ");
        boolean first = true;
        for (String value : values) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
                if (space)
                    sb.append(" ");
            }

            sb.append(value);
        }

        return sb.toString();
    }
}
