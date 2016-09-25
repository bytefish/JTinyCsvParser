// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.utils.JUnitUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class CsvMappingErrorTest {
    @Test
    public void testToString_NoThrow() {
        CsvMappingError[] errors = new CsvMappingError[] {
                new CsvMappingError(0, null, ""),
                new CsvMappingError(0, null, null),
                new CsvMappingError(0, "", null),
                new CsvMappingError(0, "", ""),
        };

        for (CsvMappingError error : errors) {
            JUnitUtils.assertDoesNotThrow(() -> error.toString());
        }
    }
}
