package de.bytefish.jtinycsvparser.types;

import de.bytefish.jtinycsvparser.core.CsvColumnBinding;
import de.bytefish.jtinycsvparser.core.CsvType;

public interface CsvIntegerType extends CsvType<Integer> {
    default <T> CsvColumnBinding<T> primitive(String columnName, ObjIntConsumer<T> setter) {
        return new CsvColumnBinding<>(columnName, (entity, raw) -> {
            int val = (raw == null || raw.isBlank()) ? 0 : Integer.parseInt(raw.trim());
            setter.accept(entity, val);
        });
    }
}
