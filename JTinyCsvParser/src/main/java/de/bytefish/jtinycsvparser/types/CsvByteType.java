package de.bytefish.jtinycsvparser.types;

import de.bytefish.jtinycsvparser.core.CsvColumnBinding;
import de.bytefish.jtinycsvparser.core.CsvType;

public interface CsvByteType extends CsvType<Byte> {
    default <T> CsvColumnBinding<T> primitive(String columnName, ObjByteConsumer<T> setter) {
        return new CsvColumnBinding<>(columnName, (entity, raw) -> {
            byte val = (raw == null || raw.isBlank()) ? 0 : Byte.parseByte(raw.trim());
            setter.accept(entity, val);
        });
    }
    default <T> CsvColumnBinding<T> primitive(int columnIndex, ObjByteConsumer<T> setter) {
        return new CsvColumnBinding<>(columnIndex, (entity, raw) -> {
            byte val = (raw == null || raw.isBlank()) ? 0 : Byte.parseByte(raw.trim());
            setter.accept(entity, val);
        });
    }
}