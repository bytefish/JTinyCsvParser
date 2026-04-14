package de.bytefish.jtinycsvparser.core;


sealed public interface CsvMappingResult<T> permits CsvMappingResult.Success, CsvMappingResult.Error, CsvMappingResult.Comment {

    int recordIndex();

    int lineNumber();

    record Success<T>(T entity, int recordIndex, int lineNumber) implements CsvMappingResult<T> {}

    record Error<T>(CsvMappingError error, int recordIndex, int lineNumber) implements CsvMappingResult<T> {}

    record Comment<T>(String comment, int recordIndex, int lineNumber) implements CsvMappingResult<T> {}
}