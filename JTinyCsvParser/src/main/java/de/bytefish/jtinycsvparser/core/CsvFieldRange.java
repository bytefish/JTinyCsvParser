package de.bytefish.jtinycsvparser.core;

public record CsvFieldRange(int start, int length, boolean isQuoted, boolean needsUnescape) {}
