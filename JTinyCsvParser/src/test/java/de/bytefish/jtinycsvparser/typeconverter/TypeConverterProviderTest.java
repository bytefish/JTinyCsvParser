// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.typeconverter;

import de.bytefish.jtinycsvparser.exceptions.TypeConverterAlreadyRegisteredException;
import de.bytefish.jtinycsvparser.utils.JUnitUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;

public class TypeConverterProviderTest {

    TypeConverterProvider provider;

    @Before
    public void SetUp() {
        provider = new TypeConverterProvider();
    }

    @Test
    public void testAdd_Duplicate() throws Exception {
        JUnitUtils.assertThrows(() -> provider.add(new IntegerConverter()), TypeConverterAlreadyRegisteredException.class);
    }

    public class SampleEntity {

    }

    public class SampleEntityConverter implements ITypeConverter<SampleEntity> {

        @Override
        public SampleEntity convert(String value) {
            return null;
        }

        @Override
        public Type getTargetType() {
            return SampleEntity.class;
        }
    }

    @Test
    public void testAdd_New() throws Exception {
        JUnitUtils.assertDoesNotThrow(() -> provider.add(new SampleEntityConverter()));
        Assert.assertEquals(SampleEntityConverter.class, provider.resolve(SampleEntity.class).getClass());
    }

    @Test
    public void testResolve() throws Exception {
        Object converter = provider.resolve(Integer.class);

        Assert.assertNotNull(converter);
        Assert.assertEquals(converter.getClass(), IntegerConverter.class);
    }

    @Test
    public void testResolve_NoRegistration() throws Exception {
        JUnitUtils.assertThrows(() -> provider.resolve(SampleEntity.class));
    }


    @Test
    public void testOverride() throws Exception {
        JUnitUtils.assertDoesNotThrow(() -> provider.override(new IntegerConverter()));
    }

    @Test
    public void testToString() throws Exception {
        JUnitUtils.assertDoesNotThrow(() -> provider.toString());
    }
}
