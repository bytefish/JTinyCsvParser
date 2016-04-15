// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.fixed;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;
import de.bytefish.jtinycsvparser.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FixedLengthTokenizer implements ITokenizer {



    private ColumnDefinition[] columns;

    public FixedLengthTokenizer(List<ColumnDefinition> columns) {
        if(columns == null) {
            throw new IllegalArgumentException("columns");
        }
        this.columns = columns.toArray(new ColumnDefinition[columns.size()]);
    }

    public FixedLengthTokenizer(ColumnDefinition[] columns) {
        if(columns == null) {
            throw new IllegalArgumentException("columns");
        }
        this.columns = columns;
    }

    @Override
    public String[] tokenize(String input) {
        String[] tokenizedLine = new String[columns.length];

        for (int columnIndex = 0; columnIndex < columns.length; columnIndex++)
        {
            ColumnDefinition columnDefinition = columns[columnIndex];
            String columnData = input.substring(columnDefinition.getStartIndex(), columnDefinition.getEndIndex());

            tokenizedLine[columnIndex] = columnData;
        }

        return tokenizedLine;
    }


    @Override
    public String toString() {
        String columnDefinitionsString = Arrays.asList(columns)
                .stream()
                .map(e -> e.toString())
                .collect(Collectors.joining(", "));

        return "FixedLengthTokenizer{" +
                "columns=[" + Arrays.toString(columns) + "]" +
                '}';
    }
}
