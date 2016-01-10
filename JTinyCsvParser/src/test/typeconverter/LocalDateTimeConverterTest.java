// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeConverterTest {

    @Test
    public void testConvert() throws Exception {

        LocalDateTime val = new LocalDateTimeConverter().convert("2011-12-03T10:15:30");

        Assert.assertEquals(2011, val.getYear());
        Assert.assertEquals(12, val.getMonthValue());
        Assert.assertEquals(3, val.getDayOfMonth());

        Assert.assertEquals(10, val.getHour());
        Assert.assertEquals(15, val.getMinute());
        Assert.assertEquals(30, val.getSecond());
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(LocalDateTime.class, new LocalDateTimeConverter().getTargetType());
    }
}