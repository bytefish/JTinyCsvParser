// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.rfc4180;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;

import java.util.stream.Collectors;

public class RFC4180Tokenizer implements ITokenizer {

    private final Reader reader;

    public RFC4180Tokenizer(Options options) {
        this.reader = new Reader(options);
    }

    @Override
    public String[] tokenize(String input) {
        return internalTokenize(input);
    }

    private String[] internalTokenize(String input) {
        try (PeekableReader a = PeekableReader.createFromString(input)) {
            return reader.readTokens(a)
                    .stream()
                    .map(t -> t.getContent())
                    .toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{};
        }
    }
}
