// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import de.bytefish.jtinycsvparser.exceptions.TypeConverterAlreadyRegisteredException;
import de.bytefish.jtinycsvparser.exceptions.TypeConverterNotRegisteredException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeConverterProvider implements ITypeConverterProvider {

    private Map<Type, TypeConverter> typeConverters;

    public TypeConverterProvider() {

        typeConverters = new HashMap<>();

        add(new BigIntegerConverter());
        add(new ByteConverter());
        add(new DoubleConverter());
        add(new DurationConverter());
        add(new FloatConverter());
        add(new IntegerConverter());
        add(new InstantConverter());
        add(new LocalTimeConverter());
        add(new LocalDateConverter());
        add(new LocalDateTimeConverter());
        add(new LongConverter());
        add(new ShortConverter());
        add(new StringConverter());
    }

    public <TTargetType> TypeConverterProvider add(ITypeConverter<TTargetType> typeConverter) {
        Type targetType = typeConverter.getTargetType();

        if(typeConverters.containsKey(targetType)) {
            throw new TypeConverterAlreadyRegisteredException(String.format("TargetType '%s' has already been registered", targetType));
        }

        typeConverters.put(typeConverter.getTargetType(), typeConverter);

        return this;
    }

    public <TTargetType> ITypeConverter<TTargetType> resolve(Type targetType) {
        if(!typeConverters.containsKey(targetType)) {
            throw new TypeConverterNotRegisteredException(String.format("TargetType '%s' has not been registered", targetType));
        }
        return (ITypeConverter<TTargetType>) typeConverters.get(targetType);
    }

    public <TTargetType> TypeConverterProvider override(ITypeConverter<TTargetType> typeConverter) {

        typeConverters.put(typeConverter.getTargetType(), typeConverter);

        return this;
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
