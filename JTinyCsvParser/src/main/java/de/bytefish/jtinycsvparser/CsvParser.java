// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.mapping.CsvMapping;
import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;
import de.bytefish.jtinycsvparser.utils.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
    public Stream<CsvMappingResult<TEntity>> Parse(Stream<String> stream) {

        // Make the stream parallel, if the option is set:
        if(options.getParallel()) {
            stream = stream.parallel();
        }

        // Now parse the CSV file:
        return stream
                .skip(options.getSkipHeader() ? 1 : 0) // Skip the line or not?
                .filter(s1 -> !StringUtils.isNullOrWhiteSpace(s1)) // Filter Lines with Content!
                .map(s -> options.getTokenizer().tokenize(s)) // Tokenize the Line into parts
                .map(a -> mapping.Map(a)); // MapProperty the Result to the strongly-typed object
    }

    public Stream<CsvMappingResult<TEntity>> Parse(Iterable<String> csvData) {
        return Parse(StreamSupport.stream(csvData.spliterator(), false));
    }

    public Stream<CsvMappingResult<TEntity>> ReadFromFile(Path path, Charset charset) {
        try {
            return Parse(Files.lines(path, charset));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Stream<CsvMappingResult<TEntity>> ReadFromString(String csvData, CsvReaderOptions options) {
        return Parse(Arrays.asList(csvData.split(options.getNewLine())));
    }
}