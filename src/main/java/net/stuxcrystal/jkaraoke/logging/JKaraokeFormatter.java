package net.stuxcrystal.jkaraoke.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Returns a configurable simple formatter.
 */
public class JKaraokeFormatter extends Formatter {

    /**
     * The maximal length of the level translation.
     */
    private final int maximalLevelSize;

    /**
     * Creates a new formatter for jKaraoke.
     */
    public JKaraokeFormatter() {
        int size = 0;
        for (Level level : new Level[] {
                Level.ALL,  Level.CONFIG, Level.OFF,
                Level.FINE, Level.FINER,  Level.FINEST,
                Level.INFO, Level.SEVERE, Level.WARNING
        }) {
            size = Math.max(level.getLocalizedName().length(), size);
        }
        maximalLevelSize = size;
    }

    @Override
    public String format(LogRecord record) {
        Date date = new Date(record.getMillis());
        StringBuilder sb = new StringBuilder();

        String level = record.getLevel().getLocalizedName();

        sb.append("[").append(new SimpleDateFormat("HH:mm:ss").format(date)).append("]");
        sb.append("[").append(record.getLoggerName()).append("]");
        sb.append("[").append(level).append("] ");
        for (int i = level.length(); i<maximalLevelSize; i++)
            sb.append(" ");

        sb.append(record.getMessage());
        if (record.getThrown() != null) {
            sb.append("\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            sb.append(sw.toString());
        } else {
            sb.append("\n");
        }
        return sb.toString();
    }
}
