// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer;

import org.junit.Assert;
import org.junit.Test;

public class QuotedStringTokenizerTest {

    @Test
    public void testQuotedStringTokenizer_Trim() {
        String input = "\" Value0,Value1 \",Value2 ";

        String[] result = new QuotedStringTokenizer(",", true).tokenize(input);

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("Value0,Value1", result[0]);
        Assert.assertEquals("Value2", result[1]);
    }

    @Test
    public void testQuotedStringTokenizer_Without_Trim() {
        String input = "\" Value0,Value1 \", Value2";

        String[] result = new QuotedStringTokenizer(",", false).tokenize(input);

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals(" Value0,Value1 ", result[0]);
        Assert.assertEquals(" Value2", result[1]);
    }

}