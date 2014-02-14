package net.stuxcrystal.jass.overrides;

import java.util.List;

/**
 * Override utilities.
 */
public class AssOverrideUtils {

    /**
     * This is a utility class.
     */
    private AssOverrideUtils() {}

    /**
     * Trims all strings inside a list of strings.
     * @param values The list of strings.
     */
    public static void trimStringList(List<String> values) {
        for (int i = 0; i<values.size(); i++)
            values.set(i, values.get(i).trim());
    }

    /**
     * Integer to alphanumeric.
     * @param f The float to convert.
     * @return The converted float
     */
    public static String itoa(float f) {
        if ((int) f == f)
            return Integer.toString((int) f);
        return Float.toString(f);
    }

    /**
     * Integer to alphanumeric.
     * @param d The double to convert.
     * @return The converted double
     */
    public static String itoa(double d) {
        if ((int) d == d)
            return Integer.toString((int) d);
        return Double.toString(d);
    }

}
