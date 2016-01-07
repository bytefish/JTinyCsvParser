// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.typeconverter.ITypeConverter;
import de.bytefish.jtinycsvparser.typeconverter.TypeConverterProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.function.BiConsumer;

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
    }

    private IObjectCreator creator;
    private TypeConverterProvider typeConverterProvider;
    private ArrayList<IndexToPropertyMapping> csvPropertyMappings;

    public CsvMapping(IObjectCreator creator) {
        this.creator = creator;
        this.csvPropertyMappings = new ArrayList<>();
        this.typeConverterProvider = new TypeConverterProvider();
    }

    public <TProperty> void Map(int columnIndex, Type targetType, BiConsumer<TEntity, TProperty> setter) {
       ITypeConverter<TProperty> converter = typeConverterProvider.Resolve(targetType);

        csvPropertyMappings.add(new IndexToPropertyMapping(columnIndex, new CsvPropertyMapping(setter, converter)));
    }

    public CsvMappingResult<TEntity> Map(String[] values) {

        TEntity entity = (TEntity) creator.create();

        for (int pos = 0; pos < csvPropertyMappings.size(); pos++)
        {
            IndexToPropertyMapping indexToPropertyMapping = csvPropertyMappings.get(pos);

            int columnIndex = indexToPropertyMapping.getColumnIndex();

            if (columnIndex >= values.length)
            {
                return new CsvMappingResult(new CsvMappingError(columnIndex, String.format("Column %s Out Of Range", columnIndex), null));
            }

            String value = values[columnIndex];

            if (!indexToPropertyMapping.getPropertyMapping().tryMapValue(entity, value))
            {
                return new CsvMappingResult(new CsvMappingError(columnIndex,value, "Conversion failed"));
            }
        }

        return new CsvMappingResult(entity);
    }
}
