package net.stuxcrystal.jkaraoke.python;

import net.stuxcrystal.jkaraoke.JKaraoke;
import net.stuxcrystal.jkaraoke.LanguageHandler;
import net.stuxcrystal.jkaraoke.logging.LogOutputStream;
import net.stuxcrystal.jkaraoke.structures.Syllable;
import org.python.core.Py;
import org.python.core.PyModule;
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
 * Implementing the Python-Language into JKaraoke.
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
    public void generate(JKaraoke karaoke, File file) throws IOException {
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
