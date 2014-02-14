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

package net.stuxcrystal.jkaraoke;

/**
 * Detects the current operating system.
 */
public enum OperatingSystem {

    /**
     * Set if the current operating system is windows.
     */
    WINDOWS("Windows"),

    /**
     * Set if the current operating system is linux.
     */
    LINUX("Linux"),

    /**
     * Set if the software runs on a mac.
     */
    MAC("Mac OS"),

    /**
     * If the software runs on solaris.
     */
    SOLARIS("Solaris"),

    /**
     * Other operating systems.
     */
    OTHER("<Unknown>");

    private final String name;

    /**
     * Contains the current operating system.
     */
    public static final OperatingSystem CURRENT_OPERATING_SYSTEM = OperatingSystem.getOperatingSystem();

    /**
     * Creates a new Operating-System implementation.
     * @param name The name of the operating system.
     */
    OperatingSystem(String name) {
        this.name = name;
    }

    /**
     * Detects the current operating system.
     * @return The current operating system.
     */
    private static OperatingSystem getOperatingSystem() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win"))
            return OperatingSystem.WINDOWS;
        else if (osName.contains("mac"))
            return OperatingSystem.MAC;
        else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix"))
            return OperatingSystem.LINUX;
        else if (osName.contains("sunos"))
            return OperatingSystem.SOLARIS;
        else
            return OperatingSystem.OTHER;
    }

    /**
     * Returns the name of the operating system.
     * @return The name of the operating system.
     */
    public String toString() {
        return this.name;
    }

}
