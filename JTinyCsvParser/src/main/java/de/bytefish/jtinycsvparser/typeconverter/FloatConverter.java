// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Locale;

public class FloatConverter implements ITypeConverter<Float> {

    private NumberFormat numberFormat;

    public FloatConverter() {
        this(NumberFormat.getInstance(Locale.US));
    }

    public FloatConverter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Float convert(String value) {
        try {
            return numberFormat.parse(value).floatValue();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Float.class;
    }

    @Override
    public String toString() {
        return "FloatConverter{" +
                "numberFormat=" + numberFormat +
                '}';
    }
}
