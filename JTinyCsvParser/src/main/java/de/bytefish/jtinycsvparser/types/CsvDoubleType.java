package de.bytefish.jtinycsvparser.types;

import de.bytefish.jtinycsvparser.core.CsvColumnBinding;
import de.bytefish.jtinycsvparser.core.CsvType;

public interface CsvDoubleType extends CsvType<Double> {
    default <T> CsvColumnBinding<T> primitive(String columnName, ObjDoubleConsumer<T> setter) {
        return new CsvColumnBinding<>(columnName, (entity, raw) -> {
            double val = (raw == null || raw.isBlank()) ? 0.0 : Double.parseDouble(raw.trim());
            setter.accept(entity, val);
        });
    }
}
