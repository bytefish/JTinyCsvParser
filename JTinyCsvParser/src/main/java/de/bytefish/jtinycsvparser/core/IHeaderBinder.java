package de.bytefish.jtinycsvparser.core;


public interface IHeaderBinder {

    boolean needsHeaderResolution();

    CsvHeaderResolution bindHeaders(CsvRow headerRow);

}