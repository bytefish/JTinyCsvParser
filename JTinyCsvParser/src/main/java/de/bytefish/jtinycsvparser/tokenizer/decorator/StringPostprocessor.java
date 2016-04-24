// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.decorator;

import java.util.function.Function;

public class StringPostprocessor implements IStringProcessor {

    private Function<String, String> postprocessor;

    public StringPostprocessor() {
        this((s) -> s);
    }

    public StringPostprocessor(Function<String, String> postprocessor) {
        this.postprocessor = postprocessor;
    }

    @Override
    public String process(String input) {
        return postprocessor.apply(input);
    }
}
