// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntegerConverterTest {

    @Test
    public void testConvert() throws Exception {
        int val = new IntegerConverter().convert("1");

        Assert.assertEquals(1, val);
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(Integer.class, new IntegerConverter().getTargetType());
    }
}