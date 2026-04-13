package de.bytefish.jtinycsvparser.types;

import de.bytefish.jtinycsvparser.core.CsvColumnBinding;
import de.bytefish.jtinycsvparser.core.CsvType;

public interface CsvLongType extends CsvType<Long> {
    default <T> CsvColumnBinding<T> primitive(String columnName, ObjLongConsumer<T> setter) {
        return new CsvColumnBinding<>(columnName, (entity, raw) -> {
            long val = (raw == null || raw.isBlank()) ? 0L : Long.parseLong(raw.trim());
            setter.accept(entity, val);
        });
    }
    default <T> CsvColumnBinding<T> primitive(int columnIndex, ObjLongConsumer<T> setter) {
        return new CsvColumnBinding<>(columnIndex, (entity, raw) -> {
            long val = (raw == null || raw.isBlank()) ? 0L : Long.parseLong(raw.trim());
            setter.accept(entity, val);
        });
    }
}