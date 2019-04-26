# JTinyCsvParser #

[MIT License]: https://opensource.org/licenses/MIT
[JTinyCsvParser]: https://github.com/bytefish/JTinyCsvParser
[TinyCsvParser]: https://github.com/bytefish/TinyCsvParser

[JTinyCsvParser] is a high-performance CSV Parser for Java 1.8. As the name implies it is a Java version of [TinyCsvParser].

It makes no use of Reflection and is very easy to use and extend.

## Setup ###

[JTinyCsvParser] is available in the Central Maven Repository. 

You can add the following dependencies to your pom.xml to include [JTinyCsvParser] in your project.

```xml
<dependency>
	<groupId>de.bytefish</groupId>
	<artifactId>jtinycsvparser</artifactId>
	<version>1.2</version>
</dependency>
```

## Documentation ##

[JTinyCsvParser] comes with a documentation, which is located at:

* [http://bytefish.github.io/JTinyCsvParser/](http://bytefish.github.io/JTinyCsvParser/)

## Quickstart ##

This is only an example for the most common use of JTinyCsvParser. For more detailed information on custom formats and more advanced use-cases, 
please consult the full User Guide of the official documentation.

Imagine we have list of persons in a CSV file ``persons.csv`` with their first name, last name and birthdate. The columns are separated by 
``,`` as column delimiter, which each line will be split at:

```
Philipp;Wagner;1986/05/12
Max;Musterman;2014/01/02
```

The corresponding domain model in our Java code might look like this.

```java
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
```

When using [JTinyCsvParser] you have to define the mapping between the columns in the CSV data and 
the property in you domain model, which is done by implementing the class ``CsvMapping<TEntity>``, 
where ``TEntity`` is the class ``Person``.

The ``IObjectCreator`` passed into the mapping is a functional interface, which returns a 
``TEntity`` object. It is used to instantiate instances of the target objects, without using 
the Java Reflection API in the library.

```java
public class PersonMapping extends CsvMapping<Person> {

    public PersonMapping(IObjectCreator creator) {
        super(creator);

        mapProperty(0, String.class, Person::setFirstName);
        mapProperty(1, String.class, Person::setLastName);
        mapProperty(2, LocalDate.class, Person::setBirthDate);
    }
}
```
	
The method ``mapProperty`` is used to map between the column number in the CSV file and the property in the 
domain model. 

Then we can use the mapping to parse the CSV data with a ``CsvParser``. In the ``CsvParserOptions`` we are 
defining to not skip the header and use a ``,`` as column delimiter. I have assumed, that the file is encoded 
as ``UTF8``.

```java
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
```

	
### Reading From a String ###

Reading from a string is possible with the :code:`CsvParser.readFromString` method.

```java
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
```	
