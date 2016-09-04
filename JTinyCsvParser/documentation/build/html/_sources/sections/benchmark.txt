.. _benchmark:

Benchmark
=========

.. highlight: java



Dataset
~~~~~~~

In this benchmark the local weather data in March 2015 gathered by all weather stations in the USA is parsed. 

You can obtain the data :code:`QCLCD201503.zip` from:
 
* http://www.ncdc.noaa.gov/orders/qclcd](http://www.ncdc.noaa.gov/orders/qclcd

The File size is :code:`557 MB` and it has :code:`4,496,262` lines.

Setup
~~~~~

Software
--------

The Java Version used is :code:`1.8.0_66`.

::

    C:\Users\philipp>java -version
    java version "1.8.0_66"
    Java(TM) SE Runtime Environment (build 1.8.0_66-b18)
    Java HotSpot(TM) 64-Bit Server VM (build 25.66-b18, mixed mode)

Hardware
--------

* Intel (R) Core (TM) i5-3450 
* Hitachi HDS721010CLA330 (1 TB Capacity, 32 MB Cache, 7200 RPM)
* 16 GB RAM 

Measuring the Elapsed Time
--------------------------

Java 1.8 introduced new classes like :code:`LocalDate`, :code:`LocalDateTime` or :code:`Duration` to work with time. Combined with lambda functions we can 
easily write a nice helper class :code:`MeasurementUtils`, that measures the elapsed time of a function.

You simply have to pass a description and an :code:`Action` into the :code:`MeasurementUtils.MeasureElapsedTime` method, and it will print out the elapsed time.

.. code-block:: java

    // Copyright (c) Philipp Wagner. All rights reserved.
    // Licensed under the MIT license. See LICENSE file in the project root for full license information.
    
    package de.bytefish.jtinycsvparser.utils;
    
    import java.time.Duration;
    import java.time.Instant;
    
    public class MeasurementUtils {
    
        /**
         * Java 1.8 doesn't have a Consumer without parameters (why not?), so we
         * are defining a FunctionalInterface with a nullary function.
         *
         * I call it Action, so I am consistent with .NET.
         */
        @FunctionalInterface
        public interface Action {
    
            void invoke();
    
        }
    
        public static void MeasureElapsedTime(String description, Action action) {
            Duration duration = MeasureElapsedTime(action);
            System.out.println(String.format("[%s] %s", description, duration));
        }
    
        public static Duration MeasureElapsedTime(Action action) {
            Instant start = Instant.now();
    
            action.invoke();
    
            Instant end = Instant.now();
    
            return Duration.between(start, end);
        }
    }

Reading a File Sequentially
~~~~~~~~~~~~~~~~~~~~~~~~~~~

First we have to find out, if the CSV parsing is an I/O or CPU bound task. The lower bound of the CSV Parsing is obviously given by the time needed to read a text 
file, the actual CSV parsing and mapping cannot be any faster. I am using :code:`Files.lines`, in order to consume a ``Stream<String>`` with the data.

Benchmark Code
--------------

