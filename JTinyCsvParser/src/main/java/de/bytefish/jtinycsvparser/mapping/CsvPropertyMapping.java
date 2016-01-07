// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.typeconverter.ITypeConverter;

import java.util.function.BiConsumer;

public class CsvPropertyMapping<TEntity, TProperty> implements ICsvPropertyMapping<TEntity> {

    private BiConsumer<TEntity, TProperty> setter;
    private ITypeConverter<TProperty> typeConverter;

    public CsvPropertyMapping(BiConsumer<TEntity, TProperty> setter, ITypeConverter<TProperty> typeConverter) {
        this.setter = setter;
        this.typeConverter = typeConverter;
    }

    public boolean tryMapValue(TEntity entity, String value) {
        try {
            setter.accept(entity, typeConverter.convert(value));

            return true;
        } catch(Exception e) {
            return false;
        }
    }
}