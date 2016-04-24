// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.decorator;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;

import java.util.Arrays;

public class ProcessingTokenizerDecorator implements ITokenizer {

    private ITokenizer tokenizer;

    private StringPreprocessor preprocessor;
    private StringPostprocessor postprocessor;

    public ProcessingTokenizerDecorator(ITokenizer tokenizer, StringPostprocessor postprocessor) {
        this(tokenizer, new StringPreprocessor(), postprocessor);
    }

    public ProcessingTokenizerDecorator(ITokenizer tokenizer, StringPreprocessor preprocessor) {
        this(tokenizer, preprocessor, new StringPostprocessor());
    }

    public ProcessingTokenizerDecorator(ITokenizer tokenizer, StringPreprocessor preprocessor, StringPostprocessor postprocessor) {
        this.tokenizer = tokenizer;
        this.preprocessor = preprocessor;
        this.postprocessor = postprocessor;
    }

    @Override
    public String[] tokenize(String input) {
        // Preprocessing Step:
        final String preprocessed_input = preprocessor.process(input);
        // Tokenizing Step:
        final String[] tokenized_input = tokenizer.tokenize(preprocessed_input);
        // Postprocessing Step:
        return Arrays.asList(tokenized_input)
                .stream().map(s -> postprocessor.process(s))
                .toArray(String[]::new);
    }
}
