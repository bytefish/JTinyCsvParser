.. _userguide_type_converter:

Type Converter
==============

Introduction
~~~~~~~~~~~~

A :code:`TypeConverter` is used to convert the text value in a column of your CSV data into a Java type.

All available :code:`TypeConverter` in the library are initialized with sane default formats to parse data. These 
formats default to the same formats Java uses to parse string data. 

If you need to parse custom formats, which do not match the default format, you have to specify a custom format for 
the converter when defining the :code:`CsvMapping`.

Available Type Converters
~~~~~~~~~~~~~~~~~~~~~~~~~

+---------------+-----------------------------+
| Java     Type | Type Converter              |
+===============+=============================+
|BigInteger     | BigIntegerConverter         |
+---------------+-----------------------------+
|Byte           | ByteConverter               |
+---------------+-----------------------------+
|Double         | DoubleConverter             |
+---------------+-----------------------------+
|Duration       | DurationConverter           |
+---------------+-----------------------------+
|Enums          | EnumConverter               |
+---------------+-----------------------------+
|Float          | FloatConverter              |
+---------------+-----------------------------+
|Instant        | InstantConverter            |
+---------------+-----------------------------+
|Integer        | IntegerConverter            |
+---------------+-----------------------------+
|LocalDate      | LocalDateConverter          |
+---------------+-----------------------------+
|LocalDateTime  | LocalDateTimeConverter      |
+---------------+-----------------------------+
|LocalTime      | LocalTimeConverter          |
+---------------+-----------------------------+
|Long           | LongConverter               |
+---------------+-----------------------------+
|Short          | ShortConverter              |
+---------------+-----------------------------+
|String         | StringConverter             |
+---------------+-----------------------------+

.. _TinyCsvParser: https://github.com/bytefish/TinyCsvParser
.. _NUnit: http://www.nunit.org
.. MIT License: https://opensource.org/licenses/MIT