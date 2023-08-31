package org.example.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CsvFormatter extends Formatter {

    private static final String DELIMITER = ";";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();

        sb.append(record.getMessage());  // message
        sb.append(LINE_SEPARATOR);
        return sb.toString();
    }
}
