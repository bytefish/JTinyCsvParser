package de.bytefish.jtinycsvparser.core;

import java.util.function.BiConsumer;

public record CsvColumnBinding<T>(String columnName, BiConsumer<T, String> binder) {}
