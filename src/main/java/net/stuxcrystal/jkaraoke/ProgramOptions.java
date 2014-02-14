package net.stuxcrystal.jkaraoke;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import net.stuxcrystal.jass.AssFileEntryUtils;

import java.io.*;

import static java.util.Arrays.asList;

/**
 * The options for the program.
 */
public class ProgramOptions {

    /**
     * The supported arguments.
     */
    private static final OptionParser PARSER = new OptionParser() {{
        acceptsAll(asList("?", "h", "help"), "Prints this help");

        acceptsAll(asList("d", "debug"), "Show debug-messages.");

        acceptsAll(asList("n", "headless"), "Execute this software in headless mode.");

        acceptsAll(asList("i", "input"), "The file that contains the timing.")
                .withRequiredArg()
                .withValuesConvertedBy(new ValueConverter<InputStream>() {
                    @Override
                    public InputStream convert(String value) throws ValueConversionException {
                        try {
                            return new FileInputStream(new File(value));
                        } catch (FileNotFoundException e) {
                            throw new ValueConversionException("Failed to create input stream", e);
                        }
                    }

                    @Override
                    public Class<InputStream> valueType() {
                        return InputStream.class;
                    }

                    @Override
                    public String valuePattern() {
                        return null;
                    }
                })
                .defaultsTo(new FileInputStream(FileDescriptor.in));

        acceptsAll(asList("o", "output"), "The output file.")
                .withRequiredArg()
                .withValuesConvertedBy(new ValueConverter<OutputStream>() {
                    @Override
                    public OutputStream convert(String s) throws ValueConversionException {
                        try {
                            return new FileOutputStream(new File(s));
                        } catch (FileNotFoundException e) {
                            throw new ValueConversionException("Failed to convert value", e);
                        }
                    }

                    @Override
                    public Class<OutputStream> valueType() {
                        return OutputStream.class;
                    }

                    @Override
                    public String valuePattern() {
                        return null;
                    }
                })
                .defaultsTo(new FileOutputStream(FileDescriptor.out));

        acceptsAll(asList("l", "language-handler"), "The handler for the file.")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo("net.stuxcrystal.jkaraoke.python.PythonLanguage")
                .describedAs("Class");

        acceptsAll(asList("s", "script"), "The script to execute")
                .withRequiredArg()
                .ofType(File.class)
                .required();
    }};

    /**
     * Parses the options.
     * @param arguments The arguments.
     * @return The set of options.
     */
    public static OptionSet parseOptions(String[] arguments) {
        return PARSER.parse(arguments);
    }

    public static void printHelpTo(OutputStream writer) throws IOException {
        ProgramOptions.move(ProgramOptions.class.getClassLoader().getResourceAsStream("help.txt"), writer);
    }

    /**
     * Moves the content from the input stream to the output stream.
     * @param in   The input stream.
     * @param out  The output stream.
     * @throws IOException If an I/O-Operation fails.
     */
    private static void move(InputStream in, OutputStream out) throws IOException {
        int length;
        byte[] buffer = new byte[4096];
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
    }
}
