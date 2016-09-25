// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.mapping;

import de.bytefish.jtinycsvparser.utils.JUnitUtils;
import org.junit.Test;

public class CsvMappingResultTest {
    @Test
    public void testToString_NoThrow() {
        CsvMappingResult<Integer> no_error = new CsvMappingResult<Integer>(1);
        CsvMappingResult<Integer> error = new CsvMappingResult<Integer>(new CsvMappingError(0, null, null));

        JUnitUtils.assertDoesNotThrow(() -> error.toString());
        JUnitUtils.assertDoesNotThrow(() -> no_error.toString());
    }
}
