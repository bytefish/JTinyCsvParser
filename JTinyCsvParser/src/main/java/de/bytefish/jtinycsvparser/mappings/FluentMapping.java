package de.bytefish.jtinycsvparser.mappings;

import de.bytefish.jtinycsvparser.core.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FluentMapping<T> implements ICsvMapping<T>, IHeaderBinder {
    private final Supplier<T> factory;
    private final List<CsvColumnBinding<T>> bindings = new ArrayList<>();
    private int[] resolvedIndices;
    private boolean headersResolved = false;

    public FluentMapping(Supplier<T> factory) {
        this.factory = factory;
    }

    public FluentMapping<T> map(CsvColumnBinding<T> binding) {
        this.bindings.add(binding);
        return this;
    }

    public FluentMapping<T> map(String columnName, BiConsumer<T, String> binder) {
        return map(new CsvColumnBinding<>(columnName, binder));
    }

    public FluentMapping<T> map(int columnIndex, BiConsumer<T, String> binder) {
        return map(new CsvColumnBinding<>(columnIndex, binder));
    }

    @Override
    public boolean needsHeaderResolution() {
        return !headersResolved && bindings.stream().anyMatch(b -> b.columnName() != null);
    }

    @Override
    public CsvHeaderResolution bindHeaders(CsvRow headerRow) {
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headerRow.getFieldCount(); i++) {
            String header = headerRow.getField(i);
            if (header != null) {
                header = header.trim().toLowerCase(Locale.ROOT);
                if (header.startsWith("\uFEFF")) header = header.substring(1);
                headerMap.put(header, i);
            }
        }

        resolvedIndices = new int[bindings.size()];
        for (int i = 0; i < bindings.size(); i++) {
            CsvColumnBinding<T> binding = bindings.get(i);
            if (binding.columnIndex() != null) {
                resolvedIndices[i] = binding.columnIndex();
            } else {
                String boundName = binding.columnName().trim().toLowerCase(Locale.ROOT);
                if (boundName.startsWith("\uFEFF")) boundName = boundName.substring(1);

                Integer idx = headerMap.get(boundName);
                resolvedIndices[i] = (idx != null) ? idx : -1;
            }
        }
        headersResolved = true;
        return new CsvHeaderResolution(resolvedIndices);
    }

    @Override
    public CsvMappingResult<T> map(CsvRow row, CsvHeaderResolution headerResolution) {
        try {
            T entity = factory.get();
            for (int i = 0; i < bindings.size(); i++) {
                int colIdx = -1;
                CsvColumnBinding<T> binding = bindings.get(i);

                if (headerResolution != null) {
                    colIdx = headerResolution.getColumnIndex(i);
                } else if (binding.columnIndex() != null) {
                    colIdx = binding.columnIndex();
                }

                if (colIdx != -1) {
                    binding.binder().accept(entity, row.getField(colIdx));
                }
            }
            return new CsvMappingResult.Success<>(entity, row.getRecordIndex(), row.getLineNumber());
        } catch (Exception ex) {
            return new CsvMappingResult.Error<>(new CsvMappingError(row.getRecordIndex(), row.getLineNumber(), -1, ex.getMessage()), row.getRecordIndex(), row.getLineNumber());
        }
    }
}