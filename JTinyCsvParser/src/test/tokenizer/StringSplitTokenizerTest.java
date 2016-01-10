// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer;

import de.bytefish.jtinycsvparser.utils.JUnitUtils;
import org.junit.Assert;
import org.junit.Test;

public class StringSplitTokenizerTest {

    @Test
    public void testStringSplitTokenizer_TrimLine() {
        String input = " Value0,Value1 ,Value2 ";

        String[] result = new StringSplitTokenizer(",", true).tokenize(input);

        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.length);
        Assert.assertEquals("Value0", result[0]);
        Assert.assertEquals("Value1", result[1]);
        Assert.assertEquals("Value2", result[2]);
    }

    @Test
    public void testStringSplitTokenizer_NoTrimLine() {
        String input = " Value0,Value1 ,Value2 ";

        String[] result = new StringSplitTokenizer(",", false).tokenize(input);

        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.length);
        Assert.assertEquals(" Value0", result[0]);
        Assert.assertEquals("Value1 ", result[1]);
        Assert.assertEquals("Value2 ", result[2]);
    }


    @Test
    public void testStringSplitTokenizer_ToString() {
        JUnitUtils.assertDoesNotThrow(() -> new StringSplitTokenizer(",", false).toString());
    }
}