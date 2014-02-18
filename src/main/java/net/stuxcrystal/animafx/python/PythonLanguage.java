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

import net.stuxcrystal.animafx.AnimaFX;
import net.stuxcrystal.animafx.LanguageHandler;
import net.stuxcrystal.animafx.logging.LogOutputStream;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementing the Python-Language into AnimaFX.
 */
public class PythonLanguage implements LanguageHandler {

    /**
     * List of all extensions.
     */
    private static final HashMap<String, Class<?>> EXTENSION_MODULES = new HashMap<>();

    /**
     * Register the extension modules.
     */
    static {
        EXTENSION_MODULES.put("_karabuilder_environment", KaraBuilderEnvironment.class);
    }

    /**
     * Generates the file to use.
     * @param file The script file.
     */
    @Override
    public void generate(AnimaFX karaoke, File file) throws IOException {
        // Initialize the python interpreter.
        this.initialize();

        // Create the logger.
        Logger logger = Logger.getLogger("Python");
        logger.setUseParentHandlers(true);
        logger.setParent(karaoke.getLogger());

        // Prepare the interpreter.
        PythonInterpreter interp = new PythonInterpreter();
        interp.setOut(new LogOutputStream(logger, Level.INFO));
        interp.setErr(new LogOutputStream(logger, Level.WARNING));

        // Execute the script.
        interp.execfile(new FileInputStream(file), file.getName());

    }

    /**
     * Initializes the python interpreter.
     */
    private void initialize() {
        this.registerModules();
    }

    /**
     * Registers the modules.
     */
    private void registerModules() {
        Properties properties = new Properties();
        StringBuilder modules = new StringBuilder(System.getProperty("python.modules.builtin", ""));
        for (Map.Entry<String, Class<?>> module : EXTENSION_MODULES.entrySet()) {
            if (modules.length() > 0) {
                modules.append(",");
            }

            modules.append(module.getKey()).append(":").append(module.getValue().getCanonicalName());
        }
        properties.put("python.modules.builtin", modules.toString());
        PythonInterpreter.initialize(System.getProperties(), properties, new String[0]);
    }
}
