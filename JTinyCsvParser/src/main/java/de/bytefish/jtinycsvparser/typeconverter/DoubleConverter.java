// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Locale;

public class DoubleConverter implements ITypeConverter<Double> {

    private NumberFormat numberFormat;

    public DoubleConverter() {
        this(NumberFormat.getInstance(Locale.US));
    }

    public DoubleConverter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Double convert(String value) {
        try {
            return numberFormat.parse(value).doubleValue();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Double.class;
    }
}
