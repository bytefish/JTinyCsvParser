// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class LocalTimeConverterTest {

    @Test
    public void testConvert() throws Exception {

        LocalTime val = new LocalTimeConverter().convert("23:50");

        Assert.assertEquals(23, val.getHour());
        Assert.assertEquals(50, val.getMinute());
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(LocalTime.class, new LocalTimeConverter().getTargetType());
    }

}