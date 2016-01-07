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
