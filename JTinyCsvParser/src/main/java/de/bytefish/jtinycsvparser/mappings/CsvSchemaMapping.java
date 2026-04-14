package de.bytefish.jtinycsvparser.mappings;

import de.bytefish.jtinycsvparser.core.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class CsvSchemaMapping implements ICsvMapping<Map<String, Object>>, IHeaderBinder {
    private final CsvSchema schema;
    private String[] columnNames;

    public CsvSchemaMapping() { this(new CsvSchema()); }
    public CsvSchemaMapping(CsvSchema schema) { this.schema = schema; }

    @Override public boolean needsHeaderResolution() { return columnNames == null; }

    @Override public CsvHeaderResolution bindHeaders(CsvRow headerRow) {
        columnNames = new String[headerRow.getFieldCount()];
        for (int i = 0; i < headerRow.getFieldCount(); i++) {
            String header = headerRow.getField(i);
            // Ensure Map keys are perfectly clean of BOM and spaces
            if (header != null) {
                header = header.trim();
                if (header.startsWith("\uFEFF")) {
                    header = header.substring(1);
                }
            }
            columnNames[i] = header;
        }
        return new CsvHeaderResolution(null);
    }

    @Override public CsvMappingResult<Map<String, Object>> map(CsvRow row, CsvHeaderResolution headerResolution) {
        try {
            Map<String, Object> dict = new LinkedHashMap<>();
            for (int i = 0; i < Math.min(row.getFieldCount(), columnNames.length); i++) {
                String colName = columnNames[i];
                String rawValue = row.getField(i);
                dict.put(colName, schema.parse(colName, rawValue));
            }
            return new CsvMappingResult.Success<>(dict, row.getRecordIndex(), row.getLineNumber());
        } catch (Exception ex) {
            return new CsvMappingResult.Error<>(new CsvMappingError(row.getRecordIndex(), row.getLineNumber(), -1, ex.getMessage()), row.getRecordIndex(), row.getLineNumber());
        }
    }
}