// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

public class ByteConverterTest {

    @Test
    public void testConvert() throws Exception {
        byte val = new ByteConverter().convert("2");
        Assert.assertEquals(2, val);
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(Byte.class, new ByteConverter().getTargetType());
    }
}