// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.decorator;

import java.util.function.Function;

public class StringPreprocessor implements IStringProcessor {

    private Function<String, String> postprocessor;

    public StringPreprocessor() {
        this((s) -> s);
    }

    public StringPreprocessor(Function<String, String> postprocessor) {
        this.postprocessor = postprocessor;
    }

    @Override
    public String process(String input) {
        return postprocessor.apply(input);
    }
}
