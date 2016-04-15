// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.decorator;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;
import de.bytefish.jtinycsvparser.tokenizer.fixed.ColumnDefinition;
import de.bytefish.jtinycsvparser.tokenizer.fixed.FixedLengthTokenizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PostProcessingTokenizerDecoratorTest {

    @Test
    public void testFixedLengthTokenizer_TokenizeLine() {

        List<ColumnDefinition> columnDefinition = new ArrayList<>();

        columnDefinition.add(new ColumnDefinition(0, 10));
        columnDefinition.add(new ColumnDefinition(10, 20));

        String input = " Philipp   Wagner   ";

        ITokenizer tokenizer = new FixedLengthTokenizer(columnDefinition);
        ITokenizer decoratedTokenizer = new PostProcessingTokenizerDecorator(tokenizer, s -> s.trim());

        String[] result = decoratedTokenizer.tokenize(input);

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("Philipp", result[0]);
        Assert.assertEquals("Wagner", result[1]);
    }
}