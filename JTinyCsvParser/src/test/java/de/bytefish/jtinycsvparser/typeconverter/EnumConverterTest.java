// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import de.bytefish.jtinycsvparser.utils.JUnitUtils;
import org.junit.Assert;
import org.junit.Test;

public class EnumConverterTest {

    public enum SampleEnum {
        Bike,
        Car
    }

    @Test
    public void testConvert() throws Exception {

        EnumConverter<SampleEnum> converter = new EnumConverter<>(SampleEnum.class);

        Assert.assertEquals(SampleEnum.Bike, converter.convert("Bike"));
        Assert.assertEquals(SampleEnum.Car, converter.convert("Car"));

        JUnitUtils.assertThrows(() -> converter.convert("Plane"));
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(SampleEnum.class, new EnumConverter<>(SampleEnum.class).getTargetType());
    }
}