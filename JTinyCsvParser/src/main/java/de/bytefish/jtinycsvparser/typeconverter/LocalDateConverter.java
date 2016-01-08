// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements ITypeConverter<LocalDate> {

    private DateTimeFormatter dateTimeFormatter;

    public LocalDateConverter() {
        this(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public LocalDateConverter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public LocalDate convert(String value) {
        return LocalDate.parse(value, dateTimeFormatter);
    }

    @Override
    public Type getTargetType() {
        return LocalDate.class;
    }
}
