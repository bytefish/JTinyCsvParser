package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import junit.framework.Assert;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

/**
 * Created by philipp on 1/6/2016.
 */
public class CsvMappingTest {

    public class SampleEntity {

        public SampleEntity() {

        }

        public Integer A;

        public void setX(Integer val) {
            A = val;
        }
    }

    public class SampleEntityMapping extends CsvMapping<SampleEntity> {
        public SampleEntityMapping() {
            super(new IObjectCreator() {
                @Override
                public Object create() {
                    return new SampleEntity();
                }
            });

            Map(0, Integer.TYPE, SampleEntity::setX);
        }
    }

    @org.junit.Test
    public void testMapProperty() throws Exception {

        CsvMapping<SampleEntity> csvMapping = new SampleEntityMapping();

        CsvMappingResult<SampleEntity> result = csvMapping.Map(new String[] {"1"});

        Assert.assertEquals(null, result.getError());
        Assert.assertEquals(new Integer(1), result.getResult().A);
    }
}