package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;

public interface ITypeConverter<TTargetType> extends TypeConverter {

    TTargetType convert(String value);

    Type getTargetType();
}
