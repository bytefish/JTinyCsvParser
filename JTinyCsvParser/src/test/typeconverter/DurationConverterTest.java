// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import de.bytefish.jtinycsvparser.utils.IDurationFormatter;
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
    public void testConvertCustomConverter() {

        String unformattedValue = "23:10"; // hh:mm

        // Create a custom converter, which parses the unformatted value into a duration:
        DurationConverter customConverter = new DurationConverter(s ->
        {
            String[] parts = unformattedValue.split(":");

            String hh = parts[0];
            String mm = parts[1];

            return String.format("PT%sH%sM", hh, mm);
        });

        // 23 hours and 10 minutes:
        Duration expected = Duration.ofHours(23).plusMinutes(10);

        // Parse with the custom converter:
        Duration actual = customConverter.convert(unformattedValue);

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testGetTargetType() {
        Assert.assertEquals(Duration.class, new DurationConverter().getTargetType());
    }
}