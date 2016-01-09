// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;
import de.bytefish.jtinycsvparser.tokenizer.StringSplitTokenizer;

public class CsvParserOptions {

    private boolean skipHeader;

    private boolean parallel;

    private ITokenizer tokenizer;

    public CsvParserOptions(boolean skipHeader, String delimiter) {
        this(skipHeader, new StringSplitTokenizer(delimiter, false));
    }

    public CsvParserOptions(boolean skipHeader, String delimiter, boolean parallel) {
        this(skipHeader, new StringSplitTokenizer(delimiter, false), parallel);
    }

    public CsvParserOptions(boolean skipHeader, ITokenizer tokenizer) {
        this(skipHeader, tokenizer, false);
    }

    public CsvParserOptions(boolean skipHeader, ITokenizer tokenizer, boolean parallel) {
        this.skipHeader = skipHeader;
        this.tokenizer = tokenizer;
        this.parallel = parallel;
    }

    public boolean getSkipHeader() {
        return skipHeader;
    }

    public ITokenizer getTokenizer() {
        return tokenizer;
    }

    public boolean getParallel() {
        return parallel;
    }

    @Override
    public String toString() {
        return "CsvParserOptions{" +
                "skipHeader=" + skipHeader +
                ", parallel=" + parallel +
                ", tokenizer=" + tokenizer +
                '}';
    }
}
