// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateConverterTest {

    @Test
    public void testConvert() throws Exception {

        LocalDate val = new LocalDateConverter().convert("2011-12-03");

        Assert.assertEquals(2011, val.getYear());
        Assert.assertEquals(12, val.getMonthValue());
        Assert.assertEquals(3, val.getDayOfMonth());
    }

    @Test
    public void testGetTargetType() throws Exception {
        Assert.assertEquals(LocalDate.class, new LocalDateConverter().getTargetType());
    }
}