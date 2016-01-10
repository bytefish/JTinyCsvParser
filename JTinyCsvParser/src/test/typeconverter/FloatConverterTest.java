// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

public class FloatConverterTest {

    @Test
    public void testConvert() throws Exception {

        Float val = new FloatConverter().convert("2.00001");
        Assert.assertEquals(2.00001, val, 1e-5);
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(Float.class, new FloatConverter().getTargetType());
    }
}