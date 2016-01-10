// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer;

import de.bytefish.jtinycsvparser.utils.StringUtils;

import java.util.Arrays;

public class StringSplitTokenizer implements ITokenizer {

    private String columnDelimiter;
    private boolean trimValues;

    public StringSplitTokenizer(String columnDelimiter, boolean trimValues) {
        this.columnDelimiter = columnDelimiter;
        this.trimValues = trimValues;
    }

    @Override
    public String[] tokenize(String input) {
        String[] result = input.split(columnDelimiter);
        if(trimValues) {
            result = StringUtils.trimAllElements(result);
        }
        return result;
    }

    @Override
    public String toString() {
        return "StringSplitTokenizer{" +
                "columnDelimiter='" + columnDelimiter + '\'' +
                ", trimValues=" + trimValues +
                '}';
    }
}
