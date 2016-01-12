// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;

public class EnumConverter<E extends Enum<E>>
        implements ITypeConverter<E> {

    private Class<E> enumType;

    public EnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public E convert(String value) {
        try {
            return E.valueOf(enumType, value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return enumType;
    }

    @Override
    public String toString() {
        return "IntegerConverter{" +
                ", enumType=" + enumType +
                '}';
    }
}
