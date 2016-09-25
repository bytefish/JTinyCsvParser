// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.exceptions.DuplicateColumnMappingException;
import de.bytefish.jtinycsvparser.utils.JUnitUtils;
import org.junit.Assert;
import org.junit.Test;


public class CsvMappingTest {

    public class SampleEntity {

        public SampleEntity() {

        }

        public int A;

        public void setX(int val) {
            A = val;
        }
    }

    public class SampleEntityMapping extends CsvMapping<SampleEntity> {
        public SampleEntityMapping(IObjectCreator<SampleEntity> creator) {
            super(creator);

            mapProperty(0, Integer.class, SampleEntity::setX);
        }
    }

    public class InvalidSampleEntityMapping extends CsvMapping<SampleEntity> {
        public InvalidSampleEntityMapping(IObjectCreator<SampleEntity> creator) {
            super(creator);

            mapProperty(0, Integer.class, SampleEntity::setX);
            mapProperty(0, Integer.class, SampleEntity::setX);
        }
    }

    @Test
    public void testMapProperty() {
        // The ObjectCreator used to instantiate a new Object:
        IObjectCreator<SampleEntity> objectCreator = () -> new SampleEntity();
        // The Mapping to use for mapping between a CSV File and the Object:
        CsvMapping<SampleEntity> csvMapping = new SampleEntityMapping(objectCreator);
        // Perform an actual mapping between a tokenized CSV Line and the object:
        CsvMappingResult<SampleEntity> result = csvMapping.map(new String[] {"1"});
        // There shouldn't be an error:
        Assert.assertEquals(null, result.getError());
        // And the Property A should be 1:
        Assert.assertEquals(1, result.getResult().A);
    }

    @Test
    public void testMapProperty_CsvMappingResult_ToString() {
        // The ObjectCreator used to instantiate a new Object:
        IObjectCreator<SampleEntity> objectCreator = () -> new SampleEntity();
        // The Mapping to use for mapping between a CSV File and the Object:
        CsvMapping<SampleEntity> csvMapping = new SampleEntityMapping(objectCreator);
        // Perform an actual mapping between a tokenized CSV Line and the object:
        CsvMappingResult<SampleEntity> result = csvMapping.map(new String[] {"1"});
        // There shouldn't be an error:
        Assert.assertEquals(null, result.getError());
        // And the Property A should be 1:
        Assert.assertEquals(1, result.getResult().A);
    }

    @Test
    public void testMapProperty_DuplicateMapping_Throws() {
        IObjectCreator<SampleEntity> objectCreator = () -> new SampleEntity();

        JUnitUtils.assertThrows(() -> new InvalidSampleEntityMapping(objectCreator), DuplicateColumnMappingException.class);
    }

    @Test
    public void ToString_NoThrow() {
        // The ObjectCreator used to instantiate a new Object:
        IObjectCreator<SampleEntity> objectCreator = () -> new SampleEntity();
        // The Mapping to use for mapping between a CSV File and the Object:
        CsvMapping<SampleEntity> csvMapping = new SampleEntityMapping(objectCreator);

        JUnitUtils.assertDoesNotThrow(() -> csvMapping.toString());
    }

    @Test
    public void testMapProperty_TooFewData() {
        // The ObjectCreator used to instantiate a new Object:
        IObjectCreator<SampleEntity> objectCreator = () -> new SampleEntity();
        // The Mapping to use for mapping between a CSV File and the Object:
        CsvMapping<SampleEntity> csvMapping = new SampleEntityMapping(objectCreator);
        // Perform an actual mapping between a tokenized CSV Line and the object:
        CsvMappingResult<SampleEntity> result = csvMapping.map(new String[] {});
        // There should be an error:
        Assert.assertNotNull(result.getError());
        // And the Property A should be 1:
        Assert.assertEquals(0, result.getError().getIndex());
        Assert.assertEquals(null, result.getError().getValue());
        Assert.assertEquals("Column 0 Out Of Range", result.getError().getMessage());

    }


}