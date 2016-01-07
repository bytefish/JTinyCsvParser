// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;

public class CsvParserOptions {

    private boolean skipHeader;

    private ITokenizer tokenizer;

    public CsvParserOptions(boolean skipHeader, ITokenizer tokenizer) {
        this.skipHeader = skipHeader;
        this.tokenizer = tokenizer;
    }

    public boolean getSkipHeader() {
        return skipHeader;
    }

    public ITokenizer getTokenizer() {
        return tokenizer;
    }
}
