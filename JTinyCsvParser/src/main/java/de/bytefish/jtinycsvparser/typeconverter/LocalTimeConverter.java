// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeConverter implements ITypeConverter<LocalTime> {

    private DateTimeFormatter dateTimeFormatter;

    public LocalTimeConverter() {
        this(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public LocalTimeConverter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public LocalTime convert(String value) {
        return LocalTime.parse(value, dateTimeFormatter);
    }

    @Override
    public Type getTargetType() {
        return LocalTime.class;
    }

    @Override
    public String toString() {
        return "LocalTimeConverter{" +
                "dateTimeFormatter=" + dateTimeFormatter +
                '}';
    }
}
