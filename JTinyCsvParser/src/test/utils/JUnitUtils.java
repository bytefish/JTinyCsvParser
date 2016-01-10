// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.utils;


import org.junit.Assert;

import java.lang.reflect.Type;

public class JUnitUtils {

    @FunctionalInterface
    public interface Action {
        void invoke();
    }

    public static void assertThrows(Action action) {
        boolean hasThrown = false;
        try {
            action.invoke();
        } catch(Exception e) {
            hasThrown = true;
        }
        Assert.assertTrue(hasThrown);
    }

    public static void assertThrows(Action action, Type expectedType) {
        boolean hasThrown = false;
        Type actualType = null;
        try {
            action.invoke();
        } catch(Exception e) {
            hasThrown = true;
            actualType = e.getClass();
        }
        Assert.assertTrue(hasThrown);
        Assert.assertEquals(expectedType, actualType);
    }
}