package de.bytefish.jtinycsvparser.core;

public interface ICsvMapping<T> {
    CsvMappingResult<T> map(CsvRow row, CsvHeaderResolution headerResolution);
}