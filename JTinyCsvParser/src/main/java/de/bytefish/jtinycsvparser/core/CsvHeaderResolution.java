package de.bytefish.jtinycsvparser.core;


public class CsvHeaderResolution {
    private final int[] columnMapping;

    public CsvHeaderResolution(int[] mapping) { this.columnMapping = mapping; }

    public int getColumnIndex(int internalIndex) {
        return (columnMapping != null && internalIndex < columnMapping.length) ? columnMapping[internalIndex] : -1;
    }
}
