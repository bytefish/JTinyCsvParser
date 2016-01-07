package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.mapping.CsvMapping;
import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CsvParser<TEntity> implements ICsvParser<TEntity> {

    private CsvParserOptions options;
    private CsvMapping<TEntity> mapping;

    public CsvParser(CsvParserOptions options, CsvMapping<TEntity> mapping) {
        this.mapping = mapping;
        this.options = options;
    }

    @Override
    public Stream<CsvMappingResult<TEntity>> Parse(Iterable<String> csvData) {
        return StreamSupport.stream(csvData.spliterator(), false)
                .skip(options.getSkipHeader() ? 1 : 0)
                .map(s -> options.getTokenizer().tokenize(s))
                .map(a -> mapping.Map(a));
    }
}
