// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void testIsNullOrWhiteSpace() throws Exception {
        Assert.assertTrue(StringUtils.isNullOrWhiteSpace(null));
        Assert.assertTrue(StringUtils.isNullOrWhiteSpace(""));
        Assert.assertTrue(StringUtils.isNullOrWhiteSpace(" "));

        Assert.assertFalse(StringUtils.isNullOrWhiteSpace("a"));
    }
}