package de.bytefish.jtinycsvparser.internals;

public record CsvFieldRange(int start, int length, boolean isQuoted, boolean needsUnescape) {}
