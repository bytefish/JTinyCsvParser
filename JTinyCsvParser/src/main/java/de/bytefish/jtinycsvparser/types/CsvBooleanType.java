package de.bytefish.jtinycsvparser.types;

import de.bytefish.jtinycsvparser.core.CsvColumnBinding;
import de.bytefish.jtinycsvparser.core.CsvType;

public interface CsvBooleanType extends CsvType<Boolean> {
    default <T> CsvColumnBinding<T> primitive(String columnName, ObjBooleanConsumer<T> setter) {
        return new CsvColumnBinding<>(columnName, (entity, raw) -> {
            boolean val = raw != null && Boolean.parseBoolean(raw.trim());
            setter.accept(entity, val);
        });
    }
    default <T> CsvColumnBinding<T> primitive(int columnIndex, ObjBooleanConsumer<T> setter) {
        return new CsvColumnBinding<>(columnIndex, (entity, raw) -> {
            boolean val = raw != null && Boolean.parseBoolean(raw.trim());
            setter.accept(entity, val);
        });
    }
}