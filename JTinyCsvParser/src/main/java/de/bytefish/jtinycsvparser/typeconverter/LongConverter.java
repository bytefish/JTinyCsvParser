// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Locale;

public class LongConverter implements ITypeConverter<Long> {

    private NumberFormat numberFormat;

    public LongConverter() {
        this(NumberFormat.getInstance(Locale.US));
    }

    public LongConverter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Long convert(String value) {
        try {
            return numberFormat.parse(value).longValue();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Long.class;
    }

    @Override
    public String toString() {
        return "LongConverter{" +
                "numberFormat=" + numberFormat +
                '}';
    }
}