.. code-block:: java

    @Test
    public void testReadFromFile_SequentialRead() {
    
        MeasurementUtils.MeasureElapsedTime("LocalWeatherData_SequentialRead", () -> {
    
            // Read the file. Make sure to wrap it in a try, so the file handle gets disposed properly:
            try(Stream<String> stream = Files.lines(FileSystems.getDefault().getPath("C:\\Users\\philipp\\Downloads\\csv", "201503hourly.txt"), StandardCharsets.UTF_8)) {
    
                List<String> result = stream
                        .collect(Collectors.toList()); // turn it into a List!
    
                // Make sure we got the correct amount of lines in the file:
                Assert.assertEquals(4496263, result.size());
    
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
            
        });
    }


Make sure to always close the Stream returned by :code:`Files.lines`, because it is not closed automatically!

Benchmark Result
----------------

::

    [LocalWeatherData_SequentialRead] PT4.258S

Reading the CSV File takes approximately :code:`4.3` seconds. So the entire mapping from CSV to objects cannot be faster, than :code:`4.3` seconds.

Benchmarking JTinyCsvParser
~~~~~~~~~~~~~~~~~~~~~~~~~~~

In order to parse a CSV file into a strongly-typed object, you have to define the domain model in your application and a :code:`CsvMapping` for the class.

Domain Model
------------

.. code-block:: java

    public class LocalWeatherData
    {
        private String WBAN;
    
        private LocalDate Date;
    
        private String SkyCondition;
    
        public String getWBAN() {
            return WBAN;
        }
    
        public void setWBAN(String WBAN) {
            this.WBAN = WBAN;
        }
    
        public LocalDate getDate() {
            return Date;
        }
    
        public void setDate(LocalDate date) {
            Date = date;
        }
    
        public String getSkyCondition() {
            return SkyCondition;
        }
    
        public void setSkyCondition(String skyCondition) {
            SkyCondition = skyCondition;
        }
    }


CsvMapping
----------

We only want to map the columns :code:`WBAN` (Column 0), :code:`Date` (Column 1) and :code:`SkyCondition` (Column 4) to the Domain Model, which is done by using the :code:`mapProperty` function.

.. code-block:: java

    public class LocalWeatherDataMapper extends CsvMapping<LocalWeatherData>
    {
        public LocalWeatherDataMapper(IObjectCreator creator)
        {
            super(creator);
        
            mapProperty(0, String.class, LocalWeatherData::setWBAN);
            mapProperty(1, LocalDate.class, LocalWeatherData::setDate, new LocalDateConverter(DateTimeFormatter.ofPattern("yyyyMMdd")));
            mapProperty(4, String.class, LocalWeatherData::setSkyCondition);
        }
    }


Benchmarking JTinyCsvParser (Single Threaded)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Benchmark Code
--------------

.. code-block:: java

    @Test
    public void testReadFromFile_LocalWeatherData_Sequential() {
    
        // Not in parallel:
        CsvParserOptions options = new CsvParserOptions(true, ",", false);
        // The Mapping to employ:
        LocalWeatherDataMapper mapping = new LocalWeatherDataMapper(() -> new LocalWeatherData());
        // Construct the parser:
        CsvParser<LocalWeatherData> parser = new CsvParser<>(options, mapping);
        // Measure the Time using the MeasurementUtils:
        MeasurementUtils.MeasureElapsedTime("LocalWeatherData_Sequential_Parse", () -> {
    
            // Read the file. Make sure to wrap it in a try, so the file handle gets disposed properly:
            try(Stream<CsvMappingResult<LocalWeatherData>> stream = parser.readFromFile(FileSystems.getDefault().getPath("C:\\Users\\philipp\\Downloads\\csv", "201503hourly.txt"), StandardCharsets.UTF_8)) {
    
                    List<CsvMappingResult<LocalWeatherData>> result = stream
                            .filter(e -> e.isValid())
                            .collect(Collectors.toList()); // turn it into a List!
    
                Assert.assertEquals(4496262, result.size());
            }
        });
    }

Benchmark Results
-----------------

::

    [LocalWeatherData_Sequential_Parse] PT19.252S

Parsing the entire file takes approximately 20 seconds. I think this is a reasonable speed for a Single Threaded run. A lot of stuff is going on in the parsing, especially Auto Boxing Values is a 
time-consuming task I guess. I didn't profile the entire library, so I cannot tell exactely where one could squeeze out the last CPU cycles.

Benchmarking JTinyCsvParser (Parallel Streams, Without JDK Bugfix)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Java 1.8 introduced Parallel Streams to simplify parallel computing in applications. 

You can basically we can turn every simple Stream into a Parallel Stream, by calling the :code:`parallel()` method on it. One weird thing is, that I don't have any control over 
the degree of parallelism at this point. By default the number of processors is used for the default :code:`ForkJoinPool`. But describing Parallel Streams in Java 1,.8 is out of scope 
for this article. 

Why using a Parallel Stream?
----------------------------

We have learnt, that the mapping to objects is largely CPU bound. It is a well-defined problem and by throwing some more cores at it, we should see a significantly improved performance. 

Benchmark Code
--------------

In order to process the data in parallel, you have to set the ``parallel`` parameter in the ``CsvParserOption``. 

.. code-block:: java

    @Test
    public void testReadFromFile_LocalWeatherData_Parallel() {
    
        // See the third constructor argument. It sets the Parallel processing to true!
        CsvParserOptions options = new CsvParserOptions(true, ",", true);
        // The Mapping to employ:
        LocalWeatherDataMapper mapping = new LocalWeatherDataMapper(() -> new LocalWeatherData());
        // Construct the parser:
        CsvParser<LocalWeatherData> parser = new CsvParser<>(options, mapping);
        // Measure the Time using the MeasurementUtils:
        MeasurementUtils.MeasureElapsedTime("LocalWeatherData_Parallel_Parse", () -> {
    
            // Read the file. Make sure to wrap it in a try, so the file handle gets disposed properly:
            try(Stream<CsvMappingResult<LocalWeatherData>> stream = parser.readFromFile(FileSystems.getDefault().getPath("C:\\Users\\philipp\\Downloads\\csv", "201503hourly.txt"), StandardCharsets.UTF_8)) {
    
                List<CsvMappingResult<LocalWeatherData>> result = stream
                        .filter(e -> e.isValid())
                        .collect(Collectors.toList()); // turn it into a List!
    
                Assert.assertEquals(4496262, result.size());
    
            }
        });
    }

Benchmark Results (without JDK Bugfix)
--------------------------------------

The results are not satisfying! Although all cores are utilized during processing the file, it actually leads to a slow-down.

::

    [LocalWeatherData_Parallel_Parse] PT26.232S

Why is that?

Well in order to parallelize a task Java has to split the problem into sub problems somehow. This is done by using a :code:`Spliterator`, which basically means "splittable Iterator". 
The :code:`Spliterator` has a method :code:`trySplit()`, that splits off a chunk of elements to be processed by the threads. I assume, that the estimation about the size of the data 
is not known ahead and that's why Java 1.8 initializes the estimated size with :code:`Long.MAX_VALUE` (unknown size).

We can find the confirmation for it, if we take a look into the OpenJDK Bugtracker titled:

* `JDK-8072773 Files.lines needs a better splitting implementation for stream source <https://bugs.openjdk.java.net/browse/JDK-8072773>`_


Benchmarking JTinyCsvParser (Parallel Streams, With JDK Bugfix)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

We have seen, that there is a bug in the :code:`Spliterator` for the :code:`Files.lines` method, but the OpenJDK Bug ticket `JDK-8072773 <https://bugs.openjdk.java.net/browse/JDK-8072773>`_ also references a bug fix. 

When I backport the bugfix mentioned in `JDK-8072773 <https://bugs.openjdk.java.net/browse/JDK-8072773>`_ to Java 1.8 (which is very easy), then the file is split into correctly sized chunks. The file is then 
parsed in :code:`12` seconds.

::

    [LocalWeatherData_Parallel_Parse] PT11.773S

But since the OpenJDK code is released under terms of the GPL v2 license, I cannot include the mentioned bugfix into `JTinyCsvParser`_.

.. _JTinyCsvParser: https://github.com/bytefish/JTinyCsvParser
.. _NUnit: http://www.nunit.org
.. MIT License: https://opensource.org/licenses/MIT