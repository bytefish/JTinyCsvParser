// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

public class IgnoreMissingValuesConverterTest {

    @Test
    public void testConvertWithDefaultConstructor() throws Exception {

        IgnoreMissingValuesConverter<Float> floatConverter = new IgnoreMissingValuesConverter<>(new FloatConverter());

        Assert.assertEquals(null, floatConverter.convert(null));
        Assert.assertEquals(null, floatConverter.convert(""));
    }

    @Test
    public void testConvertWithCustomValues() throws Exception {

        IgnoreMissingValuesConverter<Float> floatConverter = new IgnoreMissingValuesConverter<>(new FloatConverter(), "M", "A");

        Assert.assertEquals(null, floatConverter.convert(null));
        Assert.assertEquals(null, floatConverter.convert(""));
        Assert.assertEquals(null, floatConverter.convert("M"));
        Assert.assertEquals(null, floatConverter.convert("A"));
    }
}