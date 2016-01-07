// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import de.bytefish.jtinycsvparser.exceptions.TypeConverterAlreadyRegisteredException;
import de.bytefish.jtinycsvparser.exceptions.TypeConverterNotRegisteredException;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class TypeConverterProvider {

    private Map<Type, TypeConverter> typeConverters;

    public TypeConverterProvider() {

        typeConverters = new HashMap<>();

        Add(new IntegerConverter(NumberFormat.getNumberInstance()));
    }

    public <TTargetType> TypeConverterProvider Add(ITypeConverter<TTargetType> typeConverter) {
        Type targetType = typeConverter.getTargetType();

        if(typeConverters.containsKey(targetType)) {
            throw new TypeConverterAlreadyRegisteredException(String.format("TargetType '%s' has already been registered", targetType));
        }

        typeConverters.put(typeConverter.getTargetType(), typeConverter);

        return this;
    }

    public <TTargetType> ITypeConverter<TTargetType> Resolve(Type targetType) {
        if(!typeConverters.containsKey(targetType)) {
            throw new TypeConverterNotRegisteredException(String.format("TargetType '%s' has not been registered", targetType));
        }
        return (ITypeConverter<TTargetType>) typeConverters.get(targetType);
    }

}
