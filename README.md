# JTinyCsvParser #

[MIT License]: https://opensource.org/licenses/MIT
[JTinyCsvParser]: https://github.com/bytefish/JTinyCsvParser
[TinyCsvParser]: https://github.com/bytefish/TinyCsvParser


[JTinyCsvParser] is a high-performance CSV Parser for Java. As the name implies it is a Java version of [TinyCsvParser].

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

## Quickstart ##

This is only an example for the most common use of JTinyCsvParser. For more detailed information on custom formats and more advanced use-cases, 
please consult the full User Guide of the official documentation.

Imagine we have list of Persons in a CSV file ``persons.csv`` with their first name, last name and birthdate.

```
Philipp,Wagner,1986/05/12
Max,Musterman,2014/01/02
```

The corresponding domain model in our system might look like this.

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

When using [JTinyCsvParser] you have to define the mapping between the CSV File and your domain model:

```java
public class PersonMapping extends CsvMapping<Person> {

    public PersonMapping(IObjectCreator creator) {
        super(creator);

        Map(0, String.class, Person::setFirstName);
        Map(1, String.class, Person::setLastName);
        Map(2, LocalDate.class, Person::setBirthDate);
    }
}
```

And then it can be used to read the Results. Please note, that the ``CsvParser`` returns a ``Stream``, so 
in this example they are turned into a list first.

```java
public class CsvParserTest {

    @Test
    public void testParse() throws Exception {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        ArrayList<String> csvData = new ArrayList<>();

        // Simulate CSV Data:
        csvData.add("Philipp,Wagner,1986-05-12");
        csvData.add(""); // An empty line... Should be skipped.
        csvData.add("Max,Musterman,2000-01-07");

        List<CsvMappingResult<Person>> result =  parser.parse(csvData)
                .collect(Collectors.toList()); // turn it into a List!

        Assert.assertNotNull(result);

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
