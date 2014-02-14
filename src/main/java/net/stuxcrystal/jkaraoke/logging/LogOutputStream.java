package net.stuxcrystal.jkaraoke.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Redirects all output to the log.
 */
public class LogOutputStream extends ByteArrayOutputStream {

    /**
     * The separator of the line.
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * The logger to write to.
     */
    private final Logger logger;

    /**
     * The level to use.
     */
    private final Level level;

    /**
     * Creates a new log output stream.
     * @param logger The logger
     * @param level  The level.
     */
    public LogOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void flush() throws IOException {
        String record;
        synchronized (this) {
            super.flush();
            record = this.toString();
            super.reset();

            if (record.length() == 0 || record.equals(LINE_SEPARATOR))
                return;

            for (String line : record.split("\n")) {
                this.logger.log(this.level, line);
            }
        }
    }

}
