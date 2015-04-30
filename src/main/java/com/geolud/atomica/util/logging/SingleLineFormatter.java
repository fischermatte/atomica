package com.geolud.atomica.util.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

/**
 * Use this Formatter for logging messages in a single line, due to the Java
 * Logging API doesn't support this natively.
 *
 * @author Georg Ludewig
 */
public class SingleLineFormatter extends java.util.logging.Formatter {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy.MM.dd HH:mm:ss.S");

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    @Override
    public String format(LogRecord record) {
        return new StringBuffer().append(
                dateFormat.format(new Date(record.getMillis()))).append(" ")
                .append(record.getLevel()).append(" ").append(
                        record.getSourceClassName()).append(".").append(
                        record.getSourceMethodName()).append('\t').append(
                        record.getMessage()).append('\n').toString();
    }

}