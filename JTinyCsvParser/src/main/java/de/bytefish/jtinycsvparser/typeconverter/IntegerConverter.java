package de.bytefish.jtinycsvparser.typeconverter;

import java.lang.reflect.Type;
import java.text.NumberFormat;

/**
 * Created by philipp on 1/6/2016.
 */
public class IntegerConverter implements ITypeConverter<Integer> {

    private NumberFormat numberFormat;

    public IntegerConverter(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    @Override
    public Integer convert(String value) {
        try {
            return Integer.parseInt(value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTargetType() {
        return Integer.TYPE;
    }
}
