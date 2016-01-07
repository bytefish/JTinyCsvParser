package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;

import java.util.stream.Stream;

public interface ICsvParser<TEntity> {

    Stream<CsvMappingResult<TEntity>> Parse(Iterable<String> csvData);

}
