package de.bytefish.jtinycsvparser.core;

import java.util.function.BiConsumer;

public interface CsvType<V> {

    V parse(String value);

    default <T> CsvColumnBinding<T> boxed(String columnName, BiConsumer<T, V> setter) {
        return new CsvColumnBinding<>(columnName, (entity, raw) -> {
            if (raw == null || raw.isBlank()) setter.accept(entity, null);
            else setter.accept(entity, parse(raw));
        });
    }

    default <T> CsvColumnBinding<T> boxed(int columnIndex, BiConsumer<T, V> setter) {
        return new CsvColumnBinding<>(columnIndex, (entity, raw) -> {
            if (raw == null || raw.isBlank()) setter.accept(entity, null);
            else setter.accept(entity, parse(raw));
        });
    }
}

