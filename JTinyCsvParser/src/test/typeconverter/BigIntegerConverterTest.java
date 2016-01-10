// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class BigIntegerConverterTest {

    @Test
    public void testConvert() throws Exception {
        BigInteger expected = new BigInteger("2");
        BigInteger actual = new BigIntegerConverter().convert("2");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(BigInteger.class, new BigIntegerConverter().getTargetType());
    }
}