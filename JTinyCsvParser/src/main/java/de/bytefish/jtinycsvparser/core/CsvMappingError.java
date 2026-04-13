package de.bytefish.jtinycsvparser.core;

public record CsvMappingError(int recordIndex, int lineNumber, int fieldIndex, String message) {}