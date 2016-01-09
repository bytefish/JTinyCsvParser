// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;

public class StringConverter implements ITypeConverter<String> {

    public StringConverter() {

    }

    @Override
    public String convert(String value) {
        return value;
    }

    @Override
    public Type getTargetType() {
        return String.class;
    }

    @Override
    public String toString() {
        return "StringConverter{}";
    }
}
