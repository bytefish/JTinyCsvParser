// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import de.bytefish.jtinycsvparser.utils.IDurationFormatter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

public class DurationConverter implements ITypeConverter<Duration> {

    private IDurationFormatter formatter;

    public DurationConverter() {
        formatter = s -> s;
    }

    public DurationConverter(IDurationFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Duration convert(String value) {
        try {
            // Perform eventual formatting on the string:
            String transformedText = formatter.toDurationString(value);

            return Duration.parse(transformedText);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Duration.class;
    }

    @Override
    public String toString() {
        return "DurationConverter{}";
    }
}
