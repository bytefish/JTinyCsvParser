package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.typeconverter.ITypeConverter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CsvPropertyMapping<TEntity, TProperty> implements ICsvPropertyMapping<TEntity> {

    private BiConsumer<TEntity, TProperty> setter;
    private ITypeConverter<TProperty> typeConverter;

    public CsvPropertyMapping(BiConsumer<TEntity, TProperty> setter, ITypeConverter<TProperty> typeConverter) {
        this.setter = setter;
        this.typeConverter = typeConverter;
    }

    public boolean tryMapValue(TEntity entity, String value) {
        // Initialize:
        TProperty result = typeConverter.convert(value);

        // Set the Value in the Entity:
        try {
            setter.accept(entity, result);
        } catch(Exception e) {

        }

        return true;
    }
}
