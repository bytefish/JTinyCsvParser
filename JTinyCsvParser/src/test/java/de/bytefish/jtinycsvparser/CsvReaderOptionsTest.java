// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.utils.JUnitUtils;
import org.junit.Assert;
import org.junit.Test;

public class CsvReaderOptionsTest {

    @Test
    public void testCsvReaderOptions_toString() {
        JUnitUtils.assertDoesNotThrow(() -> new CsvReaderOptions("\n").toString());
    }

    @Test
    public void testCsvReaderOptions_getNewLine() {
        Assert.assertEquals("\n", new CsvReaderOptions("\n").getNewLine());
    }

}
