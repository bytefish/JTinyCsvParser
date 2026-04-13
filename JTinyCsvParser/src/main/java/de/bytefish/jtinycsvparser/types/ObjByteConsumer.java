package de.bytefish.jtinycsvparser.types;

@FunctionalInterface public interface ObjByteConsumer<T> { void accept(T obj, byte value); }
