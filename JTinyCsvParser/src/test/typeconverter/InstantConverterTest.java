// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

public class InstantConverterTest {

    @Test
    public void testConvert() {
        Instant expected = Instant.now();

        Instant val = new InstantConverter().convert(expected.toString());

        Assert.assertEquals(expected, val);
    }

    @Test
    public void testGetTargetType() {
        Assert.assertEquals(Instant.class, new InstantConverter().getTargetType());
    }
}