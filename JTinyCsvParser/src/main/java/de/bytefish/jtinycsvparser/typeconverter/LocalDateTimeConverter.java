// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements ITypeConverter<LocalDateTime> {

    private DateTimeFormatter dateTimeFormatter;

    public LocalDateTimeConverter() {
        this(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public LocalDateTimeConverter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public LocalDateTime convert(String value) {
        return LocalDateTime.parse(value, dateTimeFormatter);
    }

    @Override
    public Type getTargetType() {
        return LocalDateTime.class;
    }

    @Override
    public String toString() {
        return "LocalDateTimeConverter{" +
                "dateTimeFormatter=" + dateTimeFormatter +
                '}';
    }
}
