// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

public class DurationConverter implements ITypeConverter<Duration> {

    public DurationConverter() {
    }

    @Override
    public Duration convert(String value) {
        try {
            return Duration.parse(value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Duration.class;
    }
}
