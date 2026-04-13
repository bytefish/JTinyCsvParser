package de.bytefish.jtinycsvparser.types;

import de.bytefish.jtinycsvparser.core.CsvColumnBinding;
import de.bytefish.jtinycsvparser.core.CsvType;

public interface CsvShortType extends CsvType<Short> {
    default <T> CsvColumnBinding<T> primitive(String columnName, ObjShortConsumer<T> setter) {
        return new CsvColumnBinding<>(columnName, (entity, raw) -> {
            short val = (raw == null || raw.isBlank()) ? 0 : Short.parseShort(raw.trim());
            setter.accept(entity, val);
        });
    }
}
