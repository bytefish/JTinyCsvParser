package de.bytefish.jtinycsvparser.core;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Defines a schema for column-based type mapping, especially useful for DictionaryMapping.
 */
public class CsvSchema {
    private final Map<String, CsvType<?>> typeMap = new HashMap<>();

    private String normalize(String key) {
        if (key == null) return "";
        key = key.trim().toLowerCase(Locale.ROOT);
        // Explicitly remove UTF-8 BOM if present
        if (key.startsWith("\uFEFF")) {
            key = key.substring(1);
        }
        return key;
    }

    public CsvSchema add(String columnName, CsvType<?> type) {
        typeMap.put(normalize(columnName), type);
        return this;
    }

    public Object parse(String columnName, String rawValue) {
        if (rawValue == null || rawValue.isBlank()) return null;
        // Ensure lookup is performed using a trimmed and lowered key
        CsvType<?> type = typeMap.get(columnName.trim().toLowerCase());
        return type != null ? type.parse(rawValue) : rawValue;
    }
}
