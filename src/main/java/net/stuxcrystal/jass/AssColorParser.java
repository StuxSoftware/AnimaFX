package net.stuxcrystal.jass;

import java.awt.*;

/**
 * Utility class to parse colors.
 */
public class AssColorParser {

    /**
     * This is a utility class.
     */
    private AssColorParser() {}

    /**
     * Parses a color in ASS.
     * @param color parses a color.
     * @return The parsed color.
     */
    public static Color parse(String color) {

        int a = 0xFF;
        int[] components = new int[3];

        if (color.startsWith("&"))
            color = color.substring(1);

        if (color.startsWith("H"))
            color = color.substring(1);

        if (color.endsWith("&"))
            color = color.substring(0, color.length() - 1);

        if (color.length() != 8 && color.length() != 6)
            throw new NumberFormatException("Invalid color length");

        if (color.length() == 8) {
            a = AssColorParser.parseAlpha(color);
        }

        int offset = color.length()==8?2:0;
        for (int i = 0; i<3; i+=1) {
            components[i] = Integer.parseInt(color.substring(offset + (i*2), offset + ((i + 1)*2)), 16);
        }

        int r = components[2], g = components[1], b = components[0];

        return new Color(r, g, b, a);
    }

    /**
     * Parse the alpha value out of the color.
     * @param alpha The alpha value.
     * @return The alpha.
     */
    public static int parseAlpha(String alpha) {
        if (alpha.startsWith("&"))
            alpha = alpha.substring(1);

        if (alpha.startsWith("H"))
            alpha = alpha.substring(1);

        if (alpha.endsWith("&"))
            alpha = alpha.substring(0, alpha.length()-1);

        if (alpha.length() != 2 && alpha.length() != 8)
            throw new NumberFormatException("Invalid color length for alpha values.");

        return 255 - Integer.parseInt(alpha.substring(0,2), 16);
    }

    /**
     * Dumps the color to an ASS-Format.
     * @param color The color to dump.
     * @return The dumped color.
     */
    public static String dump(Color color) {
        // Attention
        // Alpha Value is inversed.
        return String.format("%02X%02X%02X%02X", 255-color.getAlpha(), color.getBlue(), color.getGreen(), color.getRed());
    }

    /**
     * Dumps the color into a tag using format.
     * @param color The color to dump.
     * @return The dumped color.
     */
    public static String dumpAlphaless(Color color) {
        return String.format("&H%02X%02X%02X&", color.getBlue(), color.getGreen(), color.getRed());
    }

    /**
     * Dumps the alpha value to the ass format.
     * @param alpha The alpha value to dump.
     * @return The dumped alpha value.
     */
    public static String dumpAlpha(int alpha) {
        return String.format("&H%02X&", 255 - AssUtils.clamp(0, alpha, 255));
    }
}

