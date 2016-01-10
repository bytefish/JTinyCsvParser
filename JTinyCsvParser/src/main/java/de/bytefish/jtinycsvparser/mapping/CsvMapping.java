// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.exceptions.DuplicateColumnMappingException;
import de.bytefish.jtinycsvparser.typeconverter.ITypeConverter;
import de.bytefish.jtinycsvparser.typeconverter.TypeConverterProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class CsvMapping<TEntity> {

    private class IndexToPropertyMapping {

        private int index;

        private ICsvPropertyMapping<TEntity> mapping;

        public IndexToPropertyMapping(int index, ICsvPropertyMapping<TEntity> mapping) {
            this.index = index;
            this.mapping = mapping;
        }

        public int getColumnIndex() {
            return index;
        }

        public ICsvPropertyMapping<TEntity> getPropertyMapping() {
            return mapping;
        }

        @Override
        public String toString() {
            return "IndexToPropertyMapping{" +
                    "index=" + index +
                    ", mapping=" + mapping +
                    '}';
        }
    }

    private IObjectCreator creator;
    private TypeConverterProvider typeConverterProvider;
    private ArrayList<IndexToPropertyMapping> csvPropertyMappings;

    public CsvMapping(IObjectCreator creator) {
        this.creator = creator;
        this.csvPropertyMappings = new ArrayList<>();
        this.typeConverterProvider = new TypeConverterProvider();
    }

    public <TProperty> void MapProperty(int columnIndex, Type targetType, BiConsumer<TEntity, TProperty> setter) {
        ITypeConverter<TProperty> converter = typeConverterProvider.Resolve(targetType);

        MapProperty(columnIndex, targetType, setter, converter);
    }

    public <TProperty> void MapProperty(int columnIndex, Type targetType, BiConsumer<TEntity, TProperty> setter, ITypeConverter<TProperty> converter) {
        if(csvPropertyMappings.stream().anyMatch(e -> e.getColumnIndex() == columnIndex)) {
            throw new DuplicateColumnMappingException("Duplicate Mapping for Column " + columnIndex);
        }
        csvPropertyMappings.add(new IndexToPropertyMapping(columnIndex, new CsvPropertyMapping(setter, converter)));
    }

    public CsvMappingResult<TEntity> Map(String[] values) {

        TEntity entity = (TEntity) creator.create();

        // Iterate over all registered mappings:
        for (int pos = 0; pos < csvPropertyMappings.size(); pos++) {

            // Get the registered mapping:
            IndexToPropertyMapping indexToPropertyMapping = csvPropertyMappings.get(pos);

            // Get the column index for this property mapping:
            int columnIndex = indexToPropertyMapping.getColumnIndex();

            // Throw an error, if the current line doesn't have enough columns for the mapping:
            if (columnIndex >= values.length) {
                return new CsvMappingResult(new CsvMappingError(columnIndex, null, String.format("Column %s Out Of Range", columnIndex)));
            }

            // Get the Value of the Column:
            String value = values[columnIndex];

            // And finally try to map the value to the entity (honestly it is magical using the BiConsumer!):
            if (!indexToPropertyMapping.getPropertyMapping().tryMapValue(entity, value)) {
                return new CsvMappingResult(new CsvMappingError(columnIndex, value, "Conversion failed"));
            }
        }

        return new CsvMappingResult(entity);
    }

    @Override
    public String toString() {

        String csvPropertyMappingsString = csvPropertyMappings
                .stream()
                .map(e -> e.toString())
                .collect(Collectors.joining(", "));

        return "CsvMapping{" +
                "creator=" + creator +
                ", typeConverterProvider=" + typeConverterProvider +
                ", csvPropertyMappings=[" + csvPropertyMappingsString + "]" +
                '}';
    }
}
