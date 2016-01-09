// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Locale;

public class ShortConverter implements ITypeConverter<Short> {

    private NumberFormat numberFormat;

    public ShortConverter() {
        this(NumberFormat.getInstance(Locale.US));
    }

    public ShortConverter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Short convert(String value) {
        try {
            return numberFormat.parse(value).shortValue();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Short.class;
    }

    @Override
    public String toString() {
        return "ShortConverter{" +
                "numberFormat=" + numberFormat +
                '}';
    }
}
