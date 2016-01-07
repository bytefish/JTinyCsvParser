// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.exceptions;

public class TypeConverterNotRegisteredException extends RuntimeException {
    public TypeConverterNotRegisteredException() {
        super();
    }

    public TypeConverterNotRegisteredException(String message) {
        super(message);
    }

    public TypeConverterNotRegisteredException(String message, Exception innerException) {
        super(message, innerException);
    }
}
