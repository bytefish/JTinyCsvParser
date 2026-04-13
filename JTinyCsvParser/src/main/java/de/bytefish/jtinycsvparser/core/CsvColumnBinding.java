package de.bytefish.jtinycsvparser.core;

import java.util.function.BiConsumer;

public record CsvColumnBinding<T>(String columnName, Integer columnIndex, BiConsumer<T, String> binder) {
    public CsvColumnBinding(String columnName, BiConsumer<T, String> binder) {
        this(columnName, null, binder);
    }
    public CsvColumnBinding(int columnIndex, BiConsumer<T, String> binder) {
        this(null, columnIndex, binder);
    }
}