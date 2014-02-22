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

package net.stuxcrystal.animafx;

import joptsimple.OptionException;
import joptsimple.OptionSet;
import net.stuxcrystal.jass.AssFile;
import net.stuxcrystal.jass.AssInfoEntry;
import net.stuxcrystal.animafx.logging.JKaraokeFormatter;
import net.stuxcrystal.animafx.logging.LogOutputStream;

import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

/**
 * Test text.
 */
public class AnimaFX {

    /**
     * The internal header of AnimaFX.
     */
    private static final String[] HEADER = {
            "AnimaFX 0.1-Alpha.2",
            "(c) 2014 StuxCrystal"
    };

    /**
     * The headers to keep when the headers should be cleaned.
     */
    private static List<String> CLEANED_HEADERS = Arrays.asList(System.getProperty(
            "animafx.clean.entries",
            "Collisions,PlayResX,PlayResY,ScaledBorderAndShadow,ScriptType,Video_Colorspace,YCbCr_Matrix,WrapStyle"
    ).replace("_", " ").split(","));

    public static void main(String[] args) {

        for (String line : HEADER)
            System.err.println(line);

        AnimaFX animafx;
        try {
            animafx = new AnimaFX(args);
        } catch (OptionException e) {
            System.err.println();
            System.err.println(e.getMessage());
            try {
                ProgramOptions.printHelpTo(System.err);
            } catch (IOException ignored) {}
            return;
        }

        if (!animafx.initialize())
            return;

        animafx.run();
    }

    /**
     * Contains the cache.
     */
    private Cache cache;

    /**
     * Parses the karaoke.
     */
    private KaraokeParser parser;

    /**
     * The logger.
     */
    private Logger logger;

    /**
     * Arguments passed to AnimaFX.
     */
    private OptionSet options;

    /**
     * Contains the input file.
     */
    private AssFile input;

    /**
     * Contains the output of AnimaFX.
     */
    private AssFile output;

    /**
     * Container for the metadata of the script;
     */
    private AssDocument inputData;

    /**
     * Creates a new instance of AnimaFX.
     */
    private AnimaFX(String[] arguments) {
        this.options = ProgramOptions.parseOptions(arguments);
    }

