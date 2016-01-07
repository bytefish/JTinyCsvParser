// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer;

public class StringSplitTokenizer implements ITokenizer {

    private String columnDelimiter;
    private boolean trimLine;

    public StringSplitTokenizer(String columnDelimiter, boolean trimLine) {
        this.columnDelimiter = columnDelimiter;
        this.trimLine = trimLine;
    }

    @Override
    public String[] tokenize(String input) {
        if(trimLine) {
            return input.trim().split(columnDelimiter);
        }
        return input.split(columnDelimiter);
    }
}
