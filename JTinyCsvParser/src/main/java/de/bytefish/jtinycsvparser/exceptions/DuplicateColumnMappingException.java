// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.exceptions;

public class DuplicateColumnMappingException extends RuntimeException {

    public DuplicateColumnMappingException() {
        super();
    }

    public DuplicateColumnMappingException(String message) {
        super(message);
    }

    public DuplicateColumnMappingException(String message, Exception innerException) {
        super(message, innerException);
    }

}