    /**
     * Initializes AnimaFX.
     */
    private boolean initialize() {
        if (options.has("?")) {
            try {
                ProgramOptions.printHelpTo(System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        // Create a new logger.
        this.logger = Logger.getLogger("AnimaFX");
        this.logger.setUseParentHandlers(false);
        Handler handler = new ConsoleHandler();
        handler.setFormatter(new JKaraokeFormatter());
        this.logger.addHandler(handler);

        Logger outputLogger = Logger.getLogger("Output");
        outputLogger.setParent(this.logger);

        System.setErr(new PrintStream(new LogOutputStream(outputLogger, Level.WARNING), true));
        System.setOut(new PrintStream(new LogOutputStream(outputLogger, Level.INFO), true));

        this.logger.info("Initializing AnimaFX...");
        this.logger.info("AnimaFX currently runs on " + OperatingSystem.CURRENT_OPERATING_SYSTEM.toString());

        // Set debug mode.
        if (this.isDebug()) {
            this.logger.setLevel(Level.ALL);
            handler.setLevel(Level.ALL);
        }

        // Enforce Headless-Mode if specified.
        if (options.has("n")) {
            this.logger.fine("Enforcing headless mode.");

            // Set the system to headless mode.
            if (!this.setHeadless()) {
                this.logger.warning("Failed to enforce headless mode.");
            }
        }

        // Initialize the cache.
        this.cache = new Cache(this);

        // Initialize the toolkit.
        new KaraokeToolkit(this).initialize();
        this.parser = new KaraokeParser();

        // Initialize input and output.
        this.input = new AssFile();
        this.output = new AssFile();

        // Reading input file.
        this.logger.fine("Reading input file");
        try {
            this.input.load((InputStream) this.options.valueOf("input"));
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failed to load file", e);
            this.logger.severe("Cannot read input file: Aborting");
            return false;
        } finally {
            try { ((InputStream)this.options.valueOf("input")).close(); } catch (IOException ignored) {}
        }

        this.inputData = new AssDocument(this, this.input);
        this.logger.fine("Calculating Resolution...");
        this.inputData.getSize();

        this.logger.fine("Loading attachments into cache...");
        try {
            this.cache.addFile(this.input);
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Failed to load attachments", e);
        }

        // Print debug message.
        this.logger.fine("Completed initialization.");

        return true;
    }

    /**
     * Sets the mode of the software to headless.<p />
     * @return {@code true} if the action succeeded {@code false} otherwise.
     */
    private boolean setHeadless() {
        // Set the headless mode of the jvm.
        System.setProperty("java.awt.headless", "true");

        // Trigger toolkit creation.
        // Make sure no other thread is currently active. Since the JIT is disabled while
        // creating the default toolkit.
        Toolkit.getDefaultToolkit();

        // Returns the current state of the graphics environment.
        return GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance();
    }

    public void run() {
        this.logger.fine("Creating Language handler.");
        Class<?> cls;
        try {
            cls = this.getClass().getClassLoader().loadClass((String) this.options.valueOf("language-handler"));
        } catch (ClassNotFoundException e) {
            this.logger.severe("Failed to find language-handler.");
            return;
        }

        LanguageHandler language;
        try {
            language = (LanguageHandler) cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            this.logger.log(Level.SEVERE, "Failed to instantiate language-handler.", e);
            return;
        } catch (ClassCastException e) {
            this.logger.severe("The given class is not a language handler.");
            return;
        }

        // Copy styles to the output.
        this.output.setStyles(new ArrayList<>(this.input.getStyles()));

        this.logger.info("Generating effect.");
        long t = System.currentTimeMillis();
        try {
            language.generate(this, (File) this.options.valueOf("script"));
        } catch (IOException e) {
            this.logger.log(Level.WARNING, "Failed to generate effect", e);
        }
        this.logger.info("Time elapsed: " + (System.currentTimeMillis() - t)/1000F + "s");
        this.logger.info("Writing lines...");

        StringWriter writer = new StringWriter();
        this.output.setInfoEntries(this.getOutputInfoEntries());
        this.output.getInfoEntries().add(new AssInfoEntry("Header", "Created by AnimaFX.\nhttp://github.com/StuxSoftware/AnimaFX"));
        try {
            this.output.dump(writer);
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failed to dump subtitle...", e);
            return;
        }

        OutputStreamWriter osw = new OutputStreamWriter((OutputStream) this.options.valueOf("output"));
        try {
            osw.write(writer.toString());
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failed to write document into output.", e);
        } finally {
            try { osw.close(); } catch (IOException ignored) {}
        }

        this.logger.info("Execution completed...");
    }

    private List<AssInfoEntry> getOutputInfoEntries() {
        List<AssInfoEntry> iEntries = new ArrayList<>(this.input.getInfoEntries());
        if (!this.options.has("clean"))
            return iEntries;

        List<AssInfoEntry> result = new ArrayList<>();
        for (AssInfoEntry entry : iEntries) {
            if (CLEANED_HEADERS.contains(entry.getKey()))
                result.add(entry);
        }
        return result;
    }

    /**
     * Returns the cache of the program.
     * @return  The cache of the program.
     */
    public Cache getCache() {
        return this.cache;
    }

    /**
     * Returns the parser for Karaoke-Lines.
     * @return The parser for Karaoke-Lines.
     */
    public KaraokeParser getKaraokeParser() {
        return this.parser;
    }

    /**
     * Checks if the software is in debug mode.
     * @return {@code true} if the software is in debug mode.
     */
    public boolean isDebug() {
        return this.options.has("d");
    }

    /**
     * Returns the logger of AnimaFX.
     * @return The logger of AnimaFX.
     */
    public Logger getLogger() {
        return this.logger;
    }

    /**
     * Returns the input file of AnimaFX.
     * @return The input file of AnimaFX.
     */
    public AssFile getInput() {
        return input;
    }

    /**
     * Returns the output file of AnimaFX.
     * @return The output file of AnimaFX.
     */
    public AssFile getOutput() {
        return output;
    }

    /**
     * Contains metadata about the document.
     * @return The metadata about the document.
     */
    public AssDocument getInputMetaData() { return this.inputData; }
}
