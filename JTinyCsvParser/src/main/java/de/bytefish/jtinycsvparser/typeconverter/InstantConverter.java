// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.Locale;

public class InstantConverter implements ITypeConverter<Instant> {

    public InstantConverter() {

    }

    @Override
    public Instant convert(String value) {
        try {
            return Instant.parse(value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Instant.class;
    }
}
