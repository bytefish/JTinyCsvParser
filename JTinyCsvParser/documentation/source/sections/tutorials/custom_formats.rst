.. _tutorials_custom_formats:

Parsing Custom Formats
======================

`JTinyCsvParser`_ makes assumptions about the format of the data, which defaults to the Java default 
formats. This is often sufficient for simple CSV files, but sometimes CSV data comes with values in 
special formats. When the default converter is unable to parse the format, you need to customize the 
converter.

It sounds more complex, than it actually turns out to be. All existing converters support customizing 
the the format used for parsing the value. The formatting string is the same as used for parsing string 
values in Java.

Reading a Date with a custom Format
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Imagine a client sends data with a format for dates and writes dates like this :code:`20040125`. These values 
cannot be parsed with the default date format, but in `JTinyCsvParser`_ a :java:`LocalDateConverter` with the 
custom date time format can be used for the mapping.

To use the custom converter, you are simply passing the converter into the :code:`CsvMapping`.

.. code-block:: java

    public class PersonMapping extends CsvMapping<Person> {
    
        public PersonMapping(IObjectCreator creator) {
            super(creator);
    
            mapProperty(0, String.class, Person::setFirstName);
            mapProperty(1, String.class, Person::setLastName);
            mapProperty(2, LocalDate.class, Person::setBirthDate, new LocalDateConverter(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

.. _JTinyCsvParser: https://github.com/bytefish/JTinyCsvParser