// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.mapping.CsvMapping;
import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;
import junit.framework.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvParserTest {

    class Person {

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

    class PersonMapping extends CsvMapping<Person> {

        public PersonMapping(IObjectCreator creator) {
            super(creator);

            MapProperty(0, String.class, Person::setFirstName);
            MapProperty(1, String.class, Person::setLastName);
            MapProperty(2, LocalDate.class, Person::setBirthDate);
        }
    }

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

        List<CsvMappingResult<Person>> result =  parser.Parse(csvData)
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


    @Test
    public void testParse_ReadFromString() throws Exception {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        String lineSeparator = System.getProperty("line.separator");
        String csvData = "Philipp,Wagner,1986-05-12" + lineSeparator + "Max,Musterman,2000-01-07";

        List<CsvMappingResult<Person>> result =  parser.ReadFromString(csvData, new CsvReaderOptions(lineSeparator))
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