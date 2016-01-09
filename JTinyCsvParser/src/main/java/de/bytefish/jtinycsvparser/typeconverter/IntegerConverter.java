// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Locale;

public class IntegerConverter implements ITypeConverter<Integer> {

    private NumberFormat numberFormat;

    public IntegerConverter() {
        this(NumberFormat.getInstance(Locale.US));
    }

    public IntegerConverter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Integer convert(String value) {
        try {
            return numberFormat.parse(value).intValue();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Integer.class;
    }

    @Override
    public String toString() {
        return "IntegerConverter{" +
                "numberFormat=" + numberFormat +
                '}';
    }
}
