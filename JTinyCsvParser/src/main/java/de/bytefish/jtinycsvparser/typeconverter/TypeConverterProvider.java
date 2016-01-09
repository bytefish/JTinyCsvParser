// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import de.bytefish.jtinycsvparser.exceptions.TypeConverterAlreadyRegisteredException;
import de.bytefish.jtinycsvparser.exceptions.TypeConverterNotRegisteredException;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TypeConverterProvider {

    private Map<Type, TypeConverter> typeConverters;

    public TypeConverterProvider() {

        typeConverters = new HashMap<>();

        Add(new BigIntegerConverter());
        Add(new ByteConverter());
        Add(new DoubleConverter());
        Add(new DurationConverter());
        Add(new FloatConverter());
        Add(new IntegerConverter());
        Add(new InstantConverter());
        Add(new LocalDateConverter());
        Add(new LocalDateTimeConverter());
        Add(new LongConverter());
        Add(new ShortConverter());
        Add(new StringConverter());
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

    @Override
    public String toString() {

        String typeConvertersString =
                typeConverters.entrySet()
                        .stream()
                        .map(e -> e.getValue().toString())
                        .collect(Collectors.joining(", "));

        return "TypeConverterProvider{" +
                "typeConverters=[" + typeConvertersString + "]" +
                '}';
    }
}
