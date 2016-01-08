// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Locale;

public class ByteConverter implements ITypeConverter<Byte> {

    private NumberFormat numberFormat;

    public ByteConverter() {
        this(NumberFormat.getInstance(Locale.US));
    }

    public ByteConverter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Byte convert(String value) {
        try {
            return numberFormat.parse(value).byteValue();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Byte.class;
    }
}
