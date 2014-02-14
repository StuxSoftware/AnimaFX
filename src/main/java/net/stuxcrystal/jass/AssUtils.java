package net.stuxcrystal.jass;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Utils for various tasks of the library.
 */
public final class AssUtils {

    /**
     * This is a utility class.
     */
    private AssUtils() {}

    /**
     * Clamps the value.
     * @param min  The minimal value.
     * @param cur  The value to clamp.
     * @param max  The maximal value.
     * @return The clamped value.
     */
    public static int clamp(int min, int cur, int max) {
        return Math.min(Math.max(cur, min), max);
    }

    /**
     * Converts a byte-list to a byte array.
     * @param bytes The list of bytes.
     * @return The result.
     */
    public static byte[] toByteArray(List<Byte> bytes) {
        byte[] result = new byte[bytes.size()];
        for (int index = 0; index < bytes.size(); index++)
            result[index] = bytes.get(index);
        return result;
    }

    /**
     * A file supporting UTF-8-File boms.
     * @param input The reader to use.
     * @return A reader discarding the UTF-8-BOM.
     */
    public static Reader getBOMLessReader(InputStream input) throws IOException {
        // Since we know the size of the BOM, give the stream the bom.
        PushbackInputStream stream = new PushbackInputStream(input, 3);
        byte[] bom = new byte[3];
        if (stream.read(bom) != -1)
            if (!Arrays.equals(bom, new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF})) {
                stream.unread(bom);
        }

        return new InputStreamReader(stream, "UTF-8");
    }
}
