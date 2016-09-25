// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

public class ShortConverterTest {

    @Test
    public void testConvert() throws Exception {
        short val = new ShortConverter().convert("1");

        Assert.assertEquals(1, val);
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(Short.class, new ShortConverter().getTargetType());
    }
}