// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

public class DurationConverterTest {

    @Test
    public void testConvert() {
        Duration expected = Duration.between(Instant.now().minus(100, ChronoUnit.SECONDS), Instant.now());

        Duration val = new DurationConverter().convert(expected.toString());

        Assert.assertEquals(expected, val);
    }

    @Test
    public void testGetTargetType() {
        Assert.assertEquals(Duration.class, new DurationConverter().getTargetType());
    }
}