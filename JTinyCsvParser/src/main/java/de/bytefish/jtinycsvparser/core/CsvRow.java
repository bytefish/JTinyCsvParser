package de.bytefish.jtinycsvparser.core;

import de.bytefish.jtinycsvparser.internals.CsvFieldRange;

public class CsvRow {
    private final String rawLine;
    private final CsvFieldRange[] ranges;
    private final CsvOptions options;
    private final int recordIndex;
    private final int lineNumber;

    public CsvRow(String rawLine, CsvFieldRange[] ranges, CsvOptions options, int recordIndex, int lineNumber) {
        this.rawLine = rawLine;
        this.ranges = ranges;
        this.options = options;
        this.recordIndex = recordIndex;
        this.lineNumber = lineNumber;
    }

    public int getFieldCount() {
        return ranges.length;
    }

    public String getField(int index) {
        if (index < 0 || index >= ranges.length) return null;
        CsvFieldRange range = ranges[index];

        String val = rawLine.substring(range.start(), range.start() + range.length());

        if (range.isQuoted()) {
            // Remove quotes
            if (val.length() >= 2 && val.charAt(0) == options.quoteChar() && val.charAt(val.length() - 1) == options.quoteChar()) {
                val = val.substring(1, val.length() - 1);
            }
        }

        if (range.needsUnescape()) {
            // Replace escaped quotes (e.g. "" -> ")
            String quoteStr = String.valueOf(options.quoteChar());
            String escapeStr = String.valueOf(options.escapeChar());
            val = val.replace(escapeStr + quoteStr, quoteStr);
        }

        return val;
    }

    public String getRawLine() { return rawLine; }
    public int getRecordIndex() { return recordIndex; }
    public int getLineNumber() { return lineNumber; }
}