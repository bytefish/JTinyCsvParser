// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.decorator;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;

import java.util.Arrays;
import java.util.function.Function;

public class PostProcessingTokenizerDecorator implements ITokenizer {

    private ITokenizer tokenizer;
    private Function<String, String> processor;

    public PostProcessingTokenizerDecorator(ITokenizer tokenizer, Function<String, String> processor) {
        this.tokenizer = tokenizer;
        this.processor = processor;
    }

    @Override
    public String[] tokenize(String input) {
        final String[] tokenizerResult = tokenizer.tokenize(input);

        return Arrays.asList(tokenizerResult)
                .stream().map(processor)
                .toArray(String[]::new);
    }
}
