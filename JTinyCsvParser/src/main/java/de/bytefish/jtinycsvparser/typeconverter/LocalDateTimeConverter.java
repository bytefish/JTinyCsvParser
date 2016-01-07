// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.time.LocalDateTime;

public class LocalDateTimeConverter implements ITypeConverter<LocalDateTime> {

    public LocalDateTimeConverter() {

    }

    @Override
    public LocalDateTime convert(String value) {
        return LocalDateTime.parse(value);
    }

    @Override
    public Type getTargetType() {
        return LocalDateTime.class;
    }
}
