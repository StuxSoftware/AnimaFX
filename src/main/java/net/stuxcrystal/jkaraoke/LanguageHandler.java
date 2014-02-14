package net.stuxcrystal.jkaraoke;

import java.io.File;
import java.io.IOException;

/**
 * Handles the parsing.
 */
public interface LanguageHandler {

    /**
     * Generates the file.<p />
     *
     * This method is called once.
     * @param file The script file.
     */
    public void generate(JKaraoke karaoke, File file) throws IOException;

}
