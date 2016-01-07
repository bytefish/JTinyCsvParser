// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import junit.framework.Assert;

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

            Map(0, Integer.TYPE, SampleEntity::setX);
        }
    }

    @org.junit.Test
    public void testMapProperty() throws Exception {
        // The ObjectCreator used to instantiate a new Object:
        IObjectCreator<SampleEntity> objectCreator = () -> new SampleEntity();
        // The Mapping to use for mapping between a CSV File and the Object:
        CsvMapping<SampleEntity> csvMapping = new SampleEntityMapping(objectCreator);
        // Perform an actual mapping between a tokenized CSV Line and the object:
        CsvMappingResult<SampleEntity> result = csvMapping.Map(new String[] {"1"});
        // There shouldn't be an error:
        Assert.assertEquals(null, result.getError());
        // And the Property A should be 1:
        Assert.assertEquals(1, result.getResult().A);
    }
}