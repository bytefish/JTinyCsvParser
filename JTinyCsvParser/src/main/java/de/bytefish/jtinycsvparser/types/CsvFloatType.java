package de.bytefish.jtinycsvparser.types;

import de.bytefish.jtinycsvparser.core.CsvColumnBinding;
import de.bytefish.jtinycsvparser.core.CsvType;

public interface CsvFloatType extends CsvType<Float> {
    default <T> CsvColumnBinding<T> primitive(String columnName, ObjFloatConsumer<T> setter) {
        return new CsvColumnBinding<>(columnName, (entity, raw) -> {
            float val = (raw == null || raw.isBlank()) ? 0.0f : Float.parseFloat(raw.trim());
            setter.accept(entity, val);
        });
    }
}
