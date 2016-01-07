// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

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
                .skip(options.getSkipHeader() ? 1 : 0) // Skip the line or not?
                .filter(s1 -> s1 != null && s1 != "") // Filter Lines with Content!
                .map(s -> options.getTokenizer().tokenize(s)) // Tokenize the Line into parts
                .map(a -> mapping.Map(a)); // Map the Result to the strongly-typed object
    }
}
