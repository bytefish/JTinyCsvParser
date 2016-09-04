.. _quickstart:

Quickstart
==========

This section is only a quick tour of the most common use of JTinyCsvParser. For more detailed 
information, please consult the full :ref:`User Guide <userguide>`.

The :ref:`Tutorials<tutorials>` are also a good place to find more advanced use-cases.

Quick Installation
~~~~~~~~~~~~~~~~~~

JTinyCsvParser is available in the Central Maven Repository.

You can add the following dependencies to your pom.xml to include JTinyCsvParser in your project.

.. code-block:: xml

    <dependency>
        <groupId>de.bytefish</groupId>
        <artifactId>jtinycsvparser</artifactId>
        <version>1.2</version>
    </dependency>


Getting Started
~~~~~~~~~~~~~~~

Imagine we have list of persons in a CSV file :code:`persons.csv` with their first name, last name 
and birthdate. The columns are separated by :code:`,` as column delimiter, which each line will be 
split at.

::

    Philipp;Wagner;1986/05/12
    Max;Musterman;2014/01/02

The corresponding domain model in our C# code might look like this.

.. code-block:: java

    public class Person {
    
        private String firstName;
        private String lastName;
        private LocalDate BirthDate;
    
        public String getFirstName() {
            return firstName;
        }
    
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    
        public String getLastName() {
            return lastName;
        }
    
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    
        public LocalDate getBirthDate() {
            return BirthDate;
        }
    
        public void setBirthDate(LocalDate birthDate) {
            BirthDate = birthDate;
        }
    }

When using `JTinyCsvParser`_ you have to define the mapping between the columns in the CSV data and 
the property in you domain model, which is done by implementing the class :code:`CsvMapping<TEntity>`, 
where :code:`TEntity` is the class :code:`Person`.

The :code:`IObjectCreator` is a functional interface, which returns a :code:`TEntity` object. It is used 
to instantiate instances of the target objects, without using the Java Reflection API in the library.

.. code-block:: java

    class PersonMapping extends CsvMapping<Person> {

        public PersonMapping(IObjectCreator creator) {
            super(creator);

            mapProperty(0, String.class, Person::setFirstName);
            mapProperty(1, String.class, Person::setLastName);
            mapProperty(2, LocalDate.class, Person::setBirthDate);
        }
    }


    
The method :code:`mapProperty` is used to map between the column number in the CSV file and the property in the 
domain model. 

Then we can use the mapping to parse the CSV data with a ``CsvParser``. In the `CsvParserOptions` we are 
defining to not skip the header and use a :code:`,` as column delimiter. I have assumed, that the file is encoded 
as :code:`UTF8`.

.. code-block:: java

    public class CsvParserTest {
    
        @Test
        public void testParse_readFromFile() {
        
            // Use , as column delimiter and do not skip the header:
            CsvParserOptions options = new CsvParserOptions(false, ",");
            
            // Create the Mapping:
            PersonMapping mapping = new PersonMapping(() -> new Person());
            
            // Create the Parser:
            CsvParser<Person> parser = new CsvParser<>(options, mapping);
            
            // Path to read from:
            Path csvFile = FileSystems.getDefault().getPath("C:\\csv", "persons.txt");
            
            // Holds the Results:
            List<CsvMappingResult<Person>> result;
            
            // Read the CSV File:
            try (Stream<CsvMappingResult<Person>> stream = parser.readFromFile(csvFile, StandardCharsets.UTF_8)) {
                result = stream.collect(Collectors.toList()); // turn it into a List!
            }
            
            // Do we have a result?
            Assert.assertNotNull(result);
            
            // Do we have two persons?
            Assert.assertEquals(2, result.size());
            
            // Get the first person:
            Person person0 = result.get(0).getResult();
        
            Assert.assertEquals("Philipp", person0.firstName);
            Assert.assertEquals("Wagner", person0.lastName);
            Assert.assertEquals(1986, person0.getBirthDate().getYear());
            Assert.assertEquals(5, person0.getBirthDate().getMonthValue());
            Assert.assertEquals(12, person0.getBirthDate().getDayOfMonth());
        
            // Get the second person:
            Person person1 = result.get(1).getResult();
        
            Assert.assertEquals("Max", person1.firstName);
            Assert.assertEquals("Musterman", person1.lastName);
            Assert.assertEquals(2000, person1.getBirthDate().getYear());
            Assert.assertEquals(1, person1.getBirthDate().getMonthValue());
            Assert.assertEquals(7, person1.getBirthDate().getDayOfMonth());
        }
        
    }


    
Reading From a String
"""""""""""""""""""""

Reading from a string is possible with the :java:`CsvParser.readFromString` method.

.. code-block:: java

    public class CsvParserTest {
    
        @Test
        public void testParse_ReadFromString() throws Exception {
            
            // Use , as column delimiter and do not skip the header:
            CsvParserOptions options = new CsvParserOptions(false, ",");
            
            // Create the Mapping:
            PersonMapping mapping = new PersonMapping(() -> new Person());
            
            // Create the Parser:
            CsvParser<Person> parser = new CsvParser<>(options, mapping);
            
            // Define the Line Separator:
            String lineSeparator = System.getProperty("line.separator");
            
            // Create some CSV Data:
            String csvData = "Philipp,Wagner,1986-05-12" + lineSeparator + "Max,Musterman,2000-01-07";
            
            // Parse the CSV Data String:
            List<CsvMappingResult<Person>> result =  parser.readFromString(csvData, new CsvReaderOptions(lineSeparator))
                    .collect(Collectors.toList()); // turn it into a List!
                    
            // Do we have results?
            Assert.assertNotNull(result);
        
            // Do we have two persons?
            Assert.assertEquals(2, result.size());
        
            // Get the first person:
            Person person0 = result.get(0).getResult();
        
            Assert.assertEquals("Philipp", person0.firstName);
            Assert.assertEquals("Wagner", person0.lastName);
            Assert.assertEquals(1986, person0.getBirthDate().getYear());
            Assert.assertEquals(5, person0.getBirthDate().getMonthValue());
            Assert.assertEquals(12, person0.getBirthDate().getDayOfMonth());
        
            // Get the second person:
            Person person1 = result.get(1).getResult();
        
            Assert.assertEquals("Max", person1.firstName);
            Assert.assertEquals("Musterman", person1.lastName);
            Assert.assertEquals(2000, person1.getBirthDate().getYear());
            Assert.assertEquals(1, person1.getBirthDate().getMonthValue());
            Assert.assertEquals(7, person1.getBirthDate().getDayOfMonth());
        }
        
    }    
    
.. _JTinyCsvParser: https://github.com/bytefish/JTinyCsvParser
.. MIT License: https://opensource.org/licenses/MIT