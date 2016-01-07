// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateConverter implements ITypeConverter<LocalDate> {

    public LocalDateConverter() {

    }

    @Override
    public LocalDate convert(String value) {

        LocalDate a = LocalDate.parse(value);
        return a;
    }

    @Override
    public Type getTargetType() {
        return LocalDate.class;
    }
}
