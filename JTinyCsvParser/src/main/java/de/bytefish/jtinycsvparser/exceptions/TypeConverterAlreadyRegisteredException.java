// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.exceptions;

public class TypeConverterAlreadyRegisteredException extends RuntimeException {
    public TypeConverterAlreadyRegisteredException() {
        super();
    }

    public TypeConverterAlreadyRegisteredException(String message) {
        super(message);
    }

    public TypeConverterAlreadyRegisteredException(String message, Exception innerException) {
        super(message, innerException);
    }
}
