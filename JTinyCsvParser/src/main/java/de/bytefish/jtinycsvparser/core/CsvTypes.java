package de.bytefish.jtinycsvparser.core;

import de.bytefish.jtinycsvparser.types.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Function;

/**
 * Standard CSV types using Java naming conventions.
 */
public class CsvTypes {
    public static final CsvType<String> STRING = val -> val;

    public static final CsvBooleanType BOOLEAN = val -> Boolean.parseBoolean(val.trim());

    public static final CsvByteType BYTE = val -> Byte.parseByte(val.trim());

    /**
     * Unsigned byte logic (0-255). Boxed as Integer to represent the range.
     * Use primitive(..., ObjIntConsumer) to map to a larger storage type if needed.
     */
    public static final CsvType<Integer> UNSIGNED_BYTE = val -> Integer.parseInt(val.trim()) & 0xFF;

    public static final CsvShortType SHORT = val -> Short.parseShort(val.trim());
    public static final CsvIntegerType INTEGER = val -> Integer.parseInt(val.trim());
    public static final CsvLongType LONG = val -> Long.parseLong(val.trim());
    public static final CsvFloatType FLOAT = val -> Float.parseFloat(val.trim());
    public static final CsvDoubleType DOUBLE = val -> Double.parseDouble(val.trim());

    public static final CsvType<BigInteger> BIG_INTEGER = val -> new BigInteger(val.trim());
    public static final CsvType<BigDecimal> BIG_DECIMAL = val -> new BigDecimal(val.trim());
    public static final CsvType<UUID> UUID = val -> java.util.UUID.fromString(val.trim());

    // Date and Time Types
    public static final CsvType<LocalDate> LOCAL_DATE = val -> LocalDate.parse(val.trim());
    public static final CsvType<LocalDateTime> LOCAL_DATE_TIME = val -> LocalDateTime.parse(val.trim());
    public static final CsvType<ZonedDateTime> ZONED_DATE_TIME = val -> ZonedDateTime.parse(val.trim());
    public static final CsvType<OffsetDateTime> OFFSET_DATE_TIME = val -> OffsetDateTime.parse(val.trim());
    public static final CsvType<Instant> INSTANT = val -> Instant.parse(val.trim());

    /**
     * Helper to create custom LocalDate types with specific formatters.
     */
    public static CsvType<LocalDate> localDate(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return val -> LocalDate.parse(val.trim(), dtf);
    }

    /**
     * Helper to create custom LocalDateTime types with specific formatters.
     */
    public static CsvType<LocalDateTime> localDateTime(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return val -> LocalDateTime.parse(val.trim(), dtf);
    }

    /**
     * Factory for custom types based on a parsing function.
     */
    public static <V> CsvType<V> custom(Function<String, V> parser) {
        return parser::apply;
    }
}
