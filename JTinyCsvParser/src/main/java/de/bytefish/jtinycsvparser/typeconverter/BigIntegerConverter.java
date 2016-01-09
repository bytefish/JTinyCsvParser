// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class BigIntegerConverter implements ITypeConverter<BigInteger> {

    public BigIntegerConverter() {

    }

    @Override
    public BigInteger convert(String value) {
        try {
            return new BigInteger(value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return BigInteger.class;
    }

    @Override
    public String toString() {
        return "BigIntegerConverter{}";
    }
}
