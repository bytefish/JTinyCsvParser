// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;

import java.util.stream.Stream;

public interface ICsvParser<TEntity> {

    Stream<CsvMappingResult<TEntity>> Parse(Stream<String> csvData);

}
